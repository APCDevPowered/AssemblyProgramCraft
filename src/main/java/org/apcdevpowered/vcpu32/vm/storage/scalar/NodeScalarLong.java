package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarLong extends NodeScalar<NodeScalarLong>
{
    private long data;
    
    public NodeScalarLong()
    {
        super(ScalarType.SCALAR_TYPE_LONG);
        setData(0L);
    }
    public NodeScalarLong(long data)
    {
        super(ScalarType.SCALAR_TYPE_LONG);
        setData(data);
    }
    public long getData()
    {
        return data;
    }
    public void setData(long data)
    {
        this.data = data;
    }
    @Override
    public int hashCode()
    {
        int result = 1;
        result = result * 31 + (int) (data ^ (data >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            throw new NullPointerException();
        }
        if (obj == this)
        {
            return true;
        }
        if (obj instanceof NodeScalarLong)
        {
            NodeScalarLong scalar = ((NodeScalarLong) obj);
            if (scalar.data != data)
            {
                return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public String toString()
    {
        return "{data:" + data + "}";
    }
}