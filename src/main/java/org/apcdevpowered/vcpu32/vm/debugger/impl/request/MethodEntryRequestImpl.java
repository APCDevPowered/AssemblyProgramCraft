package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.MethodEntryRequest;

public class MethodEntryRequestImpl extends ThreadedRequestImpl implements MethodEntryRequest
{
    public MethodEntryRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager)
    {
        super(virtualMachineReference, eventRequestManager);
    }
    @Override
    protected void requestStateChange(boolean isEnable)
    {
        virtualMachine().getHander().setMethodEntryRequestState(this, isEnable);
    }
}
