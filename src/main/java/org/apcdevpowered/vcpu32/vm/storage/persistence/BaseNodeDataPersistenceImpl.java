package org.apcdevpowered.vcpu32.vm.storage.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apcdevpowered.vcpu32.vm.storage.NodeElement;

public class BaseNodeDataPersistenceImpl extends NodeDataPersistence
{
    protected BaseNodeDataPersistenceImpl(int version)
    {
        super(version);
    }
    @Override
    public <E extends NodeElement> E readElement(InputStream stream, Class<E> clazz) throws IOException
    {
        return null;
    }
    @Override
    public void writeElement(OutputStream stream, NodeElement element) throws IOException
    {
    }
}
