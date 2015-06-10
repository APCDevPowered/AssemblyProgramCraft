package yuxuanchiadm.apc.apc.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceRedstoneController;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceRedstoneController extends TileEntityExternalDevice
{
    public volatile boolean needSync;
    public int[] redstoneOutputPower = new int[6];
    public ExternalDeviceRedstoneController externalDeviceRedstoneController = new ExternalDeviceRedstoneController(this);
    
    public TileEntityExternalDeviceRedstoneController()
    {
    }
    public void update()
    {
        super.update();
        if (needSync == true)
        {
            getWorld().notifyNeighborsOfStateChange(getPos(), blockType);
            needSync = false;
        }
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceRedstoneController;
    }
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        redstoneOutputPower = nbtTagCompound.getIntArray("redstoneOutputPower");
    }
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