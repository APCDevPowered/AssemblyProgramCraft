package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarShort extends NodeScalar<NodeScalarShort>
{
    private short data;
    
    public NodeScalarShort()
    {
        super(ScalarType.SCALAR_TYPE_SHORT);
        setData((short) 0);
    }
    public NodeScalarShort(short data)
    {
        super(ScalarType.SCALAR_TYPE_SHORT);
        setData(data);
    }
    public short getData()
    {
        return data;
    }
    public void setData(short data)
    {
        this.data = data;
    }
    @Override
    public int hashCode()
    {
        int result = 1;
        result = result * 31 + data;
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
        if (obj instanceof NodeScalarShort)
        {
            NodeScalarShort scalar = ((NodeScalarShort) obj);
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
    @Override
    public NodeScalarShort clone()
    {
        return new NodeScalarShort(data);
    }
}