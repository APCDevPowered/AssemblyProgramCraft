package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceMonitor;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceMonitor extends ITileEntityExternalDevice
{
    public int[] graphicsMemory = new int[7500];
    
    public ExternalDeviceMonitor externalDeviceMonitor = new ExternalDeviceMonitor(this);
    
    public TileEntityExternalDeviceMonitor()
    {
        Arrays.fill(graphicsMemory, 0xFF000000);
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceMonitor;
    }
    public ArrayList<TileEntityVCPU32ComputerConnector> getAllConnectedConnector()
    {
        ArrayList<TileEntityVCPU32ComputerConnector> connectorList = new ArrayList<TileEntityVCPU32ComputerConnector>();
        if(worldObj.getBlock(xCoord, yCoord, zCoord + 1) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1)  & 3;
            if(blockDirection == 2)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1));
            }
        }
        if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord)  & 3;
            if(blockDirection == 3)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord));
            }
        }
        if(worldObj.getBlock(xCoord, yCoord, zCoord - 1) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1)  & 3;
            if(blockDirection == 0)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1));
            }
        }
        if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
        {
            int blockDirection = worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord)  & 3;
            if(blockDirection == 1)
            {
                connectorList.add((TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord));
            }
        }
        return connectorList;
    }
    public void writeDescriptionNbt(NBTTagCompound var1)
    {
        var1.setIntArray("graphicsMemory", graphicsMemory);
    }
    public void readDescriptionNbt(NBTTagCompound var1)
    {
        graphicsMemory = var1.getIntArray("graphicsMemory");
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        graphicsMemory = par1NBTTagCompound.getIntArray("graphicsMemory");
    }
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setIntArray("graphicsMemory", graphicsMemory);
    }
    public boolean setMemoryValue(int idx, int value)
    {
        if(idx > 480000 || idx < 0)
        {
            System.out.println("超出范围:idx = " + idx);
            return false;
        }
        if(graphicsMemory == null)
        {
            System.out.println("设备未启动");
            return false;
        }
        if(idx == 0)
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
        pak.dataInt[0] = xCoord;
        pak.dataInt[1] = yCoord;
        pak.dataInt[2] = zCoord;
        pak.dataInt[3] = worldObj.getWorldInfo().getVanillaDimension();
        pak.dataInt[4] = idx;
        pak.dataInt[5] = value;
        AssemblyProgramCraft.sendToAllPlayers(pak);
    }
}
