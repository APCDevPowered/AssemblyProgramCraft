package org.apcdevpowered.vcpu32.vm.storage.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.apcdevpowered.util.io.StreamHelper;
import org.apcdevpowered.vcpu32.vm.storage.NodeElement;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray.NodeContainerArrayElementKey;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerArray.NodeContainerArrayEntry;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap.NodeContainerMapElementKey;
import org.apcdevpowered.vcpu32.vm.storage.container.NodeContainerMap.NodeContainerMapEntry;
import org.apcdevpowered.vcpu32.vm.storage.exception.DataValueMappingNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMappingNotFoundException;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarByte;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarByteArray;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarDouble;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarFloat;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarInteger;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarIntegerArray;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarLong;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarShort;
import org.apcdevpowered.vcpu32.vm.storage.scalar.NodeScalarString;

public class BaseNodeDataPersistenceImpl extends NodeDataPersistence
{
    protected ElementTypeDataValueMapping mapping = new ElementTypeDataValueMapping();
    
    protected BaseNodeDataPersistenceImpl(int version)
    {
        super(version);
    }
    @Override
    public <E extends NodeElement> E readElement(InputStream stream, Class<E> clazz) throws IOException, ElementTypeMismatchException
    {
        int dataValue = StreamHelper.readInt(stream);
        Class<? extends NodeElement> mappingType = mapping.getMapping(dataValue);
        if (mappingType == null)
        {
            throw new DataValueMappingNotFoundException(dataValue);
        }
        NodeElement element;
        if (mappingType.equals(NodeScalarByte.class))
        {
            byte data = (byte) stream.read();
            element = new NodeScalarByte(data);
        }
        else if (mappingType.equals(NodeScalarShort.class))
        {
            short data = StreamHelper.readShort(stream);
            element = new NodeScalarShort(data);
        }
        else if (mappingType.equals(NodeScalarInteger.class))
        {
            int data = StreamHelper.readInt(stream);
            element = new NodeScalarInteger(data);
        }
        else if (mappingType.equals(NodeScalarLong.class))
        {
            long data = StreamHelper.readLong(stream);
            element = new NodeScalarLong(data);
        }
        else if (mappingType.equals(NodeScalarFloat.class))
        {
            float data = StreamHelper.readFloat(stream);
            element = new NodeScalarFloat(data);
        }
        else if (mappingType.equals(NodeScalarDouble.class))
        {
            double data = StreamHelper.readDouble(stream);
            element = new NodeScalarDouble(data);
        }
        else if (mappingType.equals(NodeScalarByteArray.class))
        {
            int length = StreamHelper.readInt(stream);
            byte[] data = StreamHelper.readByteArrayFully(stream, length);
            element = new NodeScalarByteArray(data);
        }
        else if (mappingType.equals(NodeScalarIntegerArray.class))
        {
            int length = StreamHelper.readInt(stream);
            int[] data = StreamHelper.readIntArrayFully(stream, length);
            element = new NodeScalarIntegerArray(data);
        }
        else if (mappingType.equals(NodeScalarString.class))
        {
            String data = StreamHelper.readString(stream);
            element = new NodeScalarString(data);
        }
        else if (mappingType.equals(NodeContainerArray.class))
        {
            int length = StreamHelper.readInt(stream);
            NodeContainerArray container = new NodeContainerArray();
            for (int index = 0; index < length; index++)
            {
                int key = StreamHelper.readInt(stream);
                NodeElement child = readElement(stream);
                container.addElement(NodeContainerArray.makeKey(key), child);
            }
            element = container;
        }
        else if (mappingType.equals(NodeContainerMap.class))
        {
            int length = StreamHelper.readInt(stream);
            NodeContainerMap container = new NodeContainerMap();
            for (int index = 0; index < length; index++)
            {
                String key = StreamHelper.readString(stream);
                NodeElement child = readElement(stream);
                container.addElement(NodeContainerMap.makeKey(key), child);
            }
            element = container;
        }
        else
        {
            throw new IllegalStateException();
        }
        if (!clazz.isInstance(element))
        {
            throw new ElementTypeMismatchException(element.getClass(), clazz);
        }
        return clazz.cast(element);
    }
    @Override
    public void writeElement(OutputStream stream, NodeElement element) throws IOException
    {
        Class<? extends NodeElement> mappingType = element.getClass();
        Integer dataValue = mapping.getMapping(mappingType);
        if (dataValue == null)
        {
            throw new ElementTypeMappingNotFoundException(mappingType);
        }
        StreamHelper.writeInt(stream, dataValue);
        if (mappingType.equals(NodeScalarByte.class))
        {
            byte data = ((NodeScalarByte) element).getData();
            stream.write(data & 0xFF);
        }
        else if (mappingType.equals(NodeScalarShort.class))
        {
            short data = ((NodeScalarShort) element).getData();
            StreamHelper.writeShort(stream, data);
        }
        else if (mappingType.equals(NodeScalarInteger.class))
        {
            int data = ((NodeScalarInteger) element).getData();
            StreamHelper.writeInt(stream, data);
        }
        else if (mappingType.equals(NodeScalarLong.class))
        {
            long data = ((NodeScalarLong) element).getData();
            StreamHelper.writeLong(stream, data);
        }
        else if (mappingType.equals(NodeScalarFloat.class))
        {
            float data = ((NodeScalarFloat) element).getData();
            StreamHelper.writeFloat(stream, data);
        }
        else if (mappingType.equals(NodeScalarDouble.class))
        {
            double data = ((NodeScalarDouble) element).getData();
            StreamHelper.writeDouble(stream, data);
        }
        else if (mappingType.equals(NodeScalarByteArray.class))
        {
            byte[] data = ((NodeScalarByteArray) element).getData();
            int length = data.length;
            StreamHelper.writeInt(stream, length);
            stream.write(data);
        }
        else if (mappingType.equals(NodeScalarIntegerArray.class))
        {
            int[] data = ((NodeScalarIntegerArray) element).getData();
            int length = data.length;
            StreamHelper.writeInt(stream, length);
            for (int index = 0; index < length; index++)
            {
                StreamHelper.writeInt(stream, data[index]);
            }
        }
        else if (mappingType.equals(NodeScalarString.class))
        {
            String data = ((NodeScalarString) element).getData();
            StreamHelper.writeString(stream, data);
        }
        else if (mappingType.equals(NodeContainerArray.class))
        {
            NodeContainerArray container = ((NodeContainerArray) element);
            Set<NodeContainerArrayEntry> entrySet = container.entrySet();
            StreamHelper.writeInt(stream, entrySet.size());
            for (NodeContainerArrayEntry entry : entrySet)
            {
                StreamHelper.writeInt(stream, entry.getKey().castKey(NodeContainerArrayElementKey.class).getIndex());
                writeElement(stream, entry.getValue());
            }
        }
        else if (mappingType.equals(NodeContainerMap.class))
        {
            NodeContainerMap container = ((NodeContainerMap) element);
            Set<NodeContainerMapEntry> entrySet = container.entrySet();
            StreamHelper.writeInt(stream, entrySet.size());
            for (NodeContainerMapEntry entry : entrySet)
            {
                StreamHelper.writeString(stream, entry.getKey().castKey(NodeContainerMapElementKey.class).getKey());
                writeElement(stream, entry.getValue());
            }
        }
        else
        {
            throw new IllegalStateException();
        }
    }
}
