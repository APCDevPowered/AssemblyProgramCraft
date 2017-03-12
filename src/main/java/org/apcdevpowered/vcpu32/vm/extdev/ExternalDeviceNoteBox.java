package org.apcdevpowered.vcpu32.vm.extdev;

import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNoteBox;
import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

public class ExternalDeviceNoteBox extends AbstractExternalDevice
{
    private TileEntityExternalDeviceNoteBox noteBox;
    public ExternalDeviceNoteBox(TileEntityExternalDeviceNoteBox noteBox)
    {
        this.noteBox = noteBox;
    }
    @Override
	public String getDevicesName()
    {
        return "NoteBox";
    }
    @Override
	public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Sound;
    }
    @Override
	public void setMemoryValue(int idx, int value)
    {
        if(idx >= 3 || idx < 0)
        {
            return;
        }
        if(idx == 0)
        {
            if(value < 0 || value > 24)
            {
                return;
            }
            noteBox.playNote(value);
        }
        else if(idx == 1)
        {
            if(value < 0 || value > 4)
            {
                return;
            }
            noteBox.instrument = value;
        }
        else if(idx == 2)
        {
            noteBox.showParticle = (value == 0 ? false : true);
        }
    }
    @Override
	public int getMemoryValue(int idx)
    {
        if(idx >= 3 || idx < 0)
        {
            return 0;
        }
        if(idx == 1)
        {
            return noteBox.instrument;
        }
        if(idx == 2)
        {
            return noteBox.showParticle ? 1 : 0;
        }
        return 0;
    }
    @Override
	public void setMemoryValues(int idx, int[] values)
    {
        for(int i = 0;i < values.length;i++)
        {
            setMemoryValue(idx + i, values[i]);
        }
    }
    @Override
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
    @Override
	public boolean shutDown()
    {
        return true;
    }
    @Override
	public boolean reset()
    {
        return true;
    }
    @Override
	public boolean start()
    {
        return true;
    }
}