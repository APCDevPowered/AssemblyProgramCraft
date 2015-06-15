package org.apcdevpowered.vcpu32.vm.storage.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apcdevpowered.util.io.StreamHelper;
import org.apcdevpowered.vcpu32.vm.storage.NodeElement;
import org.apcdevpowered.vcpu32.vm.storage.exception.ElementTypeMismatchException;
import org.apcdevpowered.vcpu32.vm.storage.exception.UnsupportedVersionException;
import org.apcdevpowered.vcpu32.vm.storage.persistence.version.madokao.MadokaoNodeDataPersistenceImpl;

public abstract class NodeDataPersistence
{
    private static final Logger logger = LogManager.getLogger();
    private static final SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int magicNumber = 0x1D048596;
    private static NodeDataPersistence[] impls = new NodeDataPersistence[VersionNaming.getCurrentVersion() + 1];
    private final int version;
    
    protected NodeDataPersistence(int version)
    {
        this.version = version;
    }
    public final int getVersion()
    {
        return version;
    }
    public abstract <E extends NodeElement> E readElement(InputStream stream, Class<E> clazz) throws IOException, ElementTypeMismatchException;
    public final NodeElement readElement(InputStream stream) throws IOException
    {
        try
        {
            return readElement(stream, NodeElement.class);
        }
        catch (ElementTypeMismatchException e)
        {
            throw new IllegalStateException(e);
        }
    }
    public abstract void writeElement(OutputStream stream, NodeElement element) throws IOException;
    public static NodeElement loadNode(InputStream stream) throws IOException, UnsupportedVersionException
    {
        try
        {
            return loadNode(stream, NodeElement.class);
        }
        catch (ElementTypeMismatchException e)
        {
            throw new IllegalStateException(e);
        }
    }
    public static <E extends NodeElement> E loadNode(InputStream stream, Class<E> clazz) throws IOException, ElementTypeMismatchException, UnsupportedVersionException
    {
        logger.trace("Loading persistence data.");
        if (stream == null || clazz == null)
        {
            throw new NullPointerException();
        }
        int magicNumber = StreamHelper.readInt(stream);
        if (NodeDataPersistence.magicNumber != magicNumber)
        {
            throw new IOException("Incorrect magic number 0x" + Integer.toHexString(magicNumber));
        }
        int version = StreamHelper.readInt(stream);
        long timestamp = StreamHelper.readLong(stream);
        NodeDataPersistence impl = getImpl(version);
        logger.trace("Loading node data. Version " + version + ". Timestamp " + timestamp + "(" + timestampFormatter.format(new Date(timestamp)) + ").");
        E element = impl.readElement(stream, clazz);
        logger.trace("Node data loaded.");
        return element;
    }
    public static NodeElement loadNode(byte[] bytes) throws IOException, UnsupportedVersionException
    {
        return loadNode(new ByteArrayInputStream(bytes));
    }
    public static <E extends NodeElement> E loadNode(byte[] bytes, Class<E> clazz) throws IOException, ElementTypeMismatchException, UnsupportedVersionException
    {
        return loadNode(new ByteArrayInputStream(bytes), clazz);
    }
    public static NodeElement loadNode(File file) throws IOException, UnsupportedVersionException
    {
        try (FileInputStream stream = new FileInputStream(file))
        {
            return loadNode(stream);
        }
    }
    public static <E extends NodeElement> E loadNode(File file, Class<E> clazz) throws IOException, ElementTypeMismatchException, UnsupportedVersionException
    {
        try (FileInputStream stream = new FileInputStream(file))
        {
            return loadNode(stream, clazz);
        }
    }
    public static void saveNode(OutputStream stream, NodeElement element) throws IOException, UnsupportedVersionException
    {
        saveNode(stream, element, VersionNaming.getCurrentVersion(), System.currentTimeMillis());
    }
    public static void saveNode(OutputStream stream, NodeElement element, int version) throws IOException, UnsupportedVersionException
    {
        saveNode(stream, element, version, System.currentTimeMillis());
    }
    public static void saveNode(OutputStream stream, NodeElement element, int version, long timestamp) throws IOException, UnsupportedVersionException
    {
        logger.trace("Saving persistence data.");
        if (stream == null || element == null)
        {
            throw new NullPointerException();
        }
        NodeDataPersistence impl = getImpl(version);
        StreamHelper.writeInt(stream, magicNumber);
        StreamHelper.writeInt(stream, version);
        StreamHelper.writeLong(stream, timestamp);
        logger.trace("Saving node data. Version " + version + ". Timestamp " + timestamp + "(" + timestampFormatter.format(new Date(timestamp)) + ").");
        impl.writeElement(stream, element);
        logger.trace("Node data saved.");
    }
    public static byte[] saveNode(NodeElement element) throws IOException, UnsupportedVersionException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        saveNode(stream, element);
        return stream.toByteArray();
    }
    public static byte[] saveNode(NodeElement element, int version) throws IOException, UnsupportedVersionException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        saveNode(stream, element, version);
        return stream.toByteArray();
    }
    public static byte[] saveNode(NodeElement element, int version, long timestamp) throws IOException, UnsupportedVersionException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        saveNode(stream, element, version, timestamp);
        return stream.toByteArray();
    }
    public static void saveNode(File file, NodeElement element) throws IOException, UnsupportedVersionException
    {
        try (FileOutputStream stream = new FileOutputStream(file))
        {
            saveNode(stream, element);
        }
    }
    public static void saveNode(File file, NodeElement element, int version) throws IOException, UnsupportedVersionException
    {
        try (FileOutputStream stream = new FileOutputStream(file))
        {
            saveNode(stream, element, version);
        }
    }
    public static void saveNode(File file, NodeElement element, int version, long timestamp) throws IOException, UnsupportedVersionException
    {
        try (FileOutputStream stream = new FileOutputStream(file))
        {
            saveNode(stream, element, version, timestamp);
        }
    }
    public static NodeDataPersistence getImpl(int version) throws UnsupportedVersionException
    {
        if (version <= 0 || version >= impls.length)
        {
            throw new UnsupportedVersionException(version);
        }
        return impls[version];
    }
    private static void registerImpl(int version, NodeDataPersistence impl)
    {
        if (version <= 0 || version >= impls.length)
        {
            throw new IllegalStateException();
        }
        impls[version] = impl;
    }
    
    static
    {
        registerImpl(1, new MadokaoNodeDataPersistenceImpl());
    }
}