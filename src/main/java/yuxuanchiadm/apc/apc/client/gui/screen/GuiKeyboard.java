package yuxuanchiadm.apc.apc.client.gui.screen;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yuxuanchiadm.apc.apc.common.AssemblyProgramCraft;
import yuxuanchiadm.apc.apc.common.network.AssemblyProgramCraftPacket;
import yuxuanchiadm.apc.apc.common.tileEntity.TileEntityExternalDeviceKeyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiKeyboard extends GuiScreen
{
    private static final ResourceLocation guiKeyboardTextures = new ResourceLocation("AssemblyProgramCraft:textures/gui/gui_keyboard.png");
    
    private TileEntityExternalDeviceKeyboard keyboard;
    private boolean isVirtualKeyboard;
    private GuiButton leaveKeyboard;
    private GuiButton turnVirtualKeyboard;
    public GuiKeyboard(TileEntityExternalDeviceKeyboard keyboard)
    {
        this.keyboard = keyboard;
    }
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        leaveKeyboard = new GuiButton(1, (width - 86) / 2 + 165, (height - 106) / 2 + 130, 70, 20, "离开键盘");
        this.buttonList.add(leaveKeyboard);
        turnVirtualKeyboard = new GuiButton(2, (width - 86) / 2 + 165, (height - 106) / 2 + 105, 70, 20, "开关虚拟键盘");
        turnVirtualKeyboard.enabled = false;
        this.buttonList.add(turnVirtualKeyboard);
    }
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if(par1GuiButton.id == leaveKeyboard.id)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
        }
        else if(par1GuiButton.id == turnVirtualKeyboard.id)
        {
            isVirtualKeyboard = !isVirtualKeyboard;
        }
    }
    protected void keyTyped(char par1, int par2)
    {
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ClientPacket.KeyboardStatusChange.getValue();
        pak.dataByte = new byte[1];
        pak.dataInt = new int[5];
        pak.dataByte[0] = 1;
        pak.dataInt[0] = keyboard.getPos().getX();
        pak.dataInt[1] = keyboard.getPos().getY();
        pak.dataInt[2] = keyboard.getPos().getZ();
        pak.dataInt[3] = keyboard.getWorld().provider.getDimensionId();
        pak.dataInt[4] = par1;
        AssemblyProgramCraft.sendToServer(pak);
    }
    private void keyReleased(int i)
    {
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ClientPacket.KeyboardStatusChange.getValue();
        pak.dataByte = new byte[1];
        pak.dataInt = new int[5];
        pak.dataByte[0] = 3;
        pak.dataInt[0] = keyboard.getPos().getX();
        pak.dataInt[1] = keyboard.getPos().getY();
        pak.dataInt[2] = keyboard.getPos().getZ();
        pak.dataInt[3] = keyboard.getWorld().provider.getDimensionId();
        pak.dataInt[4] = i;
        AssemblyProgramCraft.sendToServer(pak);
    }
    private void keyPressed(int i)
    {
        AssemblyProgramCraftPacket pak = new AssemblyProgramCraftPacket();
        pak.packetType = AssemblyProgramCraftPacket.ClientPacket.KeyboardStatusChange.getValue();
        pak.dataByte = new byte[1];
        pak.dataInt = new int[5];
        pak.dataByte[0] = 2;
        pak.dataInt[0] = keyboard.getPos().getX();
        pak.dataInt[1] = keyboard.getPos().getY();
        pak.dataInt[2] = keyboard.getPos().getZ();
        pak.dataInt[3] = keyboard.getWorld().provider.getDimensionId();
        pak.dataInt[4] = i;
        AssemblyProgramCraft.sendToServer(pak);
    }
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    public void drawScreen(int par1, int par2, float par3)
    {
        mc.renderEngine.bindTexture(guiKeyboardTextures);
        int x = (width + 227) / 2;
        int y = (height) / 2;
        drawTexturedModalRect(x, y, 0, 0, 86, 106);
        super.drawScreen(par1,par2,par3);
    }
    public void handleKeyboardInput()
    {
        int i = Keyboard.getEventKey();
        char c0 = Keyboard.getEventCharacter();
        
        if (Keyboard.getEventKeyState())
        {
            this.keyTyped(c0, i);
            this.keyPressed(i);
        }
        else
        {
            this.keyReleased(i);
        }
    }
}
