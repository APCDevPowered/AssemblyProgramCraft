package org.apcdevpowered.apc.client.renderer.tileentity;

import org.apcdevpowered.apc.client.renderer.AssemblyProgramCraftRenderGlobal;
import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockVCPU32Computer;
import org.apcdevpowered.apc.common.tileEntity.TileEntityVCPU32Computer;
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
public class RenderTileEntityVCPU32Computer extends TileEntitySpecialRenderer<TileEntityVCPU32Computer>
{
    private static final ResourceLocation computerTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/computer.png");
    
    @Override
	public void renderTileEntityAt(TileEntityVCPU32Computer tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        if(tileentity == null)
            return;
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        EnumFacing face = tileentity.getWorld().getBlockState(tileentity.getPos()).getValue(BlockVCPU32Computer.FACING);
        
        GlStateManager.disableLighting();
        
        GlStateManager.pushMatrix();
        
        this.bindTexture(computerTextures);
        
        GlStateManager.translate(x, y, z);
        
        if(face.equals(EnumFacing.NORTH))
        {
            GlStateManager.rotate(0, 1, 0, 0);
            GlStateManager.translate(0, 0, 0);
        }
        else if(face.equals(EnumFacing.EAST))
        {
            GlStateManager.rotate(-90, 0, 1, 0);
            GlStateManager.translate(0, 0, -1);
        }
        else if(face.equals(EnumFacing.SOUTH))
        {
            GlStateManager.rotate(-180, 0, 1, 0);
            GlStateManager.translate(-1, 0, -1);
        }
        else if(face.equals(EnumFacing.WEST))
        {
            GlStateManager.rotate(-270, 0, 1, 0);
            GlStateManager.translate(-1, 0, 0);
        }
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        
        worldRenderer.pos(1, 0, (4.0/16.0)).tex(0, 0.5).endVertex();
        worldRenderer.pos(0, 0, (4.0/16.0)).tex(0.5, 0.5).endVertex();
        worldRenderer.pos(0, 1, (4.0/16.0)).tex(0.5, 0).endVertex();
        worldRenderer.pos(1, 1, (4.0/16.0)).tex(0, 0).endVertex();
        
        worldRenderer.pos(0, 0, 1).tex(0.5, 0.5).endVertex();
        worldRenderer.pos(1, 0, 1).tex(1, 0.5).endVertex();
        worldRenderer.pos(1, 1, 1).tex(1, 0).endVertex();
        worldRenderer.pos(0, 1, 1).tex(0.5, 0).endVertex();
        
        worldRenderer.pos((4.0/16.0), 0, 0).tex(0, 1).endVertex();
        worldRenderer.pos((4.0/16.0), 0, 1).tex(0.5, 1).endVertex();
        worldRenderer.pos((4.0/16.0), 1, 1).tex(0.5, 0.5).endVertex();
        worldRenderer.pos((4.0/16.0), 1, 0).tex(0, 0.5).endVertex();
        
        worldRenderer.pos((12.0/16.0), 0, 1).tex(0.5, 1).endVertex();
        worldRenderer.pos((12.0/16.0), 0, 0).tex(0, 1).endVertex();
        worldRenderer.pos((12.0/16.0), 1, 0).tex(0, 0.5).endVertex();
        worldRenderer.pos((12.0/16.0), 1, 1).tex(0.5, 0.5).endVertex();
        
        worldRenderer.pos(1, (12.0/16.0), 0).tex(0.5, 0.5).endVertex();
        worldRenderer.pos(0, (12.0/16.0), 0).tex(0.5, 1).endVertex();
        worldRenderer.pos(0, (12.0/16.0), 1).tex(1, 1).endVertex();
        worldRenderer.pos(1, (12.0/16.0), 1).tex(1, 0.5).endVertex();
        
        worldRenderer.pos(0, 0, 0).tex(0.5, 1).endVertex();
        worldRenderer.pos(1, 0, 0).tex(0.5, 0.5).endVertex();
        worldRenderer.pos(1, 0, 1).tex(1, 0.5).endVertex();
        worldRenderer.pos(0, 0, 1).tex(1, 1).endVertex();
        
        tessellator.draw();
        
        this.bindTexture(AssemblyProgramCraftRenderGlobal.colorsTextures);
        
        if(tileentity.isError())
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            worldRenderer.pos((7.0/16.0), (5.0/16.0), (4.0/16.0)).tex(0.25, 0.25).endVertex();
            worldRenderer.pos((6.0/16.0), (5.0/16.0), (4.0/16.0)).tex(0.5, 0.25).endVertex();
            worldRenderer.pos((6.0/16.0), (6.0/16.0), (4.0/16.0)).tex(0.5, 0).endVertex();
            worldRenderer.pos((7.0/16.0), (6.0/16.0), (4.0/16.0)).tex(0.25, 0).endVertex();
            
            tessellator.draw();
        }
        else
        {
            if(tileentity.isRunning())
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos((7.0/16.0), (5.0/16.0), (4.0/16.0)).tex(0, 0.5).endVertex();
                worldRenderer.pos((6.0/16.0), (5.0/16.0), (4.0/16.0)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos((6.0/16.0), (6.0/16.0), (4.0/16.0)).tex(0.25, 0.25).endVertex();
                worldRenderer.pos((7.0/16.0), (6.0/16.0), (4.0/16.0)).tex(0, 0.25).endVertex();
                
                tessellator.draw();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                
                worldRenderer.pos((7.0/16.0), (5.0/16.0), (4.0/16.0)).tex(0.25, 0.5).endVertex();
                worldRenderer.pos((6.0/16.0), (5.0/16.0), (4.0/16.0)).tex(0.5, 0.5).endVertex();
                worldRenderer.pos((6.0/16.0), (6.0/16.0), (4.0/16.0)).tex(0.5, 0.25).endVertex();
                worldRenderer.pos((7.0/16.0), (6.0/16.0), (4.0/16.0)).tex(0.25, 0.25).endVertex();
                
                tessellator.draw();
            }
        }
                
        GlStateManager.popMatrix();
        
        GlStateManager.enableLighting();
    }
}