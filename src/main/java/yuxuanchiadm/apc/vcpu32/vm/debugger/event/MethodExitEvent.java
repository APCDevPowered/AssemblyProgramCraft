package yuxuanchiadm.apc.vcpu32.vm.debugger.event;

import yuxuanchiadm.apc.vcpu32.vm.debugger.StackFrame;

public interface MethodExitEvent extends LocatableEvent
{
    StackFrame method();
}