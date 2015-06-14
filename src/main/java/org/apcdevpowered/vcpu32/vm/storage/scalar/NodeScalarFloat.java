package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarFloat extends NodeScalar<NodeScalarFloat>
{
    private float data;
    
    protected NodeScalarFloat()
    {
        super(ScalarType.SCALAR_TYPE_FLOAT);
        setData(0.0F);
    }
    protected NodeScalarFloat(float data)
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
}