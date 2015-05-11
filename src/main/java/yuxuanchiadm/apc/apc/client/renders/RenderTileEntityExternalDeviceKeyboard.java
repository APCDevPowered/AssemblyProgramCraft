package yuxuanchiadm.apc.apc.client.renders;

import org.lwjgl.opengl.GL11;

import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceKeyboard;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderTileEntityExternalDeviceKeyboard extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partial_tick_time)
    {
        TileEntityExternalDeviceKeyboard tileEntityExternalDeviceKeyboard = (TileEntityExternalDeviceKeyboard)tileentity;
        if(tileEntityExternalDeviceKeyboard == null)
        {
            return;
        }
        int direction = tileEntityExternalDeviceKeyboard.getBlockMetadata() & 3;
        this.bindTexture(AssemblyProgramCraftProxyClient.KEYBOARD_PNG_PATH);
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
        
        GL11.glPopMatrix();
        
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
