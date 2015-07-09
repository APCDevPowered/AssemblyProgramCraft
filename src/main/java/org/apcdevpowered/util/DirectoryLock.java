package org.apcdevpowered.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLockInterruptionException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public final class DirectoryLock
{
    private final File directory;
    private Object lockNotifier = new Object();
    private Object threadLock = new Object();
    private boolean pendingLock;
    private Thread currentLockThread;
    private int lockCount;
    private int wattingCount;
    private FileChannel fileChannel;
    
    public DirectoryLock(File directory) throws IOException
    {
        this.directory = directory.getCanonicalFile();
    }
    public final boolean lockDirectory() throws InterruptedException
    {
        synchronized (lockNotifier)
        {
            if (currentLockThread == Thread.currentThread())
            {
                lockCount++;
                return true;
            }
            wattingCount++;
            while (lockCount > 0 || pendingLock)
            {
                try
                {
                    lockNotifier.wait();
                }
                catch (InterruptedException e)
                {
                    if (wattingCount == 0)
                    {
                        synchronized (threadLock)
                        {
                            try
                            {
                                fileChannel.close();
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    }
                    throw e;
                }
            }
            pendingLock = true;
        }
        try
        {
            synchronized (threadLock)
            {
                if (fileChannel == null)
                {
                    File lockFile = new File(directory, ".lock");
                    FileChannel fileChannel;
                    try
                    {
                        List<OpenOption> openOptionList = new ArrayList<OpenOption>();
                        openOptionList.add(StandardOpenOption.READ);
                        openOptionList.add(StandardOpenOption.WRITE);
                        openOptionList.add(StandardOpenOption.TRUNCATE_EXISTING);
                        openOptionList.add(StandardOpenOption.CREATE);
                        openOptionList.add(StandardOpenOption.DELETE_ON_CLOSE);
                        openOptionList.add(StandardOpenOption.SYNC);
                        try
                        {
                            Class<? extends OpenOption> clazz = GenericsUtil.<Class<?>, Class<? extends OpenOption>> genericUnsafeCast(getClass().getClassLoader().loadClass("com.sun.nio.file.ExtendedOpenOption"));
                            if (OpenOption.class.isAssignableFrom(clazz))
                            {
                                if (clazz.isEnum())
                                {
                                    OpenOption[] extendedOpenOptions = clazz.getEnumConstants();
                                    Method nameMethod = clazz.getMethod("name");
                                    for (OpenOption extendedOpenOption : extendedOpenOptions)
                                    {
                                        int modifiers = nameMethod.getModifiers();
                                        if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers))
                                        {
                                            if (nameMethod.getParameterTypes().length == 0)
                                            {
                                                if (nameMethod.getReturnType().equals(String.class))
                                                {
                                                    String name = (String) nameMethod.invoke(extendedOpenOption);
                                                    if (name.equals("NOSHARE_READ") || name.equals("NOSHARE_WRITE") || name.equals("NOSHARE_DELETE"))
                                                    {
                                                        openOptionList.add(extendedOpenOption);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        catch (ClassNotFoundException e)
                        {
                        }
                        catch (NoSuchMethodException e)
                        {
                        }
                        catch (SecurityException e)
                        {
                        }
                        catch (IllegalAccessException e)
                        {
                        }
                        catch (IllegalArgumentException e)
                        {
                        }
                        catch (InvocationTargetException e)
                        {
                        }
                        catch (ClassCastException e)
                        {
                        }
                        fileChannel = FileChannel.open(lockFile.toPath(), openOptionList.toArray(new OpenOption[openOptionList.size()]));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return false;
                    }
                    try
                    {
                        fileChannel.lock();
                    }
                    catch (FileLockInterruptionException e)
                    {
                        throw new InterruptedException();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        try
                        {
                            fileChannel.close();
                        }
                        catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                        return false;
                    }
                    this.fileChannel = fileChannel;
                }
                currentLockThread = Thread.currentThread();
                lockCount = 1;
                return true;
            }
        }
        finally
        {
            synchronized (lockNotifier)
            {
                wattingCount--;
                pendingLock = false;
                lockNotifier.notify();
            }
        }
    }
    public final boolean releaseDirectory()
    {
        synchronized (lockNotifier)
        {
            if (lockCount <= 0)
            {
                return false;
            }
            if (currentLockThread != Thread.currentThread())
            {
                return false;
            }
            lockCount--;
            if (lockCount <= 0)
            {
                if (wattingCount <= 0)
                {
                    synchronized (threadLock)
                    {
                        try
                        {
                            fileChannel.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        fileChannel = null;
                    }
                }
                currentLockThread = null;
                lockNotifier.notify();
            }
            return true;
        }
    }
}