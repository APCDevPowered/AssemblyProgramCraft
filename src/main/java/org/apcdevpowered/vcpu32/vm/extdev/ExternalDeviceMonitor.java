package org.apcdevpowered.vcpu32.vm.extdev;

import java.util.Arrays;

import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceMonitor;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

public class ExternalDeviceMonitor extends AbstractExternalDevice
{
    private TileEntityExternalDeviceMonitor monitor;
    public ExternalDeviceMonitor(TileEntityExternalDeviceMonitor monitor)
    {
        this.monitor = monitor;
    }
    public String getDevicesName()
    {
        return "Monitor";
    }
    public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Display;
    }
    public void setMemoryValue(int idx, int value)
    {
        if(idx > 7500 || idx < 0)
        {
            return;
        }
        if(monitor.graphicsMemory == null)
        {
            return;
        }
        int red = (value & 0xFF000000) >>> 24;
        int green = (value & 0x00FF0000) >>> 16;
        int blue = (value & 0x0000FF00) >>> 8;
        
        value = 0xFF000000 | (red << 16) | (green << 8) | (blue << 0);
        
        if(idx == 0)
        {
            Arrays.fill(monitor.graphicsMemory, value);
            monitor.syncMonitorData(0, value);
        }
        else
        {
            monitor.graphicsMemory[idx - 1] = value;
            monitor.syncMonitorData(idx, value);
        }
    }
    public int getMemoryValue(int idx)
    {
        if(idx > 7500 || idx < 0)
        {
            return 0;
        }
        if(monitor.graphicsMemory == null)
        {
            return 0;
        }
        if(idx == 0)
        {
            return 0;
        }
        else
        {
            int value = monitor.graphicsMemory[idx - 1];
            int red = value & 0xFF ;
            int green = (value & 0xFF00) >>> 8;
            int blue = (value & 0xFF0000) >>> 16;
            
            value = red << 24 | green << 16 | blue << 8;
            
            return value;
        }
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