package org.apcdevpowered.vcpu32.vm.storage.persistence;

import org.apcdevpowered.vcpu32.vm.storage.exception.UnsupportedVersionException;
import org.apcdevpowered.vcpu32.vm.storage.persistence.version.MadokaoNodeDataPersistenceImpl;

public abstract class NodeDataPersistence
{
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
        if(version <= 0 || version >= impls.length)
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