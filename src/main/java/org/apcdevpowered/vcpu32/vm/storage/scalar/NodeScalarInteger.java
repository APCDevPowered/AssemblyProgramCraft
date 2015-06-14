package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarInteger extends NodeScalar<NodeScalarInteger>
{
    private int data;
    
    protected NodeScalarInteger()
    {
        super(ScalarType.SCALAR_TYPE_INTEGER);
        setData(0);
    }
    protected NodeScalarInteger(int data)
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
}