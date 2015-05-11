package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request;

import yuxuanchiadm.apc.vcpu32.vm.debugger.ThreadReference;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.VirtualMachineReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.filter.ThreadFilter;
import yuxuanchiadm.apc.vcpu32.vm.debugger.request.InvalidRequestStateException;
import yuxuanchiadm.apc.vcpu32.vm.debugger.request.ThreadedRequest;

public abstract class ThreadedRequestImpl extends EventRequestImpl implements ThreadedRequest
{
    public ThreadedRequestImpl(VirtualMachineReferenceImpl virtualMachineReference, EventRequestManagerImpl eventRequestManager)
    {
        super(virtualMachineReference, eventRequestManager);
    }

    @Override
    public void addThreadFilter(ThreadReference thread)
    {
        if(isEnabled() || !isVaild())
        {
            throw new InvalidRequestStateException();
        }
        if(!(thread instanceof ThreadReferenceImpl))
        {
            throw new IllegalArgumentException();
        }
        addFilter(new ThreadFilter((ThreadReferenceImpl)thread));
    }
}