package yuxuanchiadm.apc.apc.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceMonitor;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDeviceMonitor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityExternalDeviceMonitor extends TileEntitySpecialRenderer
{
    private static final ResourceLocation monitorTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/monitor.png");
    
    /*
    private static IntBuffer imageData = ByteBuffer.allocateDirect(0x1000000).order(ByteOrder.nativeOrder()).asIntBuffer();
    private int texID = -1;
    */
    
    private DynamicTexture texture;
    
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        TileEntityExternalDeviceMonitor tileEntityExternalDeviceMonitor = (TileEntityExternalDeviceMonitor)tileentity;
        if(tileEntityExternalDeviceMonitor == null)
        {
            return;
        }
        EnumFacing face = (EnumFacing) tileEntityExternalDeviceMonitor.getWorld().getBlockState(tileEntityExternalDeviceMonitor.getPos()).getValue(BlockExternalDeviceMonitor.FACING);
        
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
        
        worldRenderer.startDrawing(GL11.GL_QUADS);
        
        worldRenderer.addVertexWithUV(0, 0, 4F/16F, 0.5F, 1.0F);
        worldRenderer.addVertexWithUV(1, 0, 4F/16F, 1.0F, 1.0F);
        worldRenderer.addVertexWithUV(1, 1, 4F/16F, 1.0F, 0.5F);
        worldRenderer.addVertexWithUV(0, 1, 4F/16F, 0.5F, 0.5F);
        
        worldRenderer.addVertexWithUV(1, 0, 0, 0F, 0.5F);
        worldRenderer.addVertexWithUV(0, 0, 0, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(0, 1, 0, 0.5F, 0F);
        worldRenderer.addVertexWithUV(1, 1, 0, 0F, 0F);
        
        worldRenderer.addVertexWithUV(1, 14F/16F, 0, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(0, 14F/16F, 0, 1F, 0.5F);
        worldRenderer.addVertexWithUV(0, 14F/16F, 1, 1F, 0F);
        worldRenderer.addVertexWithUV(1, 14F/16F, 1, 0.5F, 0F);
        
        worldRenderer.addVertexWithUV(0, 1F/16F, 0, 1F, 0.5F);
        worldRenderer.addVertexWithUV(1, 1F/16F, 0, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(1, 1F/16F, 1, 0.5F, 0F);
        worldRenderer.addVertexWithUV(0, 1F/16F, 1, 1F, 0F);
        
        worldRenderer.addVertexWithUV(0, 0, 0, 0F, 1.0F);
        worldRenderer.addVertexWithUV(0, 0, 1, 0F, 0.5F);
        worldRenderer.addVertexWithUV(0, 1, 1, 0.5F, 0.5F);
        worldRenderer.addVertexWithUV(0, 1, 0, 0.5F, 1.0F);
        
        worldRenderer.addVertexWithUV(1, 0, 1, 0F, 0.5F);
        worldRenderer.addVertexWithUV(1, 0, 0, 0F, 1.0F);
        worldRenderer.addVertexWithUV(1, 1, 0, 0.5F, 1.0F);
        worldRenderer.addVertexWithUV(1, 1, 1, 0.5F, 0.5F);
        
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
            texture = new DynamicTexture(75, 100);
            
            texture.setBlurMipmap(true, true);
        }
        
        GlStateManager.bindTexture(texture.getGlTextureId());
        System.arraycopy(tileEntityExternalDeviceMonitor.graphicsMemory, 0, texture.getTextureData(), 0, 75 * 100);
        texture.updateDynamicTexture();
        
        worldRenderer.startDrawing(GL11.GL_QUADS);
        
        worldRenderer.addVertexWithUV(14F/16F, 3F/16F, 0, 0F, 1F);
        worldRenderer.addVertexWithUV(2F/16F, 3F/16F, 0, 1F, 1F);
        worldRenderer.addVertexWithUV(2F/16F, 12F/16F, 0, 1F, 0F);
        worldRenderer.addVertexWithUV(14F/16F, 12/16F, 0, 0F, 0F);
        
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