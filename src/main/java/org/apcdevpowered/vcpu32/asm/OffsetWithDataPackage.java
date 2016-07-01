package org.apcdevpowered.vcpu32.asm;

public class OffsetWithDataPackage<T>
{
    public final T data;
    public final int offset;
    
    public OffsetWithDataPackage(T data, int offset)
    {
        this.data = data;
        this.offset = offset;
    }
}
