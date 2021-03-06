package org.apcdevpowered.vcpu32.vm.debugger.event;

import java.util.Set;

import org.apcdevpowered.vcpu32.vm.debugger.Mirror;

public interface EventSet<E extends Event> extends Mirror, Set<E>
{
    EventIterator<E> eventIterator();
    void resume();
    int suspendPolicy();
}