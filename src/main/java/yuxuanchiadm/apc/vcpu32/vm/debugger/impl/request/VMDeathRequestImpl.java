package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request;

import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.request.VMDeathRequest;

public class VMDeathRequestImpl extends EventRequestImpl implements VMDeathRequest
{
    public VMDeathRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager)
    {
        super(virtualMachineReference, eventRequestManager);
    }
    @Override
    protected void requestStateChange(boolean isEnable)
    {
        virtualMachine().getHander().setVMDeathRequestState(this, isEnable);
    }
}