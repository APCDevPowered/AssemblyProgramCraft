package org.apcdevpowered.apc.common.init.blockRegistry;

import java.util.HashMap;
import java.util.Map;

import org.apcdevpowered.apc.common.block.BlockExpansionConsoleScreen;
import org.apcdevpowered.apc.common.block.BlockExternalDevice300BytesStorage;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceConsoleScreen;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceKeyboard;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceMonitor;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceNoteBox;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceNumberMonitor;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceRedstoneController;
import org.apcdevpowered.apc.common.block.BlockVCPU32Computer;
import org.apcdevpowered.apc.common.block.BlockVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.block.BlockVCPU32ComputerWire;
import org.apcdevpowered.apc.common.item.ItemBlockVCPU32ComputerWire;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AssemblyProgramCraftBlockRegistry
{
    private static final Map<String, Block> registeredBlock = new HashMap<String, Block>();
    
    public static void registerBlocks()
    {
        registerBlock(new BlockVCPU32Computer(), "block_vcpu_32_computer");
        registerBlock(new BlockVCPU32ComputerConnector(), "block_vcpu_32_computer_connector");
        registerBlock(new BlockVCPU32ComputerWire(), "block_vcpu_32_computer_wire", ItemBlockVCPU32ComputerWire.class);
        registerBlock(new BlockExternalDevice300BytesStorage(), "block_external_device_300_bytes_storage");
        registerBlock(new BlockExternalDeviceKeyboard(), "block_external_device_keyboard");
        registerBlock(new BlockExternalDeviceMonitor(), "block_external_device_monitor");
        registerBlock(new BlockExternalDeviceNumberMonitor(), "block_external_device_number_monitor");
        registerBlock(new BlockExternalDeviceConsoleScreen(), "block_external_device_console_screen");
        registerBlock(new BlockExpansionConsoleScreen(), "block_expansion_console_screen");
        registerBlock(new BlockExternalDeviceRedstoneController(), "block_external_device_redstone_controller");
        registerBlock(new BlockExternalDeviceNoteBox(), "block_external_device_note_box");
    }
    public static void registerBlock(Block block, String name)
    {
        registerBlock(block, name, ItemBlock.class);
    }
    public static void registerBlock(Block block, String name, Class<? extends ItemBlock> itemclass)
    {
        registerBlock(block, name, itemclass, new Object[]{});
    }
    public static void registerBlock(Block block, String name, Class<? extends ItemBlock> itemclass, Object... itemCtorArgs)
    {
        GameRegistry.registerBlock(block, itemclass, name, itemCtorArgs);
        
        block.setUnlocalizedName(name);
        
        registeredBlock.put(name, block);
    }
    public static Block getRegisteredBlock(String name)
    {
        return registeredBlock.get(name);
    }
}