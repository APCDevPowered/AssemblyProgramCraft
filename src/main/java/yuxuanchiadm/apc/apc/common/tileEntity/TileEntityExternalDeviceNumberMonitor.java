package yuxuanchiadm.apc.apc.common.tileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.common.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceNumberMonitor extends TileEntityExternalDevice
{
    public volatile boolean needSync;
    public int number;
    public ExternalDeviceNumberMonitor externalDeviceNumberMonitor = new ExternalDeviceNumberMonitor(this);
    
    public TileEntityExternalDeviceNumberMonitor()
    {
    }
    public void update()
    {
        super.update();
        if (needSync)
        {
            syncMonitorData();
            needSync = false;
        }
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceNumberMonitor;
    }
    public EnumFacing[] getConnectorConnectableFaces(IBlockState state)
    {
        return new EnumFacing[]
        {
                ((EnumFacing) state.getValue(BlockExternalDeviceNumberMonitor.FACING)).getOpposite()
        };
    }
    public void writeDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setInteger("number", number);
    }
    public void readDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
        number = nbtTagCompound.getInteger("number");
    }
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        number = nbtTagCompound.getInteger("number");
    }
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("number", number);
    }
    public void syncMonitorData()
    {
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ServerPacket.SyncNumberMonitorData.getValue();
        pak.dataInt = new int[5];
        pak.dataInt[0] = getPos().getX();
        pak.dataInt[1] = getPos().getY();
        pak.dataInt[2] = getPos().getZ();
        pak.dataInt[3] = getWorld().provider.getDimensionId();
        pak.dataInt[4] = number;
        AssemblyProgramCraft.sendToAllPlayers(pak);
    }
}
