package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.Event;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public abstract class EventImpl implements Event
{
    private VirtualMachineReferenceImpl virtualMachineReference;
    private EventRequestImpl eventRequest;
    
    public EventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest)
    {
        this.virtualMachineReference = virtualMachineReference;
        this.eventRequest = eventRequest;
    }
    @Override
    public VirtualMachineReferenceImpl virtualMachine()
    {
        return virtualMachineReference;
    }
    @Override
    public EventRequestImpl request()
    {
        return eventRequest;
    }
}
