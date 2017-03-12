package org.apcdevpowered.apc.common.container;

import org.apcdevpowered.apc.common.slot.SlotVCPU32ComputerCMOSChip;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32Computer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerComputerModify extends Container
{
    private TileEntityVCPU32Computer computerTileEntity;
    public ContainerComputerModify(InventoryPlayer inventory, TileEntityVCPU32Computer computerTileEntity)
    {
        this.computerTileEntity = computerTileEntity;
        this.addSlotToContainer(new SlotVCPU32ComputerCMOSChip(computerTileEntity, 0, 26, 73));
        int var3;
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 105 + var3 * 18));
            }
        }
        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(inventory, var3, 8 + var3 * 18, 163));
        }
    }
    @Override
	public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }
    @Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < computerTileEntity.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, computerTileEntity.getSizeInventory(), this.inventorySlots.size(), false))
                {
                    return null;
                }
            }
            else
            {
                for(int i = 0;i < inventorySlots.size();i++)
                {
                    if (!((Slot)this.inventorySlots.get(i)).getHasStack() && ((Slot)this.inventorySlots.get(i)).isItemValid(itemstack1))
                    {
                        if (itemstack1.hasTagCompound() && itemstack1.stackSize == 1)
                        {
                            ((Slot)this.inventorySlots.get(i)).putStack(itemstack1.copy());
                            itemstack1.stackSize = 0;
                        }
                        else if (itemstack1.stackSize >= 1)
                        {
                            ((Slot)this.inventorySlots.get(i)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getItemDamage()));
                            --itemstack1.stackSize;
                        }
                    }
                }
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
    public TileEntityVCPU32Computer getComputerTileEntity()
    {
        return computerTileEntity;
    }
}