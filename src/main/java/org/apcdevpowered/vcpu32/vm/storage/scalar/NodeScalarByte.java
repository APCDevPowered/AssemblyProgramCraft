package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarByte extends NodeScalar<NodeScalarByte>
{
    private byte data;
    
    protected NodeScalarByte()
    {
        super(ScalarType.SCALAR_TYPE_BYTE);
    }
    protected NodeScalarByte(byte data)
    {
        super(ScalarType.SCALAR_TYPE_BYTE);
        this.data = data;
    }
    public byte getData()
    {
        return data;
    }
    public void setData(byte data)
    {
        this.data = data;
    }
}