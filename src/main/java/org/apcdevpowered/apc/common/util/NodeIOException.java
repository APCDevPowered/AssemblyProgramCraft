package org.apcdevpowered.apc.common.util;

import java.io.IOException;

public class NodeIOException extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public NodeIOException()
    {
        super();
    }
    public NodeIOException(String message)
    {
        super(message);
    }
    public NodeIOException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public NodeIOException(Throwable cause)
    {
        super(cause);
    }
}
