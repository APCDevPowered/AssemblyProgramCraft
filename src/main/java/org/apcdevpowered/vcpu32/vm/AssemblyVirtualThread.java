package org.apcdevpowered.vcpu32.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apcdevpowered.util.array.DynamicSparseArray;
import org.apcdevpowered.util.bit.BitTools;
import org.apcdevpowered.util.collection.HandlerAllocateList;
import org.apcdevpowered.util.integer.IntTools;
import org.apcdevpowered.util.unsigned.UnsignedTools;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import static org.apcdevpowered.vcpu32.vm.Registers.*;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class AssemblyVirtualThread
{
    //程序指针寄存器
    private int PC = -1;
    //调用栈帧
    private Stack<AVThreadStackFrame> stack = new Stack<AVThreadStackFrame>();
    private String threadName;
    private VirtualMachine vm;
    private AssemblyVirtualThread parentThread;
    private int startRAM;
    private volatile boolean isTerminated;
    private volatile boolean isRunning;
    private VMThread thread;
    private ArrayList<AssemblyVirtualThread> childThreadList = new ArrayList<AssemblyVirtualThread>();
    private int handlerValue;
    //监视器
    private List<Monitor> ownedMonitorList = new ArrayList<Monitor>();
    private Monitor requestLockMonitor;
    private Monitor requestWaitMonitor;
    private long waitingMonitorTimeout = -1;
    private boolean isWaitingMonitor;
    //保存载入时使用
    public int parentThreadHandlerValue = -1;
    public int[] childThreadHandlerValues;
    public int[] ownedMonitorHandlers;
    public int requestLockMonitorHandler = -1;
    public int requestWaitMonitorHandler = -1;
    //暂停线程
    private int defaultThreadSuspendHandler;
    private HandlerAllocateList<Void> threadSuspendHandlerList;
    private ConcurrentSkipListSet<Integer> suspendThreadSuspendHandlerList = new ConcurrentSkipListSet<Integer>();
    
    private Object suspendLock = new Object();
    private Object lockMonitorLock = new Object(); 
    private Object waitMonitorLock = new Object();
    
    protected AssemblyVirtualThread(VirtualMachine vm)
    {
        this.vm = vm;
    }
    protected AssemblyVirtualThread(VirtualMachine vm, int startRAM, String threadName, int handlerValue)
    {
        this.thread = new VMThread();
        this.thread.setPriority(Thread.MIN_PRIORITY);
        this.handlerValue = handlerValue;
        this.startRAM = startRAM;
        this.vm = vm;
        this.threadName = threadName;
        this.threadSuspendHandlerList = new HandlerAllocateList<Void>();
        this.defaultThreadSuspendHandler = createThreadSuspendHandler();
    }
    public ThreadState getThreadState()
    {
        if(isTerminated)
        {
            return ThreadState.TERMINATED;
        }
        if(!isRunning)
        {
            return ThreadState.NEW;
        }
        if(requestLockMonitor != null)
        {
            return ThreadState.BLOCKED;
        }
        if(requestWaitMonitor != null)
        {
            if(waitingMonitorTimeout < 0)
            {
                return ThreadState.WATTING;
            }
            else
            {
                return ThreadState.TIMED_WAITING;
            }
        }
        if(thread.sleepTime != 0)
        {
            return ThreadState.TIMED_WAITING;
        }
        return ThreadState.RUNNABLE;
    }
    public int getPC()
    {
        return PC;
    }
    public boolean isRunning()
    {
        return isRunning;
    }
    public VirtualMachine getVM()
    {
        return vm;
    }
    public String getThreadName()
    {
        return threadName;
    }
    public Stack<AVThreadStackFrame> getStack()
    {
        return stack;
    }
    public synchronized boolean start()
    {
        if(!isRunning && !isTerminated)
        {
            enterMainMethod(startRAM);
            thread.start();
            isRunning = true;
            return true;
        }
        else
        {
            return false;
        }
    }
    public synchronized void shutdown()
    {
        if(!isRunning)
        {
            if(parentThread != null)
            {
                parentThread.removeChild(AssemblyVirtualThread.this);
            }
            synchronized(childThreadList)
            {
                for(int i = 0;i < childThreadList.size();i++)
                {
                    childThreadList.get(i).halt();
                }
            }
            getVM().removeFromThreadList(getThreadHandler());
            getVM().notifyMonitorsThreadDeath(AssemblyVirtualThread.this);
            isRunning = false;
            isTerminated = true;
        }
        else
        {
            thread.shutdownRequest = true;
            synchronized(thread.threadSleepLock)
            {
                thread.threadSleepLock.notifyAll();
            }
            synchronized(thread.resumeNotifier)
            {
                thread.resumeNotifier.notifyAll();
            }
            synchronized(lockMonitorLock)
            {
                if(requestLockMonitor != null)
                {
                    synchronized(requestLockMonitor)
                    {
                        requestLockMonitor.notifyAll();
                    }
                }
            }
            synchronized(waitMonitorLock)
            {
                if(requestWaitMonitor != null)
                {
                    synchronized(requestWaitMonitor)
                    {
                        requestWaitMonitor.notifyAll();
                    }
                }
            }
        }
    }
    public synchronized boolean load()
    {
        if(isRunning && !thread.isAlive())
        {
            thread.start();
            return true;
        }
        else
        {
            return false;
        }
    }
    protected void addChild(AssemblyVirtualThread avt)
    {
        synchronized(childThreadList)
        {
            childThreadList.add(avt);
        }
    }
    protected void removeChild(AssemblyVirtualThread ChildThread)
    {
        synchronized(childThreadList)
        {
            childThreadList.remove(ChildThread);
        }
    }
    private void halt()
    {
        thread.timeToQuit = true;
        if(parentThread != null)
        {
            parentThread.removeChild(this);
        }
        synchronized(childThreadList)
        {
            for(int i = 0;i < childThreadList.size();i++)
            {
                childThreadList.get(i).halt();
            }
        }
        getVM().removeFromThreadList(getThreadHandler());
        getVM().notifyMonitorsThreadDeath(this);
        synchronized(thread.threadSleepLock)
        {
            thread.threadSleepLock.notifyAll();
        }
        synchronized(thread.resumeNotifier)
        {
            thread.resumeNotifier.notifyAll();
        }
        synchronized(lockMonitorLock)
        {
            if(requestLockMonitor != null)
            {
                synchronized(requestLockMonitor)
                {
                    requestLockMonitor.notifyAll();
                }
            }
        }
        synchronized(waitMonitorLock)
        {
            if(requestWaitMonitor != null)
            {
                synchronized(requestWaitMonitor)
                {
                    requestWaitMonitor.notifyAll();
                }
            }
        }
    }
    public List<Monitor> getOwnedMonitorList()
    {
        synchronized(ownedMonitorList)
        {
            return new ArrayList<Monitor>(ownedMonitorList);
        }
    }
    public Monitor getRequestLockMonitor()
    {
        return requestLockMonitor;
    }
    public Monitor getRequestWaitMonitor()
    {
        return requestWaitMonitor;
    }
    public long getWaitingMonitorTimeout()
    {
        return waitingMonitorTimeout;
    }
    public boolean isWaiting()
    {
        return isWaitingMonitor;
    }
    public boolean isSuspend()
    {
        return isSuspend(defaultThreadSuspendHandler);
    }
    public boolean isSuspend(int suspendHandler)
    {
        synchronized(suspendLock)
        {
            return suspendThreadSuspendHandlerList.contains(suspendHandler);
        }
    }
    public int createThreadSuspendHandler()
    {
        synchronized(suspendLock)
        {
            return threadSuspendHandlerList.allocate(null);
        }
    }
    public boolean deleteThreadSuspendHandler(int suspendHandler)
    {
        synchronized(suspendLock)
        {
            if(threadSuspendHandlerList.vaild(suspendHandler))
            {
                resumeThread(suspendHandler);
                return threadSuspendHandlerList.free(suspendHandler);
            }
            else
            {
                return false;
            }
        }
    }
    public synchronized boolean suspendThread(int suspendHandler)
    {
        synchronized(suspendLock)
        {
            if(!threadSuspendHandlerList.vaild(suspendHandler))
            {
                return false;
            }
            synchronized(thread.suspendNotifier)
            {
                synchronized(thread.threadSleepLock)
                {
                    synchronized(lockMonitorLock)
                    {
                        synchronized(waitMonitorLock)
                        {
                            synchronized(suspendThreadSuspendHandlerList)
                            {
                                if(!suspendThreadSuspendHandlerList.add(suspendHandler))
                                {
                                    return false;
                                }
                            }
                            thread.threadSleepLock.notifyAll();
                            if(requestLockMonitor != null)
                            {
                                synchronized(requestLockMonitor)
                                {
                                    requestLockMonitor.notifyAll();
                                }
                            }
                            if(requestWaitMonitor != null)
                            {
                                synchronized(requestWaitMonitor)
                                {
                                    requestWaitMonitor.notifyAll();
                                }
                            }
                        }
                    }
                }
                if(isRunning)
                {
                    try
                    {
                        thread.suspendNotifier.wait();
                    }
                    catch (InterruptedException e)
                    {
                        
                    }
                }
            }
        }
        return true;
    }
    public synchronized boolean resumeThread(int suspendHandler)
    {
        synchronized(suspendLock)
        {
            synchronized(suspendThreadSuspendHandlerList)
            {
                if(suspendThreadSuspendHandlerList.remove(Integer.valueOf(suspendHandler)))
                {
                    if(suspendThreadSuspendHandlerList.isEmpty())
                    {
                        synchronized(thread.resumeNotifier)
                        {
                            thread.resumeNotifier.notifyAll();
                        }
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }
    protected synchronized void suspendThread()
    {
        suspendThread(defaultThreadSuspendHandler);
    }
    protected synchronized void resumeThread()
    {
        resumeThread(defaultThreadSuspendHandler);
    }
    protected synchronized void notifyVMSuspend()
    {
        if(!isRunning)
        {
            vm.notifyThreadSuspend(handlerValue);
            return;
        }
        synchronized(thread.threadSleepLock)
        {
            synchronized(lockMonitorLock)
            {
                synchronized(waitMonitorLock)
                {
                    thread.threadSleepLock.notifyAll();
                    if(requestLockMonitor != null)
                    {
                        synchronized(requestLockMonitor)
                        {
                            requestLockMonitor.notifyAll();
                        }
                    }
                    if(requestWaitMonitor != null)
                    {
                        synchronized(requestWaitMonitor)
                        {
                            requestWaitMonitor.notifyAll();
                        }
                    }
                }
            }
        }
    }
    public void join()
    {
        while(true)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                continue;
            }
            break;
        }
    }
    private void checkType(int[] optInfo, boolean[] par1Types, boolean[] par2Types, boolean[] par3Types, boolean[] par4Types)
    {
        if(par1Types[optInfo[1]] == false)
        {
            System.out.println("[VCPU-32]错误的参数类型");
            halt();
        }
        if(par2Types[optInfo[2]] == false)
        {
            System.out.println("[VCPU-32]错误的参数类型");
            halt();
        }
        if(par3Types[optInfo[3]] == false)
        {
            System.out.println("[VCPU-32]错误的参数类型");
            halt();
        }
        if(par4Types[optInfo[4]] == false)
        {
            System.out.println("[VCPU-32]错误的参数类型");
            halt();
        }
    }
    protected void setParentThread(AssemblyVirtualThread parentavt)
    {
        parentThread = parentavt;
    }
    //********工具函数定义开始********
    public static void getOptInfo(int opt, int optInfo[])
    {
        optInfo[0] = BitTools.copyBit(opt, 0, 0, 0, 12);
        optInfo[1] = BitTools.copyBit(opt, 29, 0, 0, 3);
        optInfo[2] = BitTools.copyBit(opt, 26, 0, 0, 3);
        optInfo[3] = BitTools.copyBit(opt, 23, 0, 0, 3);
        optInfo[4] = BitTools.copyBit(opt, 20, 0, 0, 3);
        optInfo[5] = BitTools.copyBit(opt, 12, 0, 0, 8);
    }
    public static int getParCount(int[] optInfo)
    {
        int count = 0;
        if(optInfo[1] != 0)
        {
            count++;
        }
        if(optInfo[2] != 0)
        {
            count++;
        }
        if(optInfo[3] != 0)
        {
            count++;
        }
        if(optInfo[4] != 0)
        {
            count++;
        }
        return count;
    }
    protected String getStringValue(int RAMIdx)
    {
        if(RAMIdx >= vm.ram.getSize() || RAMIdx < 0)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            halt();
            return "";
        }
        else
        {
            return getVM().ram.readStringFromAddress(RAMIdx);
        }
    }
    protected int getRAMREGValue(int register)
    {
        return getRAMValue(getRegisterValue(register));
    }
    protected void setRAMREGValue(int register, int value)
    {
        setRAMValue(getRegisterValue(register),value);
    }
    protected int getRAMValue(int RAMIdx)
    {
        if(RAMIdx >= vm.ram.getSize() || RAMIdx < 0)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            halt();
            return 0;
        }
        else
        {
            return getVM().ram.getValue(RAMIdx);
        }
    }
    protected void setRAMValue(int RAMIdx, int value)
    {
        if(RAMIdx >= vm.ram.getSize() || RAMIdx < 0)
        {
            System.out.println("[VCPU-32]内存寻址超出范围");
            halt();
        }
        else
        {
            getVM().ram.setValue(RAMIdx, value);
        }
    }
    protected int getRegisterValue(int register)
    {
        if(register == 1)
        {
            return stack.peek().A;
        }
        else if(register == 2)
        {
            return stack.peek().B;
        }
        else if(register == 3)
        {
            return stack.peek().C;
        }
        else if(register == 4)
        {
            return stack.peek().X;
        }
        else if(register == 5)
        {
            return stack.peek().Y;
        }
        else if(register == 6)
        {
            return stack.peek().Z;
        }
        else if(register == 7)
        {
            return stack.peek().I;
        }
        else if(register == 8)
        {
            return stack.peek().J;
        }
        else if(register == 9)
        {
            return stack.peek().O;
        }
        else if(register == 10)
        {
            return PC;
        }
        else if(register == 11)
        {
            return stack.peek().SP;
        }
        else
        {
            System.out.println("[VCPU-32]错误的参数值");
            halt();
            return 0;
        }
    }
    protected void setRegisterValue(int register, int value)
    {
        if(register == REG_A)
        {
            stack.peek().A = value;
        }
        else if(register == REG_B)
        {
            stack.peek().B = value;
        }
        else if(register == REG_C)
        {
            stack.peek().C = value;
        }
        else if(register == REG_X)
        {
            stack.peek().X = value;
        }
        else if(register == REG_Y)
        {
            stack.peek().Y = value;
        }
        else if(register == REG_Z)
        {
            stack.peek().Z = value;
        }
        else if(register == REG_I)
        {
            stack.peek().I = value;
        }
        else if(register == REG_J)
        {
            stack.peek().J = value;
        }
        else if(register == REG_O)
        {
            stack.peek().O = value;
        }
        else if(register == REG_PC)
        {
            PC = value;
        }
        else if(register == REG_SP)
        {
            stack.peek().SP = value;
        }
        else
        {
            System.out.println("[VCPU-32]错误的参数值");
            halt();
        }
    }
    protected Object getObjectValue(int type, int idx)
    {
        if(type == 1)
        {
            return getRegisterValue(idx);
        }
        else if(type == 2)
        {
            return getRAMValue(idx);
        }
        else if(type == 3)
        {
            return idx;
        }
        else if(type == 4)
        {
            return getStringValue(idx);
        }
        else if(type == 5)
        {
            return getRAMREGValue(idx);
        }
        else
        {
            System.out.println("[VCPU-32]此参数类型不能被GET");
            halt();
            return 0;
        }
    }
    protected void setObjectValue(int type, int idx, int value)
    {
        if(type == 1)
        {
            setRegisterValue(idx,value);
        }
        else if(type == 2)
        {
            setRAMValue(idx,value);
        }
        else if(type == 5)
        {
            setRAMREGValue(idx,value);
        }
        else
        {
            System.out.println("[VCPU-32]此参数类型不能被SET");
            halt();
        }
    }
    protected void pushInCurrentStackFrame(int num)
    {
        if(stack.isEmpty())
        {
            System.out.println("[VCPU-32]当前栈帧为空");
            halt();
            return;
        }
        stack.peek().push(num);
    }
    protected int popInCurrentStackFrame()
    {
        if(stack.isEmpty())
        {
            System.out.println("[VCPU-32]当前栈帧为空");
            halt();
            return 0;
        }
        if(stack.peek().stack.size() < 0)
        {
            System.out.println("[VCPU-32]栈顶数据不足");
            halt();
            return 0;
        }
        return stack.peek().pop();
    }
    protected void dupInCurrentStackFrame(int num)
    {
        if(stack.isEmpty())
        {
            System.out.println("当前栈帧为空");
            halt();
            return;
        }
        stack.peek().dup(num);
    }
    protected void swapInCurrentStackFrame()
    {
        if(stack.isEmpty())
        {
            System.out.println("[VCPU-32]当前栈帧为空");
            halt();
            return;
        }
        stack.peek().swap();
    }
    protected int getCurrentStackFrameReturnAddress()
    {
        if(stack.isEmpty())
        {
            System.out.println("[VCPU-32]当前栈帧为空");
            halt();
            return 0;
        }
        int returnAddress = stack.peek().returnAddress;
        if(returnAddress < 0)
        {
            System.out.println("[VCPU-32]尝试返回到不存在的返回地址");
            halt();
            return 0;
        }
        return returnAddress;
    }
    protected int getCurrentStackFrameParLength()
    {
        if(stack.isEmpty())
        {
            System.out.println("[VCPU-32]当前栈帧为空");
            halt();
            return 0;
        }
        int parLength = stack.peek().parLength;
        if(parLength < 0)
        {
            System.out.println("[VCPU-32]参数长度不能为负数");
            halt();
            return 0;
        }
        return parLength;
    }
    protected void enterMainMethod(int enterAddress)
    {
        enterMethod(enterAddress, 0, -1);
    }
    protected void enterMethod(int enterAddress)
    {
        enterMethod(enterAddress, 0, getRegisterValue(REG_PC));
    }
    protected void enterMethod(int enterAddress, int parLength)
    {
        enterMethod(enterAddress, parLength, getRegisterValue(REG_PC));
    }
    protected void enterMethod(int enterAddress, int parLength, int returnAddress)
    {
        AVThreadStackFrame stackFrame = new AVThreadStackFrame();
        stackFrame.enterAddress = enterAddress;
        stackFrame.parLength = parLength;
        stackFrame.returnAddress = returnAddress;
        if(parLength > 0)
        {
            int[] tmp = new int[parLength];
            for(int i = 0;i < parLength;i++)
            {
                tmp[tmp.length - 1 - i] = popInCurrentStackFrame();
                if(thread.timeToQuit == true)
                {
                    return;
                }
            }
            stack.push(stackFrame);
            for(int i = 0;i < tmp.length;i++)
            {
                pushInCurrentStackFrame(tmp[i]);
                if(thread.timeToQuit == true)
                {
                    return;
                }
            }
        }
        else
        {
            stack.push(stackFrame);
        }
        setRegisterValue(REG_PC, enterAddress);
        if(thread.timeToQuit == true)
        {
            return;
        }
    }
    protected void exitMethod()
    {
        exitMethod(0);
    }
    protected void exitMethod(int parLength)
    {
        int returnAddress = getCurrentStackFrameReturnAddress();
        if(thread.timeToQuit == true)
        {
            return;
        }
        if(parLength > 0)
        {
            int[] tmp = new int[parLength];
            for(int i = 0;i < parLength;i++)
            {
                tmp[tmp.length - 1 - i] = popInCurrentStackFrame();
                if(thread.timeToQuit == true)
                {
                    return;
                }
            }
            stack.pop().isInvalid = true;
            for(int i = 0;i < tmp.length;i++)
            {
                pushInCurrentStackFrame(tmp[i]);
                if(thread.timeToQuit == true)
                {
                    return;
                }
            }
        }
        else
        {
            stack.pop().isInvalid = true;
        }
        setRegisterValue(REG_PC, returnAddress);
        if(thread.timeToQuit == true)
        {
            return;
        }
    }
    public AVThreadStackFrame getStackFrame(int stackFrameIndex)
    {
        if(stackFrameIndex < 0)
        {
            stackFrameIndex = -(stackFrameIndex + 1);
        }
        else
        {
            stackFrameIndex = this.stack.size() - stackFrameIndex - 1;
        }
        if(stackFrameIndex < 0 || stackFrameIndex >= this.stack.size())
        {
            return null;
        }
        else
        {
            return stack.get(stackFrameIndex);
        }
    }
    public int getThreadHandler()
    {
        return handlerValue;
    }
    public void printThreadInfo()
    {
        VMsLogger.printThreadInfo(this);
    }
    public void loadThreadRelation()
    {
        if(parentThreadHandlerValue != -1)
        {
            setParentThread(vm.getVMThread(parentThreadHandlerValue));
            parentThreadHandlerValue = -1;
        }
        if(childThreadHandlerValues != null)
        {
            for(int i = 0 ; i < childThreadHandlerValues.length ; i++)
            {
                addChild(vm.getVMThread(childThreadHandlerValues[i]));
            }
            childThreadHandlerValues = null;
        }
        if(ownedMonitorHandlers != null)
        {
            for(int i = 0 ; i < ownedMonitorHandlers.length ; i++)
            {
                ownedMonitorList.add(vm.getMonitor(ownedMonitorHandlers[i]));
            }
            ownedMonitorHandlers = null;
        }
        if(requestLockMonitorHandler != -1)
        {
            requestLockMonitor = vm.getMonitor(requestLockMonitorHandler);
        }
        if(requestWaitMonitorHandler != -1)
        {
            requestWaitMonitor = vm.getMonitor(requestWaitMonitorHandler);
        }
    }
    public void writeToNBT(NBTTagCompound avtNbtTagCompound)
    {
        avtNbtTagCompound.setInteger("PC", PC);
        if(stack != null)
        {
            NBTTagList stackNbtTagList = new NBTTagList();
            for(int i = 0;i < stack.size();i++)
            {
                AVThreadStackFrame stackFrame = stack.get(i);
                NBTTagCompound stackFrameNbtTagCompound = new NBTTagCompound();
                stackFrame.writeToNBT(stackFrameNbtTagCompound);
                stackNbtTagList.appendTag(stackFrameNbtTagCompound);
            }
            avtNbtTagCompound.setTag("stack", stackNbtTagList);
        }
        avtNbtTagCompound.setString("threadName", threadName);
        if(parentThread != null)
        {
            avtNbtTagCompound.setInteger("parentThreadHandlerValue", parentThread.getThreadHandler());
        }
        avtNbtTagCompound.setInteger("startRAM", startRAM);
        avtNbtTagCompound.setBoolean("isRunning", isRunning);
        avtNbtTagCompound.setBoolean("isTerminated", isTerminated);
        avtNbtTagCompound.setInteger("handlerValue", handlerValue);
        
        {
            int temp[] = new int[childThreadList.size()];
            for(int i = 0 ; i < childThreadList.size() ;  i++)
            {
                temp[i] = childThreadList.get(i).handlerValue;
            }
            avtNbtTagCompound.setIntArray("childThreadHandlerValues", temp);
        }
        
        avtNbtTagCompound.setBoolean("shutdownRequest", thread.shutdownRequest);
        avtNbtTagCompound.setBoolean("timeToQuit", thread.timeToQuit);
        avtNbtTagCompound.setInteger("sleepTime", thread.sleepTime);
        avtNbtTagCompound.setBoolean("breakNextInstruction", thread.breakNextInstruction);
        
        {
            int temp[] = new int[ownedMonitorList.size()];
            for(int i = 0 ; i < ownedMonitorList.size() ;  i++)
            {
                temp[i] = ownedMonitorList.get(i).getMonitorHandler();
            }
            avtNbtTagCompound.setIntArray("ownedMonitorHandlers", temp);
        }
        if(requestLockMonitor != null)
        {
            avtNbtTagCompound.setInteger("requestLockMonitorHandler", requestLockMonitor.getMonitorHandler());
        }
        if(requestWaitMonitor != null)
        {
            avtNbtTagCompound.setInteger("requestWaitMonitorHandler", requestWaitMonitor.getMonitorHandler());
        }
        avtNbtTagCompound.setLong("waitingMonitorTimeoutTime", waitingMonitorTimeout);
        avtNbtTagCompound.setBoolean("isWaitingMonitor", isWaitingMonitor);
        avtNbtTagCompound.setInteger("defaultThreadSuspendHandler", defaultThreadSuspendHandler);
        if(threadSuspendHandlerList != null)
        {
            NBTTagCompound threadSuspendHandlerListNbtTagCompound = new NBTTagCompound();
            threadSuspendHandlerListNbtTagCompound.setIntArray("handlerList", IntTools.toIntArray(threadSuspendHandlerList.getHandlerList().keySet().toArray(new Integer[0])));
            threadSuspendHandlerListNbtTagCompound.setIntArray("closedHandlerList", IntTools.toIntArray(threadSuspendHandlerList.getClosedHandlerList().toArray(new Integer[0])));
            avtNbtTagCompound.setTag("threadSuspendHandlerList", threadSuspendHandlerListNbtTagCompound);
        }
    }
    public void readFromNBT(NBTTagCompound avtNbtTagCompound)
    {
        PC = avtNbtTagCompound.getInteger("PC");
        if(avtNbtTagCompound.hasKey("stack"))
        {
            NBTTagList stackNbtTagList = avtNbtTagCompound.getTagList("stack", 10);
            for (int i = 0; i < stackNbtTagList.tagCount(); ++i)
            {
                NBTTagCompound stackFrameNbtTagCompound = stackNbtTagList.getCompoundTagAt(i);
                AVThreadStackFrame stackFrame = new AVThreadStackFrame();
                stackFrame.readFromNBT(stackFrameNbtTagCompound);
                stack.add(stackFrame);
            }
        }
        threadName = avtNbtTagCompound.getString("threadName");
        parentThreadHandlerValue = avtNbtTagCompound.getInteger("parentThreadHandlerValue");
        startRAM = avtNbtTagCompound.getInteger("startRAM");
        isRunning = avtNbtTagCompound.getBoolean("isRunning");
        isTerminated = avtNbtTagCompound.getBoolean("isTerminated");
        handlerValue =     avtNbtTagCompound.getInteger("handlerValue");
        childThreadHandlerValues = avtNbtTagCompound.getIntArray("childThreadHandlerValues");
        
        thread = new VMThread();
        thread.shutdownRequest = avtNbtTagCompound.getBoolean("shutdownRequest");
        thread.timeToQuit = avtNbtTagCompound.getBoolean("timeToQuit");
        thread.sleepTime = avtNbtTagCompound.getInteger("sleepTime");
        thread.breakNextInstruction = avtNbtTagCompound.getBoolean("breakNextInstruction");
        
        ownedMonitorHandlers = avtNbtTagCompound.getIntArray("ownedMonitorHandlers");
        if(avtNbtTagCompound.hasKey("requestLockMonitorHandler"))
        {
            requestLockMonitorHandler = avtNbtTagCompound.getInteger("requestLockMonitorHandler");
        }
        if(avtNbtTagCompound.hasKey("requestWaitMonitorHandler"))
        {
            requestWaitMonitorHandler = avtNbtTagCompound.getInteger("requestWaitMonitorHandler");
        }
        waitingMonitorTimeout = avtNbtTagCompound.getLong("waitingMonitorTimeoutTime");
        isWaitingMonitor = avtNbtTagCompound.getBoolean("isWaitingMonitor");
        defaultThreadSuspendHandler = avtNbtTagCompound.getInteger("defaultThreadSuspendHandler");
        if(avtNbtTagCompound.hasKey("threadSuspendHandlerList"))
        {
            NBTTagCompound threadSuspendHandlerListNbtTagCompound = avtNbtTagCompound.getCompoundTag("threadSuspendHandlerList");
            int[] handlerNbtTagIntArray = threadSuspendHandlerListNbtTagCompound.getIntArray("handlerList");
            int[] closedHandlerNbtTagIntArray = threadSuspendHandlerListNbtTagCompound.getIntArray("closedHandlerList");
            Map<Integer, Void> handlerList = new HashMap<Integer, Void>();
            List<Integer> closedHandlerList = new ArrayList<Integer>();
            for(int handler : handlerNbtTagIntArray)
            {
                handlerList.put(handler, null);
            }
            for(int handler : closedHandlerNbtTagIntArray)
            {
                closedHandlerList.add(handler);
            }
            threadSuspendHandlerList = new HandlerAllocateList<Void>(10, handlerList, closedHandlerList);
        }
    }
    //********工具函数定义结束********
    public class AVThreadStackFrame
    {
        private volatile boolean isInvalid;
        
        //通用寄存器（registers）
        private int A;
        private int B;
        private int C;
        private int X;
        private int Y;
        private int Z;
        private int I;
        private int J;
        //运算溢出寄存器（需要手动重置）
        private int O;
        //栈指针寄存器
        private int SP;
        //数据栈
        public DynamicSparseArray<Integer> stack = new DynamicSparseArray<Integer>();
        //返回地址
        public int returnAddress = -1;
        //入口地址
        public int enterAddress = -1;
        //参数长度
        public int parLength;
        
        public int getRegisterValue(int register)
        {
            switch(register)
            {
                case REG_A:
                    return A;
                case REG_B:
                    return B;
                case REG_C:
                    return C;
                case REG_X:
                    return X;
                case REG_Y:
                    return Y;
                case REG_Z:
                    return Z;
                case REG_I:
                    return I;
                case REG_J:
                    return J;
                case REG_O:
                    return O;
                case REG_PC:
                    return PC;
                case REG_SP:
                    return SP;
                default:
                    throw new IllegalArgumentException("Register not found");
            }
        }
        public void setRegisterValue(int register, int value)
        {
            switch(register)
            {
                case REG_A:
                    A = value;
                    return;
                case REG_B:
                    B = value;
                    return;
                case REG_C:
                    C = value;
                    return;
                case REG_X:
                    X = value;
                    return;
                case REG_Y:
                    Y = value;
                    return;
                case REG_Z:
                    Z = value;
                    return;
                case REG_I:
                    I = value;
                    return;
                case REG_J:
                    J = value;
                    return;
                case REG_O:
                    O = value;
                    return;
                case REG_PC:
                    PC = value;
                    return;
                case REG_SP:
                    SP = value;
                    return;
                default:
                    throw new IllegalArgumentException("Register not found");
            }
        }
        
        public void push(int value)
        {
            if(SP < 0)
            {
                System.out.println("[VCPU-32]栈指针寄存器值不正确");
                halt();
                return;
            }
            stack.set(SP++, value);
        }
        public int pop()
        {
            if(SP < 1)
            {
                System.out.println("[VCPU-32]栈指针寄存器值不正确");
                halt();
                return 0;
            }
            return safeGetStack(--SP);
        }
        public void dup(int num)
        {
            if(SP < num)
            {
                System.out.println("[VCPU-32]栈指针寄存器值不正确");
                halt();
                return;
            }
            stack.copyData(stack, 0, stack.length(), stack.length());
            SP += num;
        }
        public void swap()
        {
            if(SP < 2)
            {
                System.out.println("[VCPU-32]栈指针寄存器值不正确");
                halt();
                return;
            }
            int first = safeGetStack(SP - 1);
            int second = safeGetStack(SP - 2);
            stack.set(SP - 2, first);
            stack.set(SP - 1, second);
        }
        
        private int safeGetStack(int index)
        {
            Integer num = stack.get(index);
            return num == null ? 0 : num;
        }
        
        public boolean isInvalid()
        {
            return isInvalid;
        }
        
        public void writeToNBT(NBTTagCompound Stackframenbttagcompound)
        {
            Stackframenbttagcompound.setInteger("A",A);
            Stackframenbttagcompound.setInteger("B",B);
            Stackframenbttagcompound.setInteger("C",C);
            Stackframenbttagcompound.setInteger("X",X);
            Stackframenbttagcompound.setInteger("Y",Y);
            Stackframenbttagcompound.setInteger("Z",Z);
            Stackframenbttagcompound.setInteger("I",I);
            Stackframenbttagcompound.setInteger("J",J);
            Stackframenbttagcompound.setInteger("O",O);
            Stackframenbttagcompound.setInteger("SP",SP);
            Stackframenbttagcompound.setInteger("returnAddress", returnAddress);
            Stackframenbttagcompound.setInteger("enterAddress", enterAddress);
            Stackframenbttagcompound.setInteger("parLength", parLength);
            Stackframenbttagcompound.setIntArray("stack", IntTools.toIntArray(stack.toArray(new Integer[stack.length()])));
        }
        public void readFromNBT(NBTTagCompound Stackframenbttagcompound)
        {
            A = Stackframenbttagcompound.getInteger("A");
            B = Stackframenbttagcompound.getInteger("B");
            C = Stackframenbttagcompound.getInteger("C");
            X = Stackframenbttagcompound.getInteger("X");
            Y = Stackframenbttagcompound.getInteger("Y");
            Z = Stackframenbttagcompound.getInteger("Z");
            I = Stackframenbttagcompound.getInteger("I");
            J = Stackframenbttagcompound.getInteger("J");
            O = Stackframenbttagcompound.getInteger("O");
            SP = Stackframenbttagcompound.getInteger("SP");
            returnAddress = Stackframenbttagcompound.getInteger("returnAddress");
            enterAddress = Stackframenbttagcompound.getInteger("enterAddress");
            parLength = Stackframenbttagcompound.getInteger("parLength");
            int[] stackdata = Stackframenbttagcompound.getIntArray("stack");
            if(stackdata != null)
            {
                stack.clear();
                stack.addAll(IntTools.toIntegerArray(stackdata));
            }
        }
        @Override
        public String toString()
        {
            return "StackFrame: " + enterAddress;
        }
    }
    public class VMThread extends Thread
    {
        private volatile boolean shutdownRequest = false;
        private volatile boolean timeToQuit = false;
        private volatile int sleepTime = 0;
        private boolean breakNextInstruction = false;
        
        private Object suspendNotifier = new Object();
        private Object resumeNotifier = new Object();
        private Object threadSleepLock = new Object();
        
        public VMThread()
        {
            super("VCPU-32VirtualThread");
        }
        @SuppressWarnings("unused")
        public void run()
        {
            try
            {
                interrupt : while(true)
                {
                    if(timeToQuit)
                    {
                        break interrupt;
                    }
                    if(shutdownRequest)
                    {
                        if(parentThread != null)
                        {
                            parentThread.removeChild(AssemblyVirtualThread.this);
                        }
                        synchronized(childThreadList)
                        {
                            for(int i = 0;i < childThreadList.size();i++)
                            {
                                childThreadList.get(i).halt();
                            }
                        }
                        getVM().removeFromThreadList(getThreadHandler());
                        getVM().notifyMonitorsThreadDeath(AssemblyVirtualThread.this);
                        break interrupt;
                    }
                    skipSleep:if(sleepTime > 0)
                    {
                        synchronized(threadSleepLock)
                        {
                            if(timeToQuit)
                            {
                                break interrupt;
                            }
                            if(shutdownRequest)
                            {
                                continue interrupt;
                            }
                            if(!suspendThreadSuspendHandlerList.isEmpty() || vm.isSuspend())
                            {
                                break skipSleep;
                            }
                            while(sleepTime > 0)
                            {
                                long startSleepTime = System.currentTimeMillis();
                                if(!timeToQuit && !shutdownRequest)
                                {
                                    threadSleepLock.wait(sleepTime);
                                    sleepTime = (int)(sleepTime - (System.currentTimeMillis() - startSleepTime));
                                    if(sleepTime < 0)
                                    {
                                        sleepTime = 0;
                                    }
                                }
                                if(timeToQuit)
                                {
                                    break interrupt;
                                }
                                if(shutdownRequest)
                                {
                                    continue interrupt;
                                }
                                if(!suspendThreadSuspendHandlerList.isEmpty() || vm.isSuspend())
                                {
                                    break skipSleep;
                                }
                            }
                        }
                    }
                    skipLockMonitor:if(requestLockMonitor != null)
                    {
                        synchronized(requestLockMonitor)
                        {
                            if(timeToQuit)
                            {
                                break interrupt;
                            }
                            if(shutdownRequest)
                            {
                                continue interrupt;
                            }
                            if(!suspendThreadSuspendHandlerList.isEmpty() || vm.isSuspend())
                            {
                                break skipLockMonitor;
                            }
                            if(!requestLockMonitor.isVaild())
                            {
                                setRegisterValue(REG_O, 0x1);
                                synchronized(lockMonitorLock)
                                {
                                    requestLockMonitor = null;
                                }
                                continue interrupt;
                            }
                            else
                            {
                                while(requestLockMonitor.lockMonitor(AssemblyVirtualThread.this) == -1)
                                {
                                    requestLockMonitor.waitLockNotify();
                                    if(timeToQuit)
                                    {
                                        break interrupt;
                                    }
                                    if(shutdownRequest)
                                    {
                                        continue interrupt;
                                    }
                                    if(!suspendThreadSuspendHandlerList.isEmpty() || vm.isSuspend())
                                    {
                                        break skipLockMonitor;
                                    }
                                }
                                synchronized(ownedMonitorList)
                                {
                                    ownedMonitorList.add(requestLockMonitor);
                                }
                                synchronized(lockMonitorLock)
                                {
                                    requestLockMonitor = null;
                                }
                            }
                        }
                    }
                    skipWaitMonitor:if(requestWaitMonitor != null)
                    {
                        synchronized(requestWaitMonitor)
                        {
                            if(timeToQuit)
                            {
                                break interrupt;
                            }
                            if(shutdownRequest)
                            {
                                continue interrupt;
                            }
                            if(!suspendThreadSuspendHandlerList.isEmpty() || vm.isSuspend())
                            {
                                break skipWaitMonitor;
                            }
                            if(!requestWaitMonitor.isVaild())
                            {
                                setRegisterValue(REG_O, 0x1);
                                synchronized(waitMonitorLock)
                                {
                                    requestWaitMonitor = null;
                                    waitingMonitorTimeout = -1;
                                }
                                isWaitingMonitor = false;
                                continue interrupt;
                            }
                            else
                            {
                                if(!isWaitingMonitor)
                                {
                                    if(requestWaitMonitor.waitMonitor(AssemblyVirtualThread.this))
                                    {
                                        isWaitingMonitor = true;
                                    }
                                    else
                                    {
                                        setRegisterValue(REG_O, 0x2);
                                        synchronized(waitMonitorLock)
                                        {
                                            requestWaitMonitor = null;
                                            waitingMonitorTimeout = -1;
                                        }
                                        isWaitingMonitor = false;
                                        continue interrupt;
                                    }
                                }

                                while(requestWaitMonitor.isWaitingOnMonitor(AssemblyVirtualThread.this))
                                {
                                    if(waitingMonitorTimeout < 0)
                                    {
                                        requestWaitMonitor.waitLockNotify();
                                        if(timeToQuit)
                                        {
                                            break interrupt;
                                        }
                                        if(shutdownRequest)
                                        {
                                            continue interrupt;
                                        }
                                        if(!suspendThreadSuspendHandlerList.isEmpty() || vm.isSuspend())
                                        {
                                            break skipWaitMonitor;
                                        }
                                    }
                                    else if(waitingMonitorTimeout > 0)
                                    {
                                        long startWaitTime = System.currentTimeMillis();
                                        requestWaitMonitor.waitLockNotify(waitingMonitorTimeout);
                                        waitingMonitorTimeout = (int)(waitingMonitorTimeout - (System.currentTimeMillis() - startWaitTime));
                                        if(waitingMonitorTimeout < 0)
                                        {
                                            waitingMonitorTimeout = 0;
                                        }
                                        if(timeToQuit)
                                        {
                                            break interrupt;
                                        }
                                        if(shutdownRequest)
                                        {
                                            continue interrupt;
                                        }
                                        if(!suspendThreadSuspendHandlerList.isEmpty() || vm.isSuspend())
                                        {
                                            break skipWaitMonitor;
                                        }
                                    }
                                    else
                                    {
                                        requestWaitMonitor.cancelWaitMonitor(AssemblyVirtualThread.this);
                                        break;
                                    }
                                }
                                requestWaitMonitor.restoreMonitorLock(AssemblyVirtualThread.this);
                                synchronized(waitMonitorLock)
                                {
                                    requestWaitMonitor = null;
                                    waitingMonitorTimeout = -1;
                                }
                                isWaitingMonitor = false;
                            }
                        }
                    }
                    synchronized(suspendThreadSuspendHandlerList)
                    {
                        if(vm.isSuspend())
                        {
                            suspendThreadSuspendHandlerList.add(defaultThreadSuspendHandler);
                        }
                    }
                    while(!suspendThreadSuspendHandlerList.isEmpty())
                    {
                        if(timeToQuit)
                        {
                            break interrupt;
                        }
                        if(shutdownRequest)
                        {
                            continue interrupt;
                        }
                        synchronized(resumeNotifier)
                        {
                            synchronized(suspendNotifier)
                            {
                                suspendNotifier.notifyAll();
                            }
                            vm.notifyThreadSuspend(handlerValue);
                            resumeNotifier.wait();
                            continue interrupt;
                        }
                    }
                    if(getVM().isRunning() == false)
                    {
                        break interrupt;
                    }
                    printThreadInfo();
                    int optInfo[] = new int[6];
                    getOptInfo(getVM().ram.getValue(PC),optInfo);
                    int parCount = 0;
                    parCount = getParCount(optInfo);
                    int par1data = 0;
                    int par2data = 0;
                    int par3data = 0;
                    int par4data = 0;
                    if(parCount == 0)
                    {
                        
                    }
                    else if(parCount == 1)
                    {
                        par1data = getVM().ram.getValue(PC + 1);
                    }
                    else if(parCount == 2)
                    {
                        par1data = getVM().ram.getValue(PC + 1);
                        par2data = getVM().ram.getValue(PC + 2);
                    }
                    else if(parCount == 3)
                    {
                        par1data = getVM().ram.getValue(PC + 1);
                        par2data = getVM().ram.getValue(PC + 2);
                        par3data = getVM().ram.getValue(PC + 3);
                    }
                    else if(parCount == 4)
                    {
                        par1data = getVM().ram.getValue(PC + 1);
                        par2data = getVM().ram.getValue(PC + 2);
                        par3data = getVM().ram.getValue(PC + 3);
                        par4data = getVM().ram.getValue(PC + 4);
                    }
                    PC += (1 + parCount);
                    Object par1value = null;
                    Object par2value = null;
                    Object par3value = null;
                    Object par4value = null;
                    if(breakNextInstruction == true)
                    {
                        breakNextInstruction = false;
                        continue interrupt;
                    }
                    //获取参数的值
                    //[start]
                    if(optInfo[1] == 1)
                    {
                        par1value = getRegisterValue(par1data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[1] == 2)
                    {
                        par1value = getRAMValue(par1data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[1] == 3)
                    {
                        par1value = par1data;
                    }
                    else if(optInfo[1] == 4)
                    {
                        par1value = getStringValue(par1data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[1] == 5)
                    {
                        par1value = getRAMREGValue(par1data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    if(optInfo[2] == 1)
                    {
                        par2value = getRegisterValue(par2data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[2] == 2)
                    {
                        par2value = getRAMValue(par2data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[2] == 3)
                    {
                        par2value = par2data;
                    }
                    else if(optInfo[2] == 4)
                    {
                        par2value = getStringValue(par2data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[2] == 5)
                    {
                        par2value = getRAMREGValue(par2data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    if(optInfo[3] == 1)
                    {
                        par3value = getRegisterValue(par3data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[3] == 2)
                    {
                        par3value = getRAMValue(par3data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[3] == 3)
                    {
                        par3value = par3data;
                    }
                    else if(optInfo[3] == 4)
                    {
                        par3value = getStringValue(par3data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[3] == 5)
                    {
                        par3value = getRAMREGValue(par3data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    if(optInfo[4] == 1)
                    {
                        par4value = getRegisterValue(par4data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[4] == 2)
                    {
                        par4value = getRAMValue(par4data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[4] == 3)
                    {
                        par4value = par4data;
                    }
                    else if(optInfo[4] == 4)
                    {
                        par4value = getStringValue(par4data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    else if(optInfo[4] == 5)
                    {
                        par4value = getRAMREGValue(par4data);
                        if(timeToQuit == true)
                        {
                            break interrupt;
                        }
                    }
                    //[end]
                    //------SET------
                    //[start]
                    if(optInfo[0] == 0x00000001)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，SET操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------ADD------
                    //[start]
                    else if(optInfo[0] == 0x00000002)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() + ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，ADD操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------SUB------
                    //[start]
                    else if(optInfo[0] == 0x00000003)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() - ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，SUB操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------MUL------
                    //[start]
                    else if(optInfo[0] == 0x00000004)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() * ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，MUL操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------DIV------
                    //[start]
                    else if(optInfo[0] == 0x00000005)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par2value).intValue() == 0)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() / ((Integer)par2value).intValue());
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，DIV操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------MOD------
                    //[start]
                    else if(optInfo[0] == 0x00000006)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par2value).intValue() == 0)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() % ((Integer)par2value).intValue());
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，MOD操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------AND------
                    //[start]
                    else if(optInfo[0] == 0x00000007)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() & ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，AND操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------BOR------
                    //[start]
                    else if(optInfo[0] == 0x00000008)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() | ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，BOR操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------XOR------
                    //[start]
                    else if(optInfo[0] == 0x00000009)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() ^ ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，XOR操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------SHR------
                    //[start]
                    else if(optInfo[0] == 0x0000000a)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() >>> ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，SHR操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------ASR------
                    //[start]
                    else if(optInfo[0] == 0x0000000b)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() >> ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，ASR操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------SHL------
                    //[start]
                    else if(optInfo[0] == 0x0000000c)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,((Integer)par1value).intValue() << ((Integer)par2value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，SHL操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------IFE------
                    //[start]
                    else if(optInfo[0] == 0x0000000d)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par1value).intValue() != ((Integer)par2value).intValue())
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，IFE操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------IFN------
                    //[start]
                    else if(optInfo[0] == 0x0000000e)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par1value).intValue() == ((Integer)par2value).intValue())
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，IFN操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------IFA------
                    //[start]
                    else if(optInfo[0] == 0x0000000f)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par1value).intValue() <= ((Integer)par2value).intValue())
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，IFA操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------IFU------
                    //[start]
                    else if(optInfo[0] == 0x00000010)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par1value).intValue() >= ((Integer)par2value).intValue())
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，IFU操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------JSR------
                    //[start]
                    else if(optInfo[0] == 0x00000011)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setRegisterValue(REG_PC,((Integer)par1value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，JSR操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------CRT------
                    //[start]
                    else if(optInfo[0] == 0x00000020)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[2],par2data,getVM().createVMThread(((Integer)par1value).intValue()));
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else if(parCount == 3)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,true,true},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            String threadName =null;
                            if(optInfo[3] == 1 || optInfo[3] == 2 || optInfo[3] == 3 || optInfo[3] == 5)
                            {
                                threadName = getStringValue(((Integer)par3value).intValue());
                            }
                            else if(optInfo[3] == 4)
                            {
                                threadName = (String)par3value;
                            }
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[2],par2data,getVM().createVMThread(((Integer)par1value).intValue(),threadName));
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，CRT操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------STT------
                    //[start]
                    else if(optInfo[0] == 0x00000021)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(getVM().startVMThread(((Integer)par1value).intValue()) == false)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                        }
                        else if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(getVM().getVMThread(((Integer)par2value).intValue()) == null)
                            {
                                setRegisterValue(REG_O,0x2);
                            }
                            if(getVM().startVMThread(((Integer)par1value).intValue(), ((Integer)par2value).intValue()) == false)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，STT操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------GTH------
                    //[start]
                    else if(optInfo[0] == 0x00000022)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,getThreadHandler());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，GTH操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------EXT------
                    //[start]
                    else if(optInfo[0] == 0x00000023)
                    {
                        if(parCount == 0)
                        {
                            shutdown();
                        }
                        else if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(!getVM().shutdownThread(((Integer)par1value).intValue()))
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，EXT操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------SLT------
                    //[start]
                    else if(optInfo[0] == 0x00000024)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            sleepTime = ((Integer)par1value).intValue();
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，SLT操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------CLCK------
                    //[start]
                    else if(optInfo[0] == 0x00000025)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1], par1data, vm.createMonitor());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，CLCK操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------DLCK------
                    //[start]
                    else if(optInfo[0] == 0x00000026)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int deleteResult = vm.deleteMonitor(((Integer)par1value).intValue());
       
                            switch(deleteResult)
                            {
                                case -1:
                                {
                                    setRegisterValue(REG_O,0x1);
                                    break;
                                }
                                case 0:
                                {
                                    setRegisterValue(REG_O,0x2);
                                    break;
                                }
                                default:
                                {
                                    break;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，DLCK操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------GLCK------
                    //[start]
                    else if(optInfo[0] == 0x00000027)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            Monitor monitor = vm.getMonitor(((Integer)par1value).intValue());
                            if(monitor == null)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                synchronized(lockMonitorLock)
                                {
                                    requestLockMonitor = monitor;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，GLCK操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------RLCK------
                    //[start]
                    else if(optInfo[0] == 0x00000028)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            Monitor monitor = vm.getMonitor(((Integer)par1value).intValue());
                            if(monitor == null)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                synchronized(monitor)
                                {
                                    if(!monitor.isVaild())
                                    {
                                        setRegisterValue(REG_O,0x1);
                                    }
                                    else
                                    {
                                        if(monitor.unlockMonitor(AssemblyVirtualThread.this) == -1)
                                        {
                                            setRegisterValue(REG_O,0x2);
                                        }
                                        else
                                        {
                                            ownedMonitorList.remove(monitor);
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，RLCK操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------WLCK------
                    //[start]
                    else if(optInfo[0] == 0x00000029)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            Monitor monitor = vm.getMonitor(((Integer)par1value).intValue());
                            if(monitor == null)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                synchronized(waitMonitorLock)
                                {
                                    requestWaitMonitor = monitor;
                                    waitingMonitorTimeout = -1;
                                }
                            }
                        }
                        else if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            Monitor monitor = vm.getMonitor(((Integer)par1value).intValue());
                            if(monitor == null)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                synchronized(waitMonitorLock)
                                {
                                    requestWaitMonitor = monitor;
                                    waitingMonitorTimeout = ((Integer)par2value).intValue();
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，WLCK操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------NLCK------
                    //[start]
                    else if(optInfo[0] == 0x00000030)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            Monitor monitor = vm.getMonitor(((Integer)par1value).intValue());
                            if(monitor == null)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                synchronized(monitor)
                                {
                                    if(!monitor.isVaild())
                                    {
                                        setRegisterValue(REG_O,0x1);
                                    }
                                    else
                                    {
                                        if(!monitor.notifyMonitor(AssemblyVirtualThread.this, 1))
                                        {
                                            setRegisterValue(REG_O,0x2);
                                        }
                                    }
                                }
                            }
                        }
                        else if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            Monitor monitor = vm.getMonitor(((Integer)par1value).intValue());
                            if(monitor == null)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                synchronized(monitor)
                                {
                                    if(!monitor.isVaild())
                                    {
                                        setRegisterValue(REG_O,0x1);
                                    }
                                    else
                                    {
                                        if(!monitor.notifyMonitor(AssemblyVirtualThread.this, ((Integer)par2value).intValue()))
                                        {
                                            setRegisterValue(REG_O,0x2);
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，NLCK操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------IN------
                    //[start]
                    else if(optInfo[0] == 0x00000040)
                    {
                        if(parCount == 3)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int temp = getVM().inputValueFormDevices(((Integer)par1value).intValue(), ((Integer)par2value).intValue());
                            setObjectValue(optInfo[3],par3data,temp);
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，IN操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------OUT------
                    //[start]
                    else if(optInfo[0] == 0x00000041)
                    {
                        if(parCount == 3)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            getVM().outputValueToDevices(((Integer)par1value).intValue(), ((Integer)par2value).intValue(), ((Integer)par3value).intValue());
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，OUT操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------INS------
                    //[start]
                    else if(optInfo[0] == 0x00000042)
                    {
                        if(parCount == 4)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,false,true,false,false,true});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int[] TempArray = getVM().inputsValueFormDevices(((Integer)par1value).intValue(), ((Integer)par2value).intValue(), ((Integer)par3value).intValue());
                            for(int i = 0; i < ((Integer)par3value).intValue();i++)
                            {
                                setObjectValue(optInfo[4],par4data + i,TempArray[i]);
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，INS操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------OUTS------
                    //[start]
                    else if(optInfo[0] == 0x00000043)
                    {
                        if(parCount == 4)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,false,true,false,false,true});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(optInfo[4] == 2)
                            {
                                int[] TempArray = getVM().ram.getValues(par4data, ((Integer)par3value).intValue());
                                if(TempArray == null)
                                {
                                    halt();
                                    break interrupt;
                                }
                                getVM().outputsValueToDevices(((Integer)par1value).intValue(), ((Integer)par2value).intValue(), TempArray);
                            }
                            else if(optInfo[4] == 5)
                            {
                                int idxTemp = ((Integer)getRegisterValue(par4data)).intValue();
                                int[] TempArray = getVM().ram.getValues(idxTemp, ((Integer)par3value).intValue());
                                if(TempArray == null)
                                {
                                    halt();
                                    break interrupt;
                                }
                                getVM().outputsValueToDevices(((Integer)par1value).intValue(), ((Integer)par2value).intValue(), TempArray);
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，OUTS操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------MIOP------
                    //[start]
                    else if(optInfo[0] == 0x00000044)
                    {
                        if(parCount == 4)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{false,false,true,false,false,true});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(optInfo[4] == 2)
                            {
                                if(getVM().ram.addMapping(((Integer)par1value).intValue(), ((Integer)par2value).intValue(), ((Integer)par3value).intValue(), par4data) == false)
                                {
                                    setRegisterValue(REG_O,0x1);
                                }
                            }
                            else if(optInfo[4] == 5)
                            {
                                int idxTemp = ((Integer)getRegisterValue(par4data)).intValue();
                                if(getVM().ram.addMapping(((Integer)par1value).intValue(), ((Integer)par2value).intValue(), ((Integer)par3value).intValue(), idxTemp) == false)
                                {
                                    setRegisterValue(REG_O,0x1);
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，MIOP操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------UNMP------
                    //[start]
                    else if(optInfo[0] == 0x00000045)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,false,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(optInfo[1] == 2)
                            {
                                if(getVM().ram.removeMapping(par1data) == false)
                                {
                                    setRegisterValue(REG_O,0x1);
                                }
                            }
                            else if(optInfo[1] == 5)
                            {
                                int idxTemp = ((Integer)getRegisterValue(par1data)).intValue();
                                if(getVM().ram.removeMapping(idxTemp) == false)
                                {
                                    setRegisterValue(REG_O,0x1);
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，UNMP操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------GDT------
                    //[start]
                    else if(optInfo[0] == 0x00000046)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[2],par2data,getVM().getDeviceType(((Integer)par1value).intValue()));
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，GDT操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------PUSH------
                    //[start]
                    else if(optInfo[0] == 0x00000050)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            pushInCurrentStackFrame(((Integer)par1value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，PUSH操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------POP------
                    //[start]
                    else if(optInfo[0] == 0x00000051)
                    {
                        if(parCount == 0)
                        {
                            popInCurrentStackFrame();
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,popInCurrentStackFrame());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，POP操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------DUP------
                    //[start]
                    else if(optInfo[0] == 0x00000052)
                    {
                        if(parCount == 0)
                        {
                            dupInCurrentStackFrame(1);
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            dupInCurrentStackFrame(((Integer)par1value).intValue());
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，DUP操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------SWAP------
                    //[start]
                    else if(optInfo[0] == 0x00000053)
                    {
                        if(parCount == 0)
                        {
                            swapInCurrentStackFrame();
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，SWAP操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------CALL------
                    //[start]
                    else if(optInfo[0] == 0x00000060)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int enterAddress = ((Integer)par1value).intValue();
                            enterMethod(enterAddress);
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int enterAddress = ((Integer)par1value).intValue();
                            int parLength = ((Integer)par2value).intValue();
                            if(parLength < 0)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                enterMethod(enterAddress, parLength);
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，CALL操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------RET------
                    //[start]
                    else if(optInfo[0] == 0x00000061)
                    {
                        if(parCount == 0)
                        {
                            int returnAddress = getCurrentStackFrameReturnAddress();
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            exitMethod();
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int parLength = ((Integer)par1value).intValue();
                            if(parLength < 0)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                exitMethod(parLength);
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，RET操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------GPL------
                    //[start]
                    else if(optInfo[0] == 0x00000062)
                    {
                        if(parCount == 1)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int parLength = getCurrentStackFrameParLength();
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,parLength);
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            int stackFrameIndex = ((Integer)par2value).intValue();
                            AVThreadStackFrame stackFrame = getStackFrame(stackFrameIndex);
                            if(stackFrame == null)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                int parLength = stackFrame.parLength;
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                                setObjectValue(optInfo[1],par1data,parLength);
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，GPL操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------ITF------
                    //[start]
                    else if(optInfo[0] == 0x00000070)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[2],par2data,Float.floatToRawIntBits(((Integer)par1value).floatValue()));
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，ITF操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FTI------
                    //[start]
                    else if(optInfo[0] == 0x00000071)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,false,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[2],par2data,(int)Float.intBitsToFloat(((Integer)par1value)));
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FTI操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FADD------
                    //[start]
                    else if(optInfo[0] == 0x00000080)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,Float.floatToRawIntBits(Float.intBitsToFloat(((Integer)par1value)) + Float.intBitsToFloat(((Integer)par2value))));
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FADD操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FSUB------
                    //[start]
                    else if(optInfo[0] == 0x00000081)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,Float.floatToRawIntBits(Float.intBitsToFloat(((Integer)par1value)) - Float.intBitsToFloat(((Integer)par2value))));
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FSUB操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FMUL------
                    //[start]
                    else if(optInfo[0] == 0x00000082)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            setObjectValue(optInfo[1],par1data,Float.floatToRawIntBits(Float.intBitsToFloat(((Integer)par1value)) * Float.intBitsToFloat(((Integer)par2value))));
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FMUL操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FDIV------
                    //[start]
                    else if(optInfo[0] == 0x00000083)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par2value).intValue() == 0)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                setObjectValue(optInfo[1],par1data,Float.floatToRawIntBits(Float.intBitsToFloat(((Integer)par1value)) / Float.intBitsToFloat(((Integer)par2value))));
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FDIV操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FMOD------
                    //[start]
                    else if(optInfo[0] == 0x00000084)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,false,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(((Integer)par2value).intValue() == 0)
                            {
                                setRegisterValue(REG_O,0x1);
                            }
                            else
                            {
                                setObjectValue(optInfo[1],par1data,Float.floatToRawIntBits(Float.intBitsToFloat(((Integer)par1value)) % Float.intBitsToFloat(((Integer)par2value))));
                                if(timeToQuit == true)
                                {
                                    break interrupt;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FMOD操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FIFE------
                    //[start]
                    else if(optInfo[0] == 0x00000090)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(Float.intBitsToFloat(((Integer)par1value)) != Float.intBitsToFloat(((Integer)par2value)))
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FIFE操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FIFN------
                    //[start]
                    else if(optInfo[0] == 0x00000091)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(Float.intBitsToFloat(((Integer)par1value)) == Float.intBitsToFloat(((Integer)par2value)))
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FIFN操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FIFA------
                    //[start]
                    else if(optInfo[0] == 0x00000092)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(Float.intBitsToFloat(((Integer)par1value)) <= Float.intBitsToFloat(((Integer)par2value)))
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FIFA操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    //------FIFU------
                    //[start]
                    else if(optInfo[0] == 0x00000093)
                    {
                        if(parCount == 2)
                        {
                            checkType(optInfo,new boolean[]{false,true,true,true,false,true},new boolean[]{false,true,true,true,false,true},new boolean[]{true,false,false,false,false,false},new boolean[]{true,false,false,false,false,false});
                            if(timeToQuit == true)
                            {
                                break interrupt;
                            }
                            if(Float.intBitsToFloat(((Integer)par1value)) >= Float.intBitsToFloat(((Integer)par2value)))
                            {
                                breakNextInstruction = true;
                            }
                        }
                        else
                        {
                            System.out.println("[VCPU-32]错误的参数个数，FIFU操作符参数不能为" + parCount + "个");
                            halt();
                        }
                    }
                    //[end]
                    else
                    {
                        System.out.println("[VCPU-32]不正确的机器指令 0x" + UnsignedTools.read16RadixUintValue(optInfo[0], true) + " ，线程 0x" + UnsignedTools.read16RadixUintValue(handlerValue, true) + "（" + threadName + "）终止");
                        halt();
                    }
                    yield();
                }
            }
            catch(Throwable e)
            {
                e.printStackTrace();
                halt();
            }
            finally
            {
                synchronized(AssemblyVirtualThread.this)
                {
                    isRunning = false;
                    isTerminated = true;
                }
            }
        }
    }
    
    private Object referenceInitLock = new Object();
    private ThreadReferenceImpl reference;
    private int debugSuspendHandler;

    
    public ThreadReferenceImpl getReference()
    {
        synchronized(referenceInitLock)
        {
            if(reference == null)
            {
                reference = new ThreadReferenceImpl(vm.getReference(), this);
                debugSuspendHandler = createThreadSuspendHandler();
            }
            return reference;
        }
    }
    public int getDebugSuspendHandler()
    {
        return debugSuspendHandler;
    }
}