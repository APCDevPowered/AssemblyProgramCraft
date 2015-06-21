package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarByte extends NodeScalar<NodeScalarByte>
{
    private byte data;
    
    public NodeScalarByte()
    {
        super(ScalarType.SCALAR_TYPE_BYTE);
        setData((byte) 0);
    }
    public NodeScalarByte(byte data)
    {
        super(ScalarType.SCALAR_TYPE_BYTE);
        setData(data);
    }
    public byte getData()
    {
        return data;
    }
    public void setData(byte data)
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
        if (obj instanceof NodeScalarByte)
        {
            NodeScalarByte scalar = ((NodeScalarByte) obj);
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
    public NodeScalarByte clone()
    {
        return new NodeScalarByte(data);
    }
}