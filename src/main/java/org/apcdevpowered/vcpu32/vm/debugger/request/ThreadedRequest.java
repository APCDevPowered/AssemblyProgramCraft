package org.apcdevpowered.vcpu32.vm.debugger.request;

import org.apcdevpowered.vcpu32.vm.debugger.ThreadReference;

public interface ThreadedRequest extends EventRequest
{
    void addThreadFilter(ThreadReference thread);
}