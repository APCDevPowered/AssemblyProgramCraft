package org.apcdevpowered.vcpu32.vm.extdev;

import org.apcdevpowered.vcpu32.vm.AbstractExternalDevice;

public class ExternalDeviceKeyboard extends AbstractExternalDevice
{
    private int bufferPointer;
    private int[] buffer;
    private int[] keyStatus;
    
    private final Object keyboardLock = new Object();
    
    public String getDevicesName()
    {
        return "Keyboard";
    }
    public DeviceTypes getDeviceType()
    {
        return DeviceTypes.Input;
    }
    public void setMemoryValue(int idx, int value)
    {
        synchronized(keyboardLock)
        {
            if(idx > 288 || idx < 0)
            {
                return;
            }
            if(idx == 0)
            {
                if(buffer == null)
                {
                    return;
                }
                if(value == 1)
                {
                    if(bufferPointer > 0)
                    {
                        System.arraycopy(buffer, 1, buffer, 0, 31);
                        bufferPointer--;
                    }
                }
                else if(value == 2)
                {
                    System.arraycopy(new int[32], 0, buffer, 0, 32);
                    bufferPointer = 0;
                }
            }
            else if(idx >= 1 && idx <= 32)
            {
                if(buffer == null)
                {
                    return;
                }
                idx -= 1;
                if(value == 0)
                {
                    if(bufferPointer > 0 && idx < bufferPointer)
                    {
                        System.arraycopy(buffer, idx + 1, buffer, idx, 31 - idx);
                        bufferPointer--;
                    }
                }
                else
                {
                    buffer[idx] = value;
                }
            }
            else if(idx >= 33 && idx <= 288)
            {
                if(keyStatus == null)
                {
                    return;
                }
                idx -= 33;
                if(value == 0 || value == 1)
                {
                    keyStatus[idx] = value;
                }
                else if(value == 2)
                {
                    keyStatus[idx] = keyStatus[idx] == 0 ? 1 : 0;
                }
            }
        }
    }
    public int getMemoryValue(int idx)
    {
        synchronized(keyboardLock)
        {
            if(idx > 288 || idx < 0)
            {
                return 0;
            }
            if(idx == 0)
            {
                if(buffer == null)
                {
                    return 0;
                }
                return bufferPointer;
            }
            else if(idx >= 1 && idx <= 32)
            {
                if(buffer == null)
                {
                    return 0;
                }
                return buffer[idx - 1];
            }
            else if(idx >= 33 && idx <= 288)
            {
                if(keyStatus == null)
                {
                    return 0;
                }
                return keyStatus[idx - 33];
            }
            return 0;
        }
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
        synchronized(keyboardLock)
        {
            buffer = null;
            keyStatus = null;
            return true;
        }
    }
    public boolean reset()
    {
        synchronized(keyboardLock)
        {
            buffer = new int[32];
            keyStatus = new int[256];
            return true;
        }
    }
    public boolean start()
    {
        synchronized(keyboardLock)
        {
            buffer = new int[32];
            keyStatus = new int[256];
            return true;
        }
    }
    public void onCharTyped(char c)
    {
        synchronized(keyboardLock)
        {
            if(buffer == null)
            {
                return;
            }
            if(bufferPointer < 32)
            {
                buffer[bufferPointer] = c;
                bufferPointer++;
            }
        }
    }
    public void onKeyPressed(int key)
    {
        synchronized(keyboardLock)
        {
            if(keyStatus == null)
            {
                return;
            }
            keyStatus[key] = 1;
        }
    }
    public void onKeyReleased(int key)
    {
        synchronized(keyboardLock)
        {
            if(keyStatus == null)
            {
                return;
            }
            keyStatus[key] = 0;
        }
    }
}
