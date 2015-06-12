package org.apcdevpowered.vcpu32.extdev;

import org.apcdevpowered.util.array.ArrayTools;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

import net.minecraft.nbt.NBTTagCompound;

public class ExternalDevice300BytesStorage extends AbstractExternalDevice
{    
    private int[] data = new int[300];
    public String getDevicesName()
    {
        return "300BytesStorage";
    }
    public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Storage;
    }
    public void setMemoryValue(int idx, int value)
    {
        if(idx >= data.length || idx < 0)
        {
            return;
        }
        if(data == null)
        {
            return;
        }
        data[idx] = value;
    }
    public int getMemoryValue(int idx)
    {
        if(idx >= data.length || idx < 0)
        {
            return 0;
        }
        if(data == null)
        {
            return 0;
        }
        return data[idx];
    }
    public void setMemoryValues(int idx, int[] values)
    {
        if(data == null)
        {
            return;
        }
        ArrayTools.safeArraycopy(values, 0, data, idx, values.length);
    }
    public int[] getMemoryValues(int idx, int length)
    {
        if(length <= 0)
        {
            return new int[0];
        }
        if(data == null)
        {
            return new int[length];
        }
        int[] tempArray = new int[length];
        ArrayTools.safeArraycopy(data, idx, tempArray, 0, length);
        return tempArray;
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
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        data = par1NBTTagCompound.getIntArray("data");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setIntArray("data", data);
    }
}