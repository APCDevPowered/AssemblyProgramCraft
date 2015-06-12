package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.ThreadStartEvent;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class ThreadStartEventImpl extends ThreadedEventImpl implements ThreadStartEvent
{
    public ThreadStartEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread)
    {
        super(virtualMachineReference, eventRequest, thread);
    }
}