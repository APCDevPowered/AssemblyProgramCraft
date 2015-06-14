package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarIntegerArray extends NodeScalar<NodeScalarIntegerArray>
{
    private int[] data;
    
    protected NodeScalarIntegerArray()
    {
        super(ScalarType.SCALAR_TYPE_INTEGER_ARRAY);
        setData(new int[0]);
    }
    protected NodeScalarIntegerArray(int[] data)
    {
        super(ScalarType.SCALAR_TYPE_INTEGER_ARRAY);
        setData(data);
    }
    public int[] getData()
    {
        return data;
    }
    public void setData(int[] data)
    {
        if(data == null)
        {
            throw new NullPointerException();
        }
        this.data = data;
    }
}