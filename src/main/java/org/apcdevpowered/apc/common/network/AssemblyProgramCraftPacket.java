package org.apcdevpowered.apc.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.IOException;
import java.io.UncheckedIOException;

public class AssemblyProgramCraftPacket
{
    public static enum ServerPacket
    {
        ComputerPowerChange(1), CompiledResult(2), WriteResult(3), DecompiledResult(4), ComputerRuntimeError(5), SyncMonitorData(6), SyncNumberMonitorData(7), SyncConsoleData(8), SyncExternalDevicePort(9);
        private final int value;
        
        public int getValue()
        {
            return value;
        }
        ServerPacket(int value)
        {
            this.value = value;
        }
    }
    public static enum ClientPacket
    {
        BIOSWriterDoCompiled(10), BIOSWriterDoWrite(11), BIOSWriterDoDecompiled(12), PortSettingToolSetID(13), KeyboardStatusChange(14);
        private final int value;
        
        public int getValue()
        {
            return value;
        }
        ClientPacket(int value)
        {
            this.value = value;
        }
    }
    
    public int packetType;
    public String[] dataString;
    public int[] dataInt;
    public byte[] dataByte;
    
    public AssemblyProgramCraftPacket()
    {
        this.packetType = 0;
        this.dataString = null;
        this.dataInt = null;
        this.dataByte = null;
    }
    public void toBytes(ByteBuf data)
    {
        data.writeInt(this.packetType);
        data.writeInt(this.dataString == null ? -1 : this.dataString.length);
        data.writeInt(this.dataInt == null ? -1 : this.dataInt.length);
        data.writeInt(this.dataByte == null ? -1 : this.dataByte.length);
        if (this.dataString != null)
        {
            for (String s : this.dataString)
            {
                ByteBufUtils.writeUTF8String(data, s);
            }
        }
        if (this.dataInt != null)
        {
            for (int i : this.dataInt)
            {
                data.writeInt(i);
            }
        }
        if (this.dataByte != null)
        {
            for (byte b : this.dataByte)
            {
                data.writeByte(b);
            }
        }
    }
    public void fromBytes(ByteBuf data)
    {
        this.packetType = data.readInt();
        int nString = data.readInt();
        int nInt = data.readInt();
        int nByte = data.readInt();
        if ((nString < -1) || (nInt < -1) || (nByte < -1))
        {
            throw new UncheckedIOException(new IOException("Illegal packet"));
        }
        if (nString != -1)
        {
            this.dataString = new String[nString];
            for (int k = 0; k < nString; k++)
            {
                this.dataString[k] = ByteBufUtils.readUTF8String(data);
            }
        }
        if (nInt != -1)
        {
            this.dataInt = new int[nInt];
            for (int k = 0; k < nInt; k++)
            {
                this.dataInt[k] = data.readInt();
            }
        }
        if (nByte != -1)
        {
            this.dataByte = new byte[nByte];
            for (int k = 0; k < nByte; k++)
            {
                this.dataByte[k] = data.readByte();
            }
        }
    }
}