package org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter;

import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilter;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilterResult;

public interface IFilterGroup<F extends IFilter<ER, E, R>, ER extends EventRequestImpl, E extends EventImpl, R extends Enum<R> & IFilterResult>
{
    interface IFilter<ER extends EventRequestImpl, E extends EventImpl, R extends Enum<R> & IFilterResult>
    {
        R filterEvent(ER eventRequest, E event);
    }
    interface IFilterResult
    {
    }
    
    void addFilter(F filter);
    boolean filterEvent(ER eventRequest, E event);
}
