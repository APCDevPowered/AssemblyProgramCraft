package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request;

import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.request.MethodEntryRequest;

public class MethodEntryRequestImpl extends ThreadedRequestImpl implements MethodEntryRequest
{
    public MethodEntryRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager)
    {
        super(virtualMachineReference, eventRequestManager);
    }
    
    @Override
    protected void requestStateChange(boolean isEnable)
    {
        
    }
}
