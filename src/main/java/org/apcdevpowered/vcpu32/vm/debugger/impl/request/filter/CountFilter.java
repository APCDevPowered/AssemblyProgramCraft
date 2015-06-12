package org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter;

import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public class CountFilter implements IFilter
{
    private int count;
    
    public CountFilter(int count)
    {
        if(count < 1)
        {
            throw new IllegalArgumentException();
        }
        this.count = count;
    }
    
    @Override
    public boolean filterEvent(EventRequestImpl eventRequest, EventImpl event)
    {
        if(count > 0)
        {
            count--;
            if(count == 0)
            {
                return true;
            }
        }
        if(count == 0)
        {
            if(eventRequest.getFilterNum() > 1)
            {
                eventRequest.removeCurrentFilter();
                return eventRequest.filterEvent(event);
            }
        }
        return false;
    }
}
