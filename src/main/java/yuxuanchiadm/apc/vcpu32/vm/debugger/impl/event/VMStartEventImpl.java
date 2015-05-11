package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.event;

import yuxuanchiadm.apc.vcpu32.vm.debugger.event.VMStartEvent;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class VMStartEventImpl extends ThreadedEventImpl implements VMStartEvent
{
    public VMStartEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest, ThreadReferenceImpl thread)
    {
        super(virtualMachineReference, eventRequest, thread);
    }
}