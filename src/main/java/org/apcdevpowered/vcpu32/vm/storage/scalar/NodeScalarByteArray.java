package org.apcdevpowered.vcpu32.vm.storage.scalar;

import java.util.Arrays;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarByteArray extends NodeScalar<NodeScalarByteArray>
{
    private byte[] data;
    
    public NodeScalarByteArray()
    {
        super(ScalarType.SCALAR_TYPE_BYTE_ARRAY);
        setData(new byte[0]);
    }
    public NodeScalarByteArray(byte[] data)
    {
        super(ScalarType.SCALAR_TYPE_BYTE_ARRAY);
        setData(data);
    }
    public byte[] getData()
    {
        return data;
    }
    public void setData(byte[] data)
    {
        if (data == null)
        {
            throw new NullPointerException();
        }
        this.data = data;
    }
    @Override
    public int hashCode()
    {
        int result = 1;
        result = result * 31 + Arrays.hashCode(data);
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
        if (obj instanceof NodeScalarByteArray)
        {
            NodeScalarByteArray scalar = ((NodeScalarByteArray) obj);
            if (Arrays.equals(scalar.data, data))
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
        return "{data:" + Arrays.toString(data) + "}";
    }
}