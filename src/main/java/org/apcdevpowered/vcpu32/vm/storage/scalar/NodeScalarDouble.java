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
}