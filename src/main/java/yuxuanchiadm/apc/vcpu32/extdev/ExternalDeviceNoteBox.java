package yuxuanchiadm.apc.vcpu32.extdev;

import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDeviceNoteBox;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class ExternalDeviceNoteBox extends AbstractExternalDevice
{
    private TileEntityExternalDeviceNoteBox noteBox;
    public ExternalDeviceNoteBox(TileEntityExternalDeviceNoteBox noteBox)
    {
        this.noteBox = noteBox;
    }
    public String getDevicesName()
    {
        return "NoteBox";
    }
    public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Sound;
    }
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
    public void setMemoryValues(int idx, int[] values)
    {
        for(int i = 0;i < values.length;i++)
        {
            setMemoryValue(idx + i, values[i]);
        }
    }
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
    public boolean shutDown()
    {
        return true;
    }
    public boolean reset()
    {
        return true;
    }
    public boolean start()
    {
        return true;
    }
}