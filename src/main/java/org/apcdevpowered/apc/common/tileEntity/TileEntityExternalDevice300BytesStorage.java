package org.apcdevpowered.apc.common.tileEntity;

import org.apcdevpowered.vcpu32.extdev.ExternalDevice300BytesStorage;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDevice300BytesStorage extends TileEntityExternalDevice
{
    public ExternalDevice300BytesStorage externalDevice300BytesStorage = new ExternalDevice300BytesStorage();
    
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDevice300BytesStorage;
    }
}
