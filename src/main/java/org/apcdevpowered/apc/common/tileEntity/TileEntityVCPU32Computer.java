package org.apcdevpowered.apc.common.tileEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import org.apcdevpowered.apc.client.listener.TickEventListener;
import org.apcdevpowered.apc.client.listener.TickEventListener.IGamePauseListener;
import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockVCPU32Computer;
import org.apcdevpowered.apc.common.block.BlockVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.init.AssemblyProgramCraftBlocks;
import org.apcdevpowered.apc.common.init.AssemblyProgramCraftItems;
import org.apcdevpowered.apc.common.item.ItemBlockVCPU32ComputerWire;
import org.apcdevpowered.apc.common.listener.WorldUnloadEventListener;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftGuiHandler;
import org.apcdevpowered.apc.common.network.AssemblyProgramCraftPacket;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDevice.ComputerPos;
import org.apcdevpowered.apc.common.util.MethodHandler;
import org.apcdevpowered.apc.common.util.NodeIOException;
import org.apcdevpowered.apc.common.util.ProgramDataHelper;
import org.apcdevpowered.apc.common.util.VMDataHelper;
import org.apcdevpowered.vcpu32.asm.ProgramPackage;
import org.apcdevpowered.vcpu32.vm.AssemblyVirtualThread;
import org.apcdevpowered.vcpu32.vm.Monitor;
import org.apcdevpowered.vcpu32.vm.VirtualMachine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class TileEntityVCPU32Computer extends TileEntity implements IInventory, IGamePauseListener, IUpdatePlayerListBox
{
    private MethodHandler worldUnloadMethodHandler;
    private HashMap<Integer, TileEntityExternalDevice> connectedDeviceList = new HashMap<Integer, TileEntityExternalDevice>();
    private ItemStack[] computerContents = new ItemStack[1];
    private boolean powerStatus = false;
    private boolean vmIsRunning = false;
    private boolean runtimeError = false;
    private boolean isInIt = false;
    private VirtualMachine vm;
    private UUID loadVMUUID;
    
    public TileEntityVCPU32Computer()
    {
        try
        {
            worldUnloadMethodHandler = new MethodHandler(this, TileEntityVCPU32Computer.class.getMethod("onWorldUnload", World.class));
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }
    public void recheckConnectedDevice(List<TileEntityExternalDevice> deviceList)
    {
        HashMap<Integer, TileEntityExternalDevice> portDeviceMap = new HashMap<Integer, TileEntityExternalDevice>();
        for (TileEntityExternalDevice device : deviceList)
        {
            if (portDeviceMap.get(device.getPort()) != null)
            {
                if (device.lockedComputerPos != null && device.lockedComputerPos.is(this))
                {
                    portDeviceMap.put(device.getPort(), device);
                }
            }
            else
            {
                if (device.lockedComputerPos == null || device.lockedComputerPos.is(this))
                {
                    portDeviceMap.put(device.getPort(), device);
                }
            }
        }
        Iterator<Entry<Integer, TileEntityExternalDevice>> connectedDeviceListEntryIterator = connectedDeviceList.entrySet().iterator();
        while (connectedDeviceListEntryIterator.hasNext())
        {
            Entry<Integer, TileEntityExternalDevice> entry = connectedDeviceListEntryIterator.next();
            if (portDeviceMap.get(entry.getKey()) == null)
            {
                connectedDeviceListEntryIterator.remove();
                entry.getValue().lockedComputerPos = null;
                if (vm != null)
                {
                    vm.removeExternalDevices(entry.getKey());
                }
            }
            else if (portDeviceMap.get(entry.getKey()) != entry.getValue())
            {
                entry.setValue(portDeviceMap.get(entry.getKey()));
                portDeviceMap.get(entry.getKey()).lockedComputerPos = ComputerPos.form(this);
                entry.getValue().lockedComputerPos = null;
                if (vm != null)
                {
                    vm.removeExternalDevices(entry.getKey());
                    vm.addExternalDevices(portDeviceMap.get(entry.getKey()).getExternalDevice(), entry.getKey());
                }
            }
        }
        Iterator<Entry<Integer, TileEntityExternalDevice>> portDeviceMapEntryIterator = portDeviceMap.entrySet().iterator();
        while (portDeviceMapEntryIterator.hasNext())
        {
            Entry<Integer, TileEntityExternalDevice> entry = portDeviceMapEntryIterator.next();
            if (connectedDeviceList.get(entry.getKey()) == null)
            {
                connectedDeviceList.put(entry.getKey(), entry.getValue());
                entry.getValue().lockedComputerPos = ComputerPos.form(this);
                if (vm != null)
                {
                    vm.addExternalDevices(entry.getValue().getExternalDevice(), entry.getKey());
                }
            }
            else if (connectedDeviceList.get(entry.getKey()) != entry.getValue())
            {
                connectedDeviceList.remove(entry.getKey());
                connectedDeviceList.put(entry.getKey(), entry.getValue());
                entry.getValue().lockedComputerPos = ComputerPos.form(this);
                connectedDeviceList.get(entry.getKey()).lockedComputerPos = null;
                if (vm != null)
                {
                    vm.removeExternalDevices(entry.getKey());
                    vm.addExternalDevices(entry.getValue().getExternalDevice(), entry.getKey());
                }
            }
        }
    }
    public void updataConnector(IBlockState state)
    {
        if (getWorld().isRemote)
        {
            return;
        }
        TileEntityVCPU32ComputerConnector connector = getConnectedConnector(state);
        if (connector != null)
        {
            ItemBlockVCPU32ComputerWire.updataConnector(connector);
        }
    }
    public TileEntityVCPU32ComputerConnector getConnectedConnector(IBlockState state)
    {
        EnumFacing facing = (EnumFacing) state.getValue(BlockVCPU32Computer.FACING);
        BlockPos connectorPos = getPos().offset(facing.getOpposite());
        IBlockState connectorState = getWorld().getBlockState(connectorPos);
        if (connectorState.getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_connector)
        {
            if (((EnumFacing) connectorState.getValue(BlockVCPU32ComputerConnector.FACING)) == facing.getOpposite())
            {
                return (TileEntityVCPU32ComputerConnector) getWorld().getTileEntity(connectorPos);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    public boolean isRunning()
    {
        return powerStatus;
    }
    public boolean isError()
    {
        return runtimeError;
    }
    public void setRuntimeError(boolean status)
    {
        runtimeError = status;
        if (getWorld().isRemote)
        {
            return;
        }
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ServerPacket.ComputerRuntimeError.getValue();
        pak.dataInt = new int[4];
        pak.dataByte = new byte[1];
        pak.dataInt[0] = getPos().getX();
        pak.dataInt[1] = getPos().getY();
        pak.dataInt[2] = getPos().getZ();
        pak.dataInt[3] = getWorld().provider.getDimensionId();
        pak.dataByte[0] = (runtimeError ? ((byte) 1) : ((byte) 0));
        AssemblyProgramCraft.sendToAllPlayers(pak);
    }
    public void onChunkUnload()
    {
        super.onChunkUnload();
        if (getWorld().isRemote || vm == null)
        {
            return;
        }
        shutdownVM();
        removeHook();
    }
    public void onWorldUnload(World world)
    {
        if (getWorld().isRemote || vm == null)
        {
            return;
        }
        shutdownVM();
        removeHook();
    }
    public void invalidate()
    {
        super.invalidate();
        if (getWorld().isRemote)
        {
            return;
        }
        if (vm != null)
        {
            shutdownVM();
        }
        removeHook();
    }
    public void turnPower()
    {
        if (powerStatus == false)
        {
            turnPower(true);
        }
        else
        {
            turnPower(false);
        }
    }
    public void turnPower(boolean status)
    {
        powerStatus = status;
        updataPowerStatus();
    }
    public void updataPowerStatus()
    {
        if (getWorld().isRemote)
        {
            return;
        }
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ServerPacket.ComputerPowerChange.getValue();
        pak.dataInt = new int[4];
        pak.dataByte = new byte[1];
        pak.dataInt[0] = getPos().getX();
        pak.dataInt[1] = getPos().getY();
        pak.dataInt[2] = getPos().getZ();
        pak.dataInt[3] = getWorld().provider.getDimensionId();
        pak.dataByte[0] = (powerStatus ? ((byte) 1) : ((byte) 0));
        AssemblyProgramCraft.sendToAllPlayers(pak);
    }
    public void init()
    {
        if (loadVMUUID != null)
        {
            loadVM(loadVMUUID);
        }
        TileEntityVCPU32ComputerConnector connector = this.getConnectedConnector(getWorld().getBlockState(getPos()));
        if (connector != null)
        {
            ItemBlockVCPU32ComputerWire.updataConnector(connector);
        }
        WorldUnloadEventListener.addListener(worldUnloadMethodHandler);
        TickEventListener.addListener(this);
    }
    public void update()
    {
        if (getWorld().isRemote)
        {
            return;
        }
        if (isInIt == false)
        {
            init();
            isInIt = true;
        }
        if (powerStatus == true)
        {
            if (vmIsRunning == false)
            {
                startVM();
                vmIsRunning = true;
            }
        }
        else
        {
            if (vmIsRunning == true)
            {
                if (isError() == true)
                {
                    setRuntimeError(false);
                    vmIsRunning = false;
                }
                else
                {
                    if (vm == null)
                    {
                        return;
                    }
                    shutdownVM();
                    vmIsRunning = false;
                }
            }
        }
        this.markDirty();
    }
    private void removeHook()
    {
        TickEventListener.removeListener(this);
        WorldUnloadEventListener.removeListener(worldUnloadMethodHandler);
    }
    private void shutdownVM()
    {
        vm.shutdownVM();
        vm.loadBIOS(null);
    }
    private void startVM()
    {
        if (this.computerContents[0] == null || this.computerContents[0].getItem() != AssemblyProgramCraftItems.item_vcpu_32_computer_cmos_chip || this.computerContents[0].getTagCompound() == null)
        {
            setRuntimeError(true);
            return;
        }
        if (vm == null)
        {
            vm = new VirtualMachine(true);
            Iterator<Entry<Integer, TileEntityExternalDevice>> iterator = connectedDeviceList.entrySet().iterator();
            while (iterator.hasNext())
            {
                Entry<Integer, TileEntityExternalDevice> entry = iterator.next();
                vm.addExternalDevices(entry.getValue().getExternalDevice(), entry.getKey());
            }
        }
        ProgramPackage program = new ProgramPackage();
        NBTTagCompound programNBTTagCompound = this.computerContents[0].getTagCompound();
        if(!programNBTTagCompound.hasKey("uuid", 8))
        {
            setRuntimeError(true);
            return;
        }
        UUID uuid;
        try
        {
            uuid = UUID.fromString(programNBTTagCompound.getString("uuid"));
        }
        catch (IllegalArgumentException e)
        {
            setRuntimeError(true);
            return;
        }
        try
        {
            ProgramDataHelper.readFormNode(uuid, program);
        }
        catch (NodeIOException e)
        {
            setRuntimeError(true);
            return;
        }
        vm.loadBIOS(program);
        vm.startVM(false);
    }
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setBoolean("powerStatus", powerStatus);
        nbttagcompound.setBoolean("runtimeError", runtimeError);
        return new S35PacketUpdateTileEntity(getPos(), Block.getIdFromBlock(AssemblyProgramCraftBlocks.block_vcpu_32_computer), nbttagcompound);
    }
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        powerStatus = pkt.getNbtCompound().getBoolean("powerStatus");
        runtimeError = pkt.getNbtCompound().getBoolean("runtimeError");
    }
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.computerContents.length; ++var3)
        {
            if (this.computerContents[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.computerContents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("items", var2);
        par1NBTTagCompound.setBoolean("powerStatus", this.powerStatus);
        par1NBTTagCompound.setBoolean("runtimeError", this.runtimeError);
        if (vm != null)
        {
            UUID uuid;
            try
            {
                uuid = VMDataHelper.writeToData(worldObj, vm);
            }
            catch (NodeIOException e)
            {
                return;
            }
            par1NBTTagCompound.setString("virtualMachineUUID", uuid.toString());
        }
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("items", 10);
        this.computerContents = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound) var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;
            if (var5 >= 0 && var5 < this.computerContents.length)
            {
                this.computerContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        this.powerStatus = par1NBTTagCompound.getBoolean("powerStatus");
        this.runtimeError = par1NBTTagCompound.getBoolean("runtimeError");
        if (par1NBTTagCompound.hasKey("virtualMachineUUID"))
        {
            UUID uuid = UUID.fromString(par1NBTTagCompound.getString("virtualMachineUUID"));
            if (getWorld() == null)
            {
                loadVMUUID = uuid;
            }
            else
            {
                loadVM(uuid);
            }
        }
    }
    public void loadVM(UUID uuid)
    {
        VirtualMachine vm = new VirtualMachine();
        synchronized (vm)
        {
            try
            {
                VMDataHelper.readFormNode(getWorld(), uuid, vm);
            }
            catch (NodeIOException e)
            {
                return;
            }
            this.vm = vm;
            if (this.vm.isRunning() == true)
            {
                List<AssemblyVirtualThread> avtList = vm.getVMThreadList();
                {
                    for (AssemblyVirtualThread avt : avtList)
                    {
                        avt.loadThreadRelation();
                    }
                }
                List<Monitor> monitorList = vm.getMonitorList();
                {
                    for (Monitor monitor : monitorList)
                    {
                        monitor.loadMonitorRelation();
                    }
                }
                for (AssemblyVirtualThread avt : avtList)
                {
                    if (avt.isRunning() == true)
                    {
                        avt.loadedStart();
                    }
                }
                vmIsRunning = true;
            }
        }
    }
    public void modifyComputer(EntityPlayer entityplayer)
    {
        FMLNetworkHandler.openGui(entityplayer, AssemblyProgramCraft.instance, AssemblyProgramCraftGuiHandler.COMPUTER_MODIFY_GUI_ID, getWorld(), getPos().getX(), getPos().getY(), getPos().getZ());
    }
    public int getSizeInventory()
    {
        return computerContents.length;
    }
    public ItemStack getStackInSlot(int index)
    {
        return this.computerContents[index];
    }
    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.computerContents[index] != null)
        {
            ItemStack itemstack = this.computerContents[index];
            this.computerContents[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }
    public ItemStack decrStackSize(int index, int count)
    {
        if (this.computerContents[index] != null)
        {
            ItemStack var3;
            if (this.computerContents[index].stackSize <= count)
            {
                var3 = this.computerContents[index];
                this.computerContents[index] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.computerContents[index].splitStack(count);
                if (this.computerContents[index].stackSize == 0)
                {
                    this.computerContents[index] = null;
                }
                this.markDirty();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.computerContents[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    public String getName()
    {
        return "computer_inv";
    }
    public int getInventoryStackLimit()
    {
        return 1;
    }
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.getWorld().getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
    public void openInventory(EntityPlayer player)
    {
    }
    public void closeInventory(EntityPlayer player)
    {
    }
    public boolean hasCustomName()
    {
        return true;
    }
    @Override
    public IChatComponent getDisplayName()
    {
        return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
    }
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }
    public void gamePaused()
    {
        if (vm != null)
        {
            vm.suspendVM();
        }
    }
    public void gameResume()
    {
        if (vm != null)
        {
            vm.resumeVM();
        }
    }
    @Override
    public int getField(int id)
    {
        return 0;
    }
    @Override
    public void setField(int id, int value)
    {
    }
    @Override
    public int getFieldCount()
    {
        return 0;
    }
    @Override
    public void clear()
    {
        for (int i = 0; i < this.computerContents.length; ++i)
        {
            this.computerContents[i] = null;
        }
    }
}