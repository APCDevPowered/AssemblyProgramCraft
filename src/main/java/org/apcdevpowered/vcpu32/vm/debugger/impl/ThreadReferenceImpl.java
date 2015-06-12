package org.apcdevpowered.vcpu32.vm.debugger.impl;

import java.util.ArrayList;
import java.util.List;

import org.apcdevpowered.vcpu32.vm.AssemblyVirtualThread;
import org.apcdevpowered.vcpu32.vm.Monitor;
import org.apcdevpowered.vcpu32.vm.debugger.IncompatibleThreadStateException;
import org.apcdevpowered.vcpu32.vm.debugger.ThreadReference;

public class ThreadReferenceImpl implements ThreadReference
{    
    private VirtualMachineReferenceImpl virtualMachineReference;
    
    private AssemblyVirtualThread avThread;
    private int suspendCound = 0;
    private List<StackFrameImpl> stackFrameList = new ArrayList<StackFrameImpl>();
    
    public ThreadReferenceImpl(VirtualMachineReferenceImpl virtualMachineReference, AssemblyVirtualThread avThread)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.avThread = avThread;
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public synchronized String name()
    {
        return avThread.getThreadName();
    }
    @Override
    public synchronized void suspend()
    {
        if(suspendCound == 0)
        {
            avThread.suspendThread(avThread.getDebugSuspendHandler());
        }
        suspendCound++;
    }
    @Override
    public synchronized void resume()
    {
        if(suspendCound != 0)
        {
            suspendCound--;
            if(suspendCound == 0)
            {
                for(StackFrameImpl stackFrame : stackFrameList)
                {
                    stackFrame.markInvalid();
                }
                stackFrameList.clear();
                avThread.resumeThread(avThread.getDebugSuspendHandler());
            }
        }
    }
    @Override
    public synchronized void stop()
    {
        avThread.shutdown();
    }
    @Override
    public synchronized int suspendCount()
    {
        return suspendCound;
    }
    @Override
    public synchronized  int status()
    {
        if(isSuspended())
        {
            return THREAD_STATUS_UNKNOWN;
        }
        else
        {
            switch(avThread.getThreadState())
            {
                case NEW:
                    return THREAD_STATUS_NOT_STARTED;
                case RUNNABLE:
                    return THREAD_STATUS_RUNNING;
                case BLOCKED:
                    return THREAD_STATUS_MONITOR;
                case WATTING:
                case TIMED_WAITING:
                    if(avThread.isWaiting())
                    {
                        return THREAD_STATUS_WAIT;
                    }
                    else
                    {
                        return THREAD_STATUS_SLEEPING;
                    }
                case TERMINATED:
                    return THREAD_STATUS_ZOMBIE;
                default:
                    return THREAD_STATUS_UNKNOWN;
            }
        }
    }
    @Override
    public synchronized boolean isSuspended()
    {
        return suspendCound != 0;
    }
    @Override
    public synchronized boolean isAtBreakpoint()
    {
        return false;
    }
    @Override
    public synchronized int frameCount() throws IncompatibleThreadStateException
    {
        if(!isSuspended())
        {
            throw new IncompatibleThreadStateException();
        }
        return avThread.getStack().size();
    }
    @Override
    public synchronized List<StackFrameImpl> frames() throws IncompatibleThreadStateException
    {
        if(!isSuspended())
        {
            throw new IncompatibleThreadStateException();
        }
        List<StackFrameImpl> frames = new ArrayList<StackFrameImpl>();
        int frameCount = frameCount();
        for(int frameIndex = 0;frameIndex < frameCount;frameIndex++)
        {
            frames.add(getStackFrame(frameIndex));
        }
        return frames;
    }
    @Override
    public synchronized StackFrameImpl frame(int index) throws IncompatibleThreadStateException
    {
        if(!isSuspended())
        {
            throw new IncompatibleThreadStateException();
        }
        int frameCount = frameCount();
        if(index < 0 || index >= frameCount)
        {
            throw new IndexOutOfBoundsException();
        }
        return getStackFrame(index);
    }
    @Override
    public synchronized List<StackFrameImpl> frames(int start, int length) throws IncompatibleThreadStateException
    {
        if(!isSuspended())
        {
            throw new IncompatibleThreadStateException();
        }
        int frameCount = frameCount();
        if(start < 0 || start >= frameCount || length < 0 || start + length > frameCount)
        {
            throw new IndexOutOfBoundsException();
        }
        return null;
    }
    @Override
    public synchronized List<MonitorReferenceImpl> ownedMonitors() throws IncompatibleThreadStateException
    {
        if(!isSuspended())
        {
            throw new IncompatibleThreadStateException();
        }
        List<MonitorReferenceImpl> ownedMonitors = new ArrayList<MonitorReferenceImpl>();
        for(Monitor monitor : avThread.getOwnedMonitorList())
        {
            ownedMonitors.add(monitor.getReference());
        }
        return ownedMonitors;
    }
    @Override
    public synchronized MonitorReferenceImpl currentContendedMonitor() throws IncompatibleThreadStateException
    {
        if(!isSuspended())
        {
            throw new IncompatibleThreadStateException();
        }
        if(avThread.getRequestLockMonitor() != null)
        {
            return avThread.getRequestLockMonitor().getReference();
        }
        if(avThread.getRequestWaitMonitor() != null)
        {
            return avThread.getRequestWaitMonitor().getReference();
        }
        return null;
    }
    public AssemblyVirtualThread getAVThread()
    {
        return avThread;
    }
    private StackFrameImpl getStackFrame(int frameIndex)
    {
        StackFrameImpl stackFrame = new StackFrameImpl(virtualMachineReference, this, frameIndex);
        stackFrameList.add(stackFrame);
        return stackFrame;
    }
}
