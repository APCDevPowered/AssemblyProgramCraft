package yuxuanchiadm.apc.apc.item;

import java.util.ArrayList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.block.BlockVCPU32ComputerWire;
import yuxuanchiadm.apc.apc.common.util.ConnectorFinder;
import yuxuanchiadm.apc.apc.common.util.ConnectorFinder.WirePartInfo;
import yuxuanchiadm.apc.apc.tileEntity.ITileEntityExternalDevice;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32Computer;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerWire;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockVCPU32ComputerWire extends ItemBlock
{
    public ItemBlockVCPU32ComputerWire(Block block)
    {
        super(block);
    }
    @SideOnly(Side.CLIENT)
    public boolean func_150936_a(World p_150936_1_, int p_150936_2_, int p_150936_3_, int p_150936_4_, int p_150936_5_, EntityPlayer p_150936_6_, ItemStack p_150936_7_)
    {
        Block block = p_150936_1_.getBlock(p_150936_2_, p_150936_3_, p_150936_4_);
        if (block == Blocks.snow_layer)
        {
            p_150936_5_ = 1;
        }
        else if ((block != Blocks.vine) && (block != Blocks.tallgrass) && (block != Blocks.deadbush) && (!block.isReplaceable(p_150936_1_, p_150936_2_, p_150936_3_, p_150936_4_)))
        {
            if (p_150936_5_ == 0)
            {
                p_150936_3_--;
            }
            if (p_150936_5_ == 1)
            {
                p_150936_3_++;
            }
            if (p_150936_5_ == 2)
            {
                p_150936_4_--;
            }
            if (p_150936_5_ == 3)
            {
                p_150936_4_++;
            }
            if (p_150936_5_ == 4)
            {
                p_150936_2_--;
            }
            if (p_150936_5_ == 5)
            {
                p_150936_2_++;
            }
        }
        if (p_150936_1_.getBlock(p_150936_2_, p_150936_3_, p_150936_4_) == AssemblyProgramCraft.instance.block_wire)
        {
            if (AssemblyProgramCraft.instance.block_wire.canPlaceBlockOnSide(p_150936_1_, p_150936_2_, p_150936_3_, p_150936_4_, p_150936_5_))
            {
                return true;
            }
        }
        return p_150936_1_.canPlaceEntityOnSide(this.field_150939_a, p_150936_2_, p_150936_3_, p_150936_4_, false, p_150936_5_, (net.minecraft.entity.Entity) null, p_150936_7_);
    }
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        Block block = par3World.getBlock(par4, par5, par6);
        if ((block == Blocks.snow_layer) && ((par3World.getBlockMetadata(par4, par5, par6) & 0x7) < 1))
        {
            par7 = 1;
        }
        else if ((block != Blocks.vine) && (block != Blocks.tallgrass) && (block != Blocks.deadbush) && (!block.isReplaceable(par3World, par4, par5, par6)))
        {
            if (par7 == 0)
            {
                par5--;
            }
            if (par7 == 1)
            {
                par5++;
            }
            if (par7 == 2)
            {
                par6--;
            }
            if (par7 == 3)
            {
                par6++;
            }
            if (par7 == 4)
            {
                par4--;
            }
            if (par7 == 5)
            {
                par4++;
            }
        }
        if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        if ((par5 == 255) && (this.field_150939_a.getMaterial().isSolid()))
        {
            return false;
        }
        else if (par3World.getBlock(par4, par5, par6) == AssemblyProgramCraft.instance.block_wire)
        {
            if(this.field_150939_a.canPlaceBlockOnSide(par3World, par4, par5, par6, par7))
            {
                TileEntityVCPU32ComputerWire tileEntity = (TileEntityVCPU32ComputerWire)par3World.getTileEntity(par4, par5, par6);
                int addedSide = 0;
                if(par7 == 0)
                {
                    if(tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                    {
                        return false;
                    }
                    tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_TOP);
                    addedSide = TileEntityVCPU32ComputerWire.SIDE_TOP;
                }
                else if(par7 == 1)
                {
                    if(tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                    {
                        return false;
                    }
                    tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM);
                    addedSide = TileEntityVCPU32ComputerWire.SIDE_BOTTOM;
                }
                else if(par7 == 2)
                {
                    if(tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                    {
                        return false;
                    }
                    tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_LEFT);
                    addedSide = TileEntityVCPU32ComputerWire.SIDE_LEFT;
                }
                else if(par7 == 3)
                {
                    if(tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                    {
                        return false;
                    }
                    tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT);
                    addedSide = TileEntityVCPU32ComputerWire.SIDE_RIGHT;
                }
                else if(par7 == 4)
                {
                    if(tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                    {
                        return false;
                    }
                    tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_BACK);
                    addedSide = TileEntityVCPU32ComputerWire.SIDE_BACK;
                }
                else if(par7 == 5)
                {
                    if(tileEntity.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                    {
                        return false;
                    }
                    tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_FRONT);
                    addedSide = TileEntityVCPU32ComputerWire.SIDE_FRONT;
                }
                
                par3World.playSoundEffect(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                par1ItemStack.stackSize -= 1;
                BlockVCPU32ComputerWire.updateIndirectNeighbors(par3World, par4, par5, par6, this.field_150939_a);
                
                updataConnector(addedSide, tileEntity);
                
                return true;
            }
        }
        else
        {
            if (par3World.canPlaceEntityOnSide(this.field_150939_a, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
            {
                int i1 = getMetadata(par1ItemStack.getItemDamage());
                int j1 = this.field_150939_a.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, i1);
                if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, j1))
                {
                    TileEntityVCPU32ComputerWire tileEntity = ((TileEntityVCPU32ComputerWire)par3World.getTileEntity(par4, par5, par6));
                    int addedSide = 0;
                    if(par7 == 0)
                    {
                        tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_TOP);
                        addedSide = TileEntityVCPU32ComputerWire.SIDE_TOP;
                    }
                    else if(par7 == 1)
                    {
                        tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM);
                        addedSide = TileEntityVCPU32ComputerWire.SIDE_BOTTOM;
                    }
                    else if(par7 == 2)
                    {
                        tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_LEFT);
                        addedSide = TileEntityVCPU32ComputerWire.SIDE_LEFT;
                    }
                    else if(par7 == 3)
                    {
                        tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT);
                        addedSide = TileEntityVCPU32ComputerWire.SIDE_RIGHT;
                    }
                    else if(par7 == 4)
                    {
                        tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_BACK);
                        addedSide = TileEntityVCPU32ComputerWire.SIDE_BACK;
                    }
                    else if(par7 == 5)
                    {
                        tileEntity.addSide(TileEntityVCPU32ComputerWire.SIDE_FRONT);
                        addedSide = TileEntityVCPU32ComputerWire.SIDE_FRONT;
                    }
                    
                    par3World.playSoundEffect(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                    par1ItemStack.stackSize -= 1;
                    
                    BlockVCPU32ComputerWire.updateIndirectNeighbors(par3World, par4, par5, par6, this.field_150939_a);
                    
                    if(!par3World.isRemote)
                    {
                        updataConnector(addedSide, tileEntity);
                    }
                }
                return true;
            }
        }
        return false;
    }
    public static void updataConnector(TileEntityVCPU32ComputerConnector tileEntity)
    {
        TileEntityVCPU32ComputerConnector[] connectorList = ConnectorFinder.findConnector(tileEntity);
        updataConnector(connectorList);
    }
    public static void updataConnector(int WireSide, TileEntityVCPU32ComputerWire tileEntity)
    {
        TileEntityVCPU32ComputerConnector[] connectorList = ConnectorFinder.findConnector(new WirePartInfo(WireSide, tileEntity));
        updataConnector(connectorList);
    }
    private static void updataConnector(TileEntityVCPU32ComputerConnector[] connectorList)
    {
        ArrayList<TileEntityVCPU32Computer> computerList = new ArrayList<TileEntityVCPU32Computer>();
        ArrayList<ITileEntityExternalDevice> externalDeviceList = new ArrayList<ITileEntityExternalDevice>();
        System.out.println("[ConnectorLinkSystem] Searched " + connectorList.length + " Connector");
        for (TileEntityVCPU32ComputerConnector tileEntityVCPU32ComputerConnector : connectorList)
        {
            TileEntityVCPU32Computer tileEntityVCPU32Computer = tileEntityVCPU32ComputerConnector.hasComputer();
            if (tileEntityVCPU32Computer != null)
            {
                computerList.add(tileEntityVCPU32Computer);
                continue;
            }
            ITileEntityExternalDevice iTileEntityExternalDevices = tileEntityVCPU32ComputerConnector.hasExternalDevices();
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