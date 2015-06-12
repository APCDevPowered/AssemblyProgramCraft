package org.apcdevpowered.vcpu32.extdev;

import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceRedstoneController;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

import net.minecraft.util.EnumFacing;

public class ExternalDeviceRedstoneController extends AbstractExternalDevice
{
    private TileEntityExternalDeviceRedstoneController redstoneController;
    public ExternalDeviceRedstoneController(TileEntityExternalDeviceRedstoneController redstoneController)
    {
        this.redstoneController = redstoneController;
    }
    public String getDevicesName()
    {
        return "RedstoneController";
    }
    public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Auxiliary;
    }
    public void setMemoryValue(int idx, int value)
    {
        if(idx > 11 || idx < 0)
        {
            return;
        }
        if(idx >= 0 && idx <= 5)
        {
            
        }
        else if(idx >= 6 && idx <= 11)
        {
            redstoneController.redstoneOutputPower[idx - 6] = value;
            redstoneController.needSync = true;
        }
    }
    public int getMemoryValue(int idx)
    {
        if(idx > 11 || idx < 0)
        {
            return 0;
        }
        if(idx >= 0 && idx <= 5)
        {
            return redstoneController.getRedstoneInputPower(EnumFacing.getFront(idx));
        }
        else if(idx >= 6 && idx <= 11)
        {
            return redstoneController.redstoneOutputPower[idx - 6];
        }
        return 0;
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