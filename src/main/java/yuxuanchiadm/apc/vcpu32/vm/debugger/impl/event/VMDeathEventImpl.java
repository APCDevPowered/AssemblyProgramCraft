package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.event;

import yuxuanchiadm.apc.vcpu32.vm.debugger.event.VMDeathEvent;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class VMDeathEventImpl extends EventImpl implements VMDeathEvent
{
    public VMDeathEventImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestImpl eventRequest)
    {
        super(virtualMachineReference, eventRequest);
    }
}