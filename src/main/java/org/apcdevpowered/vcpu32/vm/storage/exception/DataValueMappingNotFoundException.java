package org.apcdevpowered.vcpu32.vm.storage.exception;

import java.io.IOException;

public class DataValueMappingNotFoundException extends IOException
{
    private static final long serialVersionUID = 1L;
    private int dataValue;
    
    public DataValueMappingNotFoundException(int dataValue)
    {
        super("Data value mapping not found. Data value " + dataValue + ".");
        this.dataValue =dataValue;
    }
    public int getDataValue()
    {
        return dataValue;
    }
}