package org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter;

import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.ThreadedEventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

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
        if (event instanceof ThreadedEventImpl)
        {
            return ((ThreadedEventImpl) event).thread().equals(threadReference);
        }
        return false;
    }
}
