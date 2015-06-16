package org.apcdevpowered.apc.common.util;

import net.minecraft.nbt.NBTTagCompound;
import org.apcdevpowered.vcpu32.vm.VirtualMachine;

public class VMDataHelper
{
    /**
     * Write data from VirtualMachine to NBT node.
     * 
     * @param nbttagcompound
     *            NBT node write to.
     * @param vm
     *            The VirtualMachine read from.
     */
    @Deprecated
    public static void writeToNBT(NBTTagCompound nbttagcompound, VirtualMachine vm)
    {
        vm.writeDataToNBT(nbttagcompound);
    }
    /**
     * Read data from NBT node to VirtualMachine.
     * 
     * @param nbttagcompound
     *            NBT node read form.
     * @param vm
     *            The VirtualMachine write to.
     */
    @Deprecated
    public static void readFormNBT(NBTTagCompound nbttagcompound, VirtualMachine vm)
    {
        vm.readDataFormNBT(nbttagcompound);
    }
}