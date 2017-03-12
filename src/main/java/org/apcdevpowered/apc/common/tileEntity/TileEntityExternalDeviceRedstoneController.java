package org.apcdevpowered.apc.common.tileEntity;

import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;
import org.apcdevpowered.vcpu32.vm.extdev.ExternalDeviceRedstoneController;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityExternalDeviceRedstoneController extends TileEntityExternalDevice
{
    public volatile boolean needSync;
    public int[] redstoneOutputPower = new int[6];
    public ExternalDeviceRedstoneController externalDeviceRedstoneController = new ExternalDeviceRedstoneController(this);
    
    public TileEntityExternalDeviceRedstoneController()
    {
    }
    @Override
	public void update()
    {
        super.update();
        if (needSync == true)
        {
            getWorld().notifyNeighborsOfStateChange(getPos(), blockType);
            needSync = false;
        }
    }
    @Override
	public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceRedstoneController;
    }
    @Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        redstoneOutputPower = nbtTagCompound.getIntArray("redstoneOutputPower");
    }
    @Override
	public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setIntArray("redstoneOutputPower", redstoneOutputPower);
    }
    public int getRedstoneInputPower(EnumFacing face)
    {
        return getWorld().getRedstonePower(getPos(), face);
    }
}