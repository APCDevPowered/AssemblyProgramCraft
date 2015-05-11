package yuxuanchiadm.apc.apc.gui.structure;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiVanillaBackground extends Gui
{
    private static final ResourceLocation vanillaBackgroundTextures = new ResourceLocation("AssemblyProgramCraft:textures/gui/VanillaBackground.png");

    private int xPos;
    private int yPos;
    private final int width;
    private final int height;
    
    public GuiVanillaBackground(int xPos, int yPos, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public void drawVanillaBackground()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(vanillaBackgroundTextures);
        //LeftUp
        this.drawTexturedModalRect(xPos, yPos, 0, 0, 4, 4);
        //RightUp
        this.drawTexturedModalRect(xPos + width - 4, yPos, 8, 0, 4, 4);
        //LeftDown
        this.drawTexturedModalRect(xPos, yPos + height - 4, 0, 8, 4, 4);
        //RightDown
        this.drawTexturedModalRect(xPos + width - 4, yPos + height - 4, 8, 8, 4, 4);
        //Left
        this.drawTexturedModalRect(xPos, yPos + 4, 0, 4, 4, 4, 4, height - 8);
        //Right
        this.drawTexturedModalRect(xPos + width - 4, yPos + 4, 8, 4, 4, 4, 4, height - 8);
        //Up
        this.drawTexturedModalRect(xPos + 4, yPos, 4, 0, 4, 4, width - 8, 4);
        //Down
        this.drawTexturedModalRect(xPos + 4, yPos + height - 4, 4, 8, 4, 4, width - 8, 4);
        //Central
        this.drawTexturedModalRect(xPos + 4, yPos + 4, 4, 4, 4, 4, width - 8, height - 8);
    }
    public void drawTexturedModalRect(int x, int y, int u, int v, int uOffset, int vOffset, int width, int height)
    {
        float uMultiplier = 0.00390625F;
        float vMultiplier = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(u + 0) * uMultiplier), (double)((float)(v + vOffset) * vMultiplier));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + uOffset) * uMultiplier), (double)((float)(v + vOffset) * vMultiplier));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(u + uOffset) * uMultiplier), (double)((float)(v + 0) * vMultiplier));
        worldrenderer.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * uMultiplier), (double)((float)(v + 0) * vMultiplier));
        tessellator.draw();
    }
    public int getXPos()
    {
        return xPos;
    }
    public int getYPos()
    {
        return yPos;
    }
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }
}