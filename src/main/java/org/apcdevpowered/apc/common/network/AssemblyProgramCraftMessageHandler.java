package org.apcdevpowered.apc.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class AssemblyProgramCraftMessageHandler implements IMessageHandler<AssemblyProgramCraftPacket, AssemblyProgramCraftPacket>
{
    @Override
    public AssemblyProgramCraftPacket onMessage(AssemblyProgramCraftPacket message, MessageContext ctx)
    {
        switch (ctx.side)
        {
            case SERVER:
                MinecraftServer.getServer().addScheduledTask(() -> AssemblyProgramCraft.handlePacket(message, ctx.getServerHandler().playerEntity));
                break;
            case CLIENT:
                Minecraft.getMinecraft().addScheduledTask(() -> AssemblyProgramCraft.handlePacket(message, null));
                break;
            default:
                break;
        }
        return null;
    }
}
