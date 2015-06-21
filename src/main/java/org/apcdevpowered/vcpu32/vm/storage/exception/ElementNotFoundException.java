package org.apcdevpowered.vcpu32.vm.storage.exception;

import org.apcdevpowered.vcpu32.vm.storage.ElementKey;

public class ElementNotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;
    private ElementKey<?> key;
    
    public ElementNotFoundException(ElementKey<?> key)
    {
        this.key = key;
    }
    public ElementKey<?> getKey()
    {
        return key;
    }
}
