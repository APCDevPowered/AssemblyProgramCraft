package yuxuanchiadm.apc.apc.client.renders;

import org.lwjgl.opengl.GL11;

import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlockVCPU32ComputerConnector implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer)
    {
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        
        Tessellator tessellator = Tessellator.instance;
        this.bindTexture(AssemblyProgramCraftProxyClient.CONNECTOR_PNG_PATH);
        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F + 2.0F / 16.0F, -0.5F + 5.0F / 16.0F, -0.5F);
        tessellator.startDrawingQuads();
        
        tessellator.addVertexWithUV(x, y, z, 0.25, 0.25);
        tessellator.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
        tessellator.addVertexWithUV(x, y + 1, z + 1, 0.5, 0);
        tessellator.addVertexWithUV(x, y + 1, z, 0.25, 0);
        
        tessellator.addVertexWithUV(x + (12.0/16), y, z + 1, 0.25, 0.25);
        tessellator.addVertexWithUV(x + (12.0/16), y, z, 0, 0.25);
        tessellator.addVertexWithUV(x + (12.0/16), y + 1, z, 0, 0);
        tessellator.addVertexWithUV(x + (12.0/16), y + 1, z + 1, 0.25, 0);
        
        tessellator.addVertexWithUV(x, y, z + (13.0/16.0), 0, 0.5);
        tessellator.addVertexWithUV(x + 1, y, z + (13.0/16.0), 0.25, 0.5);
        tessellator.addVertexWithUV(x + 1, y + 1, z + (13.0/16.0), 0.25, 0.25);
        tessellator.addVertexWithUV(x, y + 1, z + (13.0/16.0), 0, 0.25);
        
        tessellator.addVertexWithUV(x + 1, y, z + (3.0/16.0), 0.25, 0.5);
        tessellator.addVertexWithUV(x, y, z + (3.0/16.0), 0, 0.5);
        tessellator.addVertexWithUV(x, y + 1, z + (3.0/16.0), 0, 0.25);
        tessellator.addVertexWithUV(x + 1, y + 1, z + (3.0/16.0), 0.25, 0.25);
        
        tessellator.addVertexWithUV(x + 1, y + (6.0/16), z, 0.25, 0.5);
        tessellator.addVertexWithUV(x, y + (6.0/16), z, 0.25, 0.25);
        tessellator.addVertexWithUV(x, y + (6.0/16), z + 1, 0.5, 0.25);
        tessellator.addVertexWithUV(x + 1, y + (6.0/16), z + 1, 0.5, 0.5);
        
        tessellator.addVertexWithUV(x, y, z, 0.75, 0.25);
        tessellator.addVertexWithUV(x + 1, y, z, 0.75, 0.5);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.5, 0.5);
        tessellator.addVertexWithUV(x, y, z + 1, 0.5, 0.25);
        
        tessellator.draw();
        
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return true;
	}
	public boolean shouldRender3DInInventory(int paramInt)
	{
		return true;
	}
	public int getRenderId()
	{
		return AssemblyProgramCraftProxyClient.RENDER_TYPE_BLOCK_VCPU_32_COMPUTER_CONNECTOR;
	}
    public void bindTexture(ResourceLocation texture)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
    }
}