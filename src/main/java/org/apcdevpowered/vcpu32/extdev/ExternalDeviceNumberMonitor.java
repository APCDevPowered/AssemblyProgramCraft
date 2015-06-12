package org.apcdevpowered.vcpu32.extdev;

import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNumberMonitor;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

public class ExternalDeviceNumberMonitor extends AbstractExternalDevice
{
    private TileEntityExternalDeviceNumberMonitor numberMonitor;
    public ExternalDeviceNumberMonitor(TileEntityExternalDeviceNumberMonitor numberMonitor)
    {
        this.numberMonitor = numberMonitor;
    }
    public String getDevicesName()
    {
        return "NumberMonitor";
    }
    public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Display;
    }
    public void setMemoryValue(int idx, int value)
    {
        if(idx != 0)
        {
            return;
        }
        numberMonitor.number = value;
        numberMonitor.needSync = true;
    }
    public int getMemoryValue(int idx)
    {
        if(idx != 0)
        {
            return 0;
        }
        return numberMonitor.number;
    }
    public void setMemoryValues(int idx, int[] values)
    {
        for(int i = 0;i < values.length;i++)
        {
            setMemoryValue(idx + i, values[i]);
        }
    }
    public int[] getMemoryValues(int idx, int length)
    {
        if(length <= 0)
        {
            return new int[0];
        }
        int[] values = new int[length];
        for(int i = 0;i < length;i++)
        {
            values[i] = getMemoryValue(idx + i);
        }
        return values;
    }
    public boolean shutDown()
    {
        return true;
    }
    public boolean reset()
    {
        return true;
    }
    public boolean start()
    {
        return true;
    }
}