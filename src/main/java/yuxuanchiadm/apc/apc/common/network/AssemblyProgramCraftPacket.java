package yuxuanchiadm.apc.apc.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
    private void writeData(DataOutputStream data) throws IOException
    {
        data.writeInt(this.packetType);
        if (this.dataString != null)
        {
            data.writeInt(this.dataString.length);
        }
        else
        {
            data.writeInt(-1);
        }
        if (this.dataInt != null)
        {
            data.writeInt(this.dataInt.length);
        }
        else
        {
            data.writeInt(-1);
        }
        if (this.dataByte != null)
        {
            data.writeInt(this.dataByte.length);
        }
        else
        {
            data.writeInt(-1);
        }
        if (this.dataString != null)
        {
            for (String s : this.dataString)
            {
                data.writeUTF(s);
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
    private void readData(DataInputStream data) throws IOException
    {
        this.packetType = data.readInt();
        int nString = data.readInt();
        int nInt = data.readInt();
        int nByte = data.readInt();
        if ((nString < -1) || (nInt < -1) || (nByte < -1))
        {
            throw new IOException();
        }
        if (nString == -1)
        {
            this.dataString = null;
        }
        else
        {
            this.dataString = new String[nString];
            for (int k = 0; k < nString; k++)
            {
                this.dataString[k] = data.readUTF();
            }
        }
        if (nInt == -1)
        {
            this.dataInt = null;
        }
        else
        {
            this.dataInt = new int[nInt];
            for (int k = 0; k < nInt; k++)
            {
                this.dataInt[k] = data.readInt();
            }
        }
        if (nByte == -1)
        {
            this.dataByte = null;
        }
        else
        {
            this.dataByte = new byte[nByte];
            for (int k = 0; k < nByte; k++)
            {
                this.dataByte[k] = data.readByte();
            }
        }
    }
    public void writeData(ByteBuf data)
    {
        ByteBufOutputStream bbos = new ByteBufOutputStream(data);
        try
        {
            writeData(new DataOutputStream(bbos));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void readData(ByteBuf data)
    {
        ByteBufInputStream bbis = new ByteBufInputStream(data);
        try
        {
            readData(new DataInputStream(bbis));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}