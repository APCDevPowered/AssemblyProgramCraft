package yuxuanchiadm.apc.vcpu32.vm.debugger.event;

public interface ModificationWatchpointEvent extends WatchpointEvent
{
    int valueToBe();
}