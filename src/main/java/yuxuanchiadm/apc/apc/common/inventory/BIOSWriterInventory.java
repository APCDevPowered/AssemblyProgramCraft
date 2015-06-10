package yuxuanchiadm.apc.apc.common.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class BIOSWriterInventory extends InventoryBasic
{
    public BIOSWriterInventory()
    {
        super("bios_writer_inv", true, 1);
    }
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }
}