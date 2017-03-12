package org.apcdevpowered.apc.common.tileEntity;

import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBlocks;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityExpansionConsoleScreen extends TileEntity
{
    @Override
    public Packet<INetHandlerPlayClient> getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        return new S35PacketUpdateTileEntity(this.getPos(), Block.getIdFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer), var1);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        
    }
}
