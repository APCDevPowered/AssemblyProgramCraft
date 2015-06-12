package org.apcdevpowered.apc.common.util;

import org.apcdevpowered.vcpu32.vm.VirtualMachine;

import net.minecraft.nbt.NBTTagCompound;

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
     * @return {@code object[0]} is VirtualMachine. {@code object[1]} is
     *         AssemblyVirtualThread list. {@code object[2]} is Monitor list.
     */
    public static Object[] readFormNBT(NBTTagCompound nbttagcompound, VirtualMachine vm)
    {
        Object vmObject[] = vm.readDataFormNBT(nbttagcompound);
        Object object[] = new Object[3];
        object[0] = vm;
        object[1] = vmObject[0];
        object[2] = vmObject[1];
        return object;
    }
}