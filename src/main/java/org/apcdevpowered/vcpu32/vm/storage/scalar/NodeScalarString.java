package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarString extends NodeScalar<NodeScalarString>
{
    private String data;
    
    public NodeScalarString()
    {
        super(ScalarType.SCALAR_TYPE_STRING);
        setData("");
    }
    public NodeScalarString(String data)
    {
        super(ScalarType.SCALAR_TYPE_STRING);
        setData(data);
    }
    public String getData()
    {
        return data;
    }
    public void setData(String data)
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
        result = result * 31 + data.hashCode();
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
        if (obj instanceof NodeScalarString)
        {
            NodeScalarString scalar = ((NodeScalarString) obj);
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