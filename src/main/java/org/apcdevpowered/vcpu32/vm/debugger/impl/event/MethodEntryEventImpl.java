package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.MethodEntryEvent;
import org.apcdevpowered.vcpu32.vm.debugger.impl.MethodImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class MethodEntryEventImpl extends ThreadedEventImpl implements MethodEntryEvent
{
    private MethodImpl method;
    
    public MethodEntryEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread, MethodImpl method)
    {
        super(virtualMachineReference, eventRequest, thread);
        this.method = method;
    }
    @Override
    public MethodImpl method()
    {
        return method;
    }
}
