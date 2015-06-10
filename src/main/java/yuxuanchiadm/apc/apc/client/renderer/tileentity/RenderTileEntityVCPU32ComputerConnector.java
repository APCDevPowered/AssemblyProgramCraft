package yuxuanchiadm.apc.apc.client.renderer.tileentity;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.client.renderer.AssemblyProgramCraftRenderGlobal;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.block.BlockVCPU32ComputerConnector;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderTileEntityVCPU32ComputerConnector extends TileEntitySpecialRenderer
{
    private static final ResourceLocation connectorTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/connector.png");
    
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        TileEntityVCPU32ComputerConnector tileentityvcpu32computerconnector = (TileEntityVCPU32ComputerConnector)tileentity;
        if(tileentityvcpu32computerconnector == null)
        {
            return;
        }
        EnumFacing face = (EnumFacing) tileentityvcpu32computerconnector.getWorld().getBlockState(tileentityvcpu32computerconnector.getPos()).getValue(BlockVCPU32ComputerConnector.FACING);
        bindTexture(connectorTextures);
        GlStateManager.disableLighting();
        
        if(face.equals(EnumFacing.NORTH))
        {
            worldRenderer.startDrawingQuads();
            
            worldRenderer.addVertexWithUV(x, y, z + (12.0/16.0), 0, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y, z + (12.0/16.0), 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + (12.0/16.0), 0.25, 0);
            worldRenderer.addVertexWithUV(x, y + 1, z + (12.0/16.0), 0, 0);
            
            worldRenderer.addVertexWithUV(x + 1, y, z, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x, y, z, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x, y + 1, z, 0.5, 0);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z, 0.25, 0);
            
            worldRenderer.addVertexWithUV(x + (13.0/16), y, z + 1, 0.25, 0.5);
            worldRenderer.addVertexWithUV(x + (13.0/16), y, z, 0, 0.5);
            worldRenderer.addVertexWithUV(x + (13.0/16), y + 1, z, 0, 0.25);
            worldRenderer.addVertexWithUV(x + (13.0/16), y + 1, z + 1, 0.25, 0.25);
            
            worldRenderer.addVertexWithUV(x + (3.0/16), y, z, 0, 0.5);
            worldRenderer.addVertexWithUV(x + (3.0/16), y, z + 1, 0.25, 0.5);
            worldRenderer.addVertexWithUV(x + (3.0/16), y + 1, z + 1, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + (3.0/16), y + 1, z, 0, 0.25);
            
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z + 1, 0.25, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.5, 0.5);
            
            worldRenderer.addVertexWithUV(x, y, z, 0.75, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y, z, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.5);
            worldRenderer.addVertexWithUV(x, y, z + 1, 0.75, 0.5);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (2.0/16), 0, 0.5);
                worldRenderer.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (2.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (3.0/16), 0.25, 0.25);
                worldRenderer.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (3.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (2.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (2.0/16), 0.5, 0.5);
                worldRenderer.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (3.0/16), 0.5, 0.25);
                worldRenderer.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (3.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        else if(face.equals(EnumFacing.EAST))
        {
            worldRenderer.startDrawingQuads();
            
            worldRenderer.addVertexWithUV(x + 1, y, z + 1, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y, z, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z, 0.5, 0);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + 1, 0.25, 0);
            
            worldRenderer.addVertexWithUV(x + (4.0/16), y, z, 0, 0.25);
            worldRenderer.addVertexWithUV(x + (4.0/16), y, z + 1, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + (4.0/16), y + 1, z + 1, 0.25, 0);
            worldRenderer.addVertexWithUV(x + (4.0/16), y + 1, z, 0, 0);
            
            worldRenderer.addVertexWithUV(x, y, z + (13.0/16.0), 0.25, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y, z + (13.0/16.0), 0, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + (13.0/16.0), 0, 0.25);
            worldRenderer.addVertexWithUV(x, y + 1, z + (13.0/16.0), 0.25, 0.25);
            
            worldRenderer.addVertexWithUV(x + 1, y, z + (3.0/16.0), 0, 0.5);
            worldRenderer.addVertexWithUV(x, y, z + (3.0/16.0), 0.25, 0.5);
            worldRenderer.addVertexWithUV(x, y + 1, z + (3.0/16.0), 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + (3.0/16.0), 0, 0.25);
            
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z, 0.25, 0.5);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z + 1, 0.5, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.5, 0.25);
            
            worldRenderer.addVertexWithUV(x, y, z, 0.75, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y, z, 0.75, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x, y, z + 1, 0.5, 0.5);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (10.0/16), 0, 0.5);
                worldRenderer.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (10.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (11.0/16), 0.25, 0.25);
                worldRenderer.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (11.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (10.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (10.0/16), 0.5, 0.5);
                worldRenderer.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (11.0/16), 0.5, 0.25);
                worldRenderer.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (11.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        else if(face.equals(EnumFacing.SOUTH))
        {
            worldRenderer.startDrawingQuads();
            
            worldRenderer.addVertexWithUV(x, y, z + 1, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + 1, 0.5, 0);
            worldRenderer.addVertexWithUV(x, y + 1, z + 1, 0.25, 0);
            
            worldRenderer.addVertexWithUV(x + 1, y, z + (4.0/16.0), 0, 0.25);
            worldRenderer.addVertexWithUV(x, y, z + (4.0/16.0), 0.25, 0.25);
            worldRenderer.addVertexWithUV(x, y + 1, z + (4.0/16.0), 0.25, 0);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + (4.0/16.0), 0, 0);
            
            worldRenderer.addVertexWithUV(x + (13.0/16), y, z + 1, 0, 0.5);
            worldRenderer.addVertexWithUV(x + (13.0/16), y, z, 0.25, 0.5);
            worldRenderer.addVertexWithUV(x + (13.0/16), y + 1, z, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + (13.0/16), y + 1, z + 1, 0, 0.25);
            
            worldRenderer.addVertexWithUV(x + (3.0/16), y, z, 0.25, 0.5);
            worldRenderer.addVertexWithUV(x + (3.0/16), y, z + 1, 0, 0.5);
            worldRenderer.addVertexWithUV(x + (3.0/16), y + 1, z + 1, 0, 0.25);
            worldRenderer.addVertexWithUV(x + (3.0/16), y + 1, z, 0.25, 0.25);
            
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z, 0.25, 0.5);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z, 0.5, 0.5);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z + 1, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.25, 0.25);
            
            worldRenderer.addVertexWithUV(x, y, z, 0.5, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y, z, 0.75, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y, z + 1, 0.75, 0.25);
            worldRenderer.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (13.0/16), 0, 0.5);
                worldRenderer.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (13.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (14.0/16), 0.25, 0.25);
                worldRenderer.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (14.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (13.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (13.0/16), 0.5, 0.5);
                worldRenderer.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (14.0/16), 0.5, 0.25);
                worldRenderer.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (14.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        else if(face.equals(EnumFacing.WEST))
        {
            worldRenderer.startDrawingQuads();
            
            worldRenderer.addVertexWithUV(x, y, z, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x, y + 1, z + 1, 0.5, 0);
            worldRenderer.addVertexWithUV(x, y + 1, z, 0.25, 0);
            
            worldRenderer.addVertexWithUV(x + (12.0/16), y, z + 1, 0, 0.25);
            worldRenderer.addVertexWithUV(x + (12.0/16), y, z, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + (12.0/16), y + 1, z, 0.25, 0);
            worldRenderer.addVertexWithUV(x + (12.0/16), y + 1, z + 1, 0, 0);
            
            worldRenderer.addVertexWithUV(x, y, z + (13.0/16.0), 0, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y, z + (13.0/16.0), 0.25, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + (13.0/16.0), 0.25, 0.25);
            worldRenderer.addVertexWithUV(x, y + 1, z + (13.0/16.0), 0, 0.25);
            
            worldRenderer.addVertexWithUV(x + 1, y, z + (3.0/16.0), 0.25, 0.5);
            worldRenderer.addVertexWithUV(x, y, z + (3.0/16.0), 0, 0.5);
            worldRenderer.addVertexWithUV(x, y + 1, z + (3.0/16.0), 0, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y + 1, z + (3.0/16.0), 0.25, 0.25);
            
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z, 0.5, 0.5);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z, 0.5, 0.25);
            worldRenderer.addVertexWithUV(x, y + (6.0/16), z + 1, 0.25, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.25, 0.5);
            
            worldRenderer.addVertexWithUV(x, y, z, 0.75, 0.25);
            worldRenderer.addVertexWithUV(x + 1, y, z, 0.75, 0.5);
            worldRenderer.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.5);
            worldRenderer.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (5.0/16), 0, 0.5);
                worldRenderer.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (5.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (6.0/16), 0.25, 0.25);
                worldRenderer.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (6.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.startDrawingQuads();
                
                worldRenderer.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (5.0/16), 0.25, 0.5);
                worldRenderer.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (5.0/16), 0.5, 0.5);
                worldRenderer.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (6.0/16), 0.5, 0.25);
                worldRenderer.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (6.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        GlStateManager.enableLighting();
    }
}