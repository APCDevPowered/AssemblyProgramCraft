package yuxuanchiadm.apc.apc.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.FMLOutboundHandler.OutboundTarget;
import net.minecraftforge.fml.relauncher.Side;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockExpansionConsoleScreen;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockExternalDeviceKeyboard;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockExternalDeviceMonitor;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockVCPU32Computer;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.client.renders.RenderBlockVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityExpansionConsoleScreen;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityExternalDeviceKeyboard;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityExternalDeviceMonitor;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityVCPU32Computer;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.client.renders.RenderTileEntityVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraftProxyCommon;
import yuxuanchiadm.apc.apc.common.util.WorldHelper;
import yuxuanchiadm.apc.apc.event.TickEventListener;
import yuxuanchiadm.apc.apc.gui.screen.GuiBIOSWriter;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.apc.tileEntity.ITileEntityExternalDevice;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExpansionConsoleScreen;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceKeyboard;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceMonitor;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32Computer;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerWire;

public class AssemblyProgramCraftProxyClient extends AssemblyProgramCraftProxyCommon
{	
	public static final ResourceLocation COMPUTER_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/computer.png");
	public static final ResourceLocation CONNECTOR_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/connector.png");
	public static final ResourceLocation WIRE_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/wire.png");
	public static final ResourceLocation KEYBOARD_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/keyboard.png");
	public static final ResourceLocation MONITOR_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/monitor.png");
	public static final ResourceLocation NUMBER_MONITOR_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/numberMonitor.png");
	public static final ResourceLocation CONSOLE_SCREEN_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/consoleScreen.png");
    public static final ResourceLocation EXPANSION_CONSOLE_SCREEN_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/expansionConsoleScreen.png");
    
	public static final ResourceLocation COLORS_PNG_PATH = new ResourceLocation("AssemblyProgramCraft:textures/tileentity/colors.png");
	
	public static final int RENDER_TYPE_BLOCK_VCPU_32_COMPUTER = 1024;
	public static final int RENDER_TYPE_BLOCK_VCPU_32_COMPUTER_CONNECTOR = 1025;
    public static final int RENDER_TYPE_BLOCK_VCPU_32_COMPUTER_WIRE = 1026;
    public static final int RENDER_TYPE_BLOCK_EXTERNAL_DEVICE_KEYBOARD = 1027;
    public static final int RENDER_TYPE_BLOCK_EXTERNAL_DEVICE_MONITOR = 1028;
    public static final int RENDER_TYPE_BLOCK_EXTERNAL_DEVICE_NUMBER_MONITOR = 1029;
    public static final int RENDER_TYPE_BLOCK_EXTERNAL_DEVICE_CONSOLE_SCREEN = 1030;
    public static final int RENDER_TYPE_BLOCK_EXPANSION_CONSOLE_SCREEN = 1031;
    
	public void registerRenderers()
	{	
		RenderingRegistry.registerBlockHandler(new RenderBlockVCPU32Computer());
		RenderingRegistry.registerBlockHandler(new RenderBlockVCPU32ComputerConnector());
		RenderingRegistry.registerBlockHandler(new RenderBlockVCPU32ComputerWire());
		RenderingRegistry.registerBlockHandler(new RenderBlockExternalDeviceKeyboard());
		RenderingRegistry.registerBlockHandler(new RenderBlockExternalDeviceMonitor());
	    RenderingRegistry.registerBlockHandler(new RenderBlockExternalDeviceNumberMonitor());
	    RenderingRegistry.registerBlockHandler(new RenderBlockExternalDeviceConsoleScreen());
	    RenderingRegistry.registerBlockHandler(new RenderBlockExpansionConsoleScreen());
	}
	public void registerEvent()
    {
	    super.registerEvent();
	    FMLCommonHandler.instance().bus().register(new TickEventListener());
    }
	public boolean isClient()
	{
		return true;
	}
	public void handlePacket(AssemblyProgramCraftPacket packet, EntityPlayer player)
	{
		super.handlePacket(packet, player);
		if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.ComputerPowerChange.getValue())
        {
            if(FMLClientHandler.instance().getClient().theWorld.getWorldInfo().getVanillaDimension() == packet.dataInt[3])
            {
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)FMLClientHandler.instance().getClient().theWorld.getTileEntity(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
                if(tileentity == null)
                {
                    return;
                }
                tileentity.turnPower((packet.dataByte[0] == ((byte)1)) ? true : false);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.CompiledResult.getValue())
        {
            GuiBIOSWriter guiBIOSWriter = (GuiBIOSWriter)FMLClientHandler.instance().getClient().currentScreen;
            if(guiBIOSWriter != null)
            {
                String info = ((packet.dataByte[0] == 1) ? "编译成功，可以写入" : "编译失败，请检查语法");
                info = info + "\n" + packet.dataString[0];
                guiBIOSWriter.displayInfoWindow(info);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.WriteResult.getValue())
        {
            GuiBIOSWriter guiBIOSWriter = (GuiBIOSWriter)FMLClientHandler.instance().getClient().currentScreen;
            if(guiBIOSWriter != null)
            {
                String info = ((packet.dataByte[0] == 1) ? "写入数据到芯片成功" : "写入失败");
                guiBIOSWriter.displayInfoWindow(info);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.DecompiledResult.getValue())
        {
            GuiBIOSWriter guiBIOSWriter = (GuiBIOSWriter)FMLClientHandler.instance().getClient().currentScreen;
            if(guiBIOSWriter != null)
            {
                String info = ((packet.dataByte[0] == 1) ? "反编译芯片数据成功" : "反编译失败");
                if(packet.dataByte[0] == 1)
                {
                    guiBIOSWriter.setProgram(packet.dataString[0]);
                }
                guiBIOSWriter.displayInfoWindow(info);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.ComputerRuntimeError.getValue())
        {
            if(FMLClientHandler.instance().getClient().theWorld.getWorldInfo().getVanillaDimension() == packet.dataInt[3])
            {
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)FMLClientHandler.instance().getClient().theWorld.getTileEntity(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
                if(tileentity == null)
                {
                    return;
                }
                tileentity.setRuntimeError((packet.dataByte[0] == ((byte)1)) ? true : false);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncMonitorData.getValue())
        {
            if(FMLClientHandler.instance().getClient().theWorld.getWorldInfo().getVanillaDimension() == packet.dataInt[3])
            {
                TileEntityExternalDeviceMonitor monitor = (TileEntityExternalDeviceMonitor)FMLClientHandler.instance().getClient().theWorld.getTileEntity(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
                if(monitor == null)
                {
                    return;
                }
                monitor.setMemoryValue(packet.dataInt[4], packet.dataInt[5]);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncNumberMonitorData.getValue())
        {
            if(FMLClientHandler.instance().getClient().theWorld.getWorldInfo().getVanillaDimension() == packet.dataInt[3])
            {
                TileEntityExternalDeviceNumberMonitor monitor = (TileEntityExternalDeviceNumberMonitor)FMLClientHandler.instance().getClient().theWorld.getTileEntity(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
                if(monitor == null)
                {
                    return;
                }
                monitor.number = packet.dataInt[4];
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncConsoleData.getValue())
        {
            if(FMLClientHandler.instance().getClient().theWorld.getWorldInfo().getVanillaDimension() == packet.dataInt[3])
            {
                TileEntityExternalDeviceConsoleScreen screen = (TileEntityExternalDeviceConsoleScreen)FMLClientHandler.instance().getClient().theWorld.getTileEntity(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
                if(screen == null)
                {
                    return;
                }
                screen.setMemoryValue(packet.dataInt[4], packet.dataInt[5]);
            }
        }
        else if(packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncExternalDevicePort.getValue())
        {
            int x = packet.dataInt[0];
            int y = packet.dataInt[1];
            int z = packet.dataInt[2];
            int dimension = packet.dataInt[3];
            int port = packet.dataInt[4];
            World world = WorldHelper.getWorldFromDimension(dimension, true);
            if(world != null)
            {
                TileEntity tileentity = world.getTileEntity(x, y, z);
                if(tileentity instanceof ITileEntityExternalDevice)
                {
                    System.out.println(port);
                    ((ITileEntityExternalDevice)tileentity).setPort(port);
                }
            }
        }
	}
	public World getPlayerWorld(EntityPlayer player)
	{
		return player.worldObj;
	}
    public World getWorldFromDimension(int dimension, boolean expectClientWorld)
    {
        Minecraft minecraft = FMLClientHandler.instance().getClient();
        MinecraftServer server = minecraft.getIntegratedServer();
        if(server == null || expectClientWorld)
        {
            World world = minecraft.theWorld;
            if(world.getWorldInfo().getVanillaDimension() == dimension)
            {
                return world;
            }
        }
        else
        {
            return server.worldServerForDimension(dimension);
        }
        return null;
    }
	public void sendToServer(AssemblyProgramCraftPacket packet)
	{
	    FMLEmbeddedChannel channel = AssemblyProgramCraft.instance.channels.get(Side.CLIENT);
        synchronized(channel)
        {
    	    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.TOSERVER);
    	    channel.writeOutbound(packet);
        }
	}
    public boolean isGamePaused()
    {
        return FMLClientHandler.instance().getClient().isGamePaused();
    }
	public void registerTileEntitys()
	{
	    super.registerTileEntitys();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVCPU32Computer.class, new RenderTileEntityVCPU32Computer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVCPU32ComputerConnector.class, new RenderTileEntityVCPU32ComputerConnector());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVCPU32ComputerWire.class, new RenderTileEntityVCPU32ComputerWire());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceKeyboard.class, new RenderTileEntityExternalDeviceKeyboard());
	    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceMonitor.class, new RenderTileEntityExternalDeviceMonitor());
	    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceNumberMonitor.class, new RenderTileEntityExternalDeviceNumberMonitor());
	    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceConsoleScreen.class, new RenderTileEntityExternalDeviceConsoleScreen());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExpansionConsoleScreen.class, new RenderTileEntityExpansionConsoleScreen());
	}
}