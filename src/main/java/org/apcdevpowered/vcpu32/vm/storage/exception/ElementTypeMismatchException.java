package org.apcdevpowered.vcpu32.vm.storage.exception;

import org.apcdevpowered.vcpu32.vm.storage.NodeElement;

public class ElementTypeMismatchException extends Exception
{
    private static final long serialVersionUID = 1L;
    private Class<? extends NodeElement> found;
    private Class<? extends NodeElement> expect;
    
    public ElementTypeMismatchException(Class<? extends NodeElement> found, Class<? extends NodeElement> expect)
    {
        super("Element type mismatch. Found " + found.getSimpleName() + ". Expect " + expect.getSimpleName());
        this.found = found;
        this.expect = expect;
    }
    public Class<? extends NodeElement> getFound()
    {
        return found;
    }
    public Class<? extends NodeElement> getExpect()
    {
        return expect;
    }
}