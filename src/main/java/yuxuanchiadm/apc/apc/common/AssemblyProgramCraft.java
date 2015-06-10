package yuxuanchiadm.apc.apc.common;

import java.util.EnumMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.minecraft.entity.player.EntityPlayer;
import yuxuanchiadm.apc.apc.common.config.ConfigSystem;
import yuxuanchiadm.apc.apc.common.network.AssemblyProgramCraftChannelHandler;
import yuxuanchiadm.apc.apc.common.network.AssemblyProgramCraftGuiHandler;
import yuxuanchiadm.apc.apc.common.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.apc.common.network.PacketHandler;
import yuxuanchiadm.apc.apc.common.proxy.AssemblyProgramCraftProxyCommon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod( modid = "AssemblyProgramCraft", name="AssemblyProgramCraft", version=AssemblyProgramCraft.VERSION)
public class AssemblyProgramCraft
{
    @Mod.Instance("AssemblyProgramCraft")
    public static AssemblyProgramCraft instance;
    @SidedProxy(clientSide="yuxuanchiadm.apc.apc.client.proxy.AssemblyProgramCraftProxyClient", serverSide="yuxuanchiadm.apc.apc.server.proxy.AssemblyProgramCraftProxyServer")
    public static AssemblyProgramCraftProxyCommon proxy;
    
    public static final String MODID = "AssemblyProgramCraft";
    public static final String NAME = "AssemblyProgramCraft";
    public static final String VERSION = "@VERSION@";
    
    public EnumMap<Side, FMLEmbeddedChannel> channels;
    
    public ConfigSystem cfgSystem = new ConfigSystem();
    
    public ExecutorService executor = Executors.newCachedThreadPool();
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        channels = NetworkRegistry.INSTANCE.newChannel("APCraft", new AssemblyProgramCraftChannelHandler(), new PacketHandler());
        
        proxy.registerBlocksAndItems();
        proxy.registerTileEntities();
        proxy.registerEvent();
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
        return VERSION ;
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