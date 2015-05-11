package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request;

import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.LocationImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.request.ModificationWatchpointRequest;

public class ModificationWatchpointRequestImpl extends WatchpointRequestImpl implements ModificationWatchpointRequest
{
    public ModificationWatchpointRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager, LocationImpl location)
    {
        super(virtualMachineReference, eventRequestManager, location);
    }
    @Override
    protected void requestStateChange(boolean isEnable)
    {
        
    }
}