package yuxuanchiadm.apc.apc.common.network;

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
    public void encodeInto(ChannelHandlerContext ctx, AssemblyProgramCraftPacket packet, ByteBuf data) throws Exception
    {
        packet.writeData(data);
    }
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, AssemblyProgramCraftPacket packet)
    {
        packet.readData(data);
    }
}