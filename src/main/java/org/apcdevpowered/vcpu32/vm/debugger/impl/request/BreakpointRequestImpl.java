package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.impl.LocationImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.BreakpointRequest;

public class BreakpointRequestImpl extends ThreadedRequestImpl implements BreakpointRequest
{
    private LocationImpl location;
    
    public BreakpointRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager, LocationImpl location)
    {
        super(virtualMachineReference, eventRequestManager);
        this.location = location;
    }
    @Override
    public LocationImpl location()
    {
        return location;
    }
    
    @Override
    protected void requestStateChange(boolean isEnable)
    {
        
    }
}
