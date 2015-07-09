package org.apcdevpowered.vcpu32.vm.storage.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.apcdevpowered.util.StreamUtils;
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
        int dataValue = StreamUtils.readInt(stream);
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
            short data = StreamUtils.readShort(stream);
            element = new NodeScalarShort(data);
        }
        else if (mappingType.equals(NodeScalarInteger.class))
        {
            int data = StreamUtils.readInt(stream);
            element = new NodeScalarInteger(data);
        }
        else if (mappingType.equals(NodeScalarLong.class))
        {
            long data = StreamUtils.readLong(stream);
            element = new NodeScalarLong(data);
        }
        else if (mappingType.equals(NodeScalarFloat.class))
        {
            float data = StreamUtils.readFloat(stream);
            element = new NodeScalarFloat(data);
        }
        else if (mappingType.equals(NodeScalarDouble.class))
        {
            double data = StreamUtils.readDouble(stream);
            element = new NodeScalarDouble(data);
        }
        else if (mappingType.equals(NodeScalarByteArray.class))
        {
            int length = StreamUtils.readInt(stream);
            byte[] data = StreamUtils.readByteArrayFully(stream, length);
            element = new NodeScalarByteArray(data);
        }
        else if (mappingType.equals(NodeScalarIntegerArray.class))
        {
            int length = StreamUtils.readInt(stream);
            int[] data = StreamUtils.readIntArrayFully(stream, length);
            element = new NodeScalarIntegerArray(data);
        }
        else if (mappingType.equals(NodeScalarString.class))
        {
            String data = StreamUtils.readString(stream);
            element = new NodeScalarString(data);
        }
        else if (mappingType.equals(NodeContainerArray.class))
        {
            int length = StreamUtils.readInt(stream);
            NodeContainerArray container = new NodeContainerArray();
            for (int index = 0; index < length; index++)
            {
                int key = StreamUtils.readInt(stream);
                NodeElement child = readElement(stream);
                container.addElement(NodeContainerArray.makeKey(key), child);
            }
            element = container;
        }
        else if (mappingType.equals(NodeContainerMap.class))
        {
            int length = StreamUtils.readInt(stream);
            NodeContainerMap container = new NodeContainerMap();
            for (int index = 0; index < length; index++)
            {
                String key = StreamUtils.readString(stream);
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
        StreamUtils.writeInt(stream, dataValue);
        if (mappingType.equals(NodeScalarByte.class))
        {
            byte data = ((NodeScalarByte) element).getData();
            stream.write(data & 0xFF);
        }
        else if (mappingType.equals(NodeScalarShort.class))
        {
            short data = ((NodeScalarShort) element).getData();
            StreamUtils.writeShort(stream, data);
        }
        else if (mappingType.equals(NodeScalarInteger.class))
        {
            int data = ((NodeScalarInteger) element).getData();
            StreamUtils.writeInt(stream, data);
        }
        else if (mappingType.equals(NodeScalarLong.class))
        {
            long data = ((NodeScalarLong) element).getData();
            StreamUtils.writeLong(stream, data);
        }
        else if (mappingType.equals(NodeScalarFloat.class))
        {
            float data = ((NodeScalarFloat) element).getData();
            StreamUtils.writeFloat(stream, data);
        }
        else if (mappingType.equals(NodeScalarDouble.class))
        {
            double data = ((NodeScalarDouble) element).getData();
            StreamUtils.writeDouble(stream, data);
        }
        else if (mappingType.equals(NodeScalarByteArray.class))
        {
            byte[] data = ((NodeScalarByteArray) element).getData();
            int length = data.length;
            StreamUtils.writeInt(stream, length);
            stream.write(data);
        }
        else if (mappingType.equals(NodeScalarIntegerArray.class))
        {
            int[] data = ((NodeScalarIntegerArray) element).getData();
            int length = data.length;
            StreamUtils.writeInt(stream, length);
            for (int index = 0; index < length; index++)
            {
                StreamUtils.writeInt(stream, data[index]);
            }
        }
        else if (mappingType.equals(NodeScalarString.class))
        {
            String data = ((NodeScalarString) element).getData();
            StreamUtils.writeString(stream, data);
        }
        else if (mappingType.equals(NodeContainerArray.class))
        {
            NodeContainerArray container = ((NodeContainerArray) element);
            Set<NodeContainerArrayEntry> entrySet = container.entrySet();
            StreamUtils.writeInt(stream, entrySet.size());
            for (NodeContainerArrayEntry entry : entrySet)
            {
                StreamUtils.writeInt(stream, entry.getKey().castKey(NodeContainerArrayElementKey.class).getIndex());
                writeElement(stream, entry.getValue());
            }
        }
        else if (mappingType.equals(NodeContainerMap.class))
        {
            NodeContainerMap container = ((NodeContainerMap) element);
            Set<NodeContainerMapEntry> entrySet = container.entrySet();
            StreamUtils.writeInt(stream, entrySet.size());
            for (NodeContainerMapEntry entry : entrySet)
            {
                StreamUtils.writeString(stream, entry.getKey().castKey(NodeContainerMapElementKey.class).getKey());
                writeElement(stream, entry.getValue());
            }
        }
        else
        {
            throw new IllegalStateException();
        }
    }
}
