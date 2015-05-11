package yuxuanchiadm.apc.vcpu32.vm.debugger;

import java.util.List;

public interface MonitorReference extends Mirror
{
    List<? extends ThreadReference> waitingThreads() throws IncompatibleThreadStateException;
    ThreadReference owningThread() throws IncompatibleThreadStateException;
    int entryCount() throws IncompatibleThreadStateException;
}