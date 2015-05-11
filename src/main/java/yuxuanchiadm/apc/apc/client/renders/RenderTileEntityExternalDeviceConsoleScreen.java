package yuxuanchiadm.apc.apc.client.renders;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import net.minecraftforge.fml.client.FMLClientHandler;
import yuxuanchiadm.apc.apc.client.AssemblyProgramCraftProxyClient;
import yuxuanchiadm.apc.apc.tileEntity.TileEntityExternalDeviceConsoleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityExternalDeviceConsoleScreen extends TileEntitySpecialRenderer
{
    public static byte[] glyphWidth = new byte[65536];
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    public static final int FONT_HEIGHT = 16;
    public static final int FONT_SPACTING = 1;
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partial_tick_time)
    {
        TileEntityExternalDeviceConsoleScreen tileEntityExternalDeviceConsoleScreen = (TileEntityExternalDeviceConsoleScreen)tileentity;
        if(tileEntityExternalDeviceConsoleScreen == null)
        {
            return;
        }
        Rectangle rectangle =  tileEntityExternalDeviceConsoleScreen.getMaximumRectangle();
        int direction = tileEntityExternalDeviceConsoleScreen.getBlockMetadata() & 3;
        int hardwareScale = tileEntityExternalDeviceConsoleScreen.hardwareScale;
        
        int offx = 0;
        int offy = 0;
        int offwidth = rectangle.getWidth();
        int offheight = rectangle.getHeight();
        if(direction == 0)
        {
            offx = rectangle.getX() - tileEntityExternalDeviceConsoleScreen.xCoord;
            offy = rectangle.getY() - tileEntityExternalDeviceConsoleScreen.yCoord;
        }
        if(direction == 2)
        {
            offx = rectangle.getX() - tileEntityExternalDeviceConsoleScreen.xCoord;
            if(offx != 0)
            {
                offx = offwidth - offx - 1;
            }
            else
            {
                offx = offwidth - 1;
            }
            offy = rectangle.getY() - tileEntityExternalDeviceConsoleScreen.yCoord;
        }
        if(direction == 1)
        {
            offx = rectangle.getX() - tileEntityExternalDeviceConsoleScreen.zCoord;
            offy = rectangle.getY() - tileEntityExternalDeviceConsoleScreen.yCoord;
        }
        if(direction == 3)
        {
            offx = rectangle.getX() - tileEntityExternalDeviceConsoleScreen.zCoord;
            if(offx != 0)
            {
                offx = offwidth - offx - 1;
            }
            else
            {
                offx = offwidth - 1;
            }
            offy = rectangle.getY() - tileEntityExternalDeviceConsoleScreen.yCoord;
        }
        this.bindTexture(AssemblyProgramCraftProxyClient.CONSOLE_SCREEN_PNG_PATH);
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
        
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glColor4f(0, 0, 0, 255);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glVertex3f(1, 0, 0);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 1, 0);
        GL11.glVertex3f(1, 1, 0);
        
        GL11.glEnd();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glPushMatrix();
        
        GL11.glRotatef(180F, 0, 0, 0);
        GL11.glTranslatef(-1, -1, 0);
        
        if(hardwareScale == 0)
        {
            hardwareScale = 8;
        }
        
        GL11.glScalef(1F/(16F * hardwareScale), 1F/(16F * hardwareScale), 1);
        drawMultipleLineTextWithMask(tileEntityExternalDeviceConsoleScreen.charBuffer,tileEntityExternalDeviceConsoleScreen.horizontalScale,tileEntityExternalDeviceConsoleScreen.verticalScale,-offx * 16 * hardwareScale,-offy * 16 * hardwareScale,16 * offwidth * hardwareScale,16 * offheight * hardwareScale,tileEntityExternalDeviceConsoleScreen.cursorEnabled,tileEntityExternalDeviceConsoleScreen.getTickCounter(),tileEntityExternalDeviceConsoleScreen.cursorLocation,tileEntityExternalDeviceConsoleScreen.cursorBlinkRate);
        
        GL11.glPopMatrix();
        
        GL11.glPopMatrix();
        
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    public void drawMultipleLineTextWithMask(int[] text,int scrollH,int scrollV,int maskX,int maskY,int maskW,int maskH,boolean cursorEnabled,int tickCounter,int cursor, int cursorBlinkRate)
    {
        int horizontalPos = 0;
        int verticalPos = 0;
        int horizontalPosTmp = 0;
        int verticalPosTmp = 0;
        for(int i = 0;i <= text.length;i+=2)
        {
            horizontalPosTmp = horizontalPos;
            verticalPosTmp = verticalPos;
            int charCount = i / 2;
            if(i >= text.length)
            {
                if(cursor == charCount)
                {
                    boolean drawCursor = cursorEnabled && tickCounter / cursorBlinkRate % 2 == 0;
                    if(drawCursor)
                    {
                        int topCut = ((verticalPosTmp + 1 - scrollV) < 0) ? -(verticalPosTmp + 1 - scrollV) : 0;
                        int buttomCut = (((verticalPosTmp + FONT_HEIGHT + 1) - (scrollV + maskH)) >0) ? ((verticalPosTmp + FONT_HEIGHT + 1) - (scrollV + maskH)) : 0;
                        int leftCut = ((horizontalPosTmp - scrollH) < 0) ? -(horizontalPosTmp - scrollH) : 0;
                        int rightCut = (((horizontalPosTmp + 1) - (scrollH + maskW)) > 0) ? ((horizontalPosTmp + 1) - (scrollH + maskW)) : 0;
                        if(buttomCut > 16)
                        {
                            buttomCut = 16;
                        }
                        if(topCut > 16)
                        {
                            topCut = 16;
                        }
                        if(leftCut > 1)
                        {
                            leftCut = 1;
                        }
                        if(rightCut > 1)
                        {
                            rightCut = 1;
                        }
                        drawRect(maskX - scrollH + horizontalPosTmp + leftCut, maskY - scrollV + verticalPosTmp + 1 + topCut, maskX - scrollH + horizontalPosTmp + 1 - rightCut, maskY - scrollV + verticalPosTmp + FONT_HEIGHT + 1 - buttomCut, 0xffffffff);
                    }
                }
                break;
            }
            char theChar = (char)text[i];
            int color = text[i + 1];
            
            int frontRed = color >> 28 & 15;
            int frontGreen = color >> 24 & 15;
            int frontBlue = color >> 20 & 15;
            
            int frontColor = 0xFF000000 | (frontRed * 17) << 16 | (frontGreen * 17) << 8 | (frontBlue * 17);
            
            int backgroundRed = color >> 16 & 15;
            int backgroundGreen = color >> 12 & 15;
            int backgroundBlue = color >> 8 & 15;
            
            int backgroundColor = 0xFF000000 | (backgroundRed * 17) << 16 | (backgroundGreen * 17) << 8 | (backgroundBlue * 17);
            
            boolean twinkling = (color >> 4 & 15) == 0 ? false : true;
            
            int width = getCharWidth(theChar);
            if(charCount != 0 && (charCount % 80) == 0)
            {
                horizontalPos = 0;
                verticalPos += FONT_HEIGHT + FONT_SPACTING  * 2;
            }
            if(theChar != 0)
            if(horizontalPos >= (scrollH - (width + FONT_SPACTING)) && verticalPos >= (scrollV - (FONT_HEIGHT + FONT_SPACTING)) && horizontalPos < (scrollH + maskW) && verticalPos < (scrollV + maskH))
            {
                int x = horizontalPos - scrollH + maskX;
                int y = verticalPos - scrollV + maskY;
                int topCut = ((verticalPos - scrollV) < 0) ? -(verticalPos - scrollV) : 0;
                int bottomCut = (((verticalPos + FONT_HEIGHT + FONT_SPACTING * 2) - (scrollV + maskH)) > 0) ? ((verticalPos + FONT_HEIGHT + FONT_SPACTING * 2) - (scrollV + maskH)) : 0;
                int leftCut = ((horizontalPos - scrollH) < 0) ? -(horizontalPos - scrollH) : 0;
                int rightCut = (((horizontalPos + width + FONT_SPACTING * 2) - (scrollH + maskW)) > 0) ? ((horizontalPos + width + FONT_SPACTING * 2) - (scrollH + maskW)) : 0;
                renderAdvancedCharAtPos(theChar,x,y,topCut,bottomCut,leftCut,rightCut,frontColor,backgroundColor,tickCounter,twinkling);
            }
            if(cursor == charCount)
            {
                if(cursorBlinkRate == 0)
                {
                    cursorBlinkRate = 8;
                }
                boolean drawCursor = cursorEnabled && tickCounter / cursorBlinkRate % 2 == 0;
                if(drawCursor)
                {
                    int topCut = ((verticalPosTmp + 1 - scrollV) < 0) ? -(verticalPosTmp + 1 - scrollV) : 0;
                    int buttomCut = (((verticalPosTmp + FONT_HEIGHT + 1) - (scrollV + maskH)) >0) ? ((verticalPosTmp + FONT_HEIGHT + 1) - (scrollV + maskH)) : 0;
                    int leftCut = ((horizontalPosTmp - scrollH) < 0) ? -(horizontalPosTmp - scrollH) : 0;
                    int rightCut = (((horizontalPosTmp + 1) - (scrollH + maskW)) > 0) ? ((horizontalPosTmp + 1) - (scrollH + maskW)) : 0;
                    if(buttomCut > 16)
                    {
                        buttomCut = 16;
                    }
                    if(topCut > 16)
                    {
                        topCut = 16;
                    }
                    if(leftCut > 1)
                    {
                        leftCut = 1;
                    }
                    if(rightCut > 1)
                    {
                        rightCut = 1;
                    }
                    drawRect(maskX - scrollH + horizontalPosTmp + leftCut, maskY - scrollV + verticalPosTmp + 1 + topCut, maskX - scrollH + horizontalPosTmp + 1 - rightCut, maskY - scrollV + verticalPosTmp + FONT_HEIGHT + 1 - buttomCut, 0xffffffff);
                }
            }
            horizontalPos += width + FONT_SPACTING * 2;
        }
    }
    public void renderAdvancedCharAtPos(char theChar,int xPos,int yPos,int topCut,int bottomCut,int leftCut,int rightCut,int frontColor,int backgroundColor, int tickCounter, boolean twinkling)
    {
        float red = (float)(frontColor >> 16 & 255) / 255.0F;
        float blue = (float)(frontColor >> 8 & 255) / 255.0F;
        float green = (float)(frontColor & 255) / 255.0F;
        float alpha = (float)(frontColor >> 24 & 255) / 255.0F;
        
        float backgroundRed = (float)(backgroundColor >> 16 & 255) / 255.0F;
        float backgroundBlue = (float)(backgroundColor >> 8 & 255) / 255.0F;
        float backgroundGreen = (float)(backgroundColor & 255) / 255.0F;
        float backgroundAlpha = (float)(backgroundColor >> 24 & 255) / 255.0F;
        
        int imageIndex = theChar / 256;
        loadGlyphTexture(imageIndex);
        int start = glyphWidth[theChar] >>> 4;
        int end = glyphWidth[theChar] & 15;
        double imageXCoord = (double)(theChar % 16 * 16) + start;
        double imageYCoord = (double)((theChar & 255) / 16 * 16);
        double width = (end + 1) - start;
        if(theChar == ' ')
        {
            width = 6.0D;
        }
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glColor4f(backgroundRed, backgroundGreen, backgroundBlue, backgroundAlpha);
        
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        
        GL11.glVertex3d(xPos + leftCut, yPos + topCut, 0.001D);
        GL11.glVertex3d(xPos + leftCut, yPos + 16.0D - bottomCut + FONT_SPACTING * 2, 0.001D);
        GL11.glVertex3d(xPos + width - rightCut + FONT_SPACTING * 2, yPos + topCut, 0.001D);
        GL11.glVertex3d(xPos + width - rightCut + FONT_SPACTING * 2, yPos + 16.0D - bottomCut + FONT_SPACTING * 2, 0.001D);
        
        GL11.glEnd();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        if(theChar != 0 && theChar != ' ')
        {
            if(twinkling)
            {
                if(tickCounter / 6 % 2 == 0)
                {
                    return;
                }
            }
            
            xPos += 1;
            yPos += 1;
            
            if(leftCut != 0)
            {
                leftCut -= 1;
            }
            if(topCut != 0)
            {
                leftCut -= 1;
            }
            if(rightCut != 0)
            {
                rightCut -= 1;
            }
            if(bottomCut != 0)
            {
                bottomCut -= 1;
            }
            
            GL11.glColor4f(red, blue, green, alpha);
            
            GL11.glBegin(GL11.GL_QUADS);
            
            GL11.glTexCoord2d(imageXCoord / 256.0D + leftCut / 256.0D, imageYCoord / 256.0D + topCut / 256.0D);
            GL11.glVertex3d(xPos + leftCut, yPos + topCut, 0.002D);
            GL11.glTexCoord2d(imageXCoord / 256.0D + leftCut / 256.0D, (imageYCoord + 16.0D) / 256.0D - bottomCut / 256.0D);
            GL11.glVertex3d(xPos + leftCut, yPos + 16.0D - bottomCut, 0.002D);
            GL11.glTexCoord2d((imageXCoord + width) / 256.0D - rightCut / 256.0D, (imageYCoord + 16.0D) / 256.0D - bottomCut / 256.0D);
            GL11.glVertex3d(xPos + width - rightCut, yPos + 16.0D - bottomCut, 0.002D);
            GL11.glTexCoord2d((imageXCoord + width) / 256.0D - rightCut / 256.0D, imageYCoord / 256.0D + topCut / 256.0D);
            GL11.glVertex3d(xPos + width - rightCut, yPos + topCut, 0.002D);
            
            GL11.glEnd();
        }
    }
    public static int getCharWidth(char theChar)
    {
        if (theChar == ' ')
        {
            return 6;
        }
        else
        {
            if (glyphWidth[theChar] != 0)
            {
                int start = glyphWidth[theChar] >>> 4;
                int end = glyphWidth[theChar] & 15;

                return (end + 1) - start;
            }
            else
            {
                return 0;
            }
        }
    }
    public static void loadGlyphTexture(int par1)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(getUnicodePageLocation(par1));
    }
    public static ResourceLocation getUnicodePageLocation(int par1)
    {
        if (unicodePageLocations[par1] == null)
        {
            unicodePageLocations[par1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] {Integer.valueOf(par1)}));
        }

        return unicodePageLocations[par1];
    }
    public static void drawRect(int par0, int par1, int par2, int par3, int par4)
    {
        int j1;

        if (par0 < par2)
        {
            j1 = par0;
            par0 = par2;
            par2 = j1;
        }

        if (par1 < par3)
        {
            j1 = par1;
            par1 = par3;
            par3 = j1;
        }

        float f = (float)(par4 >> 24 & 255) / 255.0F;
        float f1 = (float)(par4 >> 16 & 255) / 255.0F;
        float f2 = (float)(par4 >> 8 & 255) / 255.0F;
        float f3 = (float)(par4 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)par0, (double)par3, 0.002D);
        tessellator.addVertex((double)par2, (double)par3, 0.002D);
        tessellator.addVertex((double)par2, (double)par1, 0.002D);
        tessellator.addVertex((double)par0, (double)par1, 0.002D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    static
    {
        try
        {
            InputStream inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
            inputstream.read(glyphWidth);
        }
        catch (IOException var18)
        {
            throw new RuntimeException(var18);
        }
    }
}