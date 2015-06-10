package yuxuanchiadm.apc.apc.client.gui.components;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.client.gui.event.IEventNode;
import yuxuanchiadm.apc.apc.client.gui.structure.GuiVanillaBackground;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

@SideOnly(Side.CLIENT)
public class GuiInfoWindow extends Gui implements IEventNode
{
    //Auto close after 60 ticks(3 second)
    public static final int DEFAULT_DISPLAY_TICKS = 60;
    private Minecraft mc;
    private FontRenderer fontRenderer;
    private int xPos;
    private int yPos;
    private final int width;
    private final int height;
    private int infoTime;
    private boolean isForceOpen;
    private boolean isFocused;
    private GuiButton lockButton;
    private GuiButton closeButton;
    private GuiMultipleLineTextField textField;
    private GuiVanillaBackground vanillaBackground;
    
    public GuiInfoWindow(Minecraft mc, FontRenderer fontRenderer, int xPos, int yPos, int width, int height)
    {
        this.mc = mc;
        this.fontRenderer = fontRenderer;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.lockButton = new GuiButton(0, xPos + width - 37 - 48, yPos + height - 20 - 7, 37, 20, "锁定");
        this.closeButton = new GuiButton(1, xPos + width - 37 - 7, yPos + height - 20 - 7, 37, 20, "关闭");
        this.vanillaBackground = new GuiVanillaBackground(xPos, yPos, width, height);
        this.textField = new GuiMultipleLineTextField(fontRenderer, xPos + 5, yPos + 15, width - 10, height - 45, 0xFFFFFFFF, 0xFF000000, 0xFF000000, 0xFF0000FF, 0xFFFFFFFF);
        
        textField.setEditable(false);
    }
    public void drawInfoWindow(int mouseX, int mouseY)
    {
        if(isShowing())
        {
            vanillaBackground.drawVanillaBackground();
            fontRenderer.drawString("信息：", xPos + 5, yPos + 5, 0x404040);
            textField.drawMultipleLineTextField();
            if(isFocused)
            {
                closeButton.drawButton(mc, mouseX, mouseY);
                lockButton.drawButton(mc, mouseX, mouseY);
            }
            else
            {
                closeButton.drawButton(mc, Integer.MIN_VALUE, Integer.MIN_VALUE);
                lockButton.drawButton(mc, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
        }
    }
    public void displayInfoWindow(String info)
    {
        displayInfoWindow(info, DEFAULT_DISPLAY_TICKS);
    }
    public void displayInfoWindow(String info, int infoTime)
    {
        this.infoTime = infoTime;
        this.lockButton.enabled = true;
        this.isForceOpen = false;
        this.textField.setText(info);
    }
    public void checkTextFieldFocus(int mouseX, int mouseY)
    {
        if(mouseX >= textField.getXPos() && mouseY >= textField.getYPos() && mouseX < textField.getXPos() + textField.getWidth() && mouseY < textField.getYPos() + textField.getHeight())
        {
            this.lockButton.enabled = false;
            isForceOpen = true;
            textField.setFocused(true);
        }
        else
        {
            textField.setFocused(false);
        }
    }
    @Override
    public void keyPressed(char character, int key)
    {
        textField.keyPressed(character, key);
    }
    @Override
    public void keyReleased(char character, int key)
    {
        textField.keyReleased(character, key);
    }
    @Override
    public void mousePressed(int key, int mouseX, int mouseY)
    {
        if(isFocused)
        {
            checkTextFieldFocus(mouseX, mouseY);
            if(lockButton.mousePressed(mc, mouseX, mouseY))
            {
                lockButton.playPressSound(this.mc.getSoundHandler());
                lockButton.enabled = false;
                isForceOpen = true;
            }
            if(closeButton.mousePressed(mc, mouseX, mouseY))
            {
                closeButton.playPressSound(this.mc.getSoundHandler());
                lockButton.enabled = true;
                isForceOpen = false;
                infoTime = 0;
            }
        }
        textField.mousePressed(key, mouseX, mouseY);
    }
    @Override
    public void mouseReleased(int key, int mouseX, int mouseY)
    {
        textField.mouseReleased(key, mouseX, mouseY);
    }
    @Override
    public void mouseWheel(boolean state)
    {
        textField.mouseWheel(state);
    }
    @Override
    public void mouseMove(int mouseX, int mouseY, int offsetX, int offsetY)
    {
        textField.mouseMove(mouseX, mouseY, offsetX, offsetY);
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
        if(infoTime > 0)
        {
            infoTime--;
        }
        textField.tick();
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
    public boolean isShowing()
    {
        return isForceOpen || infoTime != 0;
    }
    public boolean isForceOpen()
    {
        return isForceOpen;
    }
}