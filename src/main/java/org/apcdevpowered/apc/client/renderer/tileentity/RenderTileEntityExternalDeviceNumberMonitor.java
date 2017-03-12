package org.apcdevpowered.apc.client.renderer.tileentity;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceNumberMonitor;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceNumberMonitor;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityExternalDeviceNumberMonitor extends TileEntitySpecialRenderer<TileEntityExternalDeviceNumberMonitor>
{
    private static final ResourceLocation numberMonitorTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/number_monitor.png");
    
    @Override
    public void renderTileEntityAt(TileEntityExternalDeviceNumberMonitor tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        if(tileentity == null)
            return;
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        EnumFacing face = tileentity.getWorld().getBlockState(tileentity.getPos()).getValue(BlockExternalDeviceNumberMonitor.FACING);
        
        GlStateManager.disableLighting();
        
        GlStateManager.pushMatrix();
        
        this.bindTexture(numberMonitorTextures);
        
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
        
        worldRenderer.pos(0, 0, 4F/16F).tex(0.5F, 1.0F).endVertex();
        worldRenderer.pos(1, 0, 4F/16F).tex(1.0F, 1.0F).endVertex();
        worldRenderer.pos(1, 1, 4F/16F).tex(1.0F, 0.5F).endVertex();
        worldRenderer.pos(0, 1, 4F/16F).tex(0.5F, 0.5F).endVertex();
        
        worldRenderer.pos(1, 0, 0).tex(0.5F, 1.0F).endVertex();
        worldRenderer.pos(0, 0, 0).tex(1.0F, 1.0F).endVertex();
        worldRenderer.pos(0, 1, 0).tex(1.0F, 0.5F).endVertex();
        worldRenderer.pos(1, 1, 0).tex(0.5F, 0.5F).endVertex();
        
        worldRenderer.pos(1, 14F/16F, 0).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(0, 14F/16F, 0).tex(1F, 0.5F).endVertex();
        worldRenderer.pos(0, 14F/16F, 1).tex(1F, 0F).endVertex();
        worldRenderer.pos(1, 14F/16F, 1).tex(0.5F, 0F).endVertex();
        
        worldRenderer.pos(0, 1F/16F, 0).tex(1F, 0.5F).endVertex();
        worldRenderer.pos(1, 1F/16F, 0).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(1, 1F/16F, 1).tex(0.5F, 0F).endVertex();
        worldRenderer.pos(0, 1F/16F, 1).tex(1F, 0F).endVertex();
        
        worldRenderer.pos(0, 0, 0).tex(0F, 1.0F).endVertex();
        worldRenderer.pos(0, 0, 1).tex(0F, 0.5F).endVertex();
        worldRenderer.pos(0, 1, 1).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(0, 1, 0).tex(0.5F, 1.0F).endVertex();
        
        worldRenderer.pos(1, 0, 1).tex(0F, 0.5F).endVertex();
        worldRenderer.pos(1, 0, 0).tex(0F, 1.0F).endVertex();
        worldRenderer.pos(1, 1, 0).tex(0.5F, 1.0F).endVertex();
        worldRenderer.pos(1, 1, 1).tex(0.5F, 0.5F).endVertex();
        
        tessellator.draw();
        
        /*
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3f(0, 0, 4F/16F);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(1, 0, 4F/16F);
        GL11.glTexCoord2f(1.0F, 0.5F);
        GL11.glVertex3f(1, 1, 4F/16F);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 1, 4F/16F);
        
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3f(1, 0, 0);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f(1.0F, 0.5F);
        GL11.glVertex3f(0, 1, 0);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(1, 1, 0);
        
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(1, 14F/16F, 0);
        GL11.glTexCoord2f(1F, 0.5F);
        GL11.glVertex3f(0, 14F/16F, 0);
        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex3f(0, 14F/16F, 1);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(1, 14F/16F, 1);
        
        GL11.glTexCoord2f(1F, 0.5F);
        GL11.glVertex3f(0, 1F/16F, 0);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(1, 1F/16F, 0);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(1, 1F/16F, 1);
        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex3f(0, 1F/16F, 1);
        
        GL11.glTexCoord2f(0F, 1.0F);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f(0F, 0.5F);
        GL11.glVertex3f(0, 0, 1);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 1, 1);
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3f(0, 1, 0);
        
        GL11.glTexCoord2f(0F, 0.5F);
        GL11.glVertex3f(1, 0, 1);
        GL11.glTexCoord2f(0F, 1.0F);
        GL11.glVertex3f(1, 0, 0);
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3f(1, 1, 0);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(1, 1, 1);
        
        GL11.glEnd();
        */
        
        GlStateManager.pushMatrix();
        
        String number = String.valueOf(tileentity.number);
        
        FontRenderer fontRenderer = this.getFontRenderer();
        
        GlStateManager.rotate(180F, 0, 0, 0);
        GlStateManager.translate(-0.5F - fontRenderer.getStringWidth(number) * (1F/64F) / 2, -0.5F - 8 * (1F/64F) / 2, 0.01F);
        GlStateManager.scale(1F/64F, 1F/64F, 1F/64F);
        
        fontRenderer.drawString(number, 0, 0, 0x404040, false);
        
        GlStateManager.popMatrix();
        
        GlStateManager.popMatrix();
        
        GlStateManager.enableLighting();
    }
}