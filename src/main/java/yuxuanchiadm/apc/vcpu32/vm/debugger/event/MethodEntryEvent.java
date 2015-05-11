package yuxuanchiadm.apc.vcpu32.vm.debugger.event;

import yuxuanchiadm.apc.vcpu32.vm.debugger.StackFrame;

public interface MethodEntryEvent extends LocatableEvent
{
    StackFrame method();
}