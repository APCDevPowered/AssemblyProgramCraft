package org.apcdevpowered.vcpu32.vm.debugger.event;

import org.apcdevpowered.vcpu32.vm.debugger.Method;

public interface MethodEntryEvent extends ThreadedEvent
{
    Method method();
}