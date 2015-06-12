package org.apcdevpowered.apc.common.tileEntity;

import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBlocks;
import org.apcdevpowered.vcpu32.extdev.ExternalDeviceNoteBox;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityExternalDeviceNoteBox extends TileEntityExternalDevice
{
    public int instrument;
    public boolean showParticle;
    public ExternalDeviceNoteBox externalDeviceNoteBox = new ExternalDeviceNoteBox(this);
    
    @Override
    public AbstractExternalDevice getExternalDevice()
    {
        return externalDeviceNoteBox;
    }
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        instrument = nbtTagCompound.getInteger("instrument");
        showParticle = nbtTagCompound.getBoolean("showParticle");
    }
    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("instrument", instrument);
        nbtTagCompound.setBoolean("showParticle", showParticle);
    }
    public void playNote(int note)
    {
        float pitch = (float) Math.pow(2.0D, (note - 12) / 12.0D);
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
        getWorld().playSoundEffect(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, "note." + sound, 5.0F, pitch);
        if (showParticle)
        {
            getWorld().addBlockEvent(getPos(), AssemblyProgramCraftBlocks.block_external_device_note_box, 0, note);
        }
    }
}