package org.apcdevpowered.vcpu32.vm.storage.scalar;

import org.apcdevpowered.vcpu32.vm.storage.NodeScalar;
import org.apcdevpowered.vcpu32.vm.storage.ScalarType;

public final class NodeScalarString extends NodeScalar<NodeScalarString>
{
    private String data;
    
    protected NodeScalarString()
    {
        super(ScalarType.SCALAR_TYPE_STRING);
        setData("");
    }
    protected NodeScalarString(String data)
    {
        super(ScalarType.SCALAR_TYPE_STRING);
        setData(data);
    }
    public String getData()
    {
        return data;
    }
    public void setData(String data)
    {
        if (data == null)
        {
            throw new NullPointerException();
        }
        this.data = data;
    }
}