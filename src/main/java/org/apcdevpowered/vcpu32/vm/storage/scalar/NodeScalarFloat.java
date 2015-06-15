package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarFloat extends NodeScalar<NodeScalarFloat>
{
    private float data;
    
    public NodeScalarFloat()
    {
        super(ScalarType.SCALAR_TYPE_FLOAT);
        setData(0.0F);
    }
    public NodeScalarFloat(float data)
    {
        super(ScalarType.SCALAR_TYPE_FLOAT);
        setData(data);
    }
    public float getData()
    {
        return data;
    }
    public void setData(float data)
    {
        this.data = data;
    }
    @Override
    public int hashCode()
    {
        int result = 1;
        result = result * 31 + Float.floatToRawIntBits(data);
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
        if (obj instanceof NodeScalarFloat)
        {
            NodeScalarFloat scalar = ((NodeScalarFloat) obj);
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