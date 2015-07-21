package org.apcdevpowered.vcpu32.vm.debugger;

public interface Method extends Locatable, Mirror
{
    ThreadReference thread();
    String name();
    int[] arguments();
    Location returnAddress();
    StackFrameReference frame() throws IncompatibleThreadStateException;
}