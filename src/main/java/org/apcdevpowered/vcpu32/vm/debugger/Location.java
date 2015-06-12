package org.apcdevpowered.vcpu32.vm.debugger;

public interface Location extends Mirror, Comparable<Location>
{
    int memoryAddress();
}