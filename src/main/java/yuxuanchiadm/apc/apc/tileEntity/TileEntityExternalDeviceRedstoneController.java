package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceRedstoneController;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceRedstoneController extends ITileEntityExternalDevice
{
    public volatile boolean needSync;
    public int[] redstoneOutputPower = new int[6];
    
    public ExternalDeviceRedstoneController externalDeviceRedstoneController = new ExternalDeviceRedstoneController(this);
    
    public TileEntityExternalDeviceRedstoneController()
    {
        
    }
    public void updateEntity()
    {
        super.updateEntity();
        if(needSync == true)
        {
            worldObj.notifyBlockChange(xCoord, yCoord, zCoord, blockType);
            needSync = false;
        }
    }
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceRedstoneController;
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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        redstoneOutputPower = par1NBTTagCompound.getIntArray("redstoneOutputPower");
    }
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setIntArray("redstoneOutputPower", redstoneOutputPower);
    }
    public int getRedstoneInputPower(int face)
    {
        int cx = xCoord + Facing.offsetsXForSide[face];
        int cy = yCoord + Facing.offsetsYForSide[face];
        int cz = zCoord + Facing.offsetsZForSide[face];
        int indirectPowerLevel = worldObj.getIndirectPowerLevelTo(cx, cy, cz, face);
        return indirectPowerLevel >= 15 ? indirectPowerLevel : Math.max(indirectPowerLevel, worldObj.getBlock(cx, cy, cz) == Blocks.redstone_wire ? worldObj.getBlockMetadata(cx, cy, cz) : 0);
    }
}