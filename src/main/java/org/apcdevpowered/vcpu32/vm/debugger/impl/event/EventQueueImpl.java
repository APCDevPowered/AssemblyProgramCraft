package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import java.util.LinkedList;
import java.util.List;

import org.apcdevpowered.vcpu32.vm.debugger.event.EventQueue;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;

public class EventQueueImpl implements EventQueue
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    private List<EventSetImpl> eventSets = new LinkedList<EventSetImpl>();
    
    public EventQueueImpl(VirtualMachineReferenceImpl virtualMachineReference)
    {
        this.virtualMachineReference = virtualMachineReference;
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public EventSetImpl remove() throws InterruptedException
    {
        return remove(0);
    }
    @Override
    public EventSetImpl remove(long timeout) throws InterruptedException
    {
        if (timeout < 0)
        {
            throw new IllegalArgumentException("Timeout cannot be negative");
        }
        synchronized (eventSets)
        {
            if (timeout == 0)
            {
                while (true)
                {
                    if (!eventSets.isEmpty())
                    {
                        return eventSets.remove(0);
                    }
                    eventSets.wait();
                }
            }
            else
            {
                while (timeout > 0)
                {
                    if (!eventSets.isEmpty())
                    {
                        return eventSets.remove(0);
                    }
                    long startWaitTimeMillis = System.currentTimeMillis();
                    eventSets.wait(timeout);
                    timeout = (timeout - (System.currentTimeMillis() - startWaitTimeMillis));
                }
                return null;
            }
        }
    }
    public void addEventSet(EventSetImpl eventSet)
    {
        if (!eventSet.isEmpty())
        {
            eventSet.suspendThreads();
            synchronized (eventSets)
            {
                eventSets.add(eventSet);
                eventSets.notifyAll();
            }
        }
    }
}