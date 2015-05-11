package yuxuanchiadm.apc.vcpu32.vm.debugger.event;

import yuxuanchiadm.apc.vcpu32.vm.debugger.Mirror;
import yuxuanchiadm.apc.vcpu32.vm.debugger.request.EventRequest;

public interface Event extends Mirror
{
    EventRequest request();
}