package org.apcdevpowered.vcpu32.asm;

public class OffsetWithDataPackage<T> implements Cloneable
{
    public T data;
    public int offset;
    
    public OffsetWithDataPackage()
    {
    }
    public OffsetWithDataPackage(T data, int offset)
    {
        this.data = data;
        this.offset = offset;
    }
    @Override
    public OffsetWithDataPackage<T> clone()
    {
        return new OffsetWithDataPackage<T>(data, offset);
    }
}
