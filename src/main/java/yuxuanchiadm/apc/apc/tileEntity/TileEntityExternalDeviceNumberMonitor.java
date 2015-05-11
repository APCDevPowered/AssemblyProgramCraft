package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceNumberMonitor extends ITileEntityExternalDevice
{
    public volatile boolean needSync;
    public int number;
    
    public ExternalDeviceNumberMonitor externalDeviceNumberMonitor = new ExternalDeviceNumberMonitor(this);
    
    public TileEntityExternalDeviceNumberMonitor()
    {
        
    }
    
    public void updateEntity()
    {
        super.updateEntity();
        if(needSync)
        {
            syncMonitorData();
            needSync = false;
        }
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceNumberMonitor;
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
        var1.setInteger("number", number);
    }
    public void readDescriptionNbt(NBTTagCompound var1)
    {
        number = var1.getInteger("number");
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        number = par1NBTTagCompound.getInteger("number");
    }
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("number", number);
    }
    public void syncMonitorData()
    {
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ServerPacket.SyncNumberMonitorData.getValue();
        pak.dataInt = new int[5];
        pak.dataInt[0] = xCoord;
        pak.dataInt[1] = yCoord;
        pak.dataInt[2] = zCoord;
        pak.dataInt[3] = worldObj.getWorldInfo().getVanillaDimension();
        pak.dataInt[4] = number;
        AssemblyProgramCraft.sendToAllPlayers(pak);
    }
}
