package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarByte extends NodeScalar<NodeScalarByte>
{
    private byte data;
    
    public NodeScalarByte()
    {
        super(ScalarType.SCALAR_TYPE_BYTE);
        setData((byte) 0);
    }
    public NodeScalarByte(byte data)
    {
        super(ScalarType.SCALAR_TYPE_BYTE);
        setData(data);
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