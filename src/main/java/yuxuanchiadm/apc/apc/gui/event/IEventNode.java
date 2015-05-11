package yuxuanchiadm.apc.apc.gui.event;

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