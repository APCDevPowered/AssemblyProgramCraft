package org.apcdevpowered.apc.client.renderer.tileentity;

import org.apcdevpowered.apc.client.renderer.AssemblyProgramCraftRenderGlobal;
import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockVCPU32ComputerConnector;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32ComputerConnector;
import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderTileEntityVCPU32ComputerConnector extends TileEntitySpecialRenderer<TileEntityVCPU32ComputerConnector>
{
    private static final ResourceLocation connectorTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/connector.png");
    
    @Override
	public void renderTileEntityAt(TileEntityVCPU32ComputerConnector tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        if(tileentity == null)
            return;
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                
        EnumFacing face = tileentity.getWorld().getBlockState(tileentity.getPos()).getValue(BlockVCPU32ComputerConnector.FACING);
        
        bindTexture(connectorTextures);
        GlStateManager.disableLighting();
        
        if(face.equals(EnumFacing.NORTH))
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            worldRenderer.pos(x, y, z + (12.0/16.0)).tex(0, 0.25).endVertex();
            worldRenderer.pos(x + 1, y, z + (12.0/16.0)).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + (12.0/16.0)).tex(0.25, 0).endVertex();
            worldRenderer.pos(x, y + 1, z + (12.0/16.0)).tex(0, 0).endVertex();
            
            worldRenderer.pos(x + 1, y, z).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x, y, z).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x, y + 1, z).tex(0.5, 0).endVertex();
            worldRenderer.pos(x + 1, y + 1, z).tex(0.25, 0).endVertex();
            
            worldRenderer.pos(x + (13.0/16), y, z + 1).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x + (13.0/16), y, z).tex(0, 0.5).endVertex();
            worldRenderer.pos(x + (13.0/16), y + 1, z).tex(0, 0.25).endVertex();
            worldRenderer.pos(x + (13.0/16), y + 1, z + 1).tex(0.25, 0.25).endVertex();
            
            worldRenderer.pos(x + (3.0/16), y, z).tex(0, 0.5).endVertex();
            worldRenderer.pos(x + (3.0/16), y, z + 1).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x + (3.0/16), y + 1, z + 1).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + (3.0/16), y + 1, z).tex(0, 0.25).endVertex();
            
            worldRenderer.pos(x + 1, y + (6.0/16), z).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z + 1).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x + 1, y + (6.0/16), z + 1).tex(0.5, 0.5).endVertex();
            
            worldRenderer.pos(x, y, z).tex(0.75, 0.25).endVertex();
            worldRenderer.pos(x + 1, y, z).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x + 1, y, z + 1).tex(0.5, 0.5).endVertex();
            worldRenderer.pos(x, y, z + 1).tex(0.75, 0.5).endVertex();
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentity.hasPower())
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (11.0/16), y + (6.0/16), z + (2.0/16)).tex(0, 0.5).endVertex();
                worldRenderer.pos(x + (10.0/16), y + (6.0/16), z + (2.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (10.0/16), y + (6.0/16), z + (3.0/16)).tex(0.25, 0.25).endVertex();
                worldRenderer.pos(x + (11.0/16), y + (6.0/16), z + (3.0/16)).tex(0, 0.25).endVertex();
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (11.0/16), y + (6.0/16), z + (2.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (10.0/16), y + (6.0/16), z + (2.0/16)).tex(0.5, 0.5).endVertex();
                worldRenderer.pos(x + (10.0/16), y + (6.0/16), z + (3.0/16)).tex(0.5, 0.25).endVertex();
                worldRenderer.pos(x + (11.0/16), y + (6.0/16), z + (3.0/16)).tex(0.25, 0.25).endVertex();
                
                tessellator.draw();
            }
        }
        else if(face.equals(EnumFacing.EAST))
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            worldRenderer.pos(x + 1, y, z + 1).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + 1, y, z).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x + 1, y + 1, z).tex(0.5, 0).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + 1).tex(0.25, 0).endVertex();
            
            worldRenderer.pos(x + (4.0/16), y, z).tex(0, 0.25).endVertex();
            worldRenderer.pos(x + (4.0/16), y, z + 1).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + (4.0/16), y + 1, z + 1).tex(0.25, 0).endVertex();
            worldRenderer.pos(x + (4.0/16), y + 1, z).tex(0, 0).endVertex();
            
            worldRenderer.pos(x, y, z + (13.0/16.0)).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x + 1, y, z + (13.0/16.0)).tex(0, 0.5).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + (13.0/16.0)).tex(0, 0.25).endVertex();
            worldRenderer.pos(x, y + 1, z + (13.0/16.0)).tex(0.25, 0.25).endVertex();
            
            worldRenderer.pos(x + 1, y, z + (3.0/16.0)).tex(0, 0.5).endVertex();
            worldRenderer.pos(x, y, z + (3.0/16.0)).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x, y + 1, z + (3.0/16.0)).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + (3.0/16.0)).tex(0, 0.25).endVertex();
            
            worldRenderer.pos(x + 1, y + (6.0/16), z).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z + 1).tex(0.5, 0.5).endVertex();
            worldRenderer.pos(x + 1, y + (6.0/16), z + 1).tex(0.5, 0.25).endVertex();
            
            worldRenderer.pos(x, y, z).tex(0.75, 0.5).endVertex();
            worldRenderer.pos(x + 1, y, z).tex(0.75, 0.25).endVertex();
            worldRenderer.pos(x + 1, y, z + 1).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x, y, z + 1).tex(0.5, 0.5).endVertex();
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentity.hasPower())
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (14.0/16), y + (6.0/16), z + (10.0/16)).tex(0, 0.5).endVertex();
                worldRenderer.pos(x + (13.0/16), y + (6.0/16), z + (10.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (13.0/16), y + (6.0/16), z + (11.0/16)).tex(0.25, 0.25).endVertex();
                worldRenderer.pos(x + (14.0/16), y + (6.0/16), z + (11.0/16)).tex(0, 0.25).endVertex();
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (14.0/16), y + (6.0/16), z + (10.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (13.0/16), y + (6.0/16), z + (10.0/16)).tex(0.5, 0.5).endVertex();
                worldRenderer.pos(x + (13.0/16), y + (6.0/16), z + (11.0/16)).tex(0.5, 0.25).endVertex();
                worldRenderer.pos(x + (14.0/16), y + (6.0/16), z + (11.0/16)).tex(0.25, 0.25).endVertex();
                
                tessellator.draw();
            }
        }
        else if(face.equals(EnumFacing.SOUTH))
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            worldRenderer.pos(x, y, z + 1).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + 1, y, z + 1).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + 1).tex(0.5, 0).endVertex();
            worldRenderer.pos(x, y + 1, z + 1).tex(0.25, 0).endVertex();
            
            worldRenderer.pos(x + 1, y, z + (4.0/16.0)).tex(0, 0.25).endVertex();
            worldRenderer.pos(x, y, z + (4.0/16.0)).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x, y + 1, z + (4.0/16.0)).tex(0.25, 0).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + (4.0/16.0)).tex(0, 0).endVertex();
            
            worldRenderer.pos(x + (13.0/16), y, z + 1).tex(0, 0.5).endVertex();
            worldRenderer.pos(x + (13.0/16), y, z).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x + (13.0/16), y + 1, z).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + (13.0/16), y + 1, z + 1).tex(0, 0.25).endVertex();
            
            worldRenderer.pos(x + (3.0/16), y, z).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x + (3.0/16), y, z + 1).tex(0, 0.5).endVertex();
            worldRenderer.pos(x + (3.0/16), y + 1, z + 1).tex(0, 0.25).endVertex();
            worldRenderer.pos(x + (3.0/16), y + 1, z).tex(0.25, 0.25).endVertex();
            
            worldRenderer.pos(x + 1, y + (6.0/16), z).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z).tex(0.5, 0.5).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z + 1).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x + 1, y + (6.0/16), z + 1).tex(0.25, 0.25).endVertex();
            
            worldRenderer.pos(x, y, z).tex(0.5, 0.5).endVertex();
            worldRenderer.pos(x + 1, y, z).tex(0.75, 0.5).endVertex();
            worldRenderer.pos(x + 1, y, z + 1).tex(0.75, 0.25).endVertex();
            worldRenderer.pos(x, y, z + 1).tex(0.5, 0.25).endVertex();
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentity.hasPower())
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (6.0/16), y + (6.0/16), z + (13.0/16)).tex(0, 0.5).endVertex();
                worldRenderer.pos(x + (5.0/16), y + (6.0/16), z + (13.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (5.0/16), y + (6.0/16), z + (14.0/16)).tex(0.25, 0.25).endVertex();
                worldRenderer.pos(x + (6.0/16), y + (6.0/16), z + (14.0/16)).tex(0, 0.25).endVertex();
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (6.0/16), y + (6.0/16), z + (13.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (5.0/16), y + (6.0/16), z + (13.0/16)).tex(0.5, 0.5).endVertex();
                worldRenderer.pos(x + (5.0/16), y + (6.0/16), z + (14.0/16)).tex(0.5, 0.25).endVertex();
                worldRenderer.pos(x + (6.0/16), y + (6.0/16), z + (14.0/16)).tex(0.25, 0.25).endVertex();
                
                tessellator.draw();
            }
        }
        else if(face.equals(EnumFacing.WEST))
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            worldRenderer.pos(x, y, z).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x, y, z + 1).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x, y + 1, z + 1).tex(0.5, 0).endVertex();
            worldRenderer.pos(x, y + 1, z).tex(0.25, 0).endVertex();
            
            worldRenderer.pos(x + (12.0/16), y, z + 1).tex(0, 0.25).endVertex();
            worldRenderer.pos(x + (12.0/16), y, z).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + (12.0/16), y + 1, z).tex(0.25, 0).endVertex();
            worldRenderer.pos(x + (12.0/16), y + 1, z + 1).tex(0, 0).endVertex();
            
            worldRenderer.pos(x, y, z + (13.0/16.0)).tex(0, 0.5).endVertex();
            worldRenderer.pos(x + 1, y, z + (13.0/16.0)).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + (13.0/16.0)).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x, y + 1, z + (13.0/16.0)).tex(0, 0.25).endVertex();
            
            worldRenderer.pos(x + 1, y, z + (3.0/16.0)).tex(0.25, 0.5).endVertex();
            worldRenderer.pos(x, y, z + (3.0/16.0)).tex(0, 0.5).endVertex();
            worldRenderer.pos(x, y + 1, z + (3.0/16.0)).tex(0, 0.25).endVertex();
            worldRenderer.pos(x + 1, y + 1, z + (3.0/16.0)).tex(0.25, 0.25).endVertex();
            
            worldRenderer.pos(x + 1, y + (6.0/16), z).tex(0.5, 0.5).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z).tex(0.5, 0.25).endVertex();
            worldRenderer.pos(x, y + (6.0/16), z + 1).tex(0.25, 0.25).endVertex();
            worldRenderer.pos(x + 1, y + (6.0/16), z + 1).tex(0.25, 0.5).endVertex();
            
            worldRenderer.pos(x, y, z).tex(0.75, 0.25).endVertex();
            worldRenderer.pos(x + 1, y, z).tex(0.75, 0.5).endVertex();
            worldRenderer.pos(x + 1, y, z + 1).tex(0.5, 0.5).endVertex();
            worldRenderer.pos(x, y, z + 1).tex(0.5, 0.25).endVertex();
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
            
            if(tileentity.hasPower())
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (3.0/16), y + (6.0/16), z + (5.0/16)).tex(0, 0.5).endVertex();
                worldRenderer.pos(x + (2.0/16), y + (6.0/16), z + (5.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (2.0/16), y + (6.0/16), z + (6.0/16)).tex(0.25, 0.25).endVertex();
                worldRenderer.pos(x + (3.0/16), y + (6.0/16), z + (6.0/16)).tex(0, 0.25).endVertex();
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos(x + (3.0/16), y + (6.0/16), z + (5.0/16)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos(x + (2.0/16), y + (6.0/16), z + (5.0/16)).tex(0.5, 0.5).endVertex();
                worldRenderer.pos(x + (2.0/16), y + (6.0/16), z + (6.0/16)).tex(0.5, 0.25).endVertex();
                worldRenderer.pos(x + (3.0/16), y + (6.0/16), z + (6.0/16)).tex(0.25, 0.25).endVertex();
                
                tessellator.draw();
            }
        }
        GlStateManager.enableLighting();
    }
}