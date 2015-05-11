package yuxuanchiadm.apc.apc.slot;

import yuxuanchiadm.apc.AssemblyProgramCraft;
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
        if(par1ItemStack.getItem() != AssemblyProgramCraft.instance.item_vcpu_32_computer_coms_chip)
        {
            return false;
        }
        return true;
    }
}
