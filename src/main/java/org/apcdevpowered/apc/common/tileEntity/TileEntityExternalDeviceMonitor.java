package org.apcdevpowered.apc.common.tileEntity;

import java.util.Arrays;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceMonitor;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftPacket;
import org.apcdevpowered.vcpu32.extdev.ExternalDeviceMonitor;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityExternalDeviceMonitor extends TileEntityExternalDevice
{
    public int[] graphicsMemory = new int[75 * 100];
    public ExternalDeviceMonitor externalDeviceMonitor = new ExternalDeviceMonitor(this);
    
    public TileEntityExternalDeviceMonitor()
    {
        Arrays.fill(graphicsMemory, 0xFF000000);
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceMonitor;
    }
    public EnumFacing[] getConnectorConnectableFaces(IBlockState state)
    {
        return new EnumFacing[]
        {
                ((EnumFacing) state.getValue(BlockExternalDeviceMonitor.FACING)).getOpposite()
        };
    }
    public void writeDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setIntArray("graphicsMemory", graphicsMemory);
    }
    public void readDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
        graphicsMemory = nbtTagCompound.getIntArray("graphicsMemory");
    }
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        graphicsMemory = nbtTagCompound.getIntArray("graphicsMemory");
    }
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setIntArray("graphicsMemory", graphicsMemory);
    }
    public boolean setMemoryValue(int idx, int value)
    {
        if (idx > 480000 || idx < 0)
        {
            System.out.println("超出范围:idx = " + idx);
            return false;
        }
        if (graphicsMemory == null)
        {
            System.out.println("设备未启动");
            return false;
        }
        if (idx == 0)
        {
            Arrays.fill(graphicsMemory, value);
        }
        else
        {
            graphicsMemory[idx - 1] = value;
        }
        return true;
    }
    public void syncMonitorData(int idx, int value)
    {
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ServerPacket.SyncMonitorData.getValue();
        pak.dataInt = new int[6];
        pak.dataInt[0] = getPos().getX();
        pak.dataInt[1] = getPos().getY();
        pak.dataInt[2] = getPos().getZ();
        pak.dataInt[3] = getWorld().provider.getDimensionId();
        pak.dataInt[4] = idx;
        pak.dataInt[5] = value;
        AssemblyProgramCraft.sendToAllPlayers(pak);
    }
}
