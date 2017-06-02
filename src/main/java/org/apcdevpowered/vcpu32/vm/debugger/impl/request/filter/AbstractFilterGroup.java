package org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter;

import java.util.ArrayList;
import java.util.List;

import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilter;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter.IFilterGroup.IFilterResult;

public abstract class AbstractFilterGroup<F extends IFilter<ER, E, R>, ER extends EventRequestImpl, E extends EventImpl, R extends Enum<R> & IFilterResult> implements IFilterGroup<F, ER, E, R>
{
    protected final List<F> filters = new ArrayList<>();
    
    @Override
    public void addFilter(F filter)
    {
        synchronized (filters)
        {
            filters.add(filter);
        }
    }
}
