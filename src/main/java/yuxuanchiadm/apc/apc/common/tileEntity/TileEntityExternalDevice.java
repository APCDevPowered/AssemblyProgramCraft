package yuxuanchiadm.apc.apc.common.tileEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import yuxuanchiadm.apc.apc.common.block.BlockVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.common.init.AssemblyProgramCraftBlocks;
import yuxuanchiadm.apc.apc.common.item.ItemBlockVCPU32ComputerWire;
import yuxuanchiadm.apc.vcpu32.vm.AbstractExternalDevice;

public abstract class TileEntityExternalDevice extends TileEntity implements IUpdatePlayerListBox
{
    private int port;
    private boolean isInIt = false;
    public ComputerPos lockedComputerPos = null;
    
    public boolean isConnectToConnector(World worldIn, BlockPos pos, IBlockState state)
    {
        return isConnectToConnector(worldIn, pos, state, getWorld().getBlockState(getPos()));
    }
    public boolean isConnectToConnector(World worldIn, BlockPos pos, IBlockState state, IBlockState thisState)
    {
        if (state.getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_connector)
        {
            for (EnumFacing face : getConnectorConnectableFaces(thisState))
            {
                EnumFacing connectorFace = (EnumFacing) state.getValue(BlockVCPU32ComputerConnector.FACING);
                if (face.equals(connectorFace))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public List<TileEntityVCPU32ComputerConnector> getAllConnectedConnector()
    {
        return getAllConnectedConnector(getWorld().getBlockState(getPos()));
    }
    public List<TileEntityVCPU32ComputerConnector> getAllConnectedConnector(IBlockState state)
    {
        List<TileEntityVCPU32ComputerConnector> connectors = new ArrayList<TileEntityVCPU32ComputerConnector>();
        ;
        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos pos = getPos().offset(face);
            if (isConnectToConnector(getWorld(), pos, getWorld().getBlockState(pos), state))
            {
                connectors.add((TileEntityVCPU32ComputerConnector) getWorld().getTileEntity(pos));
            }
        }
        return connectors;
    }
    public EnumFacing[] getConnectorConnectableFaces()
    {
        return getConnectorConnectableFaces(getWorld().getBlockState(getPos()));
    }
    public EnumFacing[] getConnectorConnectableFaces(IBlockState state)
    {
        return Arrays.copyOf(EnumFacing.VALUES, EnumFacing.VALUES.length);
    }
    public abstract AbstractExternalDevice getExternalDevice();
    @Override
    public void invalidate()
    {
        super.invalidate();
    }
    @Override
    public void update()
    {
        if (isInIt == false)
        {
            init();
            isInIt = true;
        }
        this.markDirty();
    }
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        port = par1NBTTagCompound.getInteger("port");
        if (par1NBTTagCompound.hasKey("lockedComputerPos"))
        {
            NBTTagCompound nbtTagCompound = par1NBTTagCompound.getCompoundTag("lockedComputerPos");
            lockedComputerPos = ComputerPos.readFromNBT(nbtTagCompound);
        }
        if (par1NBTTagCompound.hasKey("deviceData"))
        {
            NBTTagCompound nbtTagCompound = par1NBTTagCompound.getCompoundTag("deviceData");
            this.getExternalDevice().readFromNBT(nbtTagCompound);
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("port", port);
        if (lockedComputerPos != null)
        {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            lockedComputerPos.writeToNBT(nbtTagCompound);
            par1NBTTagCompound.setTag("lockedComputerPos", nbtTagCompound);
        }
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.getExternalDevice().writeToNBT(nbtTagCompound);
        par1NBTTagCompound.setTag("deviceData", nbtTagCompound);
    }
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setInteger("port", port);
        writeDescriptionNbt(nbtTagCompound);
        return new S35PacketUpdateTileEntity(getPos(), Block.getIdFromBlock(this.getBlockType()), nbtTagCompound);
    }
    public void writeDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
    }
    public void readDescriptionNbt(NBTTagCompound nbtTagCompound)
    {
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        port = pkt.getNbtCompound().getInteger("port");
        readDescriptionNbt(pkt.getNbtCompound());
    }
    public void init()
    {
        updataConnector(getWorld().getBlockState(getPos()));
    }
    public void updataConnector(IBlockState state)
    {
        if (getWorld().isRemote)
        {
            return;
        }
        for (TileEntityVCPU32ComputerConnector connector : getAllConnectedConnector(state))
        {
            ItemBlockVCPU32ComputerWire.updataConnector(connector);
        }
    }
    public int getPort()
    {
        return port;
    }
    public void setPort(int port)
    {
        if (port != this.port)
        {
            this.lockedComputerPos = null;
            this.port = port;
            updataConnector(getWorld().getBlockState(getPos()));
        }
    }
    
    public static class ComputerPos
    {
        public BlockPos pos;
        public int dimension;
        
        public boolean is(TileEntityVCPU32Computer computer)
        {
            if (computer.getPos().equals(pos) && computer.getWorld().provider.getDimensionId() == dimension)
            {
                return true;
            }
            return false;
        }
        public static ComputerPos form(TileEntityVCPU32Computer computer)
        {
            ComputerPos computerPos = new ComputerPos();
            computerPos.pos = computer.getPos();
            computerPos.dimension = computer.getWorld().provider.getDimensionId();
            return computerPos;
        }
        public static ComputerPos readFromNBT(NBTTagCompound par1NBTTagCompound)
        {
            ComputerPos computerPos = new ComputerPos();
            computerPos.pos = BlockPos.fromLong(par1NBTTagCompound.getLong("pos"));
            computerPos.dimension = par1NBTTagCompound.getInteger("dimension");
            return computerPos;
        }
        public void writeToNBT(NBTTagCompound par1NBTTagCompound)
        {
            par1NBTTagCompound.setLong("pos", pos.toLong());
            par1NBTTagCompound.setInteger("dimension", dimension);
        }
    }
}