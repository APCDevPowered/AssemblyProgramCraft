package org.apcdevpowered.vcpu32.vm.debugger.impl.event;

import org.apcdevpowered.vcpu32.vm.debugger.event.MethodExitEvent;
import org.apcdevpowered.vcpu32.vm.debugger.impl.MethodImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class MethodExitEventImpl extends ThreadedEventImpl implements MethodExitEvent
{
    private MethodImpl method;
    
    public MethodExitEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread, MethodImpl method)
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
