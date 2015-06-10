package yuxuanchiadm.apc.apc.client.gui.event;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IEventNode
{
    public void tick();
    public void keyPressed(char character, int key);
    public void keyReleased(char character, int key);
    public void mousePressed(int key, int mouseX, int mouseY);
    public void mouseReleased(int key, int mouseX, int mouseY);
    public void mouseWheel(boolean state);
    public void mouseMove(int mouseX, int mouseY, int offsetX, int offsetY);
    public void setFocused(boolean isFocused);
    public boolean isFocused();
}