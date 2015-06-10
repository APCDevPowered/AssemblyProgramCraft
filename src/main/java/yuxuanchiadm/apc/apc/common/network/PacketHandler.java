package yuxuanchiadm.apc.apc.common.network;

import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
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
        if(netHandler instanceof NetHandlerPlayServer)
        {
            AssemblyProgramCraft.handlePacket(packet, ((NetHandlerPlayServer)netHandler).playerEntity);
        }
        else
        {
            AssemblyProgramCraft.handlePacket(packet, null);
        }
    }
}