package yuxuanchiadm.apc.apc.common.tileEntity;

import yuxuanchiadm.apc.vcpu32.extdev.ExternalDevice300BytesStorage;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDevice300BytesStorage extends TileEntityExternalDevice
{
    public ExternalDevice300BytesStorage externalDevice300BytesStorage = new ExternalDevice300BytesStorage();
    
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDevice300BytesStorage;
    }
}
