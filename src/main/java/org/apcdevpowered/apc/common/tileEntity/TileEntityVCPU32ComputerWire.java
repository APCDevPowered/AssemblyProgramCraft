package org.apcdevpowered.apc.common.tileEntity;

import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBlocks;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityVCPU32ComputerWire extends TileEntity
{
    private static final int SIDE_BOTTOM_BIT = 1;
    private static final int SIDE_TOP_BIT = 2;
    private static final int SIDE_LEFT_BIT = 4;
    private static final int SIDE_RIGHT_BIT = 8;
    private static final int SIDE_FRONT_BIT = 16;
    private static final int SIDE_BACK_BIT = 32;
    public int placedSide;
    
    public void updataClient()
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null)
        {
            server.getConfigurationManager().sendPacketToAllPlayers(getDescriptionPacket());
        }
    }
    private boolean addSide(int sideBit)
    {
        boolean flag = (placedSide & sideBit) == 0;
        placedSide = placedSide | sideBit;
        if (flag)
        {
            updataClient();
        }
        this.markDirty();
        return flag;
    }
    private boolean removeSide(int sideBit)
    {
        boolean flag = (placedSide & sideBit) != 0;
        placedSide = placedSide & (~sideBit);
        if (flag)
        {
            updataClient();
        }
        this.markDirty();
        return flag;
    }
    private boolean hasSide(int sideBit)
    {
        return (placedSide & sideBit) != 0;
    }
    private static int getWireSideBit(EnumFacing facing)
    {
        return (int) Math.pow(2, facing.getIndex());
    }
    private static EnumFacing getWireFacing(int sideBit)
    {
        if (sideBit == SIDE_BOTTOM_BIT)
        {
            return EnumFacing.DOWN;
        }
        else if (sideBit == SIDE_TOP_BIT)
        {
            return EnumFacing.UP;
        }
        else if (sideBit == SIDE_LEFT_BIT)
        {
            return EnumFacing.NORTH;
        }
        else if (sideBit == SIDE_RIGHT_BIT)
        {
            return EnumFacing.SOUTH;
        }
        else if (sideBit == SIDE_FRONT_BIT)
        {
            return EnumFacing.WEST;
        }
        else if (sideBit == SIDE_BACK_BIT)
        {
            return EnumFacing.EAST;
        }
        else
        {
            return null;
        }
    }
    public boolean addSide(EnumFacing facing)
    {
        return addSide(getWireSideBit(facing));
    }
    public boolean removeSide(EnumFacing facing)
    {
        return removeSide(getWireSideBit(facing));
    }
    public boolean hasSide(EnumFacing facing)
    {
        return hasSide(getWireSideBit(facing));
    }
    public EnumFacing onlyHaveOneSide()
    {
        return ((placedSide == SIDE_BOTTOM_BIT || placedSide == SIDE_TOP_BIT || placedSide == SIDE_FRONT_BIT || placedSide == SIDE_BACK_BIT || placedSide == SIDE_LEFT_BIT || placedSide == SIDE_RIGHT_BIT) ? getWireFacing(placedSide) : null);
    }
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        placedSide = par1NBTTagCompound.getInteger("placedSide");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("placedSide", placedSide);
    }
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setInteger("placedSide", placedSide);
        return new S35PacketUpdateTileEntity(this.getPos(), Block.getIdFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer), var1);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        placedSide = pkt.getNbtCompound().getInteger("placedSide");
    }
}