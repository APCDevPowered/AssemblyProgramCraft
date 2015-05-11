package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceNoteBox;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class TileEntityExternalDeviceNoteBox extends ITileEntityExternalDevice
{
    public int instrument;
    public boolean showParticle;
    
    public ExternalDeviceNoteBox externalDeviceNoteBox = new ExternalDeviceNoteBox(this);
    
    @Override
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
    @Override
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceNoteBox;
    }
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        instrument = par1NBTTagCompound.getInteger("instrument");
        showParticle = par1NBTTagCompound.getBoolean("showParticle");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("instrument", instrument);
        par1NBTTagCompound.setBoolean("showParticle", showParticle);
    }
    public void playNote(int note)
    {
        float pitch = (float)Math.pow(2.0D, (note - 12) / 12.0D);
        String sound;
        switch (instrument)
        {
            case 0:
                sound = "harp";
                break;
            case 1:
                sound = "bd";
                break;
            case 2:
                sound = "snare";
                break;
            case 3:
                sound = "hat";
                break;
            case 4:
                sound = "bassattack";
                break;
            default:
                return;
        }
        worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "note." + sound, 5.0F, pitch);
        if(showParticle)
        {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, AssemblyProgramCraft.instance.block_note_box, 0, note);
        }
    }
}