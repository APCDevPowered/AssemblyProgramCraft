package yuxuanchiadm.apc.vcpu32.vm.debugger.request;

public class InvalidRequestStateException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public InvalidRequestStateException()
    {
        
    }
    public InvalidRequestStateException(String s)
    {
        super(s);
    }
}