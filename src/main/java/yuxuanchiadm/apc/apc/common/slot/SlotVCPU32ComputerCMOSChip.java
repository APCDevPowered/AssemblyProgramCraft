package yuxuanchiadm.apc.apc.common.slot;

import yuxuanchiadm.apc.apc.common.init.AssemblyProgramCraftItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotVCPU32ComputerCMOSChip extends Slot
{
    public SlotVCPU32ComputerCMOSChip(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
    {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        if(par1ItemStack.getItem() != AssemblyProgramCraftItems.item_vcpu_32_computer_cmos_chip)
        {
            return false;
        }
        return true;
    }
}
