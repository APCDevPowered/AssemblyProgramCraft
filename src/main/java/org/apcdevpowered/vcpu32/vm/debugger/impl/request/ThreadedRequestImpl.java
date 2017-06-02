package org.apcdevpowered.vcpu32.vm.debugger.impl.request;

import org.apcdevpowered.vcpu32.vm.debugger.ThreadReference;
import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.ThreadFilterGroup;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.ThreadFilterGroup.ThreadFilter;
import org.apcdevpowered.vcpu32.vm.debugger.request.InvalidRequestStateException;
import org.apcdevpowered.vcpu32.vm.debugger.request.ThreadedRequest;

public abstract class ThreadedRequestImpl extends EventRequestImpl implements ThreadedRequest
{
    private ThreadFilterGroup threadFilterGroup = new ThreadFilterGroup();
    
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
        threadFilterGroup.addFilter(new ThreadFilter((ThreadReferenceImpl) thread));
    }
    public ThreadFilterGroup getThreadFilterGroup()
    {
        return threadFilterGroup;
    }
}