package org.apcdevpowered.apc.client.renderer.tileentity;

import org.apcdevpowered.apc.common.AssemblyProgramCraft;
import org.apcdevpowered.apc.common.block.BlockExternalDeviceMonitor;
import org.apcdevpowered.apc.common.tileEntity.TileEntityExternalDeviceMonitor;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityExternalDeviceMonitor extends TileEntitySpecialRenderer<TileEntityExternalDeviceMonitor>
{
    private static final ResourceLocation monitorTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/monitor.png");
    
    /*
    private static IntBuffer imageData = ByteBuffer.allocateDirect(0x1000000).order(ByteOrder.nativeOrder()).asIntBuffer();
    private int texID = -1;
    */
    
    private DynamicTexture texture;
    
    @Override
    public void renderTileEntityAt(TileEntityExternalDeviceMonitor tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        if(tileentity == null)
            return;
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        EnumFacing face = tileentity.getWorld().getBlockState(tileentity.getPos()).getValue(BlockExternalDeviceMonitor.FACING);
        
        GlStateManager.disableLighting();
        
        GlStateManager.pushMatrix();
        
        this.bindTexture(monitorTextures);
        
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
        
        worldRenderer.pos(1, 0, 0).tex(0F, 0.5F).endVertex();
        worldRenderer.pos(0, 0, 0).tex(0.5F, 0.5F).endVertex();
        worldRenderer.pos(0, 1, 0).tex(0.5F, 0F).endVertex();
        worldRenderer.pos(1, 1, 0).tex(0F, 0F).endVertex();
        
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
        
        GL11.glTexCoord2f(0F, 0.5F);
        GL11.glVertex3f(1, 0, 0);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(0, 1, 0);
        GL11.glTexCoord2f(0F, 0F);
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
        
        if(texture == null)
        {
            texture = new DynamicTexture(100, 75);
            
            texture.setBlurMipmap(true, true);
        }
        
        GlStateManager.bindTexture(texture.getGlTextureId());
        System.arraycopy(tileentity.graphicsMemory, 0, texture.getTextureData(), 0, 75 * 100);
        texture.updateDynamicTexture();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        
        worldRenderer.pos(14F/16F, 3F/16F, 0).tex(0F, 1F).endVertex();
        worldRenderer.pos(2F/16F, 3F/16F, 0).tex(1F, 1F).endVertex();
        worldRenderer.pos(2F/16F, 12F/16F, 0).tex(1F, 0F).endVertex();
        worldRenderer.pos(14F/16F, 12/16F, 0).tex(0F, 0F).endVertex();
        
        tessellator.draw();
        
        /*
        imageData.clear();
        imageData.put(tileEntityExternalDeviceMonitor.graphicsMemory);
        imageData.position(0).limit(tileEntityExternalDeviceMonitor.graphicsMemory.length);
        
        if(texID == -1)
        {
            texID = GL11.glGenTextures();
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); 
        }
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 75, 100, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0F, 1F);
        GL11.glVertex3f(14F/16F, 3F/16F, 0);
        GL11.glTexCoord2f(1F, 1F);
        GL11.glVertex3f(2F/16F, 3F/16F, 0);
        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex3f(2F/16F, 12F/16F, 0);
        GL11.glTexCoord2f(0F, 0F);
        GL11.glVertex3f(14F/16F, 12/16F, 0);
        
        GL11.glEnd();
        */
        
        GlStateManager.popMatrix();
        
        GlStateManager.enableLighting();
    }
}