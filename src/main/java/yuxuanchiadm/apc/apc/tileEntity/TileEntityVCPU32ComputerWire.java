package yuxuanchiadm.apc.apc.tileEntity;

import net.minecraftforge.fml.common.FMLCommonHandler;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityVCPU32ComputerWire extends TileEntity
{
    public static final int SIDE_TOP = 1;
    public static final int SIDE_BOTTOM = 2;
    public static final int SIDE_LEFT = 4;
    public static final int SIDE_RIGHT = 8;
    public static final int SIDE_FRONT = 16;
    public static final int SIDE_BACK = 32;
    public int placedSide;
    public void updataClient()
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null)
        {
            server.getConfigurationManager().sendPacketToAllPlayers(getDescriptionPacket());
        }
    }
    public boolean addSide(int side)
    {
        boolean flag = (placedSide & side) == 0;
        placedSide = placedSide | side;
        if(flag)
        {
            updataClient();
        }
        this.markDirty();
        return flag;
    }
    public boolean removeSide(int side)
    {
        boolean flag = (placedSide & side) != 0;
        placedSide = placedSide & (~side);
        if(flag)
        {
            updataClient();
        }
        this.markDirty();
        return flag;
    }
    public boolean haveSide(int side)
    {
        return (placedSide & side) != 0;
    }
    public int onlyHaveOneSide()
    {
        return ((placedSide == SIDE_TOP || placedSide == SIDE_BOTTOM || placedSide == SIDE_FRONT || placedSide == SIDE_BACK || placedSide == SIDE_LEFT || placedSide == SIDE_RIGHT) ? placedSide : 0);
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        placedSide = par1NBTTagCompound.getInteger("placedSide");
    }
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("placedSide", placedSide);
    }
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        
        var1.setInteger("placedSide", placedSide);
        
        return new S35PacketUpdateTileEntity(this.getPos(), Block.getIdFromBlock(AssemblyProgramCraft.instance.block_computer), var1);
    }
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        placedSide = pkt.getNbtCompound().getInteger("placedSide");
    }
}