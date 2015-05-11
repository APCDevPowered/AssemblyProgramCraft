package yuxuanchiadm.apc.vcpu32.vm.debugger;

import java.util.List;

import yuxuanchiadm.apc.vcpu32.vm.debugger.event.EventQueue;
import yuxuanchiadm.apc.vcpu32.vm.debugger.request.EventRequestManager;

public interface VirtualMachineReference extends Mirror
{
    List<? extends ThreadReference> allThreads();
    MemoryReference memory();
    EventQueue eventQueue();
    EventRequestManager eventRequestManager();
    void suspend();
    void resume();
    void exit();
    String version();
    String name();
}