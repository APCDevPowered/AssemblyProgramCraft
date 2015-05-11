package yuxuanchiadm.apc.vcpu32.vm.debugger;

public class IncompatibleThreadStateException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public IncompatibleThreadStateException()
    {
        
    }
    public IncompatibleThreadStateException(String s)
    {
        super(s);
    }
}