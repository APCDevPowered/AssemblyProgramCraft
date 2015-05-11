package yuxuanchiadm.apc.apc.gui.screen;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.apc.tileEntity.ITileEntityExternalDevice;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiPortSetting extends GuiScreen
{
    private static final ResourceLocation guiPortSettingTextures = new ResourceLocation("AssemblyProgramCraft:textures/gui/GuiPortSetting.png");
    private GuiTextField portTextField;
    private ITileEntityExternalDevice device;
    private GuiButton confirm;
    private GuiButton cancel;
    public GuiPortSetting(ITileEntityExternalDevice device)
    {
        this.device = device;
    }
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        confirm = new GuiButton(1, (width - 118) / 2 + 10, (height - 83) / 2 + 50, 40, 20, "确认");
        this.buttonList.add(confirm);
        cancel = new GuiButton(2, (width - 118) / 2 + 60, (height - 83) / 2 + 50, 40, 20, "取消");
        this.buttonList.add(cancel);
        this.portTextField = new GuiTextField(3, this.fontRendererObj, (width - 118) / 2 + 10, (height - 83) / 2 + 23, 90, 20);
        this.portTextField.setMaxStringLength(100);
        this.portTextField.setFocused(true);
        this.portTextField.setText(String.valueOf(device.getPort()));
    }
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if(par1GuiButton.id == confirm.id)
        {
            int port = 0;
            try
            {
                port = Integer.parseInt(portTextField.getText());
            }
            catch (NumberFormatException e)
            {
                FMLClientHandler.instance().getClient().thePlayer.addChatMessage(new ChatComponentText("[APC]无法设置外设端口，输入无法转换为数字"));
                return;
            }
            AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
            pak.packetType = AssemblyProgramCraftPacket.ClientPacket.PortSettingToolSetID.getValue();
            pak.dataInt = new int[5];
            pak.dataInt[0] = device.xCoord;
            pak.dataInt[1] = device.yCoord;
            pak.dataInt[2] = device.zCoord;
            pak.dataInt[3] = device.getWorldObj().getWorldInfo().getVanillaDimension();
            pak.dataInt[4] = port;
            AssemblyProgramCraft.sendToServer(pak);
            this.device.setPort(port);
            this.mc.thePlayer.closeScreen();
        }
        else if(par1GuiButton.id == cancel.id)
        {
            this.mc.thePlayer.closeScreen();
        }
    }
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        this.portTextField.textboxKeyTyped(typedChar, keyCode);
    }
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.portTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    public void updateScreen()
    {
        this.portTextField.updateCursorCounter();
    }
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    public void drawScreen(int par1, int par2, float par3)
    {
        this.mc.renderEngine.bindTexture(guiPortSettingTextures);
        int x = (width - 118) / 2;
        int y = (height - 83) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, 118, 83);
        this.portTextField.drawTextBox();
        super.drawScreen(par1,par2,par3);
        this.fontRendererObj.drawString("外设端口分配器", x + 7, y + 7, 0x404040);
    }
}
