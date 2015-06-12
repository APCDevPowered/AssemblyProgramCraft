package org.apcdevpowered.vcpu32.vm.debugger.event;

import org.apcdevpowered.vcpu32.vm.debugger.StackFrame;

public interface MethodEntryEvent extends LocatableEvent
{
    StackFrame method();
}