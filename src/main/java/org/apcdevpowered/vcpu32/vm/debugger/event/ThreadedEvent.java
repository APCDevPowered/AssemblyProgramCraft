package org.apcdevpowered.vcpu32.vm.debugger.event;

import org.apcdevpowered.vcpu32.vm.debugger.ThreadReference;

public interface ThreadedEvent extends Event
{
    ThreadReference thread();
}