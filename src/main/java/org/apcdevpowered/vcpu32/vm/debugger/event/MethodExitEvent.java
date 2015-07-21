package org.apcdevpowered.vcpu32.vm.debugger.event;

import org.apcdevpowered.vcpu32.vm.debugger.Method;

public interface MethodExitEvent extends ThreadedEvent
{
    Method method();
}