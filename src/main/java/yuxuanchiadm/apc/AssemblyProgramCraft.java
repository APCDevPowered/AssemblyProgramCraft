package yuxuanchiadm.apc;

import java.util.EnumMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.minecraft.entity.player.EntityPlayer;
import yuxuanchiadm.apc.apc.block.BlockExpansionConsoleScreen;
import yuxuanchiadm.apc.apc.block.BlockExternalDevice300BytesStorage;
import yuxuanchiadm.apc.apc.block.BlockExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.apc.block.BlockExternalDeviceKeyboard;
import yuxuanchiadm.apc.apc.block.BlockExternalDeviceMonitor;
import yuxuanchiadm.apc.apc.block.BlockExternalDeviceNoteBox;
import yuxuanchiadm.apc.apc.block.BlockExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.block.BlockExternalDeviceRedstoneController;
import yuxuanchiadm.apc.apc.block.BlockVCPU32Computer;
import yuxuanchiadm.apc.apc.block.BlockVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.block.BlockVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraftProxyCommon;
import yuxuanchiadm.apc.apc.common.ConfigSystem;
import yuxuanchiadm.apc.apc.common.CreativeTabAssemblyProgramCraft;
import yuxuanchiadm.apc.apc.item.ItemBIOSWriter;
import yuxuanchiadm.apc.apc.item.ItemBlockVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.item.ItemPortSettingTool;
import yuxuanchiadm.apc.apc.item.ItemVCPU32ComputerCMOSChip;
import yuxuanchiadm.apc.apc.item.ItemVCPU32Debugger;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftChannelHandler;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftGuiHandler;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.apc.network.PacketHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod( modid = "AssemblyProgramCraft", name="AssemblyProgramCraft", version=AssemblyProgramCraft.version)
public class AssemblyProgramCraft
{
	@Mod.Instance("AssemblyProgramCraft")
	public static AssemblyProgramCraft instance;
	@SidedProxy(clientSide="yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient", serverSide="yuxuanchiadm.apc.apc.common.AssemblyProgramCraftProxyCommon")
	public static AssemblyProgramCraftProxyCommon proxy;
	
	public static final String version = "@VERSION@";
	
	public EnumMap<Side, FMLEmbeddedChannel> channels;
	
	public ConfigSystem cfgSystem = new ConfigSystem();
	public CreativeTabAssemblyProgramCraft creativeTabAPC = new CreativeTabAssemblyProgramCraft();
	
	public BlockVCPU32Computer block_computer;
	public BlockVCPU32ComputerConnector block_connector;
	public BlockVCPU32ComputerWire block_wire;
	public BlockExternalDevice300BytesStorage block_300bytesstorage;
	public BlockExternalDeviceKeyboard block_keyboard;
	public BlockExternalDeviceMonitor block_monitor;
	public BlockExternalDeviceNumberMonitor block_numberMonitor;
	public BlockExternalDeviceConsoleScreen block_consoleScreen;
	public BlockExpansionConsoleScreen block_expansionConsoleScreen;
	public BlockExternalDeviceRedstoneController block_redstoneController;
	public BlockExternalDeviceNoteBox block_note_box;
	
	public ItemVCPU32ComputerCMOSChip item_vcpu_32_computer_coms_chip;
	public ItemVCPU32Debugger item_vcpu_32_debugger;
	public ItemBIOSWriter item_bios_writer;
	public ItemPortSettingTool item_port_setting_tool;
	
	public ExecutorService executor = Executors.newCachedThreadPool();
	
	@Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
	    channels = NetworkRegistry.INSTANCE.newChannel("APCraft", new AssemblyProgramCraftChannelHandler(), new PacketHandler());
	    
		proxy.load();
		proxy.registerRenderers();
		proxy.registerTileEntitys();
		proxy.registerEvent();
		
		item_vcpu_32_computer_coms_chip = new ItemVCPU32ComputerCMOSChip();
		GameRegistry.registerItem(item_vcpu_32_computer_coms_chip, "item_vcpu_32_computer_cmos_chip");
        item_vcpu_32_debugger = new ItemVCPU32Debugger();
        GameRegistry.registerItem(item_vcpu_32_debugger, "item_vcpu_32_debugger");
		item_bios_writer = new ItemBIOSWriter();
        GameRegistry.registerItem(item_bios_writer, "item_bios_writer");
        item_port_setting_tool = new ItemPortSettingTool();
        GameRegistry.registerItem(item_port_setting_tool, "item_port_setting_tool");
		block_computer = new BlockVCPU32Computer();
		GameRegistry.registerBlock(block_computer, "block_vcpu_32_computer");
		block_connector = new BlockVCPU32ComputerConnector();
		GameRegistry.registerBlock(block_connector, "block_vcpu_32_computer_connector");
		block_wire = new BlockVCPU32ComputerWire();
		GameRegistry.registerBlock(block_wire, ItemBlockVCPU32ComputerWire.class, "block_vcpu_32_computer_wire");
		block_300bytesstorage = new BlockExternalDevice300BytesStorage();
		GameRegistry.registerBlock(block_300bytesstorage, "block_external_device_300_byte_storage");
		block_keyboard = new BlockExternalDeviceKeyboard();
		GameRegistry.registerBlock(block_keyboard, "block_external_device_keyboard");
		block_monitor = new BlockExternalDeviceMonitor();
		GameRegistry.registerBlock(block_monitor, "block_external_device_monitor");
        block_numberMonitor = new BlockExternalDeviceNumberMonitor();
        GameRegistry.registerBlock(block_numberMonitor, "block_external_device_number_monitor");
        block_consoleScreen = new BlockExternalDeviceConsoleScreen();
        GameRegistry.registerBlock(block_consoleScreen, "block_external_device_console_screen");
        block_expansionConsoleScreen = new BlockExpansionConsoleScreen();
        GameRegistry.registerBlock(block_expansionConsoleScreen, "block_expansion_console_screen");
        block_redstoneController = new BlockExternalDeviceRedstoneController();
        GameRegistry.registerBlock(block_redstoneController,"block_external_device_redstone_controller");
        block_note_box = new BlockExternalDeviceNoteBox();
        GameRegistry.registerBlock(block_note_box,"block_external_device_note_box");
    }
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	cfgSystem.setup(event.getSuggestedConfigurationFile());
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new AssemblyProgramCraftGuiHandler());
    }
	@Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        
    }
	public static String getModVersion()
    {
        return version ;
    }
	public static void sendToPlayer(EntityPlayer player, AssemblyProgramCraftPacket packet)
	{
		proxy.sendToPlayer(player, packet);
    }
	public static void sendToAllPlayers(AssemblyProgramCraftPacket packet)
	{
		proxy.sendToAllPlayers(packet);
	}
	public static void sendToServer(AssemblyProgramCraftPacket packet)
	{
		proxy.sendToServer(packet);
	}
	public static void handlePacket(AssemblyProgramCraftPacket packet, EntityPlayer player)
	{
		proxy.handlePacket(packet, player);
	}
	public static void executeAsync(Runnable runnable)
	{
	    instance.executor.execute(runnable);
	}
}