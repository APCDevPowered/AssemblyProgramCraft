package org.apcdevpowered.apc.server.proxy;

import org.apcdevpowered.apc.common.network.AssemblyProgramCraftPacket;
import org.apcdevpowered.apc.common.proxy.AssemblyProgramCraftProxyCommon;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class AssemblyProgramCraftProxyServer extends AssemblyProgramCraftProxyCommon
{
    @Override
    public boolean isClient()
    {
        return false;
    }
    @Override
    public boolean isGamePaused()
    {
        return false;
    }
    @Override
    public void handlePacket(final AssemblyProgramCraftPacket packet, final EntityPlayerMP player)
    {
        super.handlePacket(packet, player);
    }
    @Override
    public World getWorldFromDimension(int dimension, Side side)
    {
        return super.getWorldFromDimension(dimension, side);
    }
    @Override
    public void registerBlocksAndItems()
    {
        super.registerBlocksAndItems();
    }
    @Override
    public void registerTileEntities()
    {
        super.registerTileEntities();
    }
    @Override
    public void registerEvent()
    {
        super.registerEvent();
    }
}