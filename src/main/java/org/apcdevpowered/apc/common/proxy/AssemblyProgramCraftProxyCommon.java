package org.apcdevpowered.apc.common.proxy;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.container.ContainerBIOSWriter;
import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBootstrap;
import org.apcdevpowered.apc.common.listener.WorldUnloadEventListener;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftPacket;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExpansionConsoleScreen;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDevice;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDevice300BytesStorage;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceConsoleScreen;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceKeyboard;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceMonitor;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNoteBox;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNumberMonitor;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceRedstoneController;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32Computer;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerWire;
import org.apcdevpowered.apc.common.util.WorldHelper;
import org.apcdevpowered.vcpu32.asm.CompileLogger;
import org.apcdevpowered.vcpu32.vm.extdev.ExternalDeviceKeyboard;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public abstract class AssemblyProgramCraftProxyCommon
{
    public abstract boolean isClient();
    public abstract boolean isGamePaused();
    public void handlePacket(final AssemblyProgramCraftPacket packet, final EntityPlayerMP player)
    {
        if (packet.packetType == AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoCompiled.getValue())
        {
            if (player.openContainer.windowId == packet.dataByte[0])
            {
                CompileLogger logger = new CompileLogger();
                boolean result = ((ContainerBIOSWriter) player.openContainer).compileSource(packet.dataString[0], logger);
                AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                pak.packetType = AssemblyProgramCraftPacket.ServerPacket.CompiledResult.getValue();
                pak.dataString = new String[1];
                pak.dataString[0] = logger.toString();
                pak.dataByte = new byte[1];
                pak.dataByte[0] = ((result == true) ? (byte) 1 : (byte) 0);
                AssemblyProgramCraft.sendTo(player, pak);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoWrite.getValue())
        {
            if (player.openContainer.windowId == packet.dataByte[0])
            {
                boolean result = ((ContainerBIOSWriter) player.openContainer).writeBytecode();
                AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                pak.packetType = AssemblyProgramCraftPacket.ServerPacket.WriteResult.getValue();
                pak.dataByte = new byte[1];
                pak.dataByte[0] = ((result == true) ? (byte) 1 : (byte) 0);
                AssemblyProgramCraft.sendTo(player, pak);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoDecompiled.getValue())
        {
            if (player.openContainer.windowId == packet.dataByte[0])
            {
                String source = ((ContainerBIOSWriter) player.openContainer).decompiledSource();
                AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                pak.packetType = AssemblyProgramCraftPacket.ServerPacket.DecompiledResult.getValue();
                pak.dataString = new String[1];
                pak.dataByte = new byte[1];
                pak.dataByte[0] = ((source == null) ? (byte) 0 : (byte) 1);
                pak.dataString[0] = (source == null ? "" : source);
                AssemblyProgramCraft.sendTo(player, pak);
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ClientPacket.PortSettingToolSetID.getValue())
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
                    ((TileEntityExternalDevice) tileentity).setPort(port);
                    AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
                    pak.packetType = AssemblyProgramCraftPacket.ServerPacket.SyncExternalDevicePort.getValue();
                    pak.dataInt = new int[5];
                    pak.dataInt[0] = tileentity.getPos().getX();
                    pak.dataInt[1] = tileentity.getPos().getX();
                    pak.dataInt[2] = tileentity.getPos().getX();
                    pak.dataInt[3] = tileentity.getWorld().provider.getDimensionId();
                    pak.dataInt[4] = port;
                    AssemblyProgramCraft.sendToAll(pak);
                }
            }
        }
        else if (packet.packetType == AssemblyProgramCraftPacket.ClientPacket.KeyboardStatusChange.getValue())
        {
            byte event = packet.dataByte[0];
            int x = packet.dataInt[0];
            int y = packet.dataInt[1];
            int z = packet.dataInt[2];
            int dimension = packet.dataInt[3];
            int value = packet.dataInt[4];
            World world = WorldHelper.getWorldFromDimension(dimension);
            if (world != null)
            {
                TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
                if (tileentity instanceof TileEntityExternalDeviceKeyboard)
                {
                    TileEntityExternalDeviceKeyboard tileEntityKeyboard = (TileEntityExternalDeviceKeyboard) tileentity;
                    ExternalDeviceKeyboard keyboard = tileEntityKeyboard.externalDeviceKeyboard;
                    if (event == 1)
                    {
                        keyboard.onCharTyped((char) value);
                    }
                    else if (event == 2)
                    {
                        keyboard.onKeyPressed(value);
                    }
                    else if (event == 3)
                    {
                        keyboard.onKeyReleased(value);
                    }
                }
            }
        }
    }
    public World getWorldFromDimension(int dimension, Side side)
    {
        if (side == Side.SERVER)
        {
            MinecraftServer minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (minecraftServer == null)
            {
                return null;
            }
            else
            {
                return minecraftServer.worldServerForDimension(dimension);
            }
        }
        else
        {
            return null;
        }
    }
    public void registerBlocksAndItems()
    {
        AssemblyProgramCraftBootstrap.register();
    }
    public void registerTileEntities()
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
    public void registerEvent()
    {
        MinecraftForge.EVENT_BUS.register(new WorldUnloadEventListener());
    }
}