package yuxuanchiadm.apc.apc.client.renderer.tileentity;

import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.block.BlockVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityVCPU32ComputerWire;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderTileEntityVCPU32ComputerWire extends TileEntitySpecialRenderer
{
    private static final ResourceLocation wireTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/wire.png");
    
    /* 拐角渲染规则。
     * 用途：防止渲染重复。
     * 一.外侧：
     *     1.顶部、底部接中间时底部、顶部负责渲染数据线侧面拐角。
     *     2.中间接中间时，右边渲染左边数据线拐角。
     * 二.内侧：
     *     1.顶部、底部接中间时底部、顶部负责渲染数据线侧面拐角。
     *     2.中间接中间时，右边渲染左边数据线拐角。
     */
    /* 渲染方法：
     * 1.每个方块渲染时，按单独数据线为单元渲染。
     * 2.渲染时先渲染必要中间部分。
     * 3.其次判断底部是否有数据接口连接，有则跳转到第5步。
     * 4.其次判断是否有其他数据线连接，如果没有则底部和顶部沿X轴渲染成长方形，中间部分沿Y轴渲染成长方形，渲染结束。
     * 5.其次再单独判断某侧是否有数据线连接，如果是底部则包括数据接口，并且在判断数据接口朝向是否正确。
     * 6.如果没有则判断是否只有对面有数据线连接，若果是，渲染成长方形，否则渲染胶皮。
     * 7.如果有数据线连接，平行则直接连接，否则按拐角渲染规则渲染。
     * 8.渲染下个数据线。
     */
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        TileEntityVCPU32ComputerWire tileEntityWire = (TileEntityVCPU32ComputerWire)tileentity;
        this.bindTexture(wireTextures);
        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        
        GlStateManager.translate(x, y, z);
        
        if(tileEntityWire.hasSide(EnumFacing.UP))
        {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(180F, 1, 0, 0);
            GlStateManager.translate(0, -1, -1);
            
            getSideStatus(tileEntityWire, tessellator, worldRenderer, EnumFacing.UP);
            
            GlStateManager.popMatrix();
        }
        if(tileEntityWire.hasSide(EnumFacing.DOWN))
        {
            getSideStatus(tileEntityWire, tessellator, worldRenderer, EnumFacing.DOWN);
        }
        if(tileEntityWire.hasSide(EnumFacing.WEST))
        {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(270F, 0, 0, 1);
            GlStateManager.rotate(180F, 0, 1, 0);
            GlStateManager.translate(0, 0, -1);
            
            getSideStatus(tileEntityWire, tessellator, worldRenderer, EnumFacing.WEST);
            
            GlStateManager.popMatrix();
        }
        if(tileEntityWire.hasSide(EnumFacing.EAST))
        {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90F, 0, 0, 1);
            GlStateManager.translate(0, -1, 0);
            
            getSideStatus(tileEntityWire, tessellator, worldRenderer, EnumFacing.EAST);
            
            GlStateManager.popMatrix();
        }
        if(tileEntityWire.hasSide(EnumFacing.SOUTH))
        {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(270F, 1, 0, 0);
            GlStateManager.rotate(90F, 0, -1, 0);
            GlStateManager.translate(0, -1, -1);
            
            getSideStatus(tileEntityWire, tessellator, worldRenderer, EnumFacing.SOUTH);
            
            GlStateManager.popMatrix();
        }
        if(tileEntityWire.hasSide(EnumFacing.NORTH))
        {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90F, 1, 0, 0);
            GlStateManager.rotate(90F, 0, 1, 0);
            
            getSideStatus(tileEntityWire, tessellator, worldRenderer, EnumFacing.NORTH);
            
            GlStateManager.popMatrix();
        }
        
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
    public void getSideStatus(TileEntityVCPU32ComputerWire tileEntityWire, Tessellator tessellator, WorldRenderer worldRenderer, EnumFacing side)
    {
        int front = 0;
        int back = 0;
        int left = 0;
        int right = 0;
        int overlappingNotRenderSide = 0;
        /* 
         * 0 bit:front
         * 1 bit:back
         * 2 bit:left
         * 3 bit:right
         */
        //[start]
        if(side == EnumFacing.UP)
        {
            overlappingNotRenderSide = 0;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(-1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, 1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            left = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, -1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, -1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            right = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == EnumFacing.DOWN)
        {
            overlappingNotRenderSide = 0;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                {
                    front = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if (((EnumFacing) tileEntityWire.getWorld().getBlockState(neighborsTileEntityConnector.getPos()).getValue(BlockVCPU32ComputerConnector.FACING)).equals(EnumFacing.WEST))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, -1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                {
                    back = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if (((EnumFacing) tileEntityWire.getWorld().getBlockState(neighborsTileEntityConnector.getPos()).getValue(BlockVCPU32ComputerConnector.FACING)).equals(EnumFacing.EAST))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(-1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, -1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                {
                    right = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if (((EnumFacing) tileEntityWire.getWorld().getBlockState(neighborsTileEntityConnector.getPos()).getValue(BlockVCPU32ComputerConnector.FACING)).equals(EnumFacing.NORTH))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, 1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, -1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                {
                    left = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if (((EnumFacing) tileEntityWire.getWorld().getBlockState(neighborsTileEntityConnector.getPos()).getValue(BlockVCPU32ComputerConnector.FACING)).equals(EnumFacing.SOUTH))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, -1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == EnumFacing.WEST)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, -1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, -1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, -1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, -1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, 1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == EnumFacing.EAST)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, -1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, -1, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, 1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, -1));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 0, -1)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == EnumFacing.SOUTH)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, -1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(-1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.SOUTH))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, 1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == EnumFacing.NORTH)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, 1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 1, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(0, -1, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, -1, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.UP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.DOWN))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(1, 0, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, 0));
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.hasSide(EnumFacing.NORTH))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorld().isAirBlock(tileEntityWire.getPos().add(-1, 0, 0)))
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(-1, 0, -1));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.EAST))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorld().getTileEntity(tileEntityWire.getPos().add(0, 0, 0));
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.hasSide(EnumFacing.WEST))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        renderSide(left, right, front, back, tessellator, worldRenderer, overlappingNotRenderSide);
    }
    public void renderSide(int left, int right, int front, int back, Tessellator tessellator, WorldRenderer worldRenderer, int overlappingNotRenderSide)
    {
        worldRenderer.startDrawingQuads();
        //Center
        worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.0D);
        worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.25D);
        worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.25D);
        worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.0D);
        
        worldRenderer.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
        worldRenderer.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
        worldRenderer.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
        worldRenderer.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
        
        if(left == 0 && right == 0 && front == 0 && back == 0)
        {
            worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
            worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
            worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
            worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
            
            worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
            worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
            worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
            worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
            
            worldRenderer.addVertexWithUV(0, 0, 0, 0.75D, 0.0D);
            worldRenderer.addVertexWithUV(1, 0, 0, 0.5D, 0.0D);
            worldRenderer.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
            worldRenderer.addVertexWithUV(0, 0, 1, 0.75D, 0.25D);
            
            worldRenderer.addVertexWithUV(0, 0, 0, 0.5D, 0.25D);
            worldRenderer.addVertexWithUV(1, 0, 0, 0.75D, 0.25D);
            worldRenderer.addVertexWithUV(1, 0, 1, 0.75D, 0.0D);
            worldRenderer.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
            
            worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.0D, 0.5D);
            worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.25D, 0.5D);
            worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.25D, 0.25D);
            worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.0D, 0.25D);
            
            worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.0D, 0.5D);
            worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.25D, 0.5D);
            worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.25D, 0.25D);
            worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.0D, 0.25D);
            
            worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.0D, 0.5D);
            worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.0D, 0.25D);
            worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.25D, 0.25D);
            worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.25D, 0.5D);
            
            worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.0D, 0.5D);
            worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.0D, 0.25D);
            worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.25D, 0.25D);
            worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.25D, 0.5D);
            
            worldRenderer.addVertexWithUV((12.0D/16.0D), 1, 1, 0.25D, 0.25D);
            worldRenderer.addVertexWithUV((12.0D/16.0D), 0, 1, 0.25D, 0.5D);
            worldRenderer.addVertexWithUV((12.0D/16.0D), 0, 0, 0.5D, 0.5D);
            worldRenderer.addVertexWithUV((12.0D/16.0D), 1, 0, 0.5D, 0.25D);
            
            worldRenderer.addVertexWithUV((4.0D/16.0D), 1, 1, 0.25D, 0.25D);
            worldRenderer.addVertexWithUV((4.0D/16.0D), 1, 0, 0.5D, 0.25D);
            worldRenderer.addVertexWithUV((4.0D/16.0D), 0, 0, 0.5D, 0.5D);
            worldRenderer.addVertexWithUV((4.0D/16.0D), 0, 1, 0.25D, 0.5D);
            
            worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.5D, 0.5D);
            worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.5D, 0.25D);
            worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.25D);
            worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.5D);
            
            worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.5D, 0.5D);
            worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.5D);
            worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.25D);
            worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.5D, 0.25D);
        }
        else
        {
            if(front == 0)
            {
                if(left == 0 && right == 0 && back != 0)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.75D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.0D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.0D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.25D, 0.5D);
                    
                    worldRenderer.addVertexWithUV((12.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV((12.0D/16.0D), 0, 1, 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV((12.0D/16.0D), 0, 0, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV((12.0D/16.0D), 1, 0, 0.5D, 0.25D);
                }
                else
                {
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.25D);
                }
            }
            else
            {
                if(front == 1)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 1.0D, 0.25D);
                }
                else if(front == 2)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    
                    if((overlappingNotRenderSide & 1) == 0)
                    {
                        worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.5D);
                        worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV(2, 1, (5.0D/16.0D), 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV(2, 0, (5.0D/16.0D), 1.0D, 0.5D);
                        
                        worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.5D);
                        worldRenderer.addVertexWithUV(2, 0, (11.0D/16.0D), 1.0D, 0.5D);
                        worldRenderer.addVertexWithUV(2, 1, (11.0D/16.0D), 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.25D);
                    }
                    
                    worldRenderer.addVertexWithUV(2, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(2, (4.0D/16.0D), 1, 0.5D, 0.25D);
                }
                else if(front == 3)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    if((overlappingNotRenderSide & 1) == 0)
                    {
                        worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 1.0D, 0.0D);
                        worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.0D);
                        
                        worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.0D);
                        worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 1.0D, 0.0D);
                        worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    }
                    else
                    {
                        worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.0D, 0.5D);
                        worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.25D, 0.5D);
                        worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.25D, 0.25D);
                        worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.0D, 0.25D);

                        worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.0D, 0.5D);
                        worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.0D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.25D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.25D, 0.5D);
                    }

                    worldRenderer.addVertexWithUV(1, 1, 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 1, 0, 0.75D, 0.25D);
                }
            }
            if(back == 0)
            {
                if(left == 0 && right == 0 && front != 0)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.0D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.25D, 0.5D);
                    
                    worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.0D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV((4.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV((4.0D/16.0D), 1, 0, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV((4.0D/16.0D), 0, 0, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV((4.0D/16.0D), 0, 1, 0.25D, 0.5D);
                }
                else
                {
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.5D, 0.5D);
                }
            }
            else
            {
                if(back == 1)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.0D);
                }
                else if(back == 2)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.0D);
                    
                    if((overlappingNotRenderSide & 2) == 0)
                    {
                        worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.5D);
                        worldRenderer.addVertexWithUV(-1, 0, (5.0D/16.0D), 1.0D, 0.5D);
                        worldRenderer.addVertexWithUV(-1, 1, (5.0D/16.0D), 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.25D);
                        
                        worldRenderer.addVertexWithUV(-1, 1, (11.0D/16.0D), 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV(-1, 0, (11.0D/16.0D), 1.0D, 0.5D);
                        worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.5D);
                        worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.25D);
                    }
                    
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(-1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(-1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.5D);
                }
                else if(back == 3)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    if((overlappingNotRenderSide & 2) == 0)
                    {
                        worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 1.0D, 0.0D);
                        worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.0D);
                        
                        worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 1.0D, 0.0D);
                        worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.0D);
                        worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    }
                    else
                    {
                        worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.0D, 0.5D);
                        worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.25D, 0.5D);
                        worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.25D, 0.25D);
                        worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.0D, 0.25D);

                        worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.0D, 0.5D);
                        worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.0D, 0.25D);
                        worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.25D, 0.25D);
                        worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.25D, 0.5D); 
                    }

                    worldRenderer.addVertexWithUV(0, 1, 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 1, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.5D, 0.5D);
                }
            }
            if(left == 0)
            {
                if(front == 0 && back == 0 && right != 0)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.75D, 0.25D);
                    
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.0D, 0.5D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.25D, 0.5D);
                    
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.0D, 0.5D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(1, 1, (4.0D/16.0D), 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, (4.0D/16.0D), 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 0, (4.0D/16.0D), 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 1, (4.0D/16.0D), 0.5D, 0.25D);
                }
                else
                {
                    worldRenderer.addVertexWithUV(1, 1, (5.0D/16.0D), 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, (5.0D/16.0D), 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.25D);
                }
            }
            else
            {
                if(left == 1)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 1.0D, 0.25D);
                }
                else if(left == 2)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 1.0D, 0.25D);
                    
                    if((overlappingNotRenderSide & 4) == 0)
                    {
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.5D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, -1, 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, -1, 1.0D, 0.5D);
                        
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, -1, 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.5D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, -1, 1.0D, 0.5D);
                    }
                    
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), -1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), -1, 0.5D, 0.25D);
                }
                else if(left == 3)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    if((overlappingNotRenderSide & 4) == 0)
                    {
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 1.0D, 0.0D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.0D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 1.0D, 0.25D);
                        
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 1.0D, 0.0D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    }

                    worldRenderer.addVertexWithUV(1, 1, 0, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(0, 1, 0, 0.75D, 0.25D);
                }
            }
            if(right == 0)
            {
                if(front == 0 && back == 0 && left != 0)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
                    
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.0D, 0.5D);
                    
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.0D, 0.5D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.25D, 0.25D);
                    
                    worldRenderer.addVertexWithUV(0, 1, (12.0D/16.0D), 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, (12.0D/16.0D), 0.25D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 0, (12.0D/16.0D), 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 1, (12.0D/16.0D), 0.5D, 0.25D);
                }
                else
                {
                    worldRenderer.addVertexWithUV(0, 1, (11.0D/16.0D), 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, (11.0D/16.0D), 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.25D);
                }
            }
            else
            {
                if(right == 1)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.0D);
                }
                else if(right == 2)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.0D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.0D);
                    
                    if((overlappingNotRenderSide & 8) == 0)
                    {
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 2, 1.0D, 0.5D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 2, 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.5D);
                        
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 2, 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 2, 1.0D, 0.5D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.5D);
                    }
                    
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 2, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 2, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.5D);
                }
                else if(right == 3)
                {
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    worldRenderer.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    worldRenderer.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    worldRenderer.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    worldRenderer.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    if((overlappingNotRenderSide & 8) == 0)
                    {
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.0D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 1.0D, 0.0D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.25D);
                        
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.0D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 1.0D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    }
                    else
                    {
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 1, 0.0D, 0.250D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 1, 0, 0.25D, 0.25D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 0, 0.25D, 0.5D);
                        worldRenderer.addVertexWithUV((5.0D/16.0D), 0, 1, 0.0D, 0.5D);
                        
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 1, 0.0D, 0.25D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 1, 0.0D, 0.5D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 0, 0, 0.25D, 0.5D);
                        worldRenderer.addVertexWithUV((11.0D/16.0D), 1, 0, 0.25D, 0.25D);
                    }

                    worldRenderer.addVertexWithUV(0, 1, 1, 0.5D, 0.25D);
                    worldRenderer.addVertexWithUV(0, 0, 1, 0.5D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 0, 1, 0.75D, 0.5D);
                    worldRenderer.addVertexWithUV(1, 1, 1, 0.75D, 0.25D);
                }
            }
        }
        tessellator.draw();
    }
}