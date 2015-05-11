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
public class RenderBlockVCPU32ComputerWire implements ISimpleBlockRenderingHandler
{
	public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer)
	{
	    double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        
        Tessellator tessellator = Tessellator.instance;
        this.bindTexture(AssemblyProgramCraftProxyClient.WIRE_PNG_PATH);
        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F + 2.0F / 16.0F, -0.5F + 5.0F / 16.0F, -0.5F);
        
        tessellator.startDrawingQuads();
        //上面
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z + 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z + 1, 0.5D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z, 0.5D, 0.0D);
        
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z, 0.0D, 0.0D);
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z + 1, 0.0D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z + 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z, 0.25D, 0.0D);
        
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z + 1, 0.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z + 1, 0.0D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z, 0.25D, 0.25D);
        
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z + 1, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z + 1, 0.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z, 0.0D, 0.25D);
        
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z, 0.0D, 0.25D);
        tessellator.addVertexWithUV(x, y + (4.0D/16.0D), z + 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z + 1, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + (4.0D/16.0D), z, 0.0D, 0.0D);
        //下面
        tessellator.addVertexWithUV(x, y, z, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y, z, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.5D, 0.25D);
        tessellator.addVertexWithUV(x, y, z + 1, 0.5D, 0.0D);
        
        tessellator.addVertexWithUV(x, y, z, 0.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y, z, 0.0D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x, y, z + 1, 0.25D, 0.0D);
        
        tessellator.addVertexWithUV(x, y, z, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y, z, 0.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.0D, 0.25D);
        tessellator.addVertexWithUV(x, y, z + 1, 0.25D, 0.25D);
        
        tessellator.addVertexWithUV(x, y, z, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, z + 1, 0.0D, 0.25D);
        
        tessellator.addVertexWithUV(x, y, z, 0.0D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.25D, 0.0D);
        tessellator.addVertexWithUV(x, y, z + 1, 0.0D, 0.0D);
        //前面
        tessellator.addVertexWithUV(x, y, z, 0.5D, 0.5D);
        tessellator.addVertexWithUV(x, y + 1, z, 0.5D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + 1, z, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z, 0.25D, 0.5D);
        //前面左方
        tessellator.addVertexWithUV(x, y, z + (5.0D/16.0D), 1.0D, 0.25D);
        tessellator.addVertexWithUV(x, y + 1, z + (5.0D/16.0D), 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + 1, z + (5.0D/16.0D), 0.75D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y, z + (5.0D/16.0D), 0.75D, 0.25D);
        //前面右方
        tessellator.addVertexWithUV(x, y, z + (5.0D/16.0D), 0.75D, 0.25D);
        tessellator.addVertexWithUV(x, y + 1, z + (5.0D/16.0D), 0.75D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + 1, z + (5.0D/16.0D), 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y, z + (5.0D/16.0D), 1.0D, 0.25D);
        //后面
        tessellator.addVertexWithUV(x, y, z + 1, 0.5D, 0.5D);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.25D, 0.5D);
        tessellator.addVertexWithUV(x + 1, y + 1, z + 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x, y + 1, z + 1, 0.5D, 0.25D);
        //后面左方
        tessellator.addVertexWithUV(x, y, z + (11.0D/16.0D), 1.0D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z + (11.0D/16.0D), 0.75D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + 1, z + (11.0D/16.0D), 0.75D, 0.0D);
        tessellator.addVertexWithUV(x, y + 1, z + (11.0D/16.0D), 1.0D, 0.0D);
        //后面右方
        tessellator.addVertexWithUV(x, y, z + (11.0D/16.0D), 0.75D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z + (11.0D/16.0D), 1.0D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + 1, z + (11.0D/16.0D), 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y + 1, z + (11.0D/16.0D), 0.75D, 0.0D);
        //右面
        tessellator.addVertexWithUV(x, y, z, 0.5D, 0.5D);
        tessellator.addVertexWithUV(x, y, z + 1, 0.25D, 0.5D);
        tessellator.addVertexWithUV(x, y + 1, z + 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x, y + 1, z, 0.5D, 0.25D);
        //右面左方
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y, z, 0.75D, 0.25D);
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y, z + 1, 1.0D, 0.25D);
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y + 1, z + 1, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y + 1, z, 0.75D, 0.0D);
        //右面右方
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y, z, 1.0D, 0.25D);
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y, z + 1, 0.75D, 0.25D);
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y + 1, z + 1, 0.75D, 0.0D);
        tessellator.addVertexWithUV(x + (5.0D/16.0D), y + 1, z, 1.0D, 0.0D);
        //左面
        tessellator.addVertexWithUV(x + 1, y, z, 0.5D, 0.5D);
        tessellator.addVertexWithUV(x + 1, y + 1, z, 0.5D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y + 1, z + 1, 0.25D, 0.25D);
        tessellator.addVertexWithUV(x + 1, y, z + 1, 0.25D, 0.5D);
        //左面左方
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y, z, 0.75D, 0.25D);
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y + 1, z, 0.75D, 0.0D);
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y + 1, z + 1, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y, z + 1, 1.0D, 0.25D);
        //左面右方
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y, z, 1.0D, 0.25D);
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y + 1, z, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y + 1, z + 1, 0.75D, 0.0D);
        tessellator.addVertexWithUV(x + (11.0D/16.0D), y, z + 1, 0.75D, 0.25D);

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
		return AssemblyProgramCraftProxyClient.RENDER_TYPE_BLOCK_VCPU_32_COMPUTER_WIRE;
	}
    public void bindTexture(ResourceLocation texture)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
    }
}