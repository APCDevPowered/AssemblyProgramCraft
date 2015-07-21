package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.impl.LocationImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.LocatableRequest;

public abstract class LocatableRequestImpl extends ThreadedRequestImpl implements LocatableRequest
{
    private LocationImpl location;
    
    public LocatableRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager, LocationImpl location)
    {
        super(virtualMachineReference, eventRequestManager);
        this.location = location;
    }
    @Override
    public LocationImpl location()
    {
        return location;
    }
}