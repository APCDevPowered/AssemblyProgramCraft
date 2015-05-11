package yuxuanchiadm.apc.vcpu32.vm.debugger.event;

public interface WatchpointEvent extends LocatableEvent
{
    int valueCurrent();
    void setValue(int value);
}