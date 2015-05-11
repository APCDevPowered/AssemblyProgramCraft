package yuxuanchiadm.apc.apc.client.renders;

import org.lwjgl.opengl.GL11;

import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBlockExpansionConsoleScreen implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        this.bindTexture(AssemblyProgramCraftProxyClient.EXPANSION_CONSOLE_SCREEN_PNG_PATH);
        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glTranslatef(-0.4F, -0.4F, 0.0F);
        
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
        GL11.glVertex3f(1, 1, 0);
        GL11.glTexCoord2f(1F, 0.5F);
        GL11.glVertex3f(0, 1, 0);
        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex3f(0, 1, 1);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(1, 1, 1);
        
        GL11.glTexCoord2f(1F, 0.5F);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f(0.5F, 0.5F);
        GL11.glVertex3f(1, 0, 0);
        GL11.glTexCoord2f(0.5F, 0F);
        GL11.glVertex3f(1, 0, 1);
        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex3f(0, 0, 1);
        
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
        
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        return true;
    }
    @Override
    public boolean shouldRender3DInInventory(int paramInt)
    {
        return true;
    }
    @Override
    public int getRenderId()
    {
        return AssemblyProgramCraftProxyClient.RENDER_TYPE_BLOCK_EXPANSION_CONSOLE_SCREEN;
    }
    public void bindTexture(ResourceLocation texture)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
    }
}
