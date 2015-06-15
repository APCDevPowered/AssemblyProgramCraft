package org.apcdevpowered.vcpu32.vm.storage.exception;

import java.io.IOException;

import org.apcdevpowered.vcpu32.vm.storage.NodeElement;

public class ElementTypeMappingNotFoundException extends IOException
{
    private static final long serialVersionUID = 1L;
    private Class<? extends NodeElement> elementType;
    
    public ElementTypeMappingNotFoundException(Class<? extends NodeElement> elementType)
    {
        super("Element type mapping not found. Element type " + elementType + ".");
        this.elementType = elementType;
    }
    public Class<? extends NodeElement> getElementTyp()
    {
        return elementType;
    }
}