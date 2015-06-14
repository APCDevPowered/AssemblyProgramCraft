package org.apcdevpowered.vcpu32.vm.storage.exception;

import org.apcdevpowered.vcpu32.vm.storage.persistence.VersionNaming;

public class UnsupportedVersionException extends Exception
{
    private static final long serialVersionUID = 1L;
    private int version;
    
    public UnsupportedVersionException(int version)
    {
        super("Unsupported version " + version + ". Current version" + VersionNaming.getCurrentVersion());
        this.version = version;
    }
    public int getVersion()
    {
        return version;
    }
}