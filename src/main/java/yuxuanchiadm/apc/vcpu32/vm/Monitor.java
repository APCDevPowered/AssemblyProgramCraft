package yuxuanchiadm.apc.vcpu32.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.MonitorReferenceImpl;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Monitor
{
    private VirtualMachine vm;
    
    private int handleValue;
    
    private List<AssemblyVirtualThread> blockingThreadList = new ArrayList<AssemblyVirtualThread>();
    
    private List<AssemblyVirtualThread> waitingThreadList = new ArrayList<AssemblyVirtualThread>();
    private Map<AssemblyVirtualThread, Integer> waitingThreadLockCountMap = new HashMap<AssemblyVirtualThread, Integer>();
    
    private AssemblyVirtualThread holdsLockThread;
    private int lockCount;
    
    private int[] blockingThreadHandlerList;
    
    private int[] waitingThreadHandlerList;
    private Map<Integer, Integer> waitingThreadHandlerLockCountMap;
    
    private int holdsLockThreadHandleValue = -1;
    
    public Monitor(VirtualMachine vm)
    {
        this(vm, 0);
    }
    
    public Monitor(VirtualMachine vm, int handlerValue)
    {
        this.vm = vm;
        this.handleValue = handlerValue;
    }
    public synchronized void waitLockNotify()
    {
        waitLockNotify(0);
    }

    public synchronized void waitLockNotify(long timeout)
    {
        try
        {
            this.wait(timeout);
        }
        catch (InterruptedException e)
        {
            
        }
    }

    /**
     * @param avThread 要获取监视器锁的线程
     * @return 返回-1为获取锁失败，其他线程已经锁定此监视器，线程应该进入等待监视器通知状态，否则返回当前线程的锁定次数
     */
    public synchronized int lockMonitor(AssemblyVirtualThread avThread)
    {
        return lockMonitor(avThread, 1);
    }
    /**
     * @param avThread 要获取监视器锁的线程
     * @param times 要获取的次数，应大于0
     * @return 返回-1为获取锁失败，其他线程已经锁定此监视器，线程应该进入等待监视器通知状态，否则返回当前线程的锁定次数
     */
    public synchronized int lockMonitor(AssemblyVirtualThread avThread, int times)
    {
        if(times <= 0)
        {
            throw new IllegalArgumentException();
        }
        if(holdsLockThread == null)
        {
            if(blockingThreadList.contains(avThread))
            {
                blockingThreadList.remove(avThread);
            }
            holdsLockThread = avThread;
            lockCount += times;
            return lockCount;
        }
        else
        {
            if(holdsLockThread == avThread)
            {
                lockCount += times;
                return lockCount;
            }
            else
            {
                blockingThreadList.add(avThread);
                return -1;
            }
        }
    }
    /**
     * @param avThread 要释放监视器锁的线程
     * @return 返回-1为释放锁失败，此线程没有锁定此监视器，否则返回当前线程的锁定次数，为0则为已完全解锁
     */
    public synchronized int unlockMonitor(AssemblyVirtualThread avThread)
    {
        return unlockMonitor(avThread, 1);
    }
    /**
     * 
     * @param avThread 要查看是否在此监视器上阻塞等待的线程
     * @return 线程是否在此监视器上阻塞等待
     */
    public synchronized boolean isBlockingOnMonitor(AssemblyVirtualThread avThread)
    {
        return blockingThreadList.contains(avThread);
    }
    /**
     * @param avThread 要释放监视器锁的线程
     * @param times 最多要释放的次数，应大于0或等于-1（释放全部）
     * @return 返回-1为释放锁失败，此线程没有锁定此监视器，否则返回当前线程的锁定次数，为0则为已完全解锁
     */
    public synchronized int unlockMonitor(AssemblyVirtualThread avThread, int times)
    {
        if(times <= 0 && times != -1)
        {
            throw new IllegalArgumentException();
        }
        if(holdsLockThread != avThread)
        {
            return -1;
        }
        else
        {
            if(times == -1)
            {
                holdsLockThread = null;
                lockCount = 0;
                this.notifyAll();
                return lockCount;
            }
            else
            {
                lockCount -= times;
                if(lockCount <= 0)
                {
                    holdsLockThread = null;
                    lockCount = 0;
                    this.notifyAll();
                }
                return lockCount;
            }
        }
    }
    /**
     * @param avThread 要取消阻塞等待锁定监视器的线程
     * @return 返回true为取消成功，返回false为未在此监视器上阻塞等待
     */
    public synchronized boolean cancelLockMonitor(AssemblyVirtualThread avThread)
    {
        if(blockingThreadList.contains(avThread))
        {
            blockingThreadList.remove(avThread);
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * @return 当前获得此监视器锁的线程
     */
    public synchronized AssemblyVirtualThread getHoldsLockThread()
    {
        return holdsLockThread;
    }
    /**
     * @return 当前获得此监视器锁的线程的锁定次数
     */
    public synchronized int getLockCound()
    {
        return lockCount;
    }
    public synchronized boolean waitMonitor(AssemblyVirtualThread avThread)
    {
        if(holdsLockThread != avThread)
        {
            return false;
        }
        else
        {
            waitingThreadList.add(avThread);
            waitingThreadLockCountMap.put(avThread, lockCount);
            holdsLockThread = null;
            lockCount = 0;
            return true;
        }
    }
    public synchronized boolean cancelWaitMonitor(AssemblyVirtualThread avThread)
    {
        if(waitingThreadList.contains(avThread))
        {
            waitingThreadList.remove(avThread);
            return true;
        }
        else
        {
            return false;
        }
    }
    public synchronized boolean notifyMonitor(AssemblyVirtualThread avThread, int count)
    {
        if(holdsLockThread != avThread)
        {
            return false;
        }
        else
        {
            if(count <= 0)
            {
                waitingThreadList.clear();
            }
            else
            {
                for(int i = 0;i < count;i++)
                {
                    if(waitingThreadList.isEmpty())
                    {
                        break;
                    }
                    else
                    {
                        waitingThreadList.remove(0);
                    }
                }
            }
            this.notifyAll();
            return true;
        }
    }
    public synchronized boolean isWaitingOnMonitor(AssemblyVirtualThread avThread)
    {
        return waitingThreadList.contains(avThread);
    }
    public synchronized boolean restoreMonitorLock(AssemblyVirtualThread AVThread)
    {
        if(waitingThreadLockCountMap.containsKey(AVThread))
        {
            if(waitingThreadList.contains(AVThread))
            {
                return false;
            }
            else
            {
                if(holdsLockThread == null)
                {
                    holdsLockThread = AVThread;
                    lockCount = waitingThreadLockCountMap.get(AVThread);
                    waitingThreadLockCountMap.remove(AVThread);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
    public synchronized List<AssemblyVirtualThread> getBlockingThreadList()
    {
        return new ArrayList<AssemblyVirtualThread>(blockingThreadList);
    }
    public synchronized List<AssemblyVirtualThread> getWaitingThreadList()
    {
        return new ArrayList<AssemblyVirtualThread>(waitingThreadList);
    }
    public synchronized Map<AssemblyVirtualThread, Integer> getWaitingThreadLockCountMap()
    {
        return new HashMap<AssemblyVirtualThread, Integer>(waitingThreadLockCountMap);
    }
    public synchronized int getMonitorHandler()
    {
        return handleValue;
    }
    public synchronized boolean isMonitorIdle()
    {
        return holdsLockThread == null && blockingThreadList.isEmpty() && waitingThreadList.isEmpty() && waitingThreadLockCountMap.isEmpty();
    }
    public synchronized boolean isVaild()
    {
        return vm.getMonitor(handleValue) != null;
    }
    public synchronized void shutdown()
    {
        this.notifyAll();
    }
    public synchronized void onThreadDeath(AssemblyVirtualThread avThread)
    {
        if(isBlockingOnMonitor(avThread))
        {
            cancelLockMonitor(avThread);
        }
        if(isWaitingOnMonitor(avThread))
        {
            cancelWaitMonitor(avThread);
            restoreMonitorLock(avThread);
        }
        if(getHoldsLockThread() == avThread)
        {
            unlockMonitor(avThread, -1);
        }
    }
    public synchronized void loadMonitorRelation()
    {
        if(waitingThreadHandlerList != null)
        {
            for(int i = 0 ; i < waitingThreadHandlerList.length ; i++)
            {
                waitingThreadList.add(vm.getVMThread(waitingThreadHandlerList[i]));
            }
            waitingThreadHandlerList = null;
        }
        if(blockingThreadHandlerList != null)
        {
            for(int i = 0 ; i < blockingThreadHandlerList.length ; i++)
            {
                blockingThreadList.add(vm.getVMThread(blockingThreadHandlerList[i]));
            }
            blockingThreadHandlerList = null;
        }
        if(waitingThreadHandlerLockCountMap != null)
        {
            for(Entry<Integer, Integer> entry : waitingThreadHandlerLockCountMap.entrySet())
            {
                waitingThreadLockCountMap.put(vm.getVMThread(entry.getKey()), entry.getValue());
            }
            waitingThreadHandlerLockCountMap = null;
        }
        if(holdsLockThreadHandleValue != -1)
        {
            holdsLockThread = vm.getVMThread(holdsLockThreadHandleValue);
            holdsLockThreadHandleValue = -1;
        }
    }
    public synchronized void writeToNBT(NBTTagCompound monitorNbtTagCompound)
    {
        monitorNbtTagCompound.setInteger("handleValue", handleValue);
        
        {
            int temp[] = new int[waitingThreadList.size()];
            for(int i = 0 ; i < waitingThreadList.size() ;  i++)
            {
                temp[i] = waitingThreadList.get(i).getThreadHandler();
            }
            monitorNbtTagCompound.setIntArray("waitingThreadHandlerList", temp);
        }
        
        {
            int temp[] = new int[blockingThreadList.size()];
            for(int i = 0 ; i < blockingThreadList.size() ;  i++)
            {
                temp[i] = blockingThreadList.get(i).getThreadHandler();
            }
            monitorNbtTagCompound.setIntArray("blockingThreadHandlerList", temp);
        }
        
        NBTTagList waitingThreadHandlerLockCountMapTagList = new NBTTagList();
        for(Entry<AssemblyVirtualThread, Integer> entry : waitingThreadLockCountMap.entrySet())
        {
            NBTTagCompound waitingThreadHandlerLockCountMapEntryTagCompound = new NBTTagCompound();
            waitingThreadHandlerLockCountMapEntryTagCompound.setInteger("key", entry.getKey().getThreadHandler());
            waitingThreadHandlerLockCountMapEntryTagCompound.setInteger("value", entry.getValue());
            waitingThreadHandlerLockCountMapTagList.appendTag(waitingThreadHandlerLockCountMapEntryTagCompound);
        }
        monitorNbtTagCompound.setTag("waitingThreadHandlerLockCountMap", waitingThreadHandlerLockCountMapTagList);
        
        if(holdsLockThread != null)
        {
            monitorNbtTagCompound.setInteger("holdsLockThreadHandleValue", holdsLockThread.getThreadHandler());
        }
        monitorNbtTagCompound.setInteger("lockCount", lockCount);
    }
    public synchronized void readFromNBT(NBTTagCompound monitorNbtTagCompound)
    {
        handleValue = monitorNbtTagCompound.getInteger("handleValue");
        
        waitingThreadHandlerList = monitorNbtTagCompound.getIntArray("waitingThreadHandlerList");
        blockingThreadHandlerList = monitorNbtTagCompound.getIntArray("blockingThreadHandlerList");
        
        NBTTagList waitingThreadHandlerLockCountMapTagList = monitorNbtTagCompound.getTagList("waitingThreadHandlerLockCountMap", 10);
        waitingThreadHandlerLockCountMap = new HashMap<Integer, Integer>();
        for(int i = 0;i < waitingThreadHandlerLockCountMapTagList.tagCount();i++)
        {
            NBTTagCompound waitingThreadHandlerLockCountMapEntryTagCompound = waitingThreadHandlerLockCountMapTagList.getCompoundTagAt(i);
            Integer key = waitingThreadHandlerLockCountMapEntryTagCompound.getInteger("key");
            Integer value = waitingThreadHandlerLockCountMapEntryTagCompound.getInteger("value");
            if(key != null && value != null)
            {
                waitingThreadHandlerLockCountMap.put(key, value);
            }
        }
        
        if(monitorNbtTagCompound.hasKey("holdsLockThreadHandleValue"))
        {
            holdsLockThreadHandleValue = monitorNbtTagCompound.getInteger("holdsLockThreadHandleValue");
        }
        lockCount = monitorNbtTagCompound.getInteger("lockCount");
    }
    
    private Object referenceInitLock = new Object();
    private MonitorReferenceImpl reference;
    
    public MonitorReferenceImpl getReference()
    {
        synchronized (referenceInitLock)
        {
            if(reference == null)
            {
                reference = new MonitorReferenceImpl(vm.getReference(), this);
            }
            return reference;
        }
    }
}