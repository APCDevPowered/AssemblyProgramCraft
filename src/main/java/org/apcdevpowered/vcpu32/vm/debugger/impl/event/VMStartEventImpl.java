package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.VMStartEvent;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class VMStartEventImpl extends ThreadedEventImpl implements VMStartEvent
{
    public VMStartEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread)
    {
        super(virtualMachineReference, eventRequest, thread);
    }
}