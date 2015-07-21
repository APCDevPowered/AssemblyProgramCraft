package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.LocatableEvent;
import org.apcdevpowered.vcpu32.vm.debugger.impl.LocationImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class LocatableEventImpl extends ThreadedEventImpl implements LocatableEvent
{
    private LocationImpl location;
    
    public LocatableEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread, LocationImpl location)
    {
        super(virtualMachineReference, eventRequest, thread);
        this.location = location;
    }
    @Override
    public LocationImpl location()
    {
        return location;
    }
}