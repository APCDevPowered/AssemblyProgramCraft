package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.ThreadReference;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.ThreadFilter;
import org.apcdevpowered.vcpu32.vm.debugger.request.InvalidRequestStateException;
import org.apcdevpowered.vcpu32.vm.debugger.request.ThreadedRequest;

public abstract class ThreadedRequestImpl extends EventRequestImpl implements ThreadedRequest
{
    public ThreadedRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager)
    {
        super(virtualMachineReference, eventRequestManager);
    }
    @Override
    public void addThreadFilter(ThreadReference thread)
    {
        if (isEnabled() || !isVaild())
        {
            throw new InvalidRequestStateException();
        }
        if (!(thread instanceof ThreadReferenceImpl))
        {
            throw new IllegalArgumentException();
        }
        addFilter(new ThreadFilter((ThreadReferenceImpl) thread));
    }
}