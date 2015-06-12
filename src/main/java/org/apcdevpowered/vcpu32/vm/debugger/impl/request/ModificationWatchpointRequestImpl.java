package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.impl.LocationImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.request.ModificationWatchpointRequest;

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