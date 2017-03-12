package org.apcdevpowered.apc.client.renderer.tileentity;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceKeyboard;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceKeyboard;
import org.lwjgl.opengl.GL11;

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
public class RenderTileEntityExternalDeviceKeyboard extends TileEntitySpecialRenderer<TileEntityExternalDeviceKeyboard>
{
    private static final ResourceLocation keyboardTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/keyboard.png");
    
    @Override
    public void renderTileEntityAt(TileEntityExternalDeviceKeyboard tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        if (tileentity == null)
            return;
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        EnumFacing face = tileentity.getWorld().getBlockState(tileentity.getPos()).getValue(BlockExternalDeviceKeyboard.FACING);
        
        GlStateManager.disableLighting();
        
        GlStateManager.pushMatrix();
        
        this.bindTexture(keyboardTextures);
        
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
        
        worldRenderer.pos(0, 0, 0).tex(0.5F, 1F).endVertex();
        worldRenderer.pos(1, 0, 0).tex(1F, 1F).endVertex();
        worldRenderer.pos(1, 0, 1).tex(1F, 0.5F).endVertex();
        worldRenderer.pos(0, 0, 1).tex(0.5F, 0.5F).endVertex();
        
        worldRenderer.pos(1, 2F/16F, 0).tex(0F, 0.5F).endVertex();
        worldRenderer.pos(0, 2F/16F, 0).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(0, 2F/16F, 1).tex(0.5F, 0F).endVertex();
        worldRenderer.pos(1, 2F/16F, 1).tex(0F, 0F).endVertex();
        
        worldRenderer.pos(1, 0, 17F/32F).tex(0F, 1.0F).endVertex();
        worldRenderer.pos(0, 0, 17F/32F).tex(0.5F, 1.0F).endVertex();
        worldRenderer.pos(0, 1, 17F/32F).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(1, 1, 17F/32F).tex(0F, 0.5F).endVertex();
        
        worldRenderer.pos(0, 0, 1).tex(0.5F, 1.0F).endVertex();
        worldRenderer.pos(1, 0, 1).tex(0F, 1.0F).endVertex();
        worldRenderer.pos(1, 1, 1).tex(0F, 0.5F).endVertex();
        worldRenderer.pos(0, 1, 1).tex(0.5F, 0.5F).endVertex();
        
        worldRenderer.pos(1, 0, 1).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(1, 0, 0).tex(1F, 0.5F).endVertex();
        worldRenderer.pos(1, 1, 0).tex(1F, 0F).endVertex();
        worldRenderer.pos(1, 1, 1).tex(0.5F, 0F).endVertex();
        
        worldRenderer.pos(0, 0, 0).tex(1F, 0.5F).endVertex();
        worldRenderer.pos(0, 0, 1).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(0, 1, 1).tex(0.5F, 0F).endVertex();
        worldRenderer.pos(0, 1, 0).tex(1F, 0F).endVertex();
        
        tessellator.draw();
        
        /*
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0.5F, 1F);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f(1F, 1F);
        GL11.glVertex3f(1, 0, 0);
        GL11.glTexCoord2f(1F, 0.5F);
        GL11.glVertex3f(1, 0, 1);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 0, 1);
        
        GL11.glTexCoord2f(0F, 0.5F);
        GL11.glVertex3f(1, 2F/16F, 0);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 2F/16F, 0);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(0, 2F/16F, 1);
        GL11.glTexCoord2f(0F, 0F);
        GL11.glVertex3f(1, 2F/16F, 1);
        
        GL11.glTexCoord2f(0F, 1.0F);
        GL11.glVertex3f(1, 0, 17F/32F);
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3f(0, 0, 17F/32F);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 1, 17F/32F);
        GL11.glTexCoord2f(0F, 0.5F);
        GL11.glVertex3f(1, 1, 17F/32F);
        
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3f(0, 0, 1);
        GL11.glTexCoord2f(0F, 1.0F);
        GL11.glVertex3f(1, 0, 1);
        GL11.glTexCoord2f(0F, 0.5F);
        GL11.glVertex3f(1, 1, 1);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 1, 1);
        
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(1, 0, 1);
        GL11.glTexCoord2f(1F, 0.5F);
        GL11.glVertex3f(1, 0, 0);
        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex3f(1, 1, 0);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(1, 1, 1);
        
        GL11.glTexCoord2f(1F, 0.5F);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 0, 1);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(0, 1, 1);
        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex3f(0, 1, 0);
        
        GL11.glEnd();
        */
        
        GlStateManager.popMatrix();
        
        GlStateManager.enableLighting();
    }
}
