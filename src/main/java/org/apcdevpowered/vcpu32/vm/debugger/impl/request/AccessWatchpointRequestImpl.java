package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.impl.LocationImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.AccessWatchpointRequest;

public class AccessWatchpointRequestImpl extends WatchpointRequestImpl implements AccessWatchpointRequest
{
    public AccessWatchpointRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager, LocationImpl location)
    {
        super(virtualMachineReference, eventRequestManager, location);
    }
    @Override
    protected void requestStateChange(boolean isEnable)
    {
        
    }
}