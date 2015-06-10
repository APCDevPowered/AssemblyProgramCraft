package yuxuanchiadm.apc.apc.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.block.BlockExternalDeviceNumberMonitor;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDeviceNumberMonitor;
import net.minecraft.client.gui.FontRenderer;
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
public class RenderTileEntityExternalDeviceNumberMonitor extends TileEntitySpecialRenderer
{
    private static final ResourceLocation numberMonitorTextures = new ResourceLocation(AssemblyProgramCraft.MODID + ":" + "textures/tileentity/number_monitor.png");
    
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialBlockDamage)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        TileEntityExternalDeviceNumberMonitor tileEntityExternalDeviceNumberMonitor = (TileEntityExternalDeviceNumberMonitor)tileentity;
        if(tileEntityExternalDeviceNumberMonitor == null)
        {
            return;
        }
        EnumFacing face = (EnumFacing) tileEntityExternalDeviceNumberMonitor.getWorld().getBlockState(tileEntityExternalDeviceNumberMonitor.getPos()).getValue(BlockExternalDeviceNumberMonitor.FACING);
        
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
        
        worldRenderer.startDrawing(GL11.GL_QUADS);
        
        worldRenderer.addVertexWithUV(0, 0, 4F/16F, 0.5F, 1.0F);
        worldRenderer.addVertexWithUV(1, 0, 4F/16F, 1.0F, 1.0F);
        worldRenderer.addVertexWithUV(1, 1, 4F/16F, 1.0F, 0.5F);
        worldRenderer.addVertexWithUV(0, 1, 4F/16F, 0.5F, 0.5F);
        
        worldRenderer.addVertexWithUV(1, 0, 0, 0.5F, 1.0F);
        worldRenderer.addVertexWithUV(0, 0, 0, 1.0F, 1.0F);
        worldRenderer.addVertexWithUV(0, 1, 0, 1.0F, 0.5F);
        worldRenderer.addVertexWithUV(1, 1, 0, 0.5F, 0.5F);
        
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
        
        String number = String.valueOf(tileEntityExternalDeviceNumberMonitor.number);
        
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