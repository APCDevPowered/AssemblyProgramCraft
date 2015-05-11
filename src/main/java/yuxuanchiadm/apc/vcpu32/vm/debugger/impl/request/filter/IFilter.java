package yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.filter;

import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.event.EventImpl;
import yuxuanchiadm.apc.vcpu32.vm.debugger.impl.request.EventRequestImpl;

public interface IFilter
{
    boolean filterEvent(EventRequestImpl eventRequest, EventImpl event);
}