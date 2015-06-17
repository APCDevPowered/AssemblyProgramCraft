package org.apcdevpowered.apc.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import net.minecraft.world.World;

import org.apcdevpowered.util.io.DirectoryLock;
import org.apcdevpowered.vcpu32.asm.ProgramPackage;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;
import org.apcdevpowered.vcpu32.vm.storage.exception.UnsupportedVersionException;
import org.apcdevpowered.vcpu32.vm.storage.persistence.NodeDataPersistence;

public class ProgramDataHelper
{
    /** Virtual machine data cache. */
    private static Map<UUID, NodeContainerMap> programDataCacheMap = new WeakHashMap<UUID, NodeContainerMap>();
    /** Directory lock cache. */
    private static Map<File, DirectoryLock> directoryLockCacheMap = new WeakHashMap<File, DirectoryLock>();
    
    /**
     * Write data from ProgramPackage to node.
     * 
     * @param uuid
     *            UUID of ProgramPackage.
     * 
     * @param programPackage
     *            The ProgramPackage read from.
     */
    public static void writeToData(UUID uuid, ProgramPackage programPackage) throws NodeIOException
    {
        World world = WorldHelper.getWorldFromDimension(0);
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        File programDirectory;
        try
        {
            programDirectory = new File(worldDirectory, "programdata").getCanonicalFile();
        }
        catch (IOException e)
        {
            throw new NodeIOException(e);
        }
        if (!programDirectory.exists())
        {
            if (!programDirectory.mkdirs())
            {
                throw new NodeIOException("Can not create programdata directory");
            }
        }
        DirectoryLock directoryLock;
        synchronized (directoryLockCacheMap)
        {
            directoryLock = directoryLockCacheMap.get(programDirectory);
            if (directoryLock == null)
            {
                try
                {
                    directoryLock = new DirectoryLock(programDirectory);
                    directoryLockCacheMap.put(programDirectory, directoryLock);
                }
                catch (IOException e)
                {
                    throw new NodeIOException(e);
                }
            }
        }
        while (true)
        {
            try
            {
                directoryLock.lockDirectory();
                break;
            }
            catch (InterruptedException e)
            {
            }
        }
        try
        {
            File programdataFile = new File(programDirectory, uuid.toString());
            NodeContainerMap programPackageNodeContainerMap = new NodeContainerMap();
            programPackage.writeToNode(programPackageNodeContainerMap);
            synchronized (programDataCacheMap)
            {
                programDataCacheMap.put(uuid, programPackageNodeContainerMap);
            }
            try
            {
                NodeDataPersistence.saveNode(programdataFile, programPackageNodeContainerMap);
            }
            catch (IOException e)
            {
                throw new NodeIOException(e);
            }
        }
        finally
        {
            directoryLock.releaseDirectory();
        }
    }
    /**
     * Read data from node to ProgramPackage.
     * 
     * @param uuid
     *            UUID of ProgramPackage.
     * 
     * @param programPackage
     *            The ProgramPackage write to.
     */
    public static void readFormNode(UUID uuid, ProgramPackage programPackage) throws NodeIOException
    {
        World world = WorldHelper.getWorldFromDimension(0);
        synchronized (programDataCacheMap)
        {
            NodeContainerMap cachedVMNodeContainerMap = programDataCacheMap.get(uuid);
            if (cachedVMNodeContainerMap != null)
            {
                try
                {
                    programPackage.readFromNode(cachedVMNodeContainerMap);
                    return;
                }
                catch (ElementNotFoundException e)
                {
                    throw new NodeIOException(e);
                }
                catch (ElementTypeMismatchException e)
                {
                    throw new NodeIOException(e);
                }
            }
        }
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        File programDirectory;
        try
        {
            programDirectory = new File(worldDirectory, "programdata").getCanonicalFile();
        }
        catch (IOException e)
        {
            throw new NodeIOException(e);
        }
        if (!programDirectory.exists())
        {
            if (!programDirectory.mkdirs())
            {
                throw new NodeIOException("Can not create programdata directory");
            }
        }
        DirectoryLock directoryLock;
        synchronized (directoryLockCacheMap)
        {
            directoryLock = directoryLockCacheMap.get(programDirectory);
            if (directoryLock == null)
            {
                try
                {
                    directoryLock = new DirectoryLock(programDirectory);
                    directoryLockCacheMap.put(programDirectory, directoryLock);
                }
                catch (IOException e)
                {
                    throw new NodeIOException(e);
                }
            }
        }
        while (true)
        {
            try
            {
                directoryLock.lockDirectory();
                break;
            }
            catch (InterruptedException e)
            {
            }
        }
        try
        {
            File programdataFile = new File(programDirectory, uuid.toString());
            NodeContainerMap programPackageNodeContainerMap;
            try
            {
                programPackageNodeContainerMap = NodeDataPersistence.loadNode(programdataFile, NodeContainerMap.class);
            }
            catch (IOException e)
            {
                throw new NodeIOException(e);
            }
            catch (ElementTypeMismatchException e)
            {
                throw new NodeIOException(e);
            }
            catch (UnsupportedVersionException e)
            {
                throw new NodeIOException(e);
            }
            try
            {
                programPackage.readFromNode(programPackageNodeContainerMap);
            }
            catch (ElementNotFoundException e)
            {
                throw new NodeIOException(e);
            }
            catch (ElementTypeMismatchException e)
            {
                throw new NodeIOException(e);
            }
        }
        finally
        {
            directoryLock.releaseDirectory();
        }
    }
}