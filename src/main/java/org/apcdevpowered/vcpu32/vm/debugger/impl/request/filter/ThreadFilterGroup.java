package org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter;

import java.util.Iterator;

import org.apcdevpowered.vcpu32.vm.debugger.impl.ThreadReferenceImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.event.ThreadedEventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.ThreadedRequestImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.ThreadFilterGroup.ThreadFilter;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.ThreadFilterGroup.ThreadFilterResult;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilter;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilterResult;

public class ThreadFilterGroup extends AbstractFilterGroup<ThreadFilter, ThreadedRequestImpl, ThreadedEventImpl, ThreadFilterResult>
{
    public static class ThreadFilter implements IFilter<ThreadedRequestImpl, ThreadedEventImpl, ThreadFilterResult>
    {
        private ThreadReferenceImpl threadReference;
        
        public ThreadFilter(ThreadReferenceImpl threadReference)
        {
            this.threadReference = threadReference;
        }
        @Override
        public ThreadFilterResult filterEvent(ThreadedRequestImpl eventRequest, ThreadedEventImpl event)
        {
            return event.thread().equals(threadReference) ? ThreadFilterResult.ACCEPTED : ThreadFilterResult.INCOMPATIBLE;
        }
    }
    public enum ThreadFilterResult implements IFilterResult
    {
        INCOMPATIBLE, ACCEPTED
    }
    
    @Override
    public boolean filterEvent(ThreadedRequestImpl eventRequest, ThreadedEventImpl event)
    {
        synchronized (filters)
        {
            if (filters.isEmpty())
                return true;
            Iterator<ThreadFilter> iterator = filters.iterator();
            while (iterator.hasNext())
            {
                ThreadFilter filter = iterator.next();
                ThreadFilterResult result = filter.filterEvent(eventRequest, event);
                switch (result)
                {
                    case INCOMPATIBLE:
                        break;
                    case ACCEPTED:
                        return true;
                    default:
                        throw new IllegalStateException();
                }
            }
            return false;
        }
    }
}
