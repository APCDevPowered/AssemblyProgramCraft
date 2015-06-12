package org.apcdevpowered.vcpu32.vm.debugger.impl.request.filter;

import org.apcdevpowered.vcpu32.vm.debugger.impl.event.EventImpl;
import org.apcdevpowered.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public interface IFilter
{
    boolean filterEvent(EventRequestImpl eventRequest, EventImpl event);
}