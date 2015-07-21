package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.MethodExitRequest;

public class MethodExitRequestImpl extends ThreadedRequestImpl implements MethodExitRequest
{
    public MethodExitRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager)
    {
        super(virtualMachineReference, eventRequestManager);
    }
    @Override
    protected void requestStateChange(boolean isEnable)
    {
        virtualMachine().getHander().setMethodExitRequestState(this, isEnable);
    }
}
