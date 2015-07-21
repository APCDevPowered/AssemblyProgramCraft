package org.apcdevpowered.vcpu32.vm.debugger.impl;

import java.util.ArrayList;
import java.util.List;

import org.apcdevpowered.vcpu32.vm.AssemblyVirtualThread;
import org.apcdevpowered.vcpu32.vm.Monitor;
import org.apcdevpowered.vcpu32.vm.debugger.IncompatibleThreadStateException;
import org.apcdevpowered.vcpu32.vm.debugger.MonitorReference;

public class MonitorReferenceImpl implements MonitorReference
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    private Monitor monitor;
    
    public MonitorReferenceImpl(VirtualMachineReferenceImpl virtualMachineReference, Monitor monitor)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.monitor = monitor;
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public synchronized List<ThreadReferenceImpl> waitingThreads() throws IncompatibleThreadStateException
    {
        synchronized (monitor)
        {
            List<ThreadReferenceImpl> waitingThreads = new ArrayList<ThreadReferenceImpl>();
            for (AssemblyVirtualThread avThread : monitor.getWaitingThreadList())
            {
                ThreadReferenceImpl threadReference = avThread.getReference();
                synchronized (threadReference)
                {
                    if (!threadReference.isSuspended())
                    {
                        throw new IncompatibleThreadStateException();
                    }
                    waitingThreads.add(threadReference);
                }
            }
            return waitingThreads;
        }
    }
    @Override
    public synchronized ThreadReferenceImpl owningThread() throws IncompatibleThreadStateException
    {
        synchronized (monitor)
        {
            AssemblyVirtualThread avThread = monitor.getHoldsLockThread();
            if (avThread == null)
            {
                return null;
            }
            ThreadReferenceImpl threadReference = avThread.getReference();
            synchronized (threadReference)
            {
                if (!threadReference.isSuspended())
                {
                    throw new IncompatibleThreadStateException();
                }
                return threadReference;
            }
        }
    }
    @Override
    public synchronized int entryCount() throws IncompatibleThreadStateException
    {
        synchronized (monitor)
        {
            AssemblyVirtualThread avThread = monitor.getHoldsLockThread();
            ThreadReferenceImpl threadReference = avThread.getReference();
            synchronized (threadReference)
            {
                if (!threadReference.isSuspended())
                {
                    throw new IncompatibleThreadStateException();
                }
                return monitor.getLockCound();
            }
        }
    }
    public Monitor getHander()
    {
        return monitor;
    }
}
