package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarLong extends NodeScalar<NodeScalarLong>
{
    private long data;
    
    protected NodeScalarLong()
    {
        super(ScalarType.SCALAR_TYPE_LONG);
        setData(0L);
    }
    protected NodeScalarLong(long data)
    {
        super(ScalarType.SCALAR_TYPE_LONG);
        setData(data);
    }
    public long getData()
    {
        return data;
    }
    public void setData(long data)
    {
        this.data = data;
    }
}