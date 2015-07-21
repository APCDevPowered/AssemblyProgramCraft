package org.apcdevpowered.vcpu32.vm.debugger;

public class InvalidStackFrameException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public InvalidStackFrameException()
    {
    }
    public InvalidStackFrameException(String s)
    {
        super(s);
    }
}