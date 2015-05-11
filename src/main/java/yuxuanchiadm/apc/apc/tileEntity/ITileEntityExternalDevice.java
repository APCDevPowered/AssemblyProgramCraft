package yuxuanchiadm.apc.apc.tileEntity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import yuxuanchiadm.apc.apc.item.ItemBlockVCPU32ComputerWire;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public abstract class ITileEntityExternalDevice extends TileEntity
{
    private int port;
    private boolean isInIt = false;
    public ComputerPos lockedComputerPos = null;
    public static class ComputerPos
    {
        public int x;
        public int y;
        public int z;
        public int dimension;
        public boolean is(TileEntityVCPU32Computer computer)
        {
            if(computer.xCoord == x && computer.yCoord == y && computer.zCoord == z && computer.getWorldObj().getWorldInfo().getVanillaDimension() == dimension)
            {
                return true;
            }
            return false;
        }
        public static ComputerPos form(TileEntityVCPU32Computer computer)
        {
            ComputerPos computerPos = new ComputerPos();
            computerPos.x = computer.xCoord;
            computerPos.y = computer.yCoord;
            computerPos.z = computer.zCoord;
            computerPos.dimension = computer.getWorldObj().getWorldInfo().getVanillaDimension();
            return computerPos;
        }
        public static ComputerPos readFromNBT(NBTTagCompound par1NBTTagCompound)
        {
            ComputerPos computerPos = new ComputerPos();
            computerPos.x = par1NBTTagCompound.getInteger("x");
            computerPos.y = par1NBTTagCompound.getInteger("y");
            computerPos.z = par1NBTTagCompound.getInteger("z");
            computerPos.dimension = par1NBTTagCompound.getInteger("dimension");
            return computerPos;
        }
        public void writeToNBT(NBTTagCompound par1NBTTagCompound)
        {
            par1NBTTagCompound.setInteger("x", x);
            par1NBTTagCompound.setInteger("y", y);
            par1NBTTagCompound.setInteger("z", z);
            par1NBTTagCompound.setInteger("dimension", dimension);
        }
    }
    public boolean isConnectToConnector(TileEntityVCPU32ComputerConnector tileEntity)
    {
        return getAllConnectedConnector().contains(tileEntity);
    }
    public abstract ArrayList<TileEntityVCPU32ComputerConnector> getAllConnectedConnector();
    public abstract AbstractExternalDevice getExternalDevice();
    public void invalidate()
    {
        super.invalidate();
        updataConnector();
    }
    public void updateEntity()
    {
        super.updateEntity();
        if(isInIt == false)
        {
            init();
            isInIt = true;
        }
        this.markDirty();
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        port = par1NBTTagCompound.getInteger("port");
        if(par1NBTTagCompound.hasKey("lockedComputerPos"))
        {
            NBTTagCompound nbtTagCompound = par1NBTTagCompound.getCompoundTag("lockedComputerPos");
            lockedComputerPos = ComputerPos.readFromNBT(nbtTagCompound);
        }
        
        if(par1NBTTagCompound.hasKey("deviceData"))
        {
            NBTTagCompound nbtTagCompound = par1NBTTagCompound.getCompoundTag("deviceData");
            this.getExternalDevice().readFromNBT(nbtTagCompound);
        }
    }
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("port", port);
        if(lockedComputerPos != null)
        {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            lockedComputerPos.writeToNBT(nbtTagCompound);
            par1NBTTagCompound.setTag("lockedComputerPos", nbtTagCompound);
        }
        
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.getExternalDevice().writeToNBT(nbtTagCompound);
        par1NBTTagCompound.setTag("deviceData", nbtTagCompound);
    }
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        
        var1.setInteger("port", port);
        writeDescriptionNbt(var1);
        
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, Block.getIdFromBlock(this.getBlockType()), var1);
    }
    public void writeDescriptionNbt(NBTTagCompound var1)
    {
        
    }
    public void readDescriptionNbt(NBTTagCompound var1)
    {
        
    }
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        port = pkt.func_148857_g().getInteger("port");
        readDescriptionNbt(pkt.func_148857_g());
    }
    public void init()
    {
        updataConnector();
    }
    public void updataConnector()
    {
        if(worldObj.isRemote)
        {
            return;
        }
        for(TileEntityVCPU32ComputerConnector connector : getAllConnectedConnector())
        {
            ItemBlockVCPU32ComputerWire.updataConnector(connector);
        }
    }
    public int getPort()
    {
        return port;
    }
    public void setPort(int port)
    {
        if(port != this.port)
        {
            this.lockedComputerPos = null;
            this.port = port;
            updataConnector();
        }
    }
}