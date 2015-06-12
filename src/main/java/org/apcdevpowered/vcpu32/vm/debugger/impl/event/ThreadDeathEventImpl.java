package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.ThreadDeathEvent;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class ThreadDeathEventImpl extends ThreadedEventImpl implements ThreadDeathEvent
{
    public ThreadDeathEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread)
    {
        super(virtualMachineReference, eventRequest, thread);
    }
}