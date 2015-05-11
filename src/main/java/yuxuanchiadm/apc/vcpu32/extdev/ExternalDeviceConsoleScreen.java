package yuxuanchiadm.apc.vcpu32.extdev;

import java.util.Arrays;

import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public class ExternalDeviceConsoleScreen extends AbstractExternalDevice
{
    private TileEntityExternalDeviceConsoleScreen consoleScreen;
    
    public ExternalDeviceConsoleScreen(TileEntityExternalDeviceConsoleScreen consoleScreen)
    {
        this.consoleScreen = consoleScreen;
    }
    public String getDevicesName()
    {
        return "ConsoleScreen";
    }
    public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Display;
    }
    public void setMemoryValue(int idx, int value)
    {
        if(idx > 15008 || idx < 0)
        {
            return;
        }
        if(idx >= 0 && idx <= 14999)
        {
            consoleScreen.charBuffer[idx] = value;
        }
        else if(idx == 15000)
        {
            if(value == 0)
            {
                Arrays.fill(consoleScreen.charBuffer, 0);
            }
            else if(value == 1)
            {
                int index = 0;
                for(int length = consoleScreen.charBuffer.length; index < length; index++)
                {
                    if(index % 2 == 0)
                    {
                        consoleScreen.charBuffer[index] = consoleScreen.defaultChar;
                    }
                    else
                    {
                        consoleScreen.charBuffer[index] = consoleScreen.defaultColor;
                    }
                }
            }
            else if(value == 2)
            {
                int index = 0;
                for(int length = consoleScreen.charBuffer.length; index < length; index++)
                {
                    if(index % 2 == 0)
                    {
                        consoleScreen.charBuffer[index] = consoleScreen.defaultChar;
                    }
                    else
                    {
                        consoleScreen.charBuffer[index] = 0;
                    }
                }
            }
            else if(value == 3)
            {
                int index = 0;
                for(int length = consoleScreen.charBuffer.length; index < length; index++)
                {
                    if(index % 2 == 0)
                    {
                        consoleScreen.charBuffer[index] = 0;
                    }
                    else
                    {
                        consoleScreen.charBuffer[index] = consoleScreen.defaultColor;
                    }
                }
            }
        }
        else if(idx == 15001)
        {
            consoleScreen.defaultChar = value;
        }
        else if(idx == 15002)
        {
            consoleScreen.defaultColor = value;
        }
        else if(idx == 15003)
        {
            consoleScreen.cursorBlinkRate = value;
        }
        else if(idx == 15004)
        {
            consoleScreen.cursorLocation = value;
        }
        else if(idx == 15005)
        {
            consoleScreen.cursorEnabled = value == 0 ? false : true;
        }
        else if(idx == 15006)
        {
            consoleScreen.hardwareScale = value;
        }
        else if(idx == 15007)
        {
            consoleScreen.horizontalScale = value;
        }
        else if(idx == 15008)
        {
            consoleScreen.verticalScale = value;
        }
        consoleScreen.syncConsoleData(idx, value);
        return;
    }
    public int getMemoryValue(int idx)
    {
        if(idx > 15008 || idx < 0)
        {
            return 0;
        }
        if(idx >= 0 && idx <= 14999)
        {
            return consoleScreen.charBuffer[idx];
        }
        else if(idx == 15000)
        {
            if(consoleScreen.defaultChar == 0)
            {
                if(consoleScreen.defaultColor == 0)
                {
                    return 0;
                }
                else
                {
                    return 3;
                }
            }
            else
            {
                if(consoleScreen.defaultColor == 0)
                {
                    return 2;
                }
                else
                {
                    return 1;
                }
            }
        }
        else if(idx == 15001)
        {
            return consoleScreen.defaultChar;
        }
        else if(idx == 15002)
        {
            return consoleScreen.defaultColor;
        }
        else if(idx == 15003)
        {
            return consoleScreen.cursorBlinkRate;
        }
        else if(idx == 15004)
        {
            return consoleScreen.cursorLocation;
        }
        else if(idx == 15005)
        {
            return consoleScreen.cursorEnabled ? 1 : 0;
        }
        else if(idx == 15006)
        {
            return consoleScreen.hardwareScale;
        }
        else if(idx == 15007)
        {
            return consoleScreen.horizontalScale;
        }
        else if(idx == 15008)
        {
            return consoleScreen.verticalScale;
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