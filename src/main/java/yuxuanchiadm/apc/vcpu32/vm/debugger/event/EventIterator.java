package yuxuanchiadm.apc.vcpu32.vm.debugger.event;

import java.util.Iterator;

public interface EventIterator<E extends Event> extends Iterator<E>
{
    E nextEvent();
}