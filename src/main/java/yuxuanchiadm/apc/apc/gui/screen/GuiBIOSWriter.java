package yuxuanchiadm.apc.apc.gui.screen;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.container.ContainerBIOSWriter;
import yuxuanchiadm.apc.apc.gui.components.GuiInfoWindow;
import yuxuanchiadm.apc.apc.gui.components.GuiMultipleLineTextField;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiBIOSWriter extends GuiContainer
{
    private static final ResourceLocation guiBIOSWriteTextures = new ResourceLocation("AssemblyProgramCraft:textures/gui/GuiBIOSWriter.png");
    private static String programCatch = "";
    private GuiMultipleLineTextField textField;
    private GuiInfoWindow guiInfoWindow;
    private GuiButton compiledSource;
    private GuiButton writeBytecode;
    private GuiButton decompiledSource;
    
    public GuiBIOSWriter(InventoryPlayer inventory)
    {
        super(new ContainerBIOSWriter(inventory));
        this.xSize = 256;
        this.ySize = 231;
    }
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        compiledSource = new GuiButton(0, this.width / 2 + 45, this.height / 2  + 33, 37, 20, "汇编");
        writeBytecode = new GuiButton(1, this.width / 2 + 45, this.height / 2  + 57, 37, 20, "写入");
        decompiledSource = new GuiButton(2, this.width / 2 + 85, this.height / 2 + 33, 37, 20, "反编译");
        this.buttonList.add(compiledSource);
        this.buttonList.add(writeBytecode);
        this.buttonList.add(decompiledSource);
        Keyboard.enableRepeatEvents(true);
        if(this.textField != null)
        {
            programCatch = this.textField.getText();
        }
        this.textField = new GuiMultipleLineTextField(this.fontRendererObj, (this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 10, 236, 131);
        this.textField.setText(programCatch);
        this.guiInfoWindow = new GuiInfoWindow(mc, fontRendererObj, (this.width - 216) / 2, (this.height - 146) / 2 - 25, 216, 146);
    }
    public void displayInfoWindow(String info)
    {
        guiInfoWindow.displayInfoWindow(info);
        guiInfoWindow.setFocused(true);
        textField.setFocused(false);
    }
    public void setProgram(String program)
    {
        textField.setText(program);
    }
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if(par1GuiButton.id == 0)
        {
            AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
            pak.packetType = AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoCompiled.getValue();
            pak.dataByte = new byte[1];
            pak.dataString = new String[1];
            pak.dataByte[0] = (byte)inventorySlots.windowId;
            pak.dataString[0] = textField.getText();
            AssemblyProgramCraft.sendToServer(pak);
        }
        else if(par1GuiButton.id == 1)
        {
            AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
            pak.packetType = AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoWrite.getValue();
            pak.dataByte = new byte[1];
            pak.dataByte[0] = (byte)inventorySlots.windowId;
            AssemblyProgramCraft.sendToServer(pak);
        }
        else if(par1GuiButton.id == 2)
        {
            AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
            pak.packetType = AssemblyProgramCraftPacket.ClientPacket.BIOSWriterDoDecompiled.getValue();
            pak.dataByte = new byte[1];
            pak.dataByte[0] = (byte)inventorySlots.windowId;
            AssemblyProgramCraft.sendToServer(pak);
        }
    }
    public void updateScreen()
    {
        if(!guiInfoWindow.isShowing())
        {
            guiInfoWindow.setFocused(false);
        }
        this.textField.tick();
        this.guiInfoWindow.tick();
        if(!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
        {
            this.mc.thePlayer.closeScreen();
        }
    }
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        programCatch = this.textField.getText();
        if (this.mc.thePlayer != null)
        {
            this.inventorySlots.onContainerClosed(this.mc.thePlayer);
        }
    }
    public void handleMouseInput()
    {
        if(!guiInfoWindow.isShowing())
        {
            super.handleMouseInput();
        }
        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        int mouseX = Mouse.getX() * var2 / this.mc.displayWidth;
        int mouseY = var3 - Mouse.getY() * var3 / this.mc.displayHeight - 1;
        int eventButton = Mouse.getEventButton();
        switch (eventButton)
        {
            case -1:
                int wheel = Mouse.getEventDWheel();
                if (wheel != 0)
                {
                    textField.mouseWheel((wheel < 0) ? false : true);
                    guiInfoWindow.mouseWheel((wheel < 0) ? false : true);
                }
                else
                {
                    textField.mouseMove(mouseX, mouseY, Mouse.getEventDX(), -Mouse.getEventDY());
                    guiInfoWindow.mouseMove(mouseX, mouseY, Mouse.getEventDX(), -Mouse.getEventDY());
                }
                break;
            default:
                if (Mouse.getEventButtonState())
                {
                    if(!guiInfoWindow.isShowing() && mouseX >= textField.getXPos() && mouseY >= textField.getYPos() && mouseX < textField.getXPos() + textField.getWidth() && mouseY < textField.getYPos() + textField.getHeight())
                    {
                        textField.setFocused(true);
                    }
                    else
                    {
                        textField.setFocused(false);
                    }
                    textField.mousePressed(eventButton, mouseX, mouseY);
                    guiInfoWindow.mousePressed(eventButton, mouseX, mouseY);
                }
                else
                {
                    textField.mouseReleased(eventButton, mouseX, mouseY);
                    guiInfoWindow.mouseReleased(eventButton, mouseX, mouseY);
                }
                break;
        }
    }
    public void handleKeyboardInput()
    {
        int key = Keyboard.getEventKey();
        char character = Keyboard.getEventCharacter();
        if (Keyboard.getEventKeyState())
        {
            if(key == 87)
            {
                this.mc.toggleFullscreen();
            }
            else if (key == 1)
            {
                this.mc.thePlayer.closeScreen();
            }
            else
            {
                textField.keyPressed(character, key);
                guiInfoWindow.keyPressed(character, key);
                if(!guiInfoWindow.isShowing() && !guiInfoWindow.isFocused() && !textField.isFocused())
                {
                    keyTyped(character, key);
                }
            }
        }
        else
        {
            textField.keyReleased(character, key);
            guiInfoWindow.keyReleased(character, key);
        }
    }
    @SuppressWarnings("unchecked")
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks)
    {
        if(!guiInfoWindow.isShowing())
        {
            super.drawScreen(mouseX, mouseY, renderPartialTicks);
        }
        else
        {
            drawDefaultBackground();
            drawGuiContainerBackgroundLayer(renderPartialTicks, mouseX, mouseY);
            for(GuiButton guiButton : ((List<GuiButton>)buttonList))
            {
                guiButton.drawButton(this.mc, Integer.MIN_VALUE, Integer.MIN_VALUE);
            }
            for(GuiLabel guiLabel : (List<GuiLabel>)labelList)
            {
                guiLabel.func_146159_a(this.mc, Integer.MIN_VALUE, Integer.MIN_VALUE);
            }
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            GL11.glTranslatef(guiLeft, guiTop, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); i1++)
            {
                Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i1);
                drawSlot(slot);
            }
            GL11.glDisable(GL11.GL_LIGHTING);
            drawGuiContainerForegroundLayer(mouseX, mouseY);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }
    private void drawSlot(Slot slot)
    {
        int xDisplayPosition = slot.xDisplayPosition;
        int yDisplayPosition = slot.yDisplayPosition;
        ItemStack itemstack = slot.getStack();
        if (itemstack == null)
        {
            IIcon iicon = slot.getBackgroundIconIndex();
            if (iicon != null)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
                drawTexturedModelRectFromIcon(xDisplayPosition, yDisplayPosition, iicon, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, 0.0D, -50.0D);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, xDisplayPosition, yDisplayPosition);
        GL11.glPopMatrix();
        itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, xDisplayPosition, yDisplayPosition, null);
    }
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY)
    {
        textField.drawMultipleLineTextField();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(guiBIOSWriteTextures);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(-guiLeft, -guiTop, 0.0F);
        guiInfoWindow.drawInfoWindow(mouseX, mouseY);
        GL11.glPopMatrix();
    }
}