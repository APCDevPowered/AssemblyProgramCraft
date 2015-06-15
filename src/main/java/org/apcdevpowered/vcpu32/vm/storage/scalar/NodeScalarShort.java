package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarShort extends NodeScalar<NodeScalarShort>
{
    private short data;
    
    public NodeScalarShort()
    {
        super(ScalarType.SCALAR_TYPE_SHORT);
        setData((short) 0);
    }
    public NodeScalarShort(short data)
    {
        super(ScalarType.SCALAR_TYPE_SHORT);
        setData(data);
    }
    public short getData()
    {
        return data;
    }
    public void setData(short data)
    {
        this.data = data;
    }
}