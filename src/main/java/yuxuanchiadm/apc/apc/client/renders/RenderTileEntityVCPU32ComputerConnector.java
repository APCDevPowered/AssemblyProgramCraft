package yuxuanchiadm.apc.apc.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityVCPU32ComputerConnector;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@SideOnly(Side.CLIENT)
public class RenderTileEntityVCPU32ComputerConnector extends TileEntitySpecialRenderer
{
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partial_tick_time)
	{
	    TileEntityVCPU32ComputerConnector tileentityvcpu32computerconnector = (TileEntityVCPU32ComputerConnector)tileentity;
        if(tileentityvcpu32computerconnector == null)
        {
            return;
        }
        int direction = tileentityvcpu32computerconnector.getBlockMetadata() & 3;
        bindTexture(AssemblyProgramCraftProxyClient.CONNECTOR_PNG_PATH);
        GL11.glDisable(GL11.GL_LIGHTING);
        Tessellator tessellator = Tessellator.instance;
        
        if(direction == 0)
        {
            tessellator.startDrawingQuads();
            
            tessellator.addVertexWithUV(x, y, z + (12.0/16.0), 0, 0.25);
            tessellator.addVertexWithUV(x + 1, y, z + (12.0/16.0), 0.25, 0.25);
            tessellator.addVertexWithUV(x + 1, y + 1, z + (12.0/16.0), 0.25, 0);
            tessellator.addVertexWithUV(x, y + 1, z + (12.0/16.0), 0, 0);
            
            tessellator.addVertexWithUV(x + 1, y, z, 0.25, 0.25);
            tessellator.addVertexWithUV(x, y, z, 0.5, 0.25);
            tessellator.addVertexWithUV(x, y + 1, z, 0.5, 0);
            tessellator.addVertexWithUV(x + 1, y + 1, z, 0.25, 0);
            
            tessellator.addVertexWithUV(x + (13.0/16), y, z + 1, 0.25, 0.5);
            tessellator.addVertexWithUV(x + (13.0/16), y, z, 0, 0.5);
            tessellator.addVertexWithUV(x + (13.0/16), y + 1, z, 0, 0.25);
            tessellator.addVertexWithUV(x + (13.0/16), y + 1, z + 1, 0.25, 0.25);
            
            tessellator.addVertexWithUV(x + (3.0/16), y, z, 0, 0.5);
            tessellator.addVertexWithUV(x + (3.0/16), y, z + 1, 0.25, 0.5);
            tessellator.addVertexWithUV(x + (3.0/16), y + 1, z + 1, 0.25, 0.25);
            tessellator.addVertexWithUV(x + (3.0/16), y + 1, z, 0, 0.25);
            
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z, 0.5, 0.25);
            tessellator.addVertexWithUV(x, y + (6.0/16), z, 0.25, 0.25);
            tessellator.addVertexWithUV(x, y + (6.0/16), z + 1, 0.25, 0.5);
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.5, 0.5);
            
            tessellator.addVertexWithUV(x, y, z, 0.75, 0.25);
            tessellator.addVertexWithUV(x + 1, y, z, 0.5, 0.25);
            tessellator.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.5);
            tessellator.addVertexWithUV(x, y, z + 1, 0.75, 0.5);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftProxyClient.COLORS_PNG_PATH);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (2.0/16), 0, 0.5);
                tessellator.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (2.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (3.0/16), 0.25, 0.25);
                tessellator.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (3.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (2.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (2.0/16), 0.5, 0.5);
                tessellator.addVertexWithUV(x + (10.0/16), y + (6.0/16), z + (3.0/16), 0.5, 0.25);
                tessellator.addVertexWithUV(x + (11.0/16), y + (6.0/16), z + (3.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        else if(direction == 1)
        {
            tessellator.startDrawingQuads();
            
            tessellator.addVertexWithUV(x + 1, y, z + 1, 0.25, 0.25);
            tessellator.addVertexWithUV(x + 1, y, z, 0.5, 0.25);
            tessellator.addVertexWithUV(x + 1, y + 1, z, 0.5, 0);
            tessellator.addVertexWithUV(x + 1, y + 1, z + 1, 0.25, 0);
            
            tessellator.addVertexWithUV(x + (4.0/16), y, z, 0, 0.25);
            tessellator.addVertexWithUV(x + (4.0/16), y, z + 1, 0.25, 0.25);
            tessellator.addVertexWithUV(x + (4.0/16), y + 1, z + 1, 0.25, 0);
            tessellator.addVertexWithUV(x + (4.0/16), y + 1, z, 0, 0);
            
            tessellator.addVertexWithUV(x, y, z + (13.0/16.0), 0.25, 0.5);
            tessellator.addVertexWithUV(x + 1, y, z + (13.0/16.0), 0, 0.5);
            tessellator.addVertexWithUV(x + 1, y + 1, z + (13.0/16.0), 0, 0.25);
            tessellator.addVertexWithUV(x, y + 1, z + (13.0/16.0), 0.25, 0.25);
            
            tessellator.addVertexWithUV(x + 1, y, z + (3.0/16.0), 0, 0.5);
            tessellator.addVertexWithUV(x, y, z + (3.0/16.0), 0.25, 0.5);
            tessellator.addVertexWithUV(x, y + 1, z + (3.0/16.0), 0.25, 0.25);
            tessellator.addVertexWithUV(x + 1, y + 1, z + (3.0/16.0), 0, 0.25);
            
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z, 0.25, 0.25);
            tessellator.addVertexWithUV(x, y + (6.0/16), z, 0.25, 0.5);
            tessellator.addVertexWithUV(x, y + (6.0/16), z + 1, 0.5, 0.5);
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.5, 0.25);
            
            tessellator.addVertexWithUV(x, y, z, 0.75, 0.5);
            tessellator.addVertexWithUV(x + 1, y, z, 0.75, 0.25);
            tessellator.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.25);
            tessellator.addVertexWithUV(x, y, z + 1, 0.5, 0.5);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftProxyClient.COLORS_PNG_PATH);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (10.0/16), 0, 0.5);
                tessellator.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (10.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (11.0/16), 0.25, 0.25);
                tessellator.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (11.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (10.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (10.0/16), 0.5, 0.5);
                tessellator.addVertexWithUV(x + (13.0/16), y + (6.0/16), z + (11.0/16), 0.5, 0.25);
                tessellator.addVertexWithUV(x + (14.0/16), y + (6.0/16), z + (11.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        else if(direction == 2)
        {
            tessellator.startDrawingQuads();
            
            tessellator.addVertexWithUV(x, y, z + 1, 0.25, 0.25);
            tessellator.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.25);
            tessellator.addVertexWithUV(x + 1, y + 1, z + 1, 0.5, 0);
            tessellator.addVertexWithUV(x, y + 1, z + 1, 0.25, 0);
            
            tessellator.addVertexWithUV(x + 1, y, z + (4.0/16.0), 0, 0.25);
            tessellator.addVertexWithUV(x, y, z + (4.0/16.0), 0.25, 0.25);
            tessellator.addVertexWithUV(x, y + 1, z + (4.0/16.0), 0.25, 0);
            tessellator.addVertexWithUV(x + 1, y + 1, z + (4.0/16.0), 0, 0);
            
            tessellator.addVertexWithUV(x + (13.0/16), y, z + 1, 0, 0.5);
            tessellator.addVertexWithUV(x + (13.0/16), y, z, 0.25, 0.5);
            tessellator.addVertexWithUV(x + (13.0/16), y + 1, z, 0.25, 0.25);
            tessellator.addVertexWithUV(x + (13.0/16), y + 1, z + 1, 0, 0.25);
            
            tessellator.addVertexWithUV(x + (3.0/16), y, z, 0.25, 0.5);
            tessellator.addVertexWithUV(x + (3.0/16), y, z + 1, 0, 0.5);
            tessellator.addVertexWithUV(x + (3.0/16), y + 1, z + 1, 0, 0.25);
            tessellator.addVertexWithUV(x + (3.0/16), y + 1, z, 0.25, 0.25);
            
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z, 0.25, 0.5);
            tessellator.addVertexWithUV(x, y + (6.0/16), z, 0.5, 0.5);
            tessellator.addVertexWithUV(x, y + (6.0/16), z + 1, 0.5, 0.25);
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.25, 0.25);
            
            tessellator.addVertexWithUV(x, y, z, 0.5, 0.5);
            tessellator.addVertexWithUV(x + 1, y, z, 0.75, 0.5);
            tessellator.addVertexWithUV(x + 1, y, z + 1, 0.75, 0.25);
            tessellator.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftProxyClient.COLORS_PNG_PATH);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (13.0/16), 0, 0.5);
                tessellator.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (13.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (14.0/16), 0.25, 0.25);
                tessellator.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (14.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (13.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (13.0/16), 0.5, 0.5);
                tessellator.addVertexWithUV(x + (5.0/16), y + (6.0/16), z + (14.0/16), 0.5, 0.25);
                tessellator.addVertexWithUV(x + (6.0/16), y + (6.0/16), z + (14.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        else if(direction == 3)
        {
            tessellator.startDrawingQuads();
            
            tessellator.addVertexWithUV(x, y, z, 0.25, 0.25);
            tessellator.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
            tessellator.addVertexWithUV(x, y + 1, z + 1, 0.5, 0);
            tessellator.addVertexWithUV(x, y + 1, z, 0.25, 0);
            
            tessellator.addVertexWithUV(x + (12.0/16), y, z + 1, 0, 0.25);
            tessellator.addVertexWithUV(x + (12.0/16), y, z, 0.25, 0.25);
            tessellator.addVertexWithUV(x + (12.0/16), y + 1, z, 0.25, 0);
            tessellator.addVertexWithUV(x + (12.0/16), y + 1, z + 1, 0, 0);
            
            tessellator.addVertexWithUV(x, y, z + (13.0/16.0), 0, 0.5);
            tessellator.addVertexWithUV(x + 1, y, z + (13.0/16.0), 0.25, 0.5);
            tessellator.addVertexWithUV(x + 1, y + 1, z + (13.0/16.0), 0.25, 0.25);
            tessellator.addVertexWithUV(x, y + 1, z + (13.0/16.0), 0, 0.25);
            
            tessellator.addVertexWithUV(x + 1, y, z + (3.0/16.0), 0.25, 0.5);
            tessellator.addVertexWithUV(x, y, z + (3.0/16.0), 0, 0.5);
            tessellator.addVertexWithUV(x, y + 1, z + (3.0/16.0), 0, 0.25);
            tessellator.addVertexWithUV(x + 1, y + 1, z + (3.0/16.0), 0.25, 0.25);
            
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z, 0.5, 0.5);
            tessellator.addVertexWithUV(x, y + (6.0/16), z, 0.5, 0.25);
            tessellator.addVertexWithUV(x, y + (6.0/16), z + 1, 0.25, 0.25);
            tessellator.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.25, 0.5);
            
            tessellator.addVertexWithUV(x, y, z, 0.75, 0.25);
            tessellator.addVertexWithUV(x + 1, y, z, 0.75, 0.5);
            tessellator.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.5);
            tessellator.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
            
            tessellator.draw();
            
            this.bindTexture(AssemblyProgramCraftProxyClient.COLORS_PNG_PATH);
            
            if(tileentityvcpu32computerconnector.hasPower())
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (5.0/16), 0, 0.5);
                tessellator.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (5.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (6.0/16), 0.25, 0.25);
                tessellator.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (6.0/16), 0, 0.25);
                
                tessellator.draw();
            }
            else
            {
                tessellator.startDrawingQuads();
                
                tessellator.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (5.0/16), 0.25, 0.5);
                tessellator.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (5.0/16), 0.5, 0.5);
                tessellator.addVertexWithUV(x + (2.0/16), y + (6.0/16), z + (6.0/16), 0.5, 0.25);
                tessellator.addVertexWithUV(x + (3.0/16), y + (6.0/16), z + (6.0/16), 0.25, 0.25);
                
                tessellator.draw();
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
	}
}