package org.apcdevpowered.apc.common.container;

import org.apcdevpowered.apc.common.inventory.BIOSWriterInventory;
import org.apcdevpowered.apc.common.slot.SlotVCPU32ComputerCMOSChip;
import org.apcdevpowered.vcpu32.asm.Assembler;
import org.apcdevpowered.vcpu32.asm.CompileLogger;
import org.apcdevpowered.vcpu32.asm.ProgramPackage;
import org.apcdevpowered.vcpu32.disasm.Disassembler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerBIOSWriter extends Container
{
    private BIOSWriterInventory temp_inv = new BIOSWriterInventory();
    private ProgramPackage program = null;
    
    public ContainerBIOSWriter(InventoryPlayer inventory)
    {
        this.addSlotToContainer(new SlotVCPU32ComputerCMOSChip(temp_inv, 0, 178, 203));
        int var3;
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 149 + var3 * 18));
            }
        }
        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(inventory, var3, 8 + var3 * 18, 207));
        }
    }
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        InventoryPlayer var2 = par1EntityPlayer.inventory;

        if (var2.getItemStack() != null)
        {
            par1EntityPlayer.dropPlayerItemWithRandomChoice(var2.getItemStack(), false);
            var2.setItemStack((ItemStack)null);
        }
        
        if(temp_inv.getStackInSlot(0) != null)
        {
            par1EntityPlayer.dropPlayerItemWithRandomChoice(temp_inv.getStackInSlot(0), false);
            temp_inv.setInventorySlotContents(0, (ItemStack)null);
        }
    }
    public String decompiledSource()
    {
        if(temp_inv.getStackInSlot(0) == null)
        {
            return null;
        }
        if(temp_inv.getStackInSlot(0).getTagCompound() == null)
        {
            return null;
        }
        NBTTagCompound nbtTagCompound = (NBTTagCompound)temp_inv.getStackInSlot(0).getTagCompound().copy();
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.readFromNBT(nbtTagCompound);
        return Disassembler.decompile(programPackage);
    }
    public boolean compileSource(String source, CompileLogger logger)
    {
        program = Assembler.compile(source, 7168,  true, "BIOS", logger);
        if(program == null)
        {
            return false;
        }
        return true;
    }
    public boolean writeBytecode()
    {
        if(program == null)
        {
            return false;
        }
        if(temp_inv.getStackInSlot(0) == null)
        {
            return false;
        }
        NBTTagCompound nbtTagCompound = temp_inv.getStackInSlot(0).getTagCompound() == null ? new NBTTagCompound() : (NBTTagCompound)temp_inv.getStackInSlot(0).getTagCompound().copy();
        program.writeToNBT(nbtTagCompound);
        temp_inv.getStackInSlot(0).setTagCompound(nbtTagCompound);
        return true;
    }
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < temp_inv.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, temp_inv.getSizeInventory(), inventorySlots.size(), false))
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
}