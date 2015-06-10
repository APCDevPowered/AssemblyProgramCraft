package yuxuanchiadm.apc.apc.common.item;

import java.util.ArrayList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.common.init.AssemblyProgramCraftBlocks;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDevice;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityVCPU32Computer;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.common.util.BlockHelper;
import yuxuanchiadm.apc.apc.common.util.ConnectorFinder;
import yuxuanchiadm.apc.apc.common.util.ConnectorFinder.WirePartInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlockVCPU32ComputerWire extends ItemBlock
{
    public ItemBlockVCPU32ComputerWire(Block block)
    {
        super(block);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.snow_layer)
        {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }
        if(worldIn.getBlockState(pos).getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_wire)
        {
            if (AssemblyProgramCraftBlocks.block_vcpu_32_computer_wire.canPlaceBlockOnSide(worldIn, pos, side))
            {
                return true;
            }
        }
        return worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity) null, stack);
    }
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block == Blocks.snow_layer && ((Integer)iblockstate.getValue(BlockSnow.LAYERS)).intValue() < 1)
        {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }

        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!playerIn.canPlayerEdit(pos, side, stack))
        {
            return false;
        }
        else if (pos.getY() == 255 && this.block.getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.getBlockState(pos).getBlock() == AssemblyProgramCraftBlocks.block_vcpu_32_computer_wire)
        {
            if(this.block.canPlaceBlockOnSide(worldIn, pos, side))
            {
                TileEntityVCPU32ComputerWire tileEntity = (TileEntityVCPU32ComputerWire)worldIn.getTileEntity(pos);
                EnumFacing addSide = side.getOpposite();
                if(tileEntity.hasSide(addSide))
                {
                    return false;
                }
                tileEntity.addSide(addSide);
                
                worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                --stack.stackSize;
                BlockHelper.updateIndirectNeighbors(worldIn, pos, this.block);
                
                if(!worldIn.isRemote)
                {
                    updataConnector(addSide, tileEntity);
                }
                
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack))
        {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);

            if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, iblockstate1))
            {
                TileEntityVCPU32ComputerWire tileEntity = ((TileEntityVCPU32ComputerWire)worldIn.getTileEntity(pos));
                EnumFacing addSide = side.getOpposite();
                tileEntity.addSide(addSide);
                
                worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                --stack.stackSize;
                
                BlockHelper.updateIndirectNeighbors(worldIn, pos, this.block);
                
                if(!worldIn.isRemote)
                {
                    updataConnector(addSide, tileEntity);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
    public static void updataConnector(TileEntityVCPU32ComputerConnector tileEntity)
    {
        updataConnector(tileEntity, tileEntity.getWorld().getBlockState(tileEntity.getPos()));
    }
    public static void updataConnector(TileEntityVCPU32ComputerConnector tileEntity, IBlockState state)
    {
        TileEntityVCPU32ComputerConnector[] connectorList = ConnectorFinder.findConnector(tileEntity, state);
        updataConnector(connectorList);
    }
    public static void updataConnector(EnumFacing side, TileEntityVCPU32ComputerWire tileEntity)
    {
        TileEntityVCPU32ComputerConnector[] connectorList = ConnectorFinder.findConnector(new WirePartInfo(side, tileEntity));
        updataConnector(connectorList);
    }
    private static void updataConnector(TileEntityVCPU32ComputerConnector[] connectorList)
    {
        ArrayList<TileEntityVCPU32Computer> computerList = new ArrayList<TileEntityVCPU32Computer>();
        ArrayList<TileEntityExternalDevice> externalDeviceList = new ArrayList<TileEntityExternalDevice>();
        System.out.println("[ConnectorLinkSystem] Searched " + connectorList.length + " Connector");
        for (TileEntityVCPU32ComputerConnector tileEntityVCPU32ComputerConnector : connectorList)
        {
            TileEntityVCPU32Computer tileEntityVCPU32Computer = tileEntityVCPU32ComputerConnector.hasComputer();
            if (tileEntityVCPU32Computer != null)
            {
                computerList.add(tileEntityVCPU32Computer);
                continue;
            }
            TileEntityExternalDevice iTileEntityExternalDevices = tileEntityVCPU32ComputerConnector.hasExternalDevices();
            if (iTileEntityExternalDevices != null)
            {
                externalDeviceList.add(iTileEntityExternalDevices);
                continue;
            }
        }
        try
        {
            for (TileEntityVCPU32Computer computer : computerList)
            {
                computer.recheckConnectedDevice(externalDeviceList);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}