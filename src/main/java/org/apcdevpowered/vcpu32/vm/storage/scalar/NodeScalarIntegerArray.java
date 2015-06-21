package org.apcdevpowered.vcpu32.vm.storage.scalar;

import java.util.Arrays;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarIntegerArray extends NodeScalar<NodeScalarIntegerArray>
{
    private int[] data;
    
    public NodeScalarIntegerArray()
    {
        super(ScalarType.SCALAR_TYPE_INTEGER_ARRAY);
        setData(new int[0]);
    }
    public NodeScalarIntegerArray(int[] data)
    {
        super(ScalarType.SCALAR_TYPE_INTEGER_ARRAY);
        setData(data);
    }
    public int[] getData()
    {
        return data;
    }
    public void setData(int[] data)
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
        if (obj instanceof NodeScalarIntegerArray)
        {
            NodeScalarIntegerArray scalar = ((NodeScalarIntegerArray) obj);
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
    @Override
    public NodeScalarIntegerArray clone()
    {
        return new NodeScalarIntegerArray(data.clone());
    }
}