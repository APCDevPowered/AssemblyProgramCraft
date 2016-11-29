package org.apcdevpowered.apc.client.proxy;

import org.apcdevpowered.apc.client.gui.screen.GuiBIOSWriter;
import org.apcdevpowered.apc.client.listener.TickEventListener;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityExpansionConsoleScreen;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityExternalDeviceConsoleScreen;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityExternalDeviceKeyboard;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityExternalDeviceMonitor;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityExternalDeviceNumberMonitor;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityVCPU32Computer;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityVCPU32ComputerConnector;
import org.apcdevpowered.apc.client.renderer.tileentity.RenderTileEntityVCPU32ComputerWire;
import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBlocks;
import org.apcdevpowered.apc.common.init.AssemblyProgramCraftItems;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftPacket;
import org.apcdevpowered.apc.common.proxy.AssemblyProgramCraftProxyCommon;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExpansionConsoleScreen;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDevice;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceConsoleScreen;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceKeyboard;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceMonitor;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNumberMonitor;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32Computer;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerWire;
import org.apcdevpowered.apc.common.util.WorldHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AssemblyProgramCraftProxyClient extends AssemblyProgramCraftProxyCommon
{
    @Override
    public boolean isClient()
    {
        return true;
    }
    @Override
    public boolean isGamePaused()
    {
        return FMLClientHandler.instance().getClient().isGamePaused();
    }
    @Override
    public void handlePacket(AssemblyProgramCraftPacket packet, EntityPlayerMP player)
    {
        super.handlePacket(packet, player);
        if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.ComputerPowerChange.getValue())
        {
            if (FMLClientHandler.instance().getClient().theWorld.provider.getDimensionId() == packet.dataInt[3])
            {
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]));
                if (tileentity == null)
                {
                    return;
                }
                tileentity.turnPower((packet.dataByte[0] == ((byte) 1)) ? true : false);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.CompiledResult.getValue())
        {
            GuiBIOSWriter guiBIOSWriter = (GuiBIOSWriter) FMLClientHandler.instance().getClient().currentScreen;
            if (guiBIOSWriter != null)
            {
                String info = ((packet.dataByte[0] == 1) ? "编译成功，可以写入" : "编译失败，请检查语法");
                info = info + "\n" + packet.dataString[0];
                guiBIOSWriter.displayInfoWindow(info);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.WriteResult.getValue())
        {
            GuiBIOSWriter guiBIOSWriter = (GuiBIOSWriter) FMLClientHandler.instance().getClient().currentScreen;
            if (guiBIOSWriter != null)
            {
                String info = ((packet.dataByte[0] == 1) ? "写入数据到芯片成功" : "写入失败");
                guiBIOSWriter.displayInfoWindow(info);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.DecompiledResult.getValue())
        {
            GuiBIOSWriter guiBIOSWriter = (GuiBIOSWriter) FMLClientHandler.instance().getClient().currentScreen;
            if (guiBIOSWriter != null)
            {
                String info = ((packet.dataByte[0] == 1) ? "反编译芯片数据成功" : "反编译失败");
                if (packet.dataByte[0] == 1)
                {
                    guiBIOSWriter.setProgram(packet.dataString[0]);
                }
                guiBIOSWriter.displayInfoWindow(info);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.ComputerRuntimeError.getValue())
        {
            if (FMLClientHandler.instance().getClient().theWorld.provider.getDimensionId() == packet.dataInt[3])
            {
                TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]));
                if (tileentity == null)
                {
                    return;
                }
                tileentity.setRuntimeError((packet.dataByte[0] == ((byte) 1)) ? true : false);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncMonitorData.getValue())
        {
            if (FMLClientHandler.instance().getClient().theWorld.provider.getDimensionId() == packet.dataInt[3])
            {
                TileEntityExternalDeviceMonitor monitor = (TileEntityExternalDeviceMonitor) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]));
                if (monitor == null)
                {
                    return;
                }
                monitor.setMemoryValue(packet.dataInt[4], packet.dataInt[5]);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncNumberMonitorData.getValue())
        {
            if (FMLClientHandler.instance().getClient().theWorld.provider.getDimensionId() == packet.dataInt[3])
            {
                TileEntityExternalDeviceNumberMonitor monitor = (TileEntityExternalDeviceNumberMonitor) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]));
                if (monitor == null)
                {
                    return;
                }
                monitor.number = packet.dataInt[4];
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncConsoleData.getValue())
        {
            if (FMLClientHandler.instance().getClient().theWorld.provider.getDimensionId() == packet.dataInt[3])
            {
                TileEntityExternalDeviceConsoleScreen screen = (TileEntityExternalDeviceConsoleScreen) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]));
                if (screen == null)
                {
                    return;
                }
                screen.setMemoryValue(packet.dataInt[4], packet.dataInt[5]);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ServerPacket.SyncExternalDevicePort.getValue())
        {
            int x = packet.dataInt[0];
            int y = packet.dataInt[1];
            int z = packet.dataInt[2];
            int dimension = packet.dataInt[3];
            int port = packet.dataInt[4];
            World world = WorldHelper.getWorldFromDimension(dimension);
            if (world != null)
            {
                TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
                if (tileentity instanceof TileEntityExternalDevice)
                {
                    System.out.println(port);
                    ((TileEntityExternalDevice) tileentity).setPort(port);
                }
            }
        }
    }
    @Override
    public World getWorldFromDimension(int dimension, Side side)
    {
        if (side == Side.CLIENT)
        {
            Minecraft minecraft = FMLClientHandler.instance().getClient();
            World world = minecraft.theWorld;
            if (world.provider.getDimensionId() != dimension)
            {
                return null;
            }
            else
            {
                return world;
            }
        }
        else
        {
            return super.getWorldFromDimension(dimension, side);
        }
    }
    @Override
    public void registerBlocksAndItems()
    {
        super.registerBlocksAndItems();
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_external_device_300_bytes_storage), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_external_device_300_bytes_storage", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_external_device_note_box), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_external_device_note_box", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_external_device_redstone_controller), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_external_device_redstone_controller", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_vcpu_32_computer", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer_connector), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_vcpu_32_computer_connector", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer_wire), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_vcpu_32_computer_wire", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_external_device_keyboard), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_external_device_keyboard", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_external_device_monitor), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_external_device_monitor", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_external_device_number_monitor), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_external_device_number_monitor", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_external_device_console_screen), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_external_device_console_screen", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(AssemblyProgramCraftBlocks.block_expansion_console_screen), 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "block_expansion_console_screen", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(AssemblyProgramCraftItems.item_vcpu_32_computer_cmos_chip, 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "item_vcpu_32_computer_cmos_chip", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(AssemblyProgramCraftItems.item_vcpu_32_debugger, 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "item_vcpu_32_debugger", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(AssemblyProgramCraftItems.item_bios_writer, 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "item_bios_writer", "inventory"));
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(AssemblyProgramCraftItems.item_port_setting_tool, 0, new ModelResourceLocation(AssemblyProgramCraft.MODID + ":" + "item_port_setting_tool", "inventory"));
    }
    @Override
    public void registerTileEntities()
    {
        super.registerTileEntities();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVCPU32Computer.class, new RenderTileEntityVCPU32Computer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVCPU32ComputerConnector.class, new RenderTileEntityVCPU32ComputerConnector());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVCPU32ComputerWire.class, new RenderTileEntityVCPU32ComputerWire());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceKeyboard.class, new RenderTileEntityExternalDeviceKeyboard());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceMonitor.class, new RenderTileEntityExternalDeviceMonitor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceNumberMonitor.class, new RenderTileEntityExternalDeviceNumberMonitor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExternalDeviceConsoleScreen.class, new RenderTileEntityExternalDeviceConsoleScreen());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExpansionConsoleScreen.class, new RenderTileEntityExpansionConsoleScreen());
    }
    @Override
    public void registerEvent()
    {
        super.registerEvent();
        FMLCommonHandler.instance().bus().register(new TickEventListener());
    }
}