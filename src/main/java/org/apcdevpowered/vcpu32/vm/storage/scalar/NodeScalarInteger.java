package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarInteger extends NodeScalar<NodeScalarInteger>
{
    private int data;
    
    public NodeScalarInteger()
    {
        super(ScalarType.SCALAR_TYPE_INTEGER);
        setData(0);
    }
    public NodeScalarInteger(int data)
    {
        super(ScalarType.SCALAR_TYPE_INTEGER);
        setData(data);
    }
    public int getData()
    {
        return data;
    }
    public void setData(int data)
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
        if (obj instanceof NodeScalarInteger)
        {
            NodeScalarInteger scalar = ((NodeScalarInteger) obj);
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