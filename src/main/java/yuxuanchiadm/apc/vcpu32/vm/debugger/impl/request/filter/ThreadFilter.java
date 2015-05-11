package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.filter;

import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.event.EventImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.event.ThreadedEventImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class ThreadFilter implements IFilter
{
    private ThreadReferenceImpl threadReference;
    
    public ThreadFilter(ThreadReferenceImpl threadReference)
    {
        this.threadReference = threadReference;
    }
    @Override
    public boolean filterEvent(EventRequestImpl eventRequest, EventImpl event)
    {
        if(event instanceof ThreadedEventImpl)
        {
            return ((ThreadedEventImpl)event).thread().equals(threadReference);
        }
        return false;
    }
}
