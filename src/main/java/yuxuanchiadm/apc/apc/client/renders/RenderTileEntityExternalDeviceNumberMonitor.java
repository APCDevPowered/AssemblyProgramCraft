package yuxuanchiadm.apc.apc.client.renders;

import org.lwjgl.opengl.GL11;

import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceNumberMonitor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderTileEntityExternalDeviceNumberMonitor extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partial_tick_time)
    {
        TileEntityExternalDeviceNumberMonitor tileEntityExternalDeviceNumberMonitor = (TileEntityExternalDeviceNumberMonitor)tileentity;
        if(tileEntityExternalDeviceNumberMonitor == null)
        {
            return;
        }
        int direction = tileEntityExternalDeviceNumberMonitor.getBlockMetadata() & 3;
        this.bindTexture(AssemblyProgramCraftProxyClient.NUMBER_MONITOR_PNG_PATH);
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
        
        GL11.glPushMatrix();
        
        String number = String.valueOf(tileEntityExternalDeviceNumberMonitor.number);
        
        FontRenderer fontRenderer = this.func_147498_b();
        
        GL11.glRotatef(180F, 0, 0, 0);
        GL11.glTranslatef(-0.5F - fontRenderer.getStringWidth(number) * (1F/64F) / 2, -0.5F - 8 * (1F/64F) / 2, 0.01F);
        GL11.glScalef(1F/64F, 1F/64F, 1F/64F);
        
        fontRenderer.drawString(number, 0, 0, 0x404040, false);
        
        GL11.glPopMatrix();
        
        GL11.glPopMatrix();
        
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}