package org.apcdevpowered.apc.common.network;

import org.apcdevpowered.apc.client.gui.screen.GuiBIOSWriter;
import org.apcdevpowered.apc.client.gui.screen.GuiComputerModify;
import org.apcdevpowered.apc.client.gui.screen.GuiKeyboard;
import org.apcdevpowered.apc.client.gui.screen.GuiPortSetting;
import org.apcdevpowered.apc.common.container.ContainerBIOSWriter;
import org.apcdevpowered.apc.common.container.ContainerComputerModify;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDevice;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceKeyboard;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32Computer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AssemblyProgramCraftGuiHandler implements IGuiHandler
{
    public static final int BIOS_WRITER_GUI_ID = 0;
    public static final int COMPUTER_MODIFY_GUI_ID = 1;
    public static final int KEYBOARD_GUI_ID = 2;
    public static final int ID_SETTIING_GUI_ID = 3;
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == BIOS_WRITER_GUI_ID)
        {
            return new ContainerBIOSWriter(player.inventory);
        }
        else if(ID == COMPUTER_MODIFY_GUI_ID)
        {
            TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(new BlockPos(x, y, z));
            if(tileentity == null)
            {
                return null;
            }
            return new ContainerComputerModify(player.inventory, tileentity);
        }
        else
        {
            return null;
        }
    }
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == BIOS_WRITER_GUI_ID)
        {
            return new GuiBIOSWriter(player.inventory);
        }
        else if(ID == COMPUTER_MODIFY_GUI_ID)
        {
            TileEntityVCPU32Computer tileentity = (TileEntityVCPU32Computer)world.getTileEntity(new BlockPos(x, y, z));
            if(tileentity == null)
            {
                return null;
            }
            return new GuiComputerModify(player.inventory, tileentity);
        }
        else if(ID == KEYBOARD_GUI_ID)
        {
            TileEntityExternalDeviceKeyboard tileentity = (TileEntityExternalDeviceKeyboard)world.getTileEntity(new BlockPos(x, y, z));
            if(tileentity == null)
            {
                return null;
            }
            return new GuiKeyboard(tileentity);
        }
        else if(ID == ID_SETTIING_GUI_ID)
        {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if(tileEntity instanceof TileEntityExternalDevice)
            {
                TileEntityExternalDevice tileentityDevice = (TileEntityExternalDevice)tileEntity;
                return new GuiPortSetting(tileentityDevice);
            }
            return null;
        }
        else
        {
            return null;
        }
    }
}
