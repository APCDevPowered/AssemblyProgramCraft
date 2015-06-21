package org.apcdevpowered.vcpu32.vm.storage.persistence;

import org.apcdevpowered.vcpu32.vm.storage.exception.UnsupportedVersionException;

public class VersionNaming
{
    private static final String[] versionNames;
    
    public static String getName(int version) throws UnsupportedVersionException
    {
        if (version <= 0 || version >= versionNames.length)
        {
            throw new UnsupportedVersionException(version);
        }
        return versionNames[version];
    }
    public static int getCurrentVersion()
    {
        return versionNames.length - 1;
    }
    
    static
    {
        versionNames = new String[]
        {
                null,
                "Madokao"
        };
    }
}