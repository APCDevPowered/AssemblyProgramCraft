package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarDouble extends NodeScalar<NodeScalarDouble>
{
    private double data;
    
    public NodeScalarDouble()
    {
        super(ScalarType.SCALAR_TYPE_DOUBLE);
        setData(0.0D);
    }
    public NodeScalarDouble(double data)
    {
        super(ScalarType.SCALAR_TYPE_DOUBLE);
        setData(data);
    }
    public double getData()
    {
        return data;
    }
    public void setData(double data)
    {
        this.data = data;
    }
    @Override
    public int hashCode()
    {
        int result = 1;
        result = result * 31 + (int) (Double.doubleToRawLongBits(data) ^ (Double.doubleToRawLongBits(data) >>> 32));
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
        if (obj instanceof NodeScalarDouble)
        {
            NodeScalarDouble scalar = ((NodeScalarDouble) obj);
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