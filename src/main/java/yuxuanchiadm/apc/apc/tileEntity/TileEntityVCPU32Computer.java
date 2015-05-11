package yuxuanchiadm.apc.apc.tileEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.util.MethodHandler;
import yuxuanchiadm.apc.apc.event.TickEventListener;
import yuxuanchiadm.apc.apc.event.WorldUnloadEventListener;
import yuxuanchiadm.apc.apc.event.listener.IGamePauseListener;
import yuxuanchiadm.apc.apc.item.ItemBlockVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftGuiHandler;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.apc.tileEntity.ITileEntityExternalDevice.ComputerPos;
import yuxuanchiadm.apc.apc.util.VMDataHelper;
import yuxuanchiadm.apc.vcpu32.asm.ProgramPackage;
import yuxuanchiadm.apc.vcpu32.vm.AssemblyVirtualThread;
import yuxuanchiadm.apc.vcpu32.vm.Monitor;
import yuxuanchiadm.apc.vcpu32.vm.VirtualMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityVCPU32Computer extends TileEntity implements IInventory,IGamePauseListener
{
    private MethodHandler worldUnloadMethodHandler;
    private HashMap<Integer, ITileEntityExternalDevice> connectedDeviceList = new HashMap<Integer, ITileEntityExternalDevice>();
    private ItemStack[] computerContents = new ItemStack[1];
	private boolean powerStatus = false;
	private boolean vmIsRunning = false;
	private boolean runtimeError = false;
	private boolean isInIt = false;
	private VirtualMachine vm;
	
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

	public void recheckConnectedDevice(List<ITileEntityExternalDevice> deviceList)
	{
        HashMap<Integer, ITileEntityExternalDevice> portDeviceMap = new HashMap<Integer, ITileEntityExternalDevice>();
        for(ITileEntityExternalDevice device : deviceList)
        {
            if(portDeviceMap.get(device.getPort()) != null)
            {
                if(device.lockedComputerPos != null && device.lockedComputerPos.is(this))
                {
                    portDeviceMap.put(device.getPort(), device);
                }
            }
            else
            {
                if(device.lockedComputerPos == null || device.lockedComputerPos.is(this))
                {
                    portDeviceMap.put(device.getPort(), device);
                }
            }
        }
        Iterator<Entry<Integer, ITileEntityExternalDevice>> connectedDeviceListEntryIterator = connectedDeviceList.entrySet().iterator();
        while(connectedDeviceListEntryIterator.hasNext())
        {
            Entry<Integer, ITileEntityExternalDevice> entry = connectedDeviceListEntryIterator.next();
            if(portDeviceMap.get(entry.getKey()) == null)
            {
                connectedDeviceListEntryIterator.remove();
                entry.getValue().lockedComputerPos = null;
                if(vm != null)
                {
                    vm.removeExternalDevices(entry.getKey());
                }
            }
            else if(portDeviceMap.get(entry.getKey()) != entry.getValue())
            {
                entry.setValue(portDeviceMap.get(entry.getKey()));
                portDeviceMap.get(entry.getKey()).lockedComputerPos = ComputerPos.form(this);
                entry.getValue().lockedComputerPos = null;
                if(vm != null)
                {
                    vm.removeExternalDevices(entry.getKey());
                    vm.addExternalDevices(portDeviceMap.get(entry.getKey()).getExternalDevice(), entry.getKey());
                }
            }
        }
        Iterator<Entry<Integer, ITileEntityExternalDevice>> portDeviceMapEntryIterator = portDeviceMap.entrySet().iterator();
        while(portDeviceMapEntryIterator.hasNext())
        {
            Entry<Integer, ITileEntityExternalDevice> entry = portDeviceMapEntryIterator.next();
            if(connectedDeviceList.get(entry.getKey()) == null)
            {
                connectedDeviceList.put(entry.getKey(), entry.getValue());
                entry.getValue().lockedComputerPos = ComputerPos.form(this);
                if(vm != null)
                {
                    vm.addExternalDevices(entry.getValue().getExternalDevice(), entry.getKey());
                }
            }
            else if(connectedDeviceList.get(entry.getKey()) != entry.getValue())
            {
                connectedDeviceList.remove(entry.getKey());
                connectedDeviceList.put(entry.getKey(), entry.getValue());
                entry.getValue().lockedComputerPos = ComputerPos.form(this);
                connectedDeviceList.get(entry.getKey()).lockedComputerPos = null;
                if(vm != null)
                {
                    vm.removeExternalDevices(entry.getKey());
                    vm.addExternalDevices(entry.getValue().getExternalDevice(), entry.getKey());
                }
            }
        }
	}
    public void updataConnector()
    {
        if(worldObj.isRemote)
        {
            return;
        }
        TileEntityVCPU32ComputerConnector connector = getConnectedConnector();
        if(connector != null)
        {
            ItemBlockVCPU32ComputerWire.updataConnector(connector);
        }
    }
	public TileEntityVCPU32ComputerConnector getConnectedConnector()
	{
	    int direction = getBlockMetadata() & 3;
        if(direction == 0)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord + 1) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1)  & 3;
                if(blockDirection == 2)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
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
        else if(direction == 1)
        {
            if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord)  & 3;
                if(blockDirection == 3)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
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
        else if(direction == 2)
        {
            if(worldObj.getBlock(xCoord, yCoord, zCoord - 1) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1)  & 3;
                if(blockDirection == 0)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
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
        else if(direction == 3)
        {
            if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) == AssemblyProgramCraft.instance.block_connector)
            {
                int blockDirection = worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord)  & 3;
                if(blockDirection == 1)
                {
                    return (TileEntityVCPU32ComputerConnector)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
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
	    if (worldObj.isRemote)
	    {
	        return;
	    }
	    AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
	    pak.packetType = AssemblyProgramCraftPacket.ServerPacket.ComputerRuntimeError.getValue();
	    pak.dataInt = new int[4];
	    pak.dataByte = new byte[1];
	    pak.dataInt[0] = xCoord;
	    pak.dataInt[1] = yCoord;
	    pak.dataInt[2] = zCoord;
	    pak.dataInt[3] = worldObj.getWorldInfo().getVanillaDimension();
	    pak.dataByte[0] = (runtimeError ? ((byte)1) : ((byte)0));
	    AssemblyProgramCraft.sendToAllPlayers(pak);
	}
	public void onChunkUnload()
    {
		super.onChunkUnload();
		if (worldObj.isRemote || vm == null)
        {
            return;
        }
        shutdownVM();
        removeHook();
    }
	public void onWorldUnload(World world)
	{
	    if (worldObj.isRemote || vm == null)
        {
        	return;
        }
		shutdownVM();
        removeHook();
	}
	public void invalidate()
    {
        super.invalidate();
        if (worldObj.isRemote)
        {
            return;
        }
        if(vm != null)
        {
            shutdownVM();
        }
        removeHook();
        updataConnector();
    }
	public void turnPower()
	{
		if(powerStatus == false)
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
		if (worldObj.isRemote)
        {
            return;
        }
		AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
		pak.packetType = AssemblyProgramCraftPacket.ServerPacket.ComputerPowerChange.getValue();
		pak.dataInt = new int[4];
		pak.dataByte = new byte[1];
		pak.dataInt[0] = xCoord;
		pak.dataInt[1] = yCoord;
		pak.dataInt[2] = zCoord;
		pak.dataInt[3] = worldObj.getWorldInfo().getVanillaDimension();
		pak.dataByte[0] = (powerStatus ? ((byte)1) : ((byte)0));
		AssemblyProgramCraft.sendToAllPlayers(pak);
	}
	public void init()
	{
        TileEntityVCPU32ComputerConnector connector = this.getConnectedConnector();
        if(connector != null)
        {
            ItemBlockVCPU32ComputerWire.updataConnector(connector);
        }
        WorldUnloadEventListener.addListener(worldUnloadMethodHandler);
        TickEventListener.addListener(this);
	}
	public void updateEntity()
	{
		super.updateEntity();
		if(worldObj.isRemote)
		{
			return;
		}
		if(isInIt == false)
		{
			init();
			isInIt = true;
		}
		if(powerStatus == true)
		{
			if(vmIsRunning == false)
			{
				startVM();
				vmIsRunning = true;
			}
		}
		else
		{
			if(vmIsRunning == true)
			{
			    if(isError() == true)
		        {
			        setRuntimeError(false);
                    vmIsRunning = false;
		        }
			    else
			    {
	                if(vm == null)
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
	    if(this.computerContents[0] == null || this.computerContents[0].getItem() != AssemblyProgramCraft.instance.item_vcpu_32_computer_coms_chip || this.computerContents[0].stackTagCompound == null)
	    {
	        setRuntimeError(true);
	        return;
	    }
		if(vm == null)
		{
			vm = new VirtualMachine();
			Iterator<Entry<Integer, ITileEntityExternalDevice>> iterator = connectedDeviceList.entrySet().iterator();
            while(iterator.hasNext())
            {
                Entry<Integer, ITileEntityExternalDevice> entry = iterator.next();
                vm.addExternalDevices(entry.getValue().getExternalDevice(), entry.getKey());
            }
		} 
		ProgramPackage program = new ProgramPackage();
		program.readFromNBT(this.computerContents[0].stackTagCompound);
		vm.loadBIOS(program);
		vm.startVM(false);
	}
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        
        var1.setBoolean("powerStatus", powerStatus);
        var1.setBoolean("runtimeError", runtimeError);
        
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, Block.getIdFromBlock(AssemblyProgramCraft.instance.block_computer), var1);
    }
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
		powerStatus = pkt.func_148857_g().getBoolean("powerStatus");
		runtimeError = pkt.func_148857_g().getBoolean("runtimeError");
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
                var4.setByte("Slot", (byte)var3);
                this.computerContents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", var2);
        
        par1NBTTagCompound.setBoolean("PowerStatus", this.powerStatus);
        par1NBTTagCompound.setBoolean("RuntimeError", this.runtimeError);
        
        if(vm != null)
        {
        	NBTTagCompound VMDataNBTTagCompound = new NBTTagCompound();
            VMDataHelper.writeToNBT(VMDataNBTTagCompound, vm);
            par1NBTTagCompound.setTag("VirtualMachineData", VMDataNBTTagCompound);
        }
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.computerContents = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.computerContents.length)
            {
                this.computerContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        
        this.powerStatus = par1NBTTagCompound.getBoolean("PowerStatus");
        this.runtimeError = par1NBTTagCompound.getBoolean("RuntimeError");
        
        if(par1NBTTagCompound.hasKey("VirtualMachineData"))
        {
            VirtualMachine VM = new VirtualMachine();
            synchronized(VM)
            {
                Object object[] = VMDataHelper.readFormNBT(par1NBTTagCompound.getCompoundTag("VirtualMachineData"), VM);
                vm = (VirtualMachine)object[0];
            
                if(vm.isRunning() == true)
                {
                    @SuppressWarnings("unchecked")
                    Map<Integer,AssemblyVirtualThread> avtList = (Map<Integer, AssemblyVirtualThread>)object[1];
                    {
                        Iterator<Entry<Integer, AssemblyVirtualThread>> iterator = avtList.entrySet().iterator();
                        while(iterator.hasNext())
                        {
                            AssemblyVirtualThread avt = iterator.next().getValue();
                            avt.loadThreadRelation();
                        }
                    }
                    @SuppressWarnings("unchecked")
                    Map<Integer, Monitor> monitorList = (Map<Integer, Monitor>)object[2];
                    {
                        Iterator<Entry<Integer, Monitor>> iterator = monitorList.entrySet().iterator();
                        while(iterator.hasNext())
                        {
                            Monitor monitor = iterator.next().getValue();
                            monitor.loadMonitorRelation();
                        }
                    }
                    Iterator<AssemblyVirtualThread> iterator = avtList.values().iterator();
                    while(iterator.hasNext())
                    {
                        AssemblyVirtualThread avt = iterator.next();
                        if(avt.isRunning() == true)
                        {
                            avt.load();
                        }
                    }
                    vmIsRunning = true;
                }
            }
        }
    }
    public void modifyComputer(EntityPlayer entityplayer)
    {
        FMLNetworkHandler.openGui(entityplayer, AssemblyProgramCraft.instance, AssemblyProgramCraftGuiHandler.COMPUTER_MODIFY_GUI_ID, worldObj, xCoord, yCoord, zCoord);
    }
    public int getSizeInventory()
    {
        return computerContents.length;
    }
    public ItemStack getStackInSlot(int var1)
    {
        return this.computerContents[var1];
    }
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.computerContents[var1] != null)
        {
            ItemStack var2 = this.computerContents[var1];
            this.computerContents[var1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.computerContents[var1] != null)
        {
            ItemStack var3;
    
            if (this.computerContents[var1].stackSize <= var2)
            {
                var3 = this.computerContents[var1];
                this.computerContents[var1] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.computerContents[var1].splitStack(var2);
    
                if (this.computerContents[var1].stackSize == 0)
                {
                    this.computerContents[var1] = null;
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
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.computerContents[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }
    public String getInventoryName()
    {
        return "computer_inv";
    }
    public int getInventoryStackLimit()
    {
        return 1;
    }
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }
    public void openInventory()
    {
        
    }
    public void closeInventory()
    {
        
    }
    public boolean hasCustomInventoryName()
    {
        return true;
    }
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }
    public void gamePaused()
    {
        if(vm != null)
        {
            vm.suspendVM();
        }
    }
    public void gameResume()
    {
        if(vm != null)
        {
            vm.resumeVM();
        }
    }
}