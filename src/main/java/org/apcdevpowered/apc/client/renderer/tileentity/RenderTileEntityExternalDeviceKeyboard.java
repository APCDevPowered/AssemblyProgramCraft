package org.apcdevpowered.apc.client.renderer.tileentity;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceKeyboard;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceKeyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityExternalDeviceKeyboard extends TileEntitySpecialRenderer
{
    private static final ResourceLocation keyboardTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/keyboard.png");
    
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        TileEntityExternalDeviceKeyboard tileEntityExternalDeviceKeyboard = (TileEntityExternalDeviceKeyboard)tileentity;
        if(tileEntityExternalDeviceKeyboard == null)
        {
            return;
        }
        EnumFacing face = (EnumFacing) tileEntityExternalDeviceKeyboard.getWorld().getBlockState(tileEntityExternalDeviceKeyboard.getPos()).getValue(BlockExternalDeviceKeyboard.FACING);
        
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
        
        worldRenderer.startDrawing(GL11.GL_QUADS);
        
        worldRenderer.addVertexWithUV(0, 0, 0, 0.5F, 1F);
        worldRenderer.addVertexWithUV(1, 0, 0, 1F, 1F);
        worldRenderer.addVertexWithUV(1, 0, 1, 1F, 0.5F);
        worldRenderer.addVertexWithUV(0, 0, 1, 0.5F, 0.5F);
        
        worldRenderer.addVertexWithUV(1, 2F/16F, 0, 0F, 0.5F);
        worldRenderer.addVertexWithUV(0, 2F/16F, 0, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(0, 2F/16F, 1, 0.5F, 0F);
        worldRenderer.addVertexWithUV(1, 2F/16F, 1, 0F, 0F);
        
        worldRenderer.addVertexWithUV(1, 0, 17F/32F, 0F, 1.0F);
        worldRenderer.addVertexWithUV(0, 0, 17F/32F, 0.5F, 1.0F);
        worldRenderer.addVertexWithUV(0, 1, 17F/32F, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(1, 1, 17F/32F, 0F, 0.5F);
        
        worldRenderer.addVertexWithUV(0, 0, 1, 0.5F, 1.0F);
        worldRenderer.addVertexWithUV(1, 0, 1, 0F, 1.0F);
        worldRenderer.addVertexWithUV(1, 1, 1, 0F, 0.5F);
        worldRenderer.addVertexWithUV(0, 1, 1, 0.5F, 0.5F);
        
        worldRenderer.addVertexWithUV(1, 0, 1, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(1, 0, 0, 1F, 0.5F);
        worldRenderer.addVertexWithUV(1, 1, 0, 1F, 0F);
        worldRenderer.addVertexWithUV(1, 1, 1, 0.5F, 0F);
        
        worldRenderer.addVertexWithUV(0, 0, 0, 1F, 0.5F);
        worldRenderer.addVertexWithUV(0, 0, 1, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(0, 1, 1, 0.5F, 0F);
        worldRenderer.addVertexWithUV(0, 1, 0, 1F, 0F);
        
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
