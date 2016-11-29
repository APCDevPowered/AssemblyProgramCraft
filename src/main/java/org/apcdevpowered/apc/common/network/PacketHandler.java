package org.apcdevpowered.apc.common.network;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class PacketHandler extends SimpleChannelInboundHandler<AssemblyProgramCraftPacket>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AssemblyProgramCraftPacket packet) throws Exception
    {
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        if (netHandler instanceof NetHandlerPlayServer)
        {
            MinecraftServer.getServer().addScheduledTask(() -> AssemblyProgramCraft.handlePacket(packet, ((NetHandlerPlayServer) netHandler).playerEntity));
        }
        else
        {
            Minecraft.getMinecraft().addScheduledTask(() -> AssemblyProgramCraft.handlePacket(packet, null));
        }
    }
}