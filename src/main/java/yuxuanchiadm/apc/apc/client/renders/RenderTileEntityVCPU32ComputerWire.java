package yuxuanchiadm.apc.apc.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerWire;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@SideOnly(Side.CLIENT)
public class RenderTileEntityVCPU32ComputerWire extends TileEntitySpecialRenderer
{
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
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partial_tick_time)
    {
        TileEntityVCPU32ComputerWire tileEntityWire = (TileEntityVCPU32ComputerWire)tileentity;
        Tessellator tessellator = Tessellator.instance;
        this.bindTexture(AssemblyProgramCraftProxyClient.WIRE_PNG_PATH);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPushMatrix();
        
        GL11.glTranslated(x, y, z);
        
        if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
        {
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 1, 0, 0);
            GL11.glTranslated(0, -1, -1);
            
            getSideStatus(tileEntityWire, tessellator, TileEntityVCPU32ComputerWire.SIDE_TOP);
            
            GL11.glPopMatrix();
        }
        if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
        {
            getSideStatus(tileEntityWire, tessellator, TileEntityVCPU32ComputerWire.SIDE_BOTTOM);
        }
        if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
        {
            GL11.glPushMatrix();
            GL11.glRotatef(270F, 0, 0, 1);
            GL11.glRotatef(180F, 0, 1, 0);
            GL11.glTranslated(0, 0, -1);
            
            getSideStatus(tileEntityWire, tessellator, TileEntityVCPU32ComputerWire.SIDE_FRONT);
            
            GL11.glPopMatrix();
        }
        if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
        {
            GL11.glPushMatrix();
            GL11.glRotatef(90F, 0, 0, 1);
            GL11.glTranslated(0, -1, 0);
            
            getSideStatus(tileEntityWire, tessellator, TileEntityVCPU32ComputerWire.SIDE_BACK);
            
            GL11.glPopMatrix();
        }
        if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
        {
            GL11.glPushMatrix();
            GL11.glRotatef(270F, 1, 0, 0);
            GL11.glRotatef(90F, 0, -1, 0);
            GL11.glTranslated(0, -1, -1);
            
            getSideStatus(tileEntityWire, tessellator, TileEntityVCPU32ComputerWire.SIDE_LEFT);
            
            GL11.glPopMatrix();
        }
        if(tileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
        {
            GL11.glPushMatrix();
            GL11.glRotatef(90F, 1, 0, 0);
            GL11.glRotatef(90F, 0, 1, 0);
            
            getSideStatus(tileEntityWire, tessellator, TileEntityVCPU32ComputerWire.SIDE_RIGHT);
            
            GL11.glPopMatrix();
        }
        
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    public void getSideStatus(TileEntityVCPU32ComputerWire tileEntityWire, Tessellator tessellator, int side)
    {
        int front = 0;
        int back = 0;
        int left = 0;
        int right = 0;
        int overlappingNotRenderSide = 0;
        /* 
         * 0位:front
         * 1位:back
         * 2位:left
         * 3位:right
         */
        //[start]
        if(side == TileEntityVCPU32ComputerWire.SIDE_TOP)
        {
            overlappingNotRenderSide = 0;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            left = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            right = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == TileEntityVCPU32ComputerWire.SIDE_BOTTOM)
        {
            overlappingNotRenderSide = 0;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                {
                    front = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if(neighborsTileEntityConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_BACK)
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                {
                    back = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if(neighborsTileEntityConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_FRONT)
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                {
                    right = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if(neighborsTileEntityConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_LEFT)
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                {
                    left = 1;
                }
            }
            else if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerConnector)
            {
                TileEntityVCPU32ComputerConnector neighborsTileEntityConnector = (TileEntityVCPU32ComputerConnector)neighborsTileEntity;
                if(neighborsTileEntityConnector.getFace() == TileEntityVCPU32ComputerConnector.FACE_RIGHT)
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == TileEntityVCPU32ComputerWire.SIDE_FRONT)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == TileEntityVCPU32ComputerWire.SIDE_BACK)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord + 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord - 1))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == TileEntityVCPU32ComputerWire.SIDE_LEFT)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_LEFT))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord + 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        //[start]
        if(side == TileEntityVCPU32ComputerWire.SIDE_RIGHT)
        {
            overlappingNotRenderSide = 11;
            TileEntity neighborsTileEntity = null;
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                {
                    front = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord + 1, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            front = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            front = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                {
                    back = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord - 1, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_TOP))
                        {
                            back = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BOTTOM))
                        {
                            back = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                {
                    right = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord + 1, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            right = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            right = 3;
                        }
                    }
                }
            }
            neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord);
            if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
            {
                TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_RIGHT))
                {
                    left = 1;
                }
            }
            else
            {
                if(tileEntityWire.getWorldObj().isAirBlock(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord))
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord - 1, tileEntityWire.yCoord, tileEntityWire.zCoord - 1);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_BACK))
                        {
                            left = 2;
                        }
                    }
                }
                else
                {
                    neighborsTileEntity = tileEntityWire.getWorldObj().getTileEntity(tileEntityWire.xCoord, tileEntityWire.yCoord, tileEntityWire.zCoord);
                    if(neighborsTileEntity != null && neighborsTileEntity instanceof TileEntityVCPU32ComputerWire)
                    {
                        TileEntityVCPU32ComputerWire neighborsTileEntityWire = (TileEntityVCPU32ComputerWire)neighborsTileEntity;
                        if(neighborsTileEntityWire.haveSide(TileEntityVCPU32ComputerWire.SIDE_FRONT))
                        {
                            left = 3;
                        }
                    }
                }
            }
        }
        //[end]
        renderSide(left, right, front, back, tessellator, overlappingNotRenderSide);
    }
    public void renderSide(int left, int right, int front, int back, Tessellator tessellator, int overlappingNotRenderSide)
    {
        tessellator.startDrawingQuads();
        //中心
        tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.0D);
        tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.25D);
        tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.0D);
        
        tessellator.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
        tessellator.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
        tessellator.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
        tessellator.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
        
        if(left == 0 && right == 0 && front == 0 && back == 0)
        {
            tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
            tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
            tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
            tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
            
            tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
            tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
            tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
            tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
            
            tessellator.addVertexWithUV(0, 0, 0, 0.75D, 0.0D);
            tessellator.addVertexWithUV(1, 0, 0, 0.5D, 0.0D);
            tessellator.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
            tessellator.addVertexWithUV(0, 0, 1, 0.75D, 0.25D);
            
            tessellator.addVertexWithUV(0, 0, 0, 0.5D, 0.25D);
            tessellator.addVertexWithUV(1, 0, 0, 0.75D, 0.25D);
            tessellator.addVertexWithUV(1, 0, 1, 0.75D, 0.0D);
            tessellator.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
            
            tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.0D, 0.5D);
            tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.25D, 0.5D);
            tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.25D, 0.25D);
            tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.0D, 0.25D);
            
            tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.0D, 0.5D);
            tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.25D, 0.5D);
            tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.25D, 0.25D);
            tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.0D, 0.25D);
            
            tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.0D, 0.5D);
            tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.0D, 0.25D);
            tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.25D, 0.25D);
            tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.25D, 0.5D);
            
            tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.0D, 0.5D);
            tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.0D, 0.25D);
            tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.25D, 0.25D);
            tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.25D, 0.5D);
            
            tessellator.addVertexWithUV((12.0D/16.0D), 1, 1, 0.25D, 0.25D);
            tessellator.addVertexWithUV((12.0D/16.0D), 0, 1, 0.25D, 0.5D);
            tessellator.addVertexWithUV((12.0D/16.0D), 0, 0, 0.5D, 0.5D);
            tessellator.addVertexWithUV((12.0D/16.0D), 1, 0, 0.5D, 0.25D);
            
            tessellator.addVertexWithUV((4.0D/16.0D), 1, 1, 0.25D, 0.25D);
            tessellator.addVertexWithUV((4.0D/16.0D), 1, 0, 0.5D, 0.25D);
            tessellator.addVertexWithUV((4.0D/16.0D), 0, 0, 0.5D, 0.5D);
            tessellator.addVertexWithUV((4.0D/16.0D), 0, 1, 0.25D, 0.5D);
            
            tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.5D, 0.5D);
            tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.5D, 0.25D);
            tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.25D);
            tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.5D);
            
            tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.5D, 0.5D);
            tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.5D);
            tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.25D);
            tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.5D, 0.25D);
        }
        else
        {
            if(front == 0)
            {
                if(left == 0 && right == 0 && back != 0)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.75D, 0.25D);
                    
                    tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.0D, 0.5D);
                    tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.25D, 0.5D);
                    tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.0D, 0.5D);
                    tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.25D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.25D, 0.5D);
                    
                    tessellator.addVertexWithUV((12.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV((12.0D/16.0D), 0, 1, 0.25D, 0.5D);
                    tessellator.addVertexWithUV((12.0D/16.0D), 0, 0, 0.5D, 0.5D);
                    tessellator.addVertexWithUV((12.0D/16.0D), 1, 0, 0.5D, 0.25D);
                }
                else
                {
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.5D, 0.5D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.5D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.25D);
                }
            }
            else
            {
                if(front == 1)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.0D);
                    tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 1.0D, 0.25D);
                }
                else if(front == 2)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.0D);
                    tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    
                    if((overlappingNotRenderSide & 1) == 0)
                    {
                        tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.5D);
                        tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.25D);
                        tessellator.addVertexWithUV(2, 1, (5.0D/16.0D), 1.0D, 0.25D);
                        tessellator.addVertexWithUV(2, 0, (5.0D/16.0D), 1.0D, 0.5D);
                        
                        tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.5D);
                        tessellator.addVertexWithUV(2, 0, (11.0D/16.0D), 1.0D, 0.5D);
                        tessellator.addVertexWithUV(2, 1, (11.0D/16.0D), 1.0D, 0.25D);
                        tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.25D);
                    }
                    
                    tessellator.addVertexWithUV(2, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.5D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.5D);
                    tessellator.addVertexWithUV(2, (4.0D/16.0D), 1, 0.5D, 0.25D);
                }
                else if(front == 3)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    if((overlappingNotRenderSide & 1) == 0)
                    {
                        tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.75D, 0.25D);
                        tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 1.0D, 0.25D);
                        tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 1.0D, 0.0D);
                        tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.75D, 0.0D);
                        
                        tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.25D);
                        tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.0D);
                        tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 1.0D, 0.0D);
                        tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    }
                    else
                    {
                        tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.0D, 0.5D);
                        tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.25D, 0.5D);
                        tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.25D, 0.25D);
                        tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.0D, 0.25D);

                        tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.0D, 0.5D);
                        tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.0D, 0.25D);
                        tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.25D, 0.25D);
                        tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.25D, 0.5D);
                    }

                    tessellator.addVertexWithUV(1, 1, 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.5D, 0.5D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.75D, 0.5D);
                    tessellator.addVertexWithUV(1, 1, 0, 0.75D, 0.25D);
                }
            }
            if(back == 0)
            {
                if(left == 0 && right == 0 && front != 0)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.0D, 0.5D);
                    tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.25D, 0.5D);
                    
                    tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.0D, 0.5D);
                    tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.25D, 0.5D);
                    tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.25D, 0.25D);
                    tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.0D, 0.25D);
                    
                    tessellator.addVertexWithUV((4.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV((4.0D/16.0D), 1, 0, 0.5D, 0.25D);
                    tessellator.addVertexWithUV((4.0D/16.0D), 0, 0, 0.5D, 0.5D);
                    tessellator.addVertexWithUV((4.0D/16.0D), 0, 1, 0.25D, 0.5D);
                }
                else
                {
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.5D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.5D, 0.5D);
                }
            }
            else
            {
                if(back == 1)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.0D);
                }
                else if(back == 2)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 1.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 1.0D, 0.0D);
                    tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.0D);
                    
                    if((overlappingNotRenderSide & 2) == 0)
                    {
                        tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.5D);
                        tessellator.addVertexWithUV(-1, 0, (5.0D/16.0D), 1.0D, 0.5D);
                        tessellator.addVertexWithUV(-1, 1, (5.0D/16.0D), 1.0D, 0.25D);
                        tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.25D);
                        
                        tessellator.addVertexWithUV(-1, 1, (11.0D/16.0D), 1.0D, 0.25D);
                        tessellator.addVertexWithUV(-1, 0, (11.0D/16.0D), 1.0D, 0.5D);
                        tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.5D);
                        tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.25D);
                    }
                    
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.5D);
                    tessellator.addVertexWithUV(-1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(-1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.5D);
                }
                else if(back == 3)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    if((overlappingNotRenderSide & 2) == 0)
                    {
                        tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 1.0D, 0.0D);
                        tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 1.0D, 0.25D);
                        tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.25D);
                        tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.0D);
                        
                        tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 1.0D, 0.0D);
                        tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.75D, 0.0D);
                        tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.75D, 0.25D);
                        tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 1.0D, 0.25D);
                    }
                    else
                    {
                        tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.0D, 0.5D);
                        tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.25D, 0.5D);
                        tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.25D, 0.25D);
                        tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.0D, 0.25D);

                        tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.0D, 0.5D);
                        tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.0D, 0.25D);
                        tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.25D, 0.25D);
                        tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.25D, 0.5D); 
                    }

                    tessellator.addVertexWithUV(0, 1, 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(0, 1, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 0, 0.75D, 0.5D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.5D, 0.5D);
                }
            }
            if(left == 0)
            {
                if(front == 0 && back == 0 && right != 0)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.75D, 0.25D);
                    
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.0D, 0.5D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.0D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.25D, 0.5D);
                    
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.0D, 0.5D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.25D, 0.5D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(1, 1, (4.0D/16.0D), 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, (4.0D/16.0D), 0.25D, 0.5D);
                    tessellator.addVertexWithUV(0, 0, (4.0D/16.0D), 0.5D, 0.5D);
                    tessellator.addVertexWithUV(0, 1, (4.0D/16.0D), 0.5D, 0.25D);
                }
                else
                {
                    tessellator.addVertexWithUV(1, 1, (5.0D/16.0D), 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, (5.0D/16.0D), 0.5D, 0.5D);
                    tessellator.addVertexWithUV(0, 0, (5.0D/16.0D), 0.75D, 0.5D);
                    tessellator.addVertexWithUV(0, 1, (5.0D/16.0D), 0.75D, 0.25D);
                }
            }
            else
            {
                if(left == 1)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 1.0D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 1.0D, 0.25D);
                }
                else if(left == 2)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.0D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 1.0D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 1.0D, 0.25D);
                    
                    if((overlappingNotRenderSide & 4) == 0)
                    {
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.5D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.25D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, -1, 1.0D, 0.25D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, -1, 1.0D, 0.5D);
                        
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, -1, 1.0D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.5D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, -1, 1.0D, 0.5D);
                    }
                    
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.5D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.5D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), -1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), -1, 0.5D, 0.25D);
                }
                else if(left == 3)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.5D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.25D, 0.25D);
                    
                    if((overlappingNotRenderSide & 4) == 0)
                    {
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 1.0D, 0.0D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.75D, 0.0D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.75D, 0.25D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 1.0D, 0.25D);
                        
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 1.0D, 0.0D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 1.0D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.75D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.75D, 0.0D);
                    }

                    tessellator.addVertexWithUV(1, 1, 0, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.5D, 0.5D);
                    tessellator.addVertexWithUV(0, 0, 0, 0.75D, 0.5D);
                    tessellator.addVertexWithUV(0, 1, 0, 0.75D, 0.25D);
                }
            }
            if(right == 0)
            {
                if(front == 0 && back == 0 && left != 0)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.75D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.5D, 0.0D);
                    
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.25D, 0.5D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.0D, 0.5D);
                    
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.25D, 0.5D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.0D, 0.5D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.25D, 0.25D);
                    
                    tessellator.addVertexWithUV(0, 1, (12.0D/16.0D), 0.25D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, (12.0D/16.0D), 0.25D, 0.5D);
                    tessellator.addVertexWithUV(1, 0, (12.0D/16.0D), 0.5D, 0.5D);
                    tessellator.addVertexWithUV(1, 1, (12.0D/16.0D), 0.5D, 0.25D);
                }
                else
                {
                    tessellator.addVertexWithUV(0, 1, (11.0D/16.0D), 0.5D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, (11.0D/16.0D), 0.5D, 0.5D);
                    tessellator.addVertexWithUV(1, 0, (11.0D/16.0D), 0.75D, 0.5D);
                    tessellator.addVertexWithUV(1, 1, (11.0D/16.0D), 0.75D, 0.25D);
                }
            }
            else
            {
                if(right == 1)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.0D);
                }
                else if(right == 2)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.25D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.0D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 1.0D, 0.25D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.0D);
                    
                    if((overlappingNotRenderSide & 8) == 0)
                    {
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 2, 1.0D, 0.5D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 2, 1.0D, 0.25D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.25D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.5D);
                        
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 2, 1.0D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 2, 1.0D, 0.5D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.5D);
                    }
                    
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 2, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 2, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.5D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.75D, 0.5D);
                }
                else if(right == 3)
                {
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 0, 0.75D, 0.25D);
                    tessellator.addVertexWithUV(0, (4.0D/16.0D), 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 1, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(1, (4.0D/16.0D), 0, 0.75D, 0.0D);
                    
                    tessellator.addVertexWithUV(0, 0, 0, 0.25D, 0.0D);
                    tessellator.addVertexWithUV(1, 0, 0, 0.25D, 0.25D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.0D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.0D, 0.0D);
                    
                    if((overlappingNotRenderSide & 8) == 0)
                    {
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.75D, 0.0D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 1.0D, 0.0D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 1.0D, 0.25D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.75D, 0.25D);
                        
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.75D, 0.0D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.75D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 1.0D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 1.0D, 0.0D);
                    }
                    else
                    {
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 1, 0.0D, 0.250D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 1, 0, 0.25D, 0.25D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 0, 0.25D, 0.5D);
                        tessellator.addVertexWithUV((5.0D/16.0D), 0, 1, 0.0D, 0.5D);
                        
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 1, 0.0D, 0.25D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 1, 0.0D, 0.5D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 0, 0, 0.25D, 0.5D);
                        tessellator.addVertexWithUV((11.0D/16.0D), 1, 0, 0.25D, 0.25D);
                    }

                    tessellator.addVertexWithUV(0, 1, 1, 0.5D, 0.25D);
                    tessellator.addVertexWithUV(0, 0, 1, 0.5D, 0.5D);
                    tessellator.addVertexWithUV(1, 0, 1, 0.75D, 0.5D);
                    tessellator.addVertexWithUV(1, 1, 1, 0.75D, 0.25D);
                }
            }
        }
        tessellator.draw();
    }
}