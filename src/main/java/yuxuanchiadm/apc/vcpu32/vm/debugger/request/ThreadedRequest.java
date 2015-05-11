package yuxuanchiadm.apc.vcpu32.vm.debugger.request;

import yuxuanchiadm.apc.vcpu32.vm.debugger.ThreadReference;

public interface ThreadedRequest extends EventRequest
{
    void addThreadFilter(ThreadReference thread);
}