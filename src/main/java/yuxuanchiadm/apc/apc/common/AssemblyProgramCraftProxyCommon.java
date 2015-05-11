package yuxuanchiadm.apc.apc.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.util.WorldHelper;
import yuxuanchiadm.apc.apc.container.ContainerBIOSWriter;
import yuxuanchiadm.apc.apc.event.WorldUnloadEventListener;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.apc.tileEntity.ITileEntityExternalDevice;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExpansionConsoleScreen;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDevice300BytesStorage;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceKeyboard;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceMonitor;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceNoteBox;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceRedstoneController;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32Computer;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerWire;
import yuxuanchiadm.apc.vcpu32.asm.CompileLogger;
import yuxuanchiadm.apc.vcpu32.extdev.ExternalDeviceKeyboard;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class AssemblyProgramCraftProxyCommon
{
	public void load()
	{
		System.out.println("加载AssemblyProgramCraft（" + AssemblyProgramCraft.getModVersion() + "）中 -by yuxuanchiadm");
	}
	public void registerRenderers()
	{
		
	}
	public boolean isClient()
	{
		return false;
	}
	public void handlePacket(final AssemblyProgramCraftPacket packet, final EntityPlayer player)
	{
        if(packet.packetType == AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoCompiled.getValue())
        {
            if (player.openContainer.windowId == packet.dataByte[0])
            {
                CompileLogger logger = new CompileLogger();
                boolean result = ((ContainerBIOSWriter)player.openContainer).compileSource(packet.dataString[0], logger);
                AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                pak.packetType = AssemblyProgramCraftPacket.ServerPacket.CompiledResult.getValue();
                pak.dataString = new String[1];
                pak.dataString[0] = logger.toString();
                pak.dataByte = new byte[1];
                pak.dataByte[0] = ((result == true) ? (byte)1 : (byte)0);
                AssemblyProgramCraft.sendToPlayer(player, pak);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoWrite.getValue())
        {
            if (player.openContainer.windowId == packet.dataByte[0])
            {
                boolean result = ((ContainerBIOSWriter)player.openContainer).writeBytecode();
                AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                pak.packetType = AssemblyProgramCraftPacket.ServerPacket.WriteResult.getValue();
                pak.dataByte = new byte[1];
                pak.dataByte[0] = ((result == true) ? (byte)1 : (byte)0);
                AssemblyProgramCraft.sendToPlayer(player, pak);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoDecompiled.getValue())
        {
            if (player.openContainer.windowId == packet.dataByte[0])
            {
                String source = ((ContainerBIOSWriter)player.openContainer).decompiledSource();
                AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                pak.packetType = AssemblyProgramCraftPacket.ServerPacket.DecompiledResult.getValue();
                pak.dataString = new String[1];
                pak.dataByte = new byte[1];
                pak.dataByte[0] = ((source == null) ? (byte)0 : (byte)1);
                pak.dataString[0] = (source == null ? "" : source);
                AssemblyProgramCraft.sendToPlayer(player, pak);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ClientPacket.PortSettingToolSetID.getValue())
        {
            int x = packet.dataInt[0];
            int y = packet.dataInt[1];
            int z = packet.dataInt[2];
            int dimension = packet.dataInt[3];
            int port = packet.dataInt[4];
            World world = WorldHelper.getWorldFromDimension(dimension);
            if(world != null)
            {
                TileEntity tileentity = world.getTileEntity(x, y, z);
                if(tileentity instanceof ITileEntityExternalDevice)
                {
                    ((ITileEntityExternalDevice)tileentity).setPort(port);
                    AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                    pak.packetType = AssemblyProgramCraftPacket.ServerPacket.SyncExternalDevicePort.getValue();
                    pak.dataInt = new int[5];
                    pak.dataInt[0] = tileentity.xCoord;
                    pak.dataInt[1] = tileentity.yCoord;
                    pak.dataInt[2] = tileentity.zCoord;
                    pak.dataInt[3] = tileentity.getWorldObj().getWorldInfo().getVanillaDimension();
                    pak.dataInt[4] = port;
                    AssemblyProgramCraft.sendToAllPlayers(pak);
                }
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ClientPacket.KeyboardStatusChange.getValue())
        {
            byte event = packet.dataByte[0];
            int x = packet.dataInt[0];
            int y = packet.dataInt[1];
            int z = packet.dataInt[2];
            int dimension = packet.dataInt[3];
            int value = packet.dataInt[4];
            World world = WorldHelper.getWorldFromDimension(dimension);
            if(world != null)
            {
                TileEntity tileentity = world.getTileEntity(x, y, z);
                if(tileentity instanceof TileEntityExternalDeviceKeyboard)
                {
                    TileEntityExternalDeviceKeyboard tileEntityKeyboard = (TileEntityExternalDeviceKeyboard)tileentity;
                    ExternalDeviceKeyboard keyboard = tileEntityKeyboard.externalDeviceKeyboard;
                    if(event == 1)
                    {
                        keyboard.onCharTyped((char)value);
                    }
                    else if(event == 2)
                    {
                        keyboard.onKeyPressed(value);
                    }
                    else if(event == 3)
                    {
                        keyboard.onKeyReleased(value);
                    }
                }
            }
        }
	}
    public World getWorldFromDimension(int dimension, boolean expectClientWorld)
    {
        MinecraftServer minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(minecraftServer == null)
        {
            return null;
        }
        else
        {
            return minecraftServer.worldServerForDimension(dimension);
        }
    }
	public void sendToServer(AssemblyProgramCraftPacket packet)
	{
		
	}
	public void sendToAllPlayers(AssemblyProgramCraftPacket packet)
	{
	    FMLEmbeddedChannel channel = AssemblyProgramCraft.instance.channels.get(Side.SERVER);
	    synchronized(channel)
	    {
    	    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
    	    channel.writeOutbound(packet);
	    }
	}
    public void sendToPlayer(EntityPlayer player, AssemblyProgramCraftPacket packet)
    {
        FMLEmbeddedChannel channel = AssemblyProgramCraft.instance.channels.get(Side.SERVER);
        synchronized(channel)
        {
            channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            channel.writeOutbound(packet);
        }
    }
	public boolean isGamePaused()
	{
	    return false;
	}
    public void registerEvent()
    {
        MinecraftForge.EVENT_BUS.register(new WorldUnloadEventListener());
    }
    public void registerTileEntitys()
    {
        GameRegistry.registerTileEntity(TileEntityVCPU32Computer.class, "tile_entity_vcpu_32_computer");
        GameRegistry.registerTileEntity(TileEntityVCPU32ComputerConnector.class, "tile_entity_vcpu_32_computer_connector");
        GameRegistry.registerTileEntity(TileEntityVCPU32ComputerWire.class, "tile_entity_vcpu_32_computer_wire");
        GameRegistry.registerTileEntity(TileEntityExternalDevice300BytesStorage.class, "tile_entity_external_device_300_byte_storage");
        GameRegistry.registerTileEntity(TileEntityExternalDeviceKeyboard.class, "tile_entity_external_device_keyboard");
        GameRegistry.registerTileEntity(TileEntityExternalDeviceMonitor.class, "tile_entity_external_device_monitor");
        GameRegistry.registerTileEntity(TileEntityExternalDeviceNumberMonitor.class, "tile_entity_external_device_number_monitor");
        GameRegistry.registerTileEntity(TileEntityExternalDeviceConsoleScreen.class, "tile_entity_external_device_console_screen");
        GameRegistry.registerTileEntity(TileEntityExpansionConsoleScreen.class, "tile_entity_expansion_console_screen");
        GameRegistry.registerTileEntity(TileEntityExternalDeviceRedstoneController.class, "tile_entity_external_device_redstone_controller");
        GameRegistry.registerTileEntity(TileEntityExternalDeviceNoteBox.class, "tile_entity_external_device_note_box");
    }
}