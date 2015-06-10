package yuxuanchiadm.apc.apc.common.init;

import yuxuanchiadm.apc.apc.common.block.BlockExpansionConsoleScreen;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDevice300BytesStorage;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceConsoleScreen;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceKeyboard;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceMonitor;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceNoteBox;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceRedstoneController;
import yuxuanchiadm.apc.apc.common.block.BlockVCPU32Computer;
import yuxuanchiadm.apc.apc.common.block.BlockVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.common.block.BlockVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.common.init.blockRegistry.AssemblyProgramCraftBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Bootstrap;

public class AssemblyProgramCraftBlocks
{
    public static final BlockVCPU32Computer block_vcpu_32_computer;
    public static final BlockVCPU32ComputerConnector block_vcpu_32_computer_connector;
    public static final BlockVCPU32ComputerWire block_vcpu_32_computer_wire;
    public static final BlockExternalDevice300BytesStorage block_external_device_300_bytes_storage;
    public static final BlockExternalDeviceKeyboard block_external_device_keyboard;
    public static final BlockExternalDeviceMonitor block_external_device_monitor;
    public static final BlockExternalDeviceNumberMonitor block_external_device_number_monitor;
    public static final BlockExternalDeviceConsoleScreen block_external_device_console_screen;
    public static final BlockExpansionConsoleScreen block_expansion_console_screen;
    public static final BlockExternalDeviceRedstoneController block_external_device_redstone_controller;
    public static final BlockExternalDeviceNoteBox block_external_device_note_box;
    
    private static Block getRegisteredBlock(String name)
    {
        return AssemblyProgramCraftBlockRegistry.getRegisteredBlock(name);
    }
    static
    {
        if (!Bootstrap.isRegistered())
        {
            throw new RuntimeException("Accessed Blocks before Bootstrap!");
        }
        else
        {
            block_vcpu_32_computer = (BlockVCPU32Computer) getRegisteredBlock("block_vcpu_32_computer");
            block_vcpu_32_computer_connector = (BlockVCPU32ComputerConnector) getRegisteredBlock("block_vcpu_32_computer_connector");
            block_vcpu_32_computer_wire = (BlockVCPU32ComputerWire) getRegisteredBlock("block_vcpu_32_computer_wire");
            block_external_device_300_bytes_storage = (BlockExternalDevice300BytesStorage) getRegisteredBlock("block_external_device_300_bytes_storage");
            block_external_device_keyboard = (BlockExternalDeviceKeyboard) getRegisteredBlock("block_external_device_keyboard");
            block_external_device_monitor = (BlockExternalDeviceMonitor) getRegisteredBlock("block_external_device_monitor");
            block_external_device_number_monitor = (BlockExternalDeviceNumberMonitor) getRegisteredBlock("block_external_device_number_monitor");
            block_external_device_console_screen = (BlockExternalDeviceConsoleScreen) getRegisteredBlock("block_external_device_console_screen");
            block_expansion_console_screen = (BlockExpansionConsoleScreen) getRegisteredBlock("block_expansion_console_screen");
            block_external_device_redstone_controller = (BlockExternalDeviceRedstoneController) getRegisteredBlock("block_external_device_redstone_controller");
            block_external_device_note_box = (BlockExternalDeviceNoteBox) getRegisteredBlock("block_external_device_note_box");
        }
    }
}