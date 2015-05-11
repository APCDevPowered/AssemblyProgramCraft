package yuxuanchiadm.apc.apc.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32Computer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@SideOnly(Side.CLIENT)
public class RenderTileEntityVCPU32Computer extends TileEntitySpecialRenderer
{
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partial_tick_time)
    {
        TileEntityVCPU32Computer tileentityvcpu32computer = (TileEntityVCPU32Computer)tileentity;
        if(tileentityvcpu32computer == null)
        {
            return;
        }
        int direction = tileentityvcpu32computer.getBlockMetadata() & 3;
        
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GL11.glPushMatrix();
        
        this.bindTexture(AssemblyProgramCraftProxyClient.COMPUTER_PNG_PATH);
        
        GL11.glTranslated(x, y, z);
        
        if(direction == 0)
        {
            GL11.glRotated(0, 1, 0, 0);
        }
        else if(direction == 1)
        {
            GL11.glRotated(-90, 0, 1, 0);
            GL11.glTranslated(0, 0, -1);
        }
        else if(direction == 2)
        {
            GL11.glRotated(-180, 0, 1, 0);
            GL11.glTranslated(-1, 0, -1);
        }
        else if(direction == 3)
        {
            GL11.glRotated(-270, 0, 1, 0);
            GL11.glTranslated(-1, 0, 0);
        }
        
        Tessellator tessellator = Tessellator.instance;
        
        tessellator.startDrawingQuads();
        
        tessellator.addVertexWithUV(1, 0, (4.0/16.0), 0, 0.5);
        tessellator.addVertexWithUV(0, 0, (4.0/16.0), 0.5, 0.5);
        tessellator.addVertexWithUV(0, 1, (4.0/16.0), 0.5, 0);
        tessellator.addVertexWithUV(1, 1, (4.0/16.0), 0, 0);
        
        tessellator.addVertexWithUV(0, 0, 1, 0.5, 0.5);
        tessellator.addVertexWithUV(1, 0, 1, 1, 0.5);
        tessellator.addVertexWithUV(1, 1, 1, 1, 0);
        tessellator.addVertexWithUV(0, 1, 1, 0.5, 0);
        
        tessellator.addVertexWithUV((4.0/16.0), 0, 0, 0, 1);
        tessellator.addVertexWithUV((4.0/16.0), 0, 1, 0.5, 1);
        tessellator.addVertexWithUV((4.0/16.0), 1, 1, 0.5, 0.5);
        tessellator.addVertexWithUV((4.0/16.0), 1, 0, 0, 0.5);
        
        tessellator.addVertexWithUV((12.0/16.0), 0, 1, 0.5, 1);
        tessellator.addVertexWithUV((12.0/16.0), 0, 0, 0, 1);
        tessellator.addVertexWithUV((12.0/16.0), 1, 0, 0, 0.5);
        tessellator.addVertexWithUV((12.0/16.0), 1, 1, 0.5, 0.5);
        
        tessellator.addVertexWithUV(1, (12.0/16.0), 0, 0.5, 0.5);
        tessellator.addVertexWithUV(0, (12.0/16.0), 0, 0.5, 1);
        tessellator.addVertexWithUV(0, (12.0/16.0), 1, 1, 1);
        tessellator.addVertexWithUV(1, (12.0/16.0), 1, 1, 0.5);
        
        tessellator.addVertexWithUV(0, 0, 0, 0.5, 1);
        tessellator.addVertexWithUV(1, 0, 0, 0.5, 0.5);
        tessellator.addVertexWithUV(1, 0, 1, 1, 0.5);
        tessellator.addVertexWithUV(0, 0, 1, 1, 1);
        
        tessellator.draw();
        
        this.bindTexture(AssemblyProgramCraftProxyClient.COLORS_PNG_PATH);
        
        if(tileentityvcpu32computer.isError())
        {
            tessellator.startDrawingQuads();
            
            tessellator.addVertexWithUV((7.0/16.0), (5.0/16.0), (4.0/16.0), 0.25, 0.25);
            tessellator.addVertexWithUV((6.0/16.0), (5.0/16.0), (4.0/16.0), 0.5, 0.25);
            tessellator.addVertexWithUV((6.0/16.0), (6.0/16.0), (4.0/16.0), 0.5, 0);
            tessellator.addVertexWithUV((7.0/16.0), (6.0/16.0), (4.0/16.0), 0.25, 0);
            
            tessellator.draw();
        }
        else
        {
            if(tileentityvcpu32computer.isRunning())
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV((7.0/16.0), (5.0/16.0), (4.0/16.0), 0, 0.5);
                tessellator.addVertexWithUV((6.0/16.0), (5.0/16.0), (4.0/16.0), 0.25, 0.5);
                tessellator.addVertexWithUV((6.0/16.0), (6.0/16.0), (4.0/16.0), 0.25, 0.25);
                tessellator.addVertexWithUV((7.0/16.0), (6.0/16.0), (4.0/16.0), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV((7.0/16.0), (5.0/16.0), (4.0/16.0), 0.25, 0.5);
                tessellator.addVertexWithUV((6.0/16.0), (5.0/16.0), (4.0/16.0), 0.5, 0.5);
                tessellator.addVertexWithUV((6.0/16.0), (6.0/16.0), (4.0/16.0), 0.5, 0.25);
                tessellator.addVertexWithUV((7.0/16.0), (6.0/16.0), (4.0/16.0), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
                
        GL11.glPopMatrix();
        
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}