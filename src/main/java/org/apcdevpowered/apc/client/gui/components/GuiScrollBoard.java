package org.apcdevpowered.apc.client.gui.components;

import org.apcdevpowered.apc.client.gui.event.IEventNode;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiScrollBoard extends Gui implements IEventNode
{
    private static final ResourceLocation scrollTextures = new ResourceLocation("AssemblyProgramCraft:textures/gui/scroll.png");
    private int xPos;
    private int yPos;
    private final int width;
    private final int height;
    private int scrollH;
    private int scrollV;
    private int objWidth;
    private int objHight;
    private boolean isLeftButtonDown;
    private boolean isRightButtonDown;
    private boolean isUpButtonDown;
    private boolean isDownButtonDown;
    private boolean isScrollVButtonDown;
    private boolean isScrollHButtonDown;
    private boolean isFocused;
    
    public GuiScrollBoard(int xPos, int yPos, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public boolean isScrolling()
    {
        return (isLeftButtonDown || isRightButtonDown || isDownButtonDown || isDownButtonDown || isScrollVButtonDown || isScrollHButtonDown);
    }
    public void drawHorizontalScrollButton(int x, int y, int length)
    {
        length -= 14;
        this.drawTexturedModalRect(x, y, 0, 0, 7, 14);
        this.drawTexturedModalRect(x + 7, y, 0, 28, 14, 14, length, 14);
        this.drawTexturedModalRect(x + 7 + length, y, 7, 0, 7, 14);
    }
    public void drawVerticalScrollButton(int x, int y, int length)
    {
        length -= 14;
        this.drawTexturedModalRect(x, y, 0, 0, 14, 7);
        this.drawTexturedModalRect(x, y + 7, 0, 14, 14, 14, 14, length);
        this.drawTexturedModalRect(x, y + 7 + length, 0, 7, 14, 7);
    }
    public void drawScrollBoard(int maxScrollH, int maxScrollV)
    {
        objWidth = maxScrollH;
        objHight = maxScrollV;
        checkBorder();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(scrollTextures);
        this.drawTexturedModalRect(xPos + width - 14, yPos + height - 14, 14, 0, 14, 14);
        this.drawTexturedModalRect(xPos, yPos + height - 14, 14, 0, 7, 14);
        this.drawTexturedModalRect(xPos + 7, yPos + height - 14, 14, 28, 14, 14, width - 28, 14);
        this.drawTexturedModalRect(xPos + width - 21, yPos + height - 14, 21, 0, 7, 14);
        this.drawTexturedModalRect(xPos + width - 14, yPos + height - 21, 14, 7, 14, 7);
        this.drawTexturedModalRect(xPos + width - 14, yPos + 7, 14, 14, 14, 14, 14, height - 28);
        this.drawTexturedModalRect(xPos + width - 14, yPos, 14, 0, 14, 7);
        // Draw bottoms
        this.drawTexturedModalRect(xPos, yPos + height - 14, 0, 0, 14, 14);
        if (isLeftButtonDown)
        {
            drawRect(xPos + 2, yPos + height - 12, xPos + 12, yPos + height - 2, 0xffb0b0b0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        this.drawTexturedModalRect(xPos, yPos + height - 14, 42, 42, 14, 14);
        this.drawTexturedModalRect(xPos + width - 28, yPos + height - 14, 0, 0, 14, 14);
        if (isRightButtonDown)
        {
            drawRect(xPos + width - 26, yPos + height - 12, xPos + width - 16, yPos + height - 2, 0xffb0b0b0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        this.drawTexturedModalRect(xPos + width - 28, yPos + height - 14, 42, 28, 14, 14);
        this.drawTexturedModalRect(xPos + width - 14, yPos + height - 28, 0, 0, 14, 14);
        if (isDownButtonDown)
        {
            drawRect(xPos + width - 12, yPos + height - 26, xPos + width - 2, yPos + height - 16, 0xffb0b0b0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        this.drawTexturedModalRect(xPos + width - 14, yPos + height - 28, 42, 14, 14, 14);
        this.drawTexturedModalRect(xPos + width - 14, yPos, 0, 0, 14, 14);
        if (isUpButtonDown)
        {
            drawRect(xPos + width - 12, yPos + 2, xPos + width - 2, yPos + 12, 0xffb0b0b0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        this.drawTexturedModalRect(xPos + width - 14, yPos, 42, 0, 14, 14);
        if (objWidth > width - 14)
        {
            int HorizontalMaxLength = (xPos + width - 28) - (xPos + 10);
            int HorizontalScrollButtonLength = (int) (((float) (width - 14) / (float) objWidth) * HorizontalMaxLength);
            HorizontalScrollButtonLength = HorizontalScrollButtonLength < 14 ? 14 : HorizontalScrollButtonLength;
            int HorizontalScrollButtonOffset = (int) (((float) scrollH / (float) objWidth) * (HorizontalMaxLength - HorizontalScrollButtonLength));
            drawHorizontalScrollButton(xPos + 12 + HorizontalScrollButtonOffset, yPos + height - 14, HorizontalScrollButtonLength);
        }
        if (objHight > height - 14)
        {
            int VerticalMaxLength = (yPos + height - 28) - (yPos + 10);
            int VerticalScrollButtonLength = (int) (((float) (height - 14) / (float) objHight) * VerticalMaxLength);
            VerticalScrollButtonLength = VerticalScrollButtonLength < 14 ? 14 : VerticalScrollButtonLength;
            int VerticalScrollButtonOffset = (int) (((float) scrollV / (float) objHight) * (VerticalMaxLength - VerticalScrollButtonLength));
            drawVerticalScrollButton(xPos + width - 14, yPos + 12 + VerticalScrollButtonOffset, VerticalScrollButtonLength);
        }
    }
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6, int par7, int par8)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV((double) (par1 + 0), (double) (par2 + par8), (double) this.zLevel, (double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + par6) * var8));
        worldRenderer.addVertexWithUV((double) (par1 + par7), (double) (par2 + par8), (double) this.zLevel, (double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + par6) * var8));
        worldRenderer.addVertexWithUV((double) (par1 + par7), (double) (par2 + 0), (double) this.zLevel, (double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + 0) * var8));
        worldRenderer.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), (double) this.zLevel, (double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + 0) * var8));
        tessellator.draw();
    }
    public void checkBorder()
    {
        if (scrollH < 0)
        {
            scrollH = 0;
        }
        if (scrollH > objWidth)
        {
            scrollH = objWidth;
        }
        if (scrollV < 0)
        {
            scrollV = 0;
        }
        if (scrollV > objHight)
        {
            scrollV = objHight;
        }
    }
    @Override
    public void keyReleased(char character, int key)
    {
    }
    @Override
    public void keyPressed(char character, int key)
    {
    }
    @Override
    public void mousePressed(int key, int mouseX, int mouseY)
    {
        if (isFocused)
        {
            if (key == 0)
            {
                if ((mouseX >= xPos + 1) && (mouseY >= (yPos + height - 14 + 1)) && (mouseX < (xPos + 14 - 1)) && (mouseY < (yPos + height - 1)))
                {
                    isLeftButtonDown = true;
                }
                else if ((mouseX >= (xPos + width - 28 + 1)) && (mouseY >= (yPos + height - 14 + 1)) && (mouseX < (xPos + width - 14 - 1)) && (mouseY < (yPos + height - 1)))
                {
                    isRightButtonDown = true;
                }
                else if ((mouseX >= (xPos + width - 14 + 1)) && (mouseY >= (yPos + height - 28 + 1)) && (mouseX < (xPos + width + 1)) && (mouseY < (yPos + height - 14 + 1)))
                {
                    isDownButtonDown = true;
                }
                else if ((mouseX >= (xPos + width - 14 + 1)) && (mouseY >= (yPos + 1)) && (mouseX < (xPos + width - 1)) && (mouseY < (yPos + 14 - 1)))
                {
                    isUpButtonDown = true;
                }
                else
                {
                    int HorizontalMaxLength = (xPos + width - 28) - (xPos + 10);
                    int HorizontalScrollButtonLength = (int) (((float) (width - 14) / (float) objWidth) * HorizontalMaxLength);
                    HorizontalScrollButtonLength = HorizontalScrollButtonLength < 14 ? 14 : HorizontalScrollButtonLength;
                    int HorizontalScrollButtonOffset = (int) (((float) scrollH / (float) objWidth) * (HorizontalMaxLength - HorizontalScrollButtonLength));
                    if ((mouseX >= (xPos + 12 + HorizontalScrollButtonOffset + 1)) && (mouseY >= (yPos + height - 14 + 1)) && (mouseX < (xPos + 12 + HorizontalScrollButtonOffset + HorizontalScrollButtonLength - 1)) && (mouseY < yPos + height - 1))
                    {
                        isScrollHButtonDown = true;
                    }
                    int VerticalMaxLength = (yPos + height - 28) - (yPos + 10);
                    int VerticalScrollButtonLength = (int) (((float) (height - 14) / (float) objHight) * VerticalMaxLength);
                    VerticalScrollButtonLength = VerticalScrollButtonLength < 14 ? 14 : VerticalScrollButtonLength;
                    int VerticalScrollButtonOffset = (int) (((float) scrollV / (float) objHight) * (VerticalMaxLength - VerticalScrollButtonLength));
                    if ((mouseX >= (xPos + width - 14 + 1)) && (mouseY >= (yPos + 12 + VerticalScrollButtonOffset + 1)) && (mouseX < (xPos + width - 1)) && (mouseY < (yPos + 12 + VerticalScrollButtonOffset + VerticalScrollButtonLength - 1)))
                    {
                        isScrollVButtonDown = true;
                    }
                }
            }
        }
    }
    @Override
    public void mouseReleased(int key, int mouseX, int mouseY)
    {
        if (isFocused)
        {
            if (key == 0)
            {
                isLeftButtonDown = false;
                isRightButtonDown = false;
                isUpButtonDown = false;
                isDownButtonDown = false;
                isScrollHButtonDown = false;
                isScrollVButtonDown = false;
            }
        }
    }
    @Override
    public void mouseWheel(boolean state)
    {
        if (isFocused)
        {
            if (state == true)
            {
                scrollV -= 30;
            }
            else
            {
                scrollV += 30;
            }
            checkBorder();
        }
    }
    @Override
    public void mouseMove(int mouseX, int mouseY, int offsetX, int offsetY)
    {
        if (isFocused)
        {
            if (isScrollHButtonDown)
            {
                int HorizontalMaxLength = (xPos + width - 28) - (xPos + 10);
                int HorizontalScrollButtonLength = (int) (((float) (width - 14) / (float) objWidth) * HorizontalMaxLength);
                int HorizontalScrollStartPox = xPos + 12 + HorizontalScrollButtonLength / 2;
                int HorizontalScrollMaxPox = xPos + width - 28 - HorizontalScrollButtonLength / 2;
                int HorizontalScrollCanMove = HorizontalScrollMaxPox - HorizontalScrollStartPox;
                int offset = mouseX - HorizontalScrollStartPox;
                scrollH = (int) (objWidth * ((float) offset / (float) HorizontalScrollCanMove));
            }
            if (isScrollVButtonDown)
            {
                int VerticalMaxLength = (yPos + height - 28) - (yPos + 10);
                int VerticalScrollButtonLength = (int) (((float) (height - 14) / (float) objHight) * VerticalMaxLength);
                int VerticalScrollStartPox = yPos + 12 + VerticalScrollButtonLength / 2;
                int VerticalScrollMaxPox = yPos + height - 28 - VerticalScrollButtonLength / 2;
                int VerticalScrollCanMove = VerticalScrollMaxPox - VerticalScrollStartPox;
                int offset = mouseY - VerticalScrollStartPox;
                scrollV = (int) (objHight * ((float) offset / (float) VerticalScrollCanMove));
            }
            checkBorder();
        }
    }
    public int getScrollH()
    {
        return scrollH;
    }
    public void setScrollH(int scrollH)
    {
        this.scrollH = scrollH;
        checkBorder();
    }
    public int getScrollV()
    {
        return scrollV;
    }
    public void setScrollV(int scrollV)
    {
        this.scrollV = scrollV;
        checkBorder();
    }
    public void setMaxScroll(int maxScrollH, int maxScrollV)
    {
        objWidth = maxScrollH;
        objHight = maxScrollV;
        checkBorder();
    }
    @Override
    public void setFocused(boolean isFocused)
    {
        this.isFocused = isFocused;
    }
    @Override
    public boolean isFocused()
    {
        return isFocused;
    }
    @Override
    public void tick()
    {
        if (isLeftButtonDown)
        {
            scrollH -= 5;
            checkBorder();
        }
        if (isRightButtonDown)
        {
            scrollH += 5;
            checkBorder();
        }
        if (isDownButtonDown)
        {
            scrollV += 5;
            checkBorder();
        }
        if (isUpButtonDown)
        {
            scrollV -= 5;
            checkBorder();
        }
    }
}