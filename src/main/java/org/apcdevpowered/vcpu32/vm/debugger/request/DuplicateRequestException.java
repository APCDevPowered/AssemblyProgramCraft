package org.apcdevpowered.vcpu32.vm.debugger.request;

public class DuplicateRequestException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public DuplicateRequestException()
    {
    }
    public DuplicateRequestException(String s)
    {
        super(s);
    }
}