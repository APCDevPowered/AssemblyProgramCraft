package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarByteArray extends NodeScalar<NodeScalarByteArray>
{
    private byte[] data;
    
    protected NodeScalarByteArray()
    {
        super(ScalarType.SCALAR_TYPE_BYTE_ARRAY);
        setData(new byte[0]);
    }
    protected NodeScalarByteArray(byte[] data)
    {
        super(ScalarType.SCALAR_TYPE_BYTE_ARRAY);
        setData(data);
    }
    public byte[] getData()
    {
        return data;
    }
    public void setData(byte[] data)
    {
        if(data == null)
        {
            throw new NullPointerException();
        }
        this.data = data;
    }
}