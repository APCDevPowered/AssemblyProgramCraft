package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.event;

import yuxuanchiadm.apc.vcpu32.vm.debugger.event.ThreadStartEvent;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class ThreadStartEventImpl extends ThreadedEventImpl implements ThreadStartEvent
{
    public ThreadStartEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread)
    {
        super(virtualMachineReference, eventRequest, thread);
    }
}