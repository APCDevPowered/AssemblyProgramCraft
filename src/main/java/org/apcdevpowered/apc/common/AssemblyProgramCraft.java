package org.apcdevpowered.apc.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apcdevpowered.apc.common.config.ConfigSystem;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftGuiHandler;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftMessageHandler;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftPacket;
import org.apcdevpowered.apc.common.proxy.AssemblyProgramCraftProxyCommon;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod( modid = AssemblyProgramCraft.MODID, name=AssemblyProgramCraft.NAME, version=AssemblyProgramCraft.VERSION)
public class AssemblyProgramCraft
{
    @Mod.Instance("AssemblyProgramCraft")
    public static AssemblyProgramCraft instance;
    @SidedProxy(clientSide="org.apcdevpowered.apc.client.proxy.AssemblyProgramCraftProxyClient", serverSide="org.apcdevpowered.apc.server.proxy.AssemblyProgramCraftProxyServer")
    public static AssemblyProgramCraftProxyCommon proxy;
    
    public static final String MODID = "AssemblyProgramCraft";
    public static final String NAME = "AssemblyProgramCraft";
    public static final String VERSION = "@VERSION@";
    
    public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("APCraft");
    
    public ConfigSystem cfgSystem = new ConfigSystem();
    
    public ExecutorService executor = Executors.newCachedThreadPool();
    
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        NETWORK_INSTANCE.registerMessage(AssemblyProgramCraftMessageHandler.class, AssemblyProgramCraftPacket.class, 0, Side.SERVER);
        NETWORK_INSTANCE.registerMessage(AssemblyProgramCraftMessageHandler.class, AssemblyProgramCraftPacket.class, 0, Side.CLIENT);
        
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
    public static void sendTo(EntityPlayerMP player, AssemblyProgramCraftPacket message)
    {
        NETWORK_INSTANCE.sendTo(message, player);
    }
    public static void sendToAll(AssemblyProgramCraftPacket message)
    {
        NETWORK_INSTANCE.sendToAll(message);
    }
    public static void sendToServer(AssemblyProgramCraftPacket message)
    {
        NETWORK_INSTANCE.sendToServer(message);
    }
    public static void handlePacket(AssemblyProgramCraftPacket packet, EntityPlayerMP player)
    {
        proxy.handlePacket(packet, player);
    }
    public static void executeAsync(Runnable runnable)
    {
        instance.executor.execute(runnable);
    }
}