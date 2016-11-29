package org.apcdevpowered.apc.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;

public class AssemblyProgramCraftChannelHandler extends FMLIndexedMessageToMessageCodec<AssemblyProgramCraftPacket>
{
    public AssemblyProgramCraftChannelHandler()
    {
        addDiscriminator(0, AssemblyProgramCraftPacket.class);
    }
    @Override
    public void encodeInto(ChannelHandlerContext ctx, AssemblyProgramCraftPacket msg, ByteBuf target) throws Exception
    {
        msg.toBytes(target);
    }
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, AssemblyProgramCraftPacket msg)
    {
        msg.fromBytes(source);
    }
}