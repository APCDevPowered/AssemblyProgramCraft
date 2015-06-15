package org.apcdevpowered.vcpu32.vm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.apcdevpowered.vcpu32.asm.ProgramPackage;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventSetImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.ThreadDeathEventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.ThreadStartEventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.VMDeathEventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.VMStartEventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.ThreadDeathRequestImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.ThreadStartRequestImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.VMDeathRequestImpl;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;

public class VirtualMachine
{
    public static final String VERSION = "1.0.0";
    public AdvancedRAMArray ram;
    private ProgramPackage programPak;
    private ProgramPackage biosPak;
    private List<Integer> closedThreadList;
    private Map<Integer, AssemblyVirtualThread> avtList;
    private Map<Integer, AbstractExternalDevice> devicesList;
    private List<Integer> closedMonitorList;
    private Map<Integer, Monitor> monitorList;
    private volatile boolean isRunning;
    private volatile boolean isSuspend;
    private Object suspendLock = new Object();
    private List<Integer> unsuspendedThreadHandlerList = new ArrayList<Integer>();
    
    public VirtualMachine()
    {
        devicesList = new HashMap<Integer, AbstractExternalDevice>();
        ram = new AdvancedRAMArray(this, 65536);
        closedThreadList = new ArrayList<Integer>();
        avtList = new HashMap<Integer, AssemblyVirtualThread>();
        closedMonitorList = new ArrayList<Integer>();
        monitorList = new HashMap<Integer, Monitor>();
    }
    protected int getDeviceType(int port)
    {
        synchronized (devicesList)
        {
            AbstractExternalDevice devices = devicesList.get(port);
            if (devices == null)
            {
                return 0;
            }
            return devices.getDeviceType().ordinal();
        }
    }
    protected int inputValueFormDevices(int port, int idx)
    {
        synchronized (devicesList)
        {
            AbstractExternalDevice devices = devicesList.get(port);
            if (devices == null)
            {
                return 0;
            }
            synchronized (devices.uselock)
            {
                return devices.getMemoryValue(idx);
            }
        }
    }
    protected int[] inputsValueFormDevices(int port, int idx, int length)
    {
        synchronized (devicesList)
        {
            AbstractExternalDevice devices = devicesList.get(port);
            if (devices == null)
            {
                return null;
            }
            synchronized (devices.uselock)
            {
                return devices.getMemoryValues(idx, length);
            }
        }
    }
    protected void outputValueToDevices(int port, int idx, int value)
    {
        synchronized (devicesList)
        {
            AbstractExternalDevice devices = devicesList.get(port);
            if (devices != null)
            {
                synchronized (devices.uselock)
                {
                    devices.setMemoryValue(idx, value);
                }
            }
        }
    }
    protected void outputsValueToDevices(int port, int idx, int[] values)
    {
        synchronized (devicesList)
        {
            AbstractExternalDevice devices = devicesList.get(port);
            if (devices != null)
            {
                synchronized (devices.uselock)
                {
                    devices.setMemoryValues(idx, values);
                }
            }
        }
    }
    public void clearExternalDevices()
    {
        synchronized (devicesList)
        {
            for (Entry<Integer, AbstractExternalDevice> entry : devicesList.entrySet())
            {
                entry.getValue().shutDown();
            }
            devicesList.clear();
        }
    }
    public boolean addExternalDevices(AbstractExternalDevice devices, int port)
    {
        synchronized (devicesList)
        {
            if (devices == null)
            {
                return false;
            }
            if (devicesList.get(port) != null)
            {
                return false;
            }
            devicesList.put(port, devices);
            devices.start();
            return true;
        }
    }
    public boolean resetExternalDevices(int port)
    {
        synchronized (devicesList)
        {
            AbstractExternalDevice devices = devicesList.get(port);
            if (devices == null)
            {
                return false;
            }
            devices.reset();
            return true;
        }
    }
    public boolean removeExternalDevices(int port)
    {
        synchronized (devicesList)
        {
            AbstractExternalDevice devices = devicesList.get(port);
            if (devices == null)
            {
                return false;
            }
            devices.shutDown();
            devicesList.remove(port);
            return true;
        }
    }
    public boolean loadBIOS(ProgramPackage biosPak)
    {
        if (this.biosPak != null)
        {
            this.biosPak = null;
        }
        if (biosPak == null)
        {
            return true;
        }
        if (biosPak.isBIOS == false)
        {
            System.out.println("试图装载非BIOS程序为BIOS程序");
            return false;
        }
        if (biosPak.startRAM != 7168)
        {
            System.out.println("BIOS程序启动地址不在指定位置");
            return false;
        }
        if (biosPak.staticRAMEnd > 1024)
        {
            System.out.println("BIOS程序过于庞大");
            return false;
        }
        if (biosPak.startStaticRAM < 7168 + biosPak.programEnd && biosPak.startStaticRAM + (biosPak.staticRAMEnd - biosPak.programEnd) >= 8192)
        {
            System.out.println("BIOS程序静态储存区过大或不在BIOS预留内存范围内");
            return false;
        }
        this.biosPak = biosPak;
        return true;
    }
    public boolean loadProgram(ProgramPackage programPak)
    {
        if (this.programPak != null)
        {
            this.programPak = null;
        }
        if (programPak == null)
        {
            return true;
        }
        if (programPak.isBIOS == true)
        {
            System.out.println("试图装载BIOS程序为模块程序");
            return false;
        }
        if (biosPak == null)
        {
            System.out.println("你必须先加载BIOS到虚拟机");
            return false;
        }
        if (programPak.programEnd + programPak.startRAM > 3072)
        {
            System.out.println("程序过于庞大");
            return false;
        }
        if (programPak.startStaticRAM < 3072 && programPak.startStaticRAM + (programPak.staticRAMEnd - programPak.programEnd) >= 7167)
        {
            System.out.println("程序静态储存区过大或不在静态储存区内存范围内");
            return false;
        }
        this.programPak = programPak;
        return true;
    }
    protected void loadBIOSBytes()
    {
        ram.setValues(7168, 0, biosPak.programEnd, biosPak.data);
        ram.setValues(biosPak.startStaticRAM, biosPak.programEnd, biosPak.staticRAMEnd - biosPak.programEnd, biosPak.data);
    }
    protected void loadProgramBytes()
    {
        ram.setValues(programPak.startRAM, 0, programPak.programEnd, programPak.data);
        ram.setValues(programPak.startStaticRAM, programPak.programEnd, programPak.staticRAMEnd - programPak.programEnd, programPak.data);
    }
    public synchronized boolean startVM(boolean loadProgram)
    {
        if (isRunning)
        {
            return false;
        }
        if (biosPak == null)
        {
            System.out.println("在启动虚拟机前必须加载BIOS和程序");
            return false;
        }
        synchronized (devicesList)
        {
            Iterator<Entry<Integer, AbstractExternalDevice>> deviceiterator = devicesList.entrySet().iterator();
            while (deviceiterator.hasNext())
            {
                deviceiterator.next().getValue().start();
            }
        }
        loadBIOSBytes();
        if (loadProgram == true)
        {
            if (programPak == null)
            {
                System.out.println("虚拟机没有找到程序");
                return false;
            }
            loadProgramBytes();
        }
        isRunning = true;
        int mainThreadHandler = createVMThread(7168, "MainThread");
        List<EventImpl> events = new ArrayList<EventImpl>();
        events.add(new VMStartEventImpl(getReference(), null, getVMThread(mainThreadHandler).getReference()));
        getReference().eventQueue().addEventSet(new EventSetImpl(getReference(), events));
        startVMThread(mainThreadHandler);
        return true;
    }
    public synchronized boolean shutdownVM()
    {
        if (!isRunning)
        {
            return false;
        }
        if (!isSuspend)
        {
            suspendVM();
        }
        isRunning = false;
        List<EventImpl> events = new ArrayList<EventImpl>();
        events.add(new VMDeathEventImpl(getReference(), null));
        synchronized (enabledVMDeathRequestList)
        {
            for (VMDeathRequestImpl request : enabledVMDeathRequestList)
            {
                events.add(new VMDeathEventImpl(getReference(), request));
            }
        }
        getReference().eventQueue().addEventSet(new EventSetImpl(getReference(), events));
        Map<Integer, AssemblyVirtualThread> avtList;
        synchronized (suspendLock)
        {
            synchronized (this.avtList)
            {
                avtList = new HashMap<Integer, AssemblyVirtualThread>(this.avtList);
                for (Entry<Integer, AssemblyVirtualThread> entry : avtList.entrySet())
                {
                    entry.getValue().shutdown();
                }
            }
        }
        for (AssemblyVirtualThread avThread : avtList.values())
        {
            avThread.join();
        }
        synchronized (devicesList)
        {
            Iterator<Entry<Integer, AbstractExternalDevice>> deviceIterator = devicesList.entrySet().iterator();
            while (deviceIterator.hasNext())
            {
                deviceIterator.next().getValue().shutDown();
            }
        }
        synchronized (monitorList)
        {
            Iterator<Entry<Integer, Monitor>> monitorIterator = monitorList.entrySet().iterator();
            while (monitorIterator.hasNext())
            {
                monitorIterator.next().getValue().shutdown();
            }
            monitorList.clear();
        }
        ram.clear();
        synchronized (closedThreadList)
        {
            closedThreadList.clear();
        }
        synchronized (closedMonitorList)
        {
            closedMonitorList.clear();
        }
        isSuspend = false;
        return true;
    }
    public synchronized boolean resetVM()
    {
        if (!shutdownVM())
        {
            return false;
        }
        programPak = null;
        biosPak = null;
        return true;
    }
    public synchronized void suspendVM()
    {
        if (isRunning)
        {
            synchronized (suspendLock)
            {
                if (isSuspend)
                {
                    return;
                }
                isSuspend = true;
                while (!unsuspendedThreadHandlerList.isEmpty())
                {
                    synchronized (avtList)
                    {
                        for (AssemblyVirtualThread thread : avtList.values())
                        {
                            thread.notifyVMSuspend();
                        }
                    }
                    if (!unsuspendedThreadHandlerList.isEmpty())
                    {
                        try
                        {
                            suspendLock.wait();
                        }
                        catch (InterruptedException e)
                        {
                        }
                    }
                }
            }
        }
    }
    public synchronized void resumeVM()
    {
        if (isRunning)
        {
            synchronized (suspendLock)
            {
                if (!isSuspend)
                {
                    return;
                }
                isSuspend = false;
                synchronized (avtList)
                {
                    for (AssemblyVirtualThread thread : avtList.values())
                    {
                        unsuspendedThreadHandlerList.add((Integer) thread.getThreadHandler());
                        thread.resumeThread();
                    }
                }
            }
        }
    }
    protected void notifyThreadSuspend(int threadHandler)
    {
        synchronized (suspendLock)
        {
            if (isSuspend)
            {
                unsuspendedThreadHandlerList.remove((Integer) threadHandler);
                suspendLock.notifyAll();
            }
        }
    }
    public boolean isRunning()
    {
        return isRunning;
    }
    public boolean isSuspend()
    {
        return isSuspend;
    }
    protected int createVMThread(int startRAM)
    {
        return createVMThread(startRAM, null);
    }
    protected int createVMThread(int startRAM, String threadName)
    {
        synchronized (suspendLock)
        {
            synchronized (avtList)
            {
                synchronized (closedThreadList)
                {
                    int handler;
                    if (closedThreadList.size() != 0)
                    {
                        handler = closedThreadList.remove(0);
                    }
                    else
                    {
                        handler = avtList.size();
                    }
                    if (threadName == null)
                    {
                        threadName = "Thread" + handler;
                    }
                    AssemblyVirtualThread avt = new AssemblyVirtualThread(this, startRAM, threadName, handler);
                    avtList.put(handler, avt);
                    synchronized (suspendLock)
                    {
                        unsuspendedThreadHandlerList.add((Integer) handler);
                    }
                    synchronized (threadReferenceList)
                    {
                        threadReferenceList.add(avt.getReference());
                    }
                    VMsLogger.printThreadCreatedInfo(startRAM, threadName);
                    return handler;
                }
            }
        }
    }
    protected boolean startVMThread(int threadHandler)
    {
        return startVMThread(threadHandler, -1);
    }
    protected boolean startVMThread(int threadHandler, int parentThreadHanler)
    {
        synchronized (avtList)
        {
            AssemblyVirtualThread avt = avtList.get(threadHandler);
            AssemblyVirtualThread parentavt = (parentThreadHanler >= 0 ? avtList.get(parentThreadHanler) : null);
            if (avt == null)
            {
                return false;
            }
            if (parentThreadHanler >= 0 && parentavt == null)
            {
                return false;
            }
            if (avt.isRunning())
            {
                return false;
            }
            if (parentThreadHanler >= 0)
            {
                avt.setParentThread(parentavt);
                parentavt.addChild(avt);
            }
            List<EventImpl> events = new ArrayList<EventImpl>();
            synchronized (enabledThreadStartRequestList)
            {
                for (ThreadStartRequestImpl request : enabledThreadStartRequestList)
                {
                    events.add(new ThreadStartEventImpl(getReference(), request, avt.getReference()));
                }
            }
            getReference().eventQueue().addEventSet(new EventSetImpl(getReference(), events));
            avt.start();
            return true;
        }
    }
    protected AssemblyVirtualThread getVMThread(int threadHandler)
    {
        synchronized (avtList)
        {
            return avtList.get(threadHandler);
        }
    }
    public List<AssemblyVirtualThread> getVMThreadList()
    {
        synchronized (avtList)
        {
            return Collections.unmodifiableList(new ArrayList<AssemblyVirtualThread>(avtList.values()));
        }
    }
    protected boolean shutdownThread(int threadHandler)
    {
        synchronized (suspendLock)
        {
            synchronized (avtList)
            {
                AssemblyVirtualThread thread = avtList.get(threadHandler);
                if (thread != null)
                {
                    thread.shutdown();
                    return true;
                }
                return false;
            }
        }
    }
    protected boolean removeFromThreadList(int threadHandler)
    {
        synchronized (suspendLock)
        {
            synchronized (avtList)
            {
                if (avtList.containsKey(threadHandler))
                {
                    AssemblyVirtualThread avt = avtList.remove(threadHandler);
                    synchronized (threadReferenceList)
                    {
                        threadReferenceList.remove(avt.getReference());
                    }
                    synchronized (suspendLock)
                    {
                        unsuspendedThreadHandlerList.remove((Integer) threadHandler);
                        suspendLock.notifyAll();
                    }
                    synchronized (closedThreadList)
                    {
                        closedThreadList.add(threadHandler);
                    }
                    List<EventImpl> events = new ArrayList<EventImpl>();
                    synchronized (enabledThreadDeathRequestList)
                    {
                        for (ThreadDeathRequestImpl request : enabledThreadDeathRequestList)
                        {
                            events.add(new ThreadDeathEventImpl(getReference(), request, avt.getReference()));
                        }
                    }
                    getReference().eventQueue().addEventSet(new EventSetImpl(getReference(), events));
                    return true;
                }
                return false;
            }
        }
    }
    protected int createMonitor()
    {
        synchronized (monitorList)
        {
            synchronized (closedMonitorList)
            {
                if (closedMonitorList.size() != 0)
                {
                    int handler = closedMonitorList.remove(0);
                    Monitor monitor = new Monitor(this, handler);
                    monitorList.put(handler, monitor);
                    return handler;
                }
                else
                {
                    int handler = monitorList.size();
                    Monitor monitor = new Monitor(this, handler);
                    monitorList.put(handler, monitor);
                    return handler;
                }
            }
        }
    }
    protected Monitor getMonitor(int monitorHandler)
    {
        synchronized (monitorList)
        {
            return monitorList.get(monitorHandler);
        }
    }
    protected int deleteMonitor(int monitorHandler)
    {
        synchronized (monitorList)
        {
            if (monitorList.containsKey(monitorHandler))
            {
                Monitor monitor = monitorList.get(monitorHandler);
                synchronized (monitor)
                {
                    if (monitor.isMonitorIdle())
                    {
                        monitorList.remove(monitorHandler);
                        synchronized (closedMonitorList)
                        {
                            closedMonitorList.add(monitorHandler);
                        }
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            }
            return -1;
        }
    }
    protected List<Monitor> getOwnMonitorList(AssemblyVirtualThread avThread)
    {
        List<Monitor> ownMonitorList = new ArrayList<Monitor>();
        synchronized (monitorList)
        {
            for (Monitor monitor : monitorList.values())
            {
                if (monitor.getHoldsLockThread() == avThread)
                {
                    ownMonitorList.add(monitor);
                }
            }
        }
        return ownMonitorList;
    }
    protected void notifyMonitorsThreadDeath(AssemblyVirtualThread avThread)
    {
        synchronized (monitorList)
        {
            for (Monitor monitor : monitorList.values())
            {
                monitor.onThreadDeath(avThread);
            }
        }
    }
    public List<Monitor> getMonitorList()
    {
        synchronized (monitorList)
        {
            return Collections.unmodifiableList(new ArrayList<Monitor>(monitorList.values()));
        }
    }
    public synchronized void writeDataToNode(NodeContainerMap nodeContainerMap)
    {
        
    }
    public synchronized void readDataFromNode(NodeContainerMap nodeContainerMap)
    {
        
    }
    public synchronized void writeDataToNBT(NBTTagCompound nbtTagCompound)
    {
        boolean needSuspend = !isSuspend;
        if (needSuspend)
        {
            suspendVM();
        }
        nbtTagCompound.setBoolean("isRunning", this.isRunning);
        if (programPak != null)
        {
            NBTTagCompound programPakNbtTagCompound = new NBTTagCompound();
            programPak.writeToNBT(programPakNbtTagCompound);
            nbtTagCompound.setTag("programPak", (programPakNbtTagCompound));
        }
        if (biosPak != null)
        {
            NBTTagCompound biosPakNbtTagCompound = new NBTTagCompound();
            biosPak.writeToNBT(biosPakNbtTagCompound);
            nbtTagCompound.setTag("biosPak", biosPakNbtTagCompound);
        }
        if (ram != null)
        {
            NBTTagCompound ramNbtTagCompound = new NBTTagCompound();
            ram.writeToNBT(ramNbtTagCompound);
            nbtTagCompound.setTag("ram", ramNbtTagCompound);
        }
        if (closedThreadList != null)
        {
            synchronized (closedThreadList)
            {
                int[] temp1 = new int[closedThreadList.size()];
                for (int i = 0; i < closedThreadList.size(); i++)
                {
                    temp1[i] = closedThreadList.get(i);
                }
                nbtTagCompound.setIntArray("closedThreadList", temp1);
            }
        }
        if (avtList != null)
        {
            synchronized (avtList)
            {
                NBTTagList avtListNbtTagList = new NBTTagList();
                Iterator<AssemblyVirtualThread> avtListIterator = avtList.values().iterator();
                while (avtListIterator.hasNext())
                {
                    NBTTagCompound avtNbtTagCompound = new NBTTagCompound();
                    avtListIterator.next().writeToNBT(avtNbtTagCompound);
                    avtListNbtTagList.appendTag(avtNbtTagCompound);
                }
                nbtTagCompound.setTag("avtList", avtListNbtTagList);
            }
        }
        if (monitorList != null)
        {
            synchronized (monitorList)
            {
                NBTTagList monitorListNbtTagList = new NBTTagList();
                Iterator<Monitor> monitorListIterator = monitorList.values().iterator();
                while (monitorListIterator.hasNext())
                {
                    NBTTagCompound avtNbtTagCompound = new NBTTagCompound();
                    monitorListIterator.next().writeToNBT(avtNbtTagCompound);
                    monitorListNbtTagList.appendTag(avtNbtTagCompound);
                }
                nbtTagCompound.setTag("monitorList", monitorListNbtTagList);
            }
        }
        if (needSuspend)
        {
            resumeVM();
        }
    }
    public synchronized void readDataFormNBT(NBTTagCompound nbtTagCompound)
    {
        if (isRunning == true)
        {
            throw new IllegalStateException();
        }
        isRunning = nbtTagCompound.getBoolean("isRunning");
        if (nbtTagCompound.hasKey("programPak"))
        {
            programPak = new ProgramPackage();
            programPak.readFromNBT(nbtTagCompound.getCompoundTag("programPak"));
        }
        if (nbtTagCompound.hasKey("biosPak"))
        {
            biosPak = new ProgramPackage();
            biosPak.readFromNBT(nbtTagCompound.getCompoundTag("biosPak"));
        }
        ram = new AdvancedRAMArray(this);
        ram.readFromNBT(nbtTagCompound.getCompoundTag("ram"));
        synchronized (closedThreadList)
        {
            int[] temp1;
            temp1 = nbtTagCompound.getIntArray("closedThreadList");
            for (int i = 0; i < temp1.length; i++)
            {
                closedThreadList.add(temp1[i]);
            }
        }
        synchronized (avtList)
        {
            NBTTagList avtListNbtTagList = nbtTagCompound.getTagList("avtList", 10);
            for (int i = 0; i < avtListNbtTagList.tagCount(); i++)
            {
                NBTTagCompound avtNbtTagCompound = avtListNbtTagList.getCompoundTagAt(i);
                AssemblyVirtualThread avt = new AssemblyVirtualThread(this);
                avt.readFromNBT(avtNbtTagCompound);
                avtList.put(avt.getThreadHandler(), avt);
                synchronized (threadReferenceList)
                {
                    threadReferenceList.add(avt.getReference());
                }
            }
        }
        synchronized (monitorList)
        {
            NBTTagList monitorListNbtTagList = nbtTagCompound.getTagList("monitorList", 10);
            for (int i = 0; i < monitorListNbtTagList.tagCount(); i++)
            {
                NBTTagCompound monitorNbtTagCompound = monitorListNbtTagList.getCompoundTagAt(i);
                Monitor monitor = new Monitor(this);
                monitor.readFromNBT(monitorNbtTagCompound);
                monitorList.put(monitor.getMonitorHandler(), monitor);
            }
        }
    }
    public String getVersion()
    {
        return VERSION;
    }
    public String getName()
    {
        return "DCPU-32 VirtualMachine";
    }
    
    private List<VMDeathRequestImpl> enabledVMDeathRequestList = new ArrayList<VMDeathRequestImpl>();
    
    public void setVMDeathRequestState(VMDeathRequestImpl request, boolean isEnable)
    {
        synchronized (enabledVMDeathRequestList)
        {
            if (isEnable)
            {
                if (!enabledVMDeathRequestList.contains(request))
                {
                    enabledVMDeathRequestList.add(request);
                }
            }
            else
            {
                enabledVMDeathRequestList.remove(request);
            }
        }
    }
    
    private List<ThreadStartRequestImpl> enabledThreadStartRequestList = new ArrayList<ThreadStartRequestImpl>();
    
    public void setThreadStartRequestState(ThreadStartRequestImpl request, boolean isEnable)
    {
        synchronized (enabledThreadStartRequestList)
        {
            if (isEnable)
            {
                if (!enabledThreadStartRequestList.contains(request))
                {
                    enabledThreadStartRequestList.add(request);
                }
            }
            else
            {
                enabledThreadStartRequestList.remove(request);
            }
        }
    }
    
    private List<ThreadDeathRequestImpl> enabledThreadDeathRequestList = new ArrayList<ThreadDeathRequestImpl>();
    
    public void setThreadDeathRequestState(ThreadDeathRequestImpl request, boolean isEnable)
    {
        synchronized (enabledThreadDeathRequestList)
        {
            if (isEnable)
            {
                if (!enabledThreadDeathRequestList.contains(request))
                {
                    enabledThreadDeathRequestList.add(request);
                }
            }
            else
            {
                enabledThreadDeathRequestList.remove(request);
            }
        }
    }
    
    private Object referenceInitLock = new Object();
    private VirtualMachineReferenceImpl reference;
    private List<ThreadReferenceImpl> threadReferenceList = new ArrayList<ThreadReferenceImpl>();
    
    public VirtualMachineReferenceImpl getReference()
    {
        synchronized (referenceInitLock)
        {
            if (reference == null)
            {
                reference = new VirtualMachineReferenceImpl(this);
            }
            return reference;
        }
    }
    public List<ThreadReferenceImpl> getThreadReferenceList()
    {
        synchronized (threadReferenceList)
        {
            return new ArrayList<ThreadReferenceImpl>(threadReferenceList);
        }
    }
}