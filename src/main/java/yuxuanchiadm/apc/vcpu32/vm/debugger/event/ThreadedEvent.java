package yuxuanchiadm.apc.vcpu32.vm.debugger.event;

import yuxuanchiadm.apc.vcpu32.vm.debugger.ThreadReference;

public interface ThreadedEvent extends Event
{
    ThreadReference thread();
}