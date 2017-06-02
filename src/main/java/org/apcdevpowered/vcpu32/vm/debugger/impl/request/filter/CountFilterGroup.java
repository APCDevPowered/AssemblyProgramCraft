package org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter;

import java.util.Iterator;

import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.CountFilterGroup.CountFilter;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.CountFilterGroup.CountFilterResult;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilter;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilterResult;

public class CountFilterGroup extends AbstractFilterGroup<CountFilter, EventRequestImpl, EventImpl, CountFilterResult>
{
    public static class CountFilter implements IFilter<EventRequestImpl, EventImpl, CountFilterResult>
    {
        private int count;
        
        public CountFilter(int count)
        {
            this.count = count;
        }
        @Override
        public CountFilterResult filterEvent(EventRequestImpl eventRequest, EventImpl event)
        {
            if (count > 1)
            {
                count--;
                return CountFilterResult.PENDING;
            }
            else if (count == 1)
            {
                count--;
                return CountFilterResult.TRIGGERED;
            }
            else
            {
                throw new IllegalStateException();
            }
        }
    }
    public enum CountFilterResult implements IFilterResult
    {
        PENDING, TRIGGERED
    }
    
    @Override
    public boolean filterEvent(EventRequestImpl eventRequest, EventImpl event)
    {
        synchronized (filters)
        {
            Iterator<CountFilter> iterator = filters.iterator();
            while (iterator.hasNext())
            {
                CountFilter filter = iterator.next();
                CountFilterResult result = filter.filterEvent(eventRequest, event);
                switch (result)
                {
                    case PENDING:
                        return false;
                    case TRIGGERED:
                        iterator.remove();
                        if (!iterator.hasNext())
                            eventRequest.disable();
                        return true;
                    default:
                        throw new IllegalStateException();
                }
            }
            return true;
        }
    }
}
