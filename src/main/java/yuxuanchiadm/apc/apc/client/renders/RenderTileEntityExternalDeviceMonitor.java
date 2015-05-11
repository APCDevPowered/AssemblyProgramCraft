package yuxuanchiadm.apc.apc.client.renders;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceMonitor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderTileEntityExternalDeviceMonitor extends TileEntitySpecialRenderer
{
    private static IntBuffer imageData = ByteBuffer.allocateDirect(0x1000000).order(ByteOrder.nativeOrder()).asIntBuffer();
    private int texID = -1;
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partial_tick_time)
    {
        TileEntityExternalDeviceMonitor tileEntityExternalDeviceMonitor = (TileEntityExternalDeviceMonitor)tileentity;
        if(tileEntityExternalDeviceMonitor == null)
        {
            return;
        }
        int direction = tileEntityExternalDeviceMonitor.getBlockMetadata() & 3;
        this.bindTexture(AssemblyProgramCraftProxyClient.MONITOR_PNG_PATH);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GL11.glPushMatrix();
        
        GL11.glTranslated(x, y, z);
        if(direction == 1)
        {
            GL11.glTranslated(1, 0, 0);
            GL11.glRotated(270F, 0, 1, 0);
        }
        else if(direction == 2)
        {
            GL11.glTranslated(1, 0, 1);
            GL11.glRotated(180F, 0, 1, 0);
        }
        else if(direction == 3)
        {
            GL11.glTranslated(0, 0, 1);
            GL11.glRotated(90F, 0, 1, 0);
        }
        
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
        
        imageData.clear();
        imageData.put(tileEntityExternalDeviceMonitor.graphicsMemory);
        imageData.position(0).limit(tileEntityExternalDeviceMonitor.graphicsMemory.length);
        
        if(texID == -1)
        {
            texID = GL11.glGenTextures();
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
            GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); 
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
        
        GL11.glPopMatrix();
        
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}