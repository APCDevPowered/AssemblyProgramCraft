package org.apcdevpowered.vcpu32.vm.debugger.event;

import org.apcdevpowered.vcpu32.vm.debugger.Mirror;
import org.apcdevpowered.vcpu32.vm.debugger.request.EventRequest;

public interface Event extends Mirror
{
    EventRequest request();
}