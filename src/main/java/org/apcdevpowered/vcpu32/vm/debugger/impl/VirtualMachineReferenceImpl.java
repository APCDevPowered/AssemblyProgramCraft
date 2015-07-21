package org.apcdevpowered.vcpu32.vm.debugger.impl;

import java.util.List;

import org.apcdevpowered.vcpu32.vm.VirtualMachine;
import org.apcdevpowered.vcpu32.vm.debugger.MemoryReference;
import org.apcdevpowered.vcpu32.vm.debugger.VirtualMachineReference;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventQueueImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestManagerImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.EventRequestManager;

public class VirtualMachineReferenceImpl implements VirtualMachineReference
{
    private VirtualMachine virtualMachine;
    private EventQueueImpl eventQueue;
    private EventRequestManagerImpl eventRequestManager;
    
    public VirtualMachineReferenceImpl(VirtualMachine virtualMachine)
    {
        this.virtualMachine = virtualMachine;
        this.eventQueue = new EventQueueImpl(this);
        this.eventRequestManager = new EventRequestManagerImpl(this);
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return this;
    }
    @Override
    public synchronized List<ThreadReferenceImpl> allThreads()
    {
        return virtualMachine.getThreadReferenceList();
    }
    @Override
    public synchronized MemoryReference memory()
    {
        return virtualMachine.ram.getReference();
    }
    @Override
    public synchronized EventQueueImpl eventQueue()
    {
        return eventQueue;
    }
    @Override
    public synchronized EventRequestManager eventRequestManager()
    {
        return eventRequestManager;
    }
    @Override
    public synchronized void suspend()
    {
        for (ThreadReferenceImpl threadReference : virtualMachine.getThreadReferenceList())
        {
            threadReference.suspend();
        }
    }
    @Override
    public synchronized void resume()
    {
        for (ThreadReferenceImpl threadReference : virtualMachine.getThreadReferenceList())
        {
            threadReference.resume();
        }
    }
    @Override
    public synchronized void exit()
    {
        virtualMachine.shutdownVM();
    }
    @Override
    public synchronized String version()
    {
        return virtualMachine.getVersion();
    }
    @Override
    public synchronized String name()
    {
        return virtualMachine.getName();
    }
    public VirtualMachine getHander()
    {
        return virtualMachine;
    }
}
