package org.apcdevpowered.apc.client.gui.components;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apcdevpowered.apc.client.gui.event.IEventNode;
import org.apcdevpowered.apc.common.util.history.HistoryApplyException;
import org.apcdevpowered.apc.common.util.history.HistoryManager;
import org.apcdevpowered.apc.common.util.history.HistoryManager.HistoryEntry;
import org.apcdevpowered.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMultipleLineTextField extends Gui implements IEventNode
{
    private FontRenderer fontRenderer;
    private GuiScrollBoard scollBoard;
    private String text;
    private HistoryManager historyManager;
    private int backgroundColor = 0xFF000000;
    private int fontColor = 0xFFFFFFFF;
    private int cursouColor = 0xFFFFFFFF;
    private int selectionColor = 0xFF0000FF;
    private int selectedFontColor = 0xFF000000;
    private int xPos;
    private int yPos;
    private final int width;
    private final int height;
    private int selectedFrom;
    private int selectedTo;
    private int cursor;
    private int cursorCounter;
    private boolean isFocused;
    private boolean isInteractive = true;
    private boolean isEditable = true;
    public static final int FONT_HEIGHT = 8;
    public static final int FONT_SPACTING_H = 2;
    public static final int FONT_SPACTING_V = 2;
    public static final int FONT_SPACTING_UP = 2;
    public static final int FONT_SPACTING_DOWN = 2;
    public static final int FONT_SPACTING_LEFT = 2;
    public static final int FONT_SPACTING_RIGHT = 2;
    private static byte[] glyphWidth = new byte[65536];
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    
    public GuiMultipleLineTextField(FontRenderer fontRenderer, int xPos, int yPos, int width, int height)
    {
        this(fontRenderer, xPos, yPos, width, height, "", new HistoryManager());
    }
    public GuiMultipleLineTextField(FontRenderer fontRenderer, int xPos, int yPos, int width, int height, String text, HistoryManager historyManager)
    {
        this(fontRenderer, xPos, yPos, width, height, text, historyManager, 0xFF000000, 0xFFFFFFFF, 0xFFFFFFFF, 0xFF0000FF, 0xFF000000);
    }
    public GuiMultipleLineTextField(FontRenderer fontRenderer, int xPos, int yPos, int width, int height, String text, HistoryManager historyManager, int backgroundColor, int fontColor, int cursouColor, int selectionColor, int selectedFontColor)
    {
        this.fontRenderer = fontRenderer;
        this.scollBoard = new GuiScrollBoard(xPos, yPos, width, height);
        this.text = text;
        this.historyManager = historyManager;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.cursouColor = cursouColor;
        this.selectionColor = selectionColor;
        this.selectedFontColor = selectedFontColor;
    }
    public HistoryManager getHistoryManager()
    {
        return historyManager;
    }
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        setText(text, false);
    }
    public void setText(String text, boolean keepHistory)
    {
        if (keepHistory)
        {
            this.historyManager.addHistory(0, this.text.length(), text, this.text, false);
        }
        else
        {
            this.historyManager.clearHistory();
        }
        this.text = text;
        this.selectedFrom = 0;
        this.selectedTo = 0;
        this.cursor = 0;
        this.scollBoard.setScrollH(0);
        this.scollBoard.setScrollV(0);
    }
    public int getLineX()
    {
        int tmpPos = 0;
        int lineX = 0;
        while (tmpPos <= cursor)
        {
            lineX++;
            tmpPos = StringUtils.indexOfLineEndIncludeCRLN(text, tmpPos) + 1;
            if (tmpPos == 0)
            {
                break;
            }
        }
        tmpPos = 0;
        for (int i = 0; i < (lineX - 1); i++)
        {
            tmpPos = StringUtils.indexOfLineEndIncludeCRLN(text, tmpPos) + 1;
        }
        if (cursor < text.length() && text.charAt(cursor) == '\n')
        {
            if (cursor - 1 >= 0 && text.charAt(cursor - 1) == '\r')
            {
                return cursor - tmpPos - 1;
            }
        }
        return cursor - tmpPos;
    }
    public int getLineLength(int pos)
    {
        String[] lines = StringUtils.getLines(text);
        if (pos < 0)
        {
            return 0;
        }
        if (pos >= lines.length)
        {
            return 0;
        }
        return lines[pos].length();
    }
    public void setLineX(int pos)
    {
        if (pos < 0)
        {
            pos = 0;
        }
        if (pos >= getLineLength(getLineY()))
        {
            pos = getLineLength(getLineY());
        }
        setCursorSafety(cursor - (getLineX() - pos), false);
    }
    public int getLineY()
    {
        int tmpPos = 0;
        int lineY = 0;
        while (tmpPos <= cursor)
        {
            lineY++;
            tmpPos = StringUtils.indexOfLineEndIncludeCRLN(text, tmpPos) + 1;
            if (tmpPos == 0)
            {
                break;
            }
        }
        return lineY - 1;
    }
    public int getLinesCount()
    {
        return StringUtils.getLines(text).length;
    }
    public void setLineY(int pos)
    {
        if (pos < 0)
        {
            setCursor(0);
            return;
        }
        if (pos >= getLinesCount())
        {
            setCursor(text.length());
            return;
        }
        int basePos = 0;
        for (int i = 0; i < pos; i++)
        {
            basePos = StringUtils.indexOfLineEndIncludeCRLN(text, basePos) + 1;
        }
        String[] strArray = StringUtils.getLines(text);
        int lineY = getLineY();
        int lineX = getLineX();
        int offsetWidth = getStringWidth(strArray[lineY].substring(0, lineX));
        int offsetPos = 0;
        String targetLine = strArray[pos];
        int codePointCount = targetLine.codePointCount(0, targetLine.length());
        for (int codePoint = 0; codePoint <= codePointCount; codePoint++)
        {
            int index = targetLine.offsetByCodePoints(0, codePoint);
            if (offsetWidth <= getStringWidth(targetLine.substring(0, index)))
            {
                offsetPos = index;
                break;
            }
            if (index == targetLine.length())
            {
                offsetPos = index;
                break;
            }
        }
        setCursorSafety(basePos + offsetPos, false);
    }
    public String getSelectedtextSafety()
    {
        int start = Math.min(selectedFrom, selectedTo);
        int end = Math.max(selectedFrom, selectedTo);
        if (start < 0)
        {
            start = 0;
        }
        if (end > text.length())
        {
            end = text.length();
        }
        return text.substring(start, end);
    }
    public String getSelectedtext(int start, int end)
    {
        return text.substring(start, end);
    }
    public void insertText(String str)
    {
        if (selectedFrom != selectedTo)
        {
            int start = Math.min(selectedFrom, selectedTo);
            int end = Math.max(selectedFrom, selectedTo);
            historyManager.addHistory(start, end, str, getSelectedtext(start, end), false);
            text = (text.substring(0, start) + str + text.substring(end, text.length()));
            setCursorSafety(start + str.length(), false);
            selectedFrom = cursor;
            selectedTo = cursor;
        }
        else
        {
            historyManager.addHistory(cursor, cursor, str, "");
            text = (text.substring(0, cursor) + str + text.substring(cursor, text.length()));
            setCursorSafety(cursor + str.length(), false);
            selectedFrom = cursor;
            selectedTo = cursor;
        }
    }
    public void insertChar(char theChar)
    {
        if (selectedFrom != selectedTo)
        {
            int start = Math.min(selectedFrom, selectedTo);
            int end = Math.max(selectedFrom, selectedTo);
            historyManager.addHistory(start, end, Character.toString(theChar), getSelectedtext(start, end), false);
            text = (text.substring(0, start) + theChar + text.substring(end, text.length()));
            setCursorSafety(start + 1, false);
            selectedFrom = cursor;
            selectedTo = cursor;
        }
        else
        {
            historyManager.addHistory(cursor, cursor, Character.toString(theChar), "");
            text = (text.substring(0, cursor) + theChar + text.substring(cursor, text.length()));
            setCursorSafety(cursor + 1, false);
            selectedFrom = cursor;
            selectedTo = cursor;
        }
    }
    public boolean isShiftDown()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            return true;
        }
        return false;
    }
    public boolean isAltDown()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))
        {
            return true;
        }
        return false;
    }
    public boolean isCtrlDown()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
        {
            return true;
        }
        return false;
    }
    /**
     * Not safe when string has "\r\n". When set cursor between '\r' and '\n'
     * will cause unstable status. Then do something may cause game crash.
     * 
     * May only use this method set cursor to 0 or text.length(). Or be sure
     * string don`t has "\r\n". Faster a bit with
     * {@link #setCursorSafety(int, boolean)}
     * 
     * @param cursor
     *            The new cursor
     */
    @Deprecated
    public void setCursor(int cursor)
    {
        if (cursor < 0)
        {
            cursor = 0;
        }
        if (cursor > text.length())
        {
            cursor = text.length();
        }
        this.cursor = cursor;
        checkCursorDisplay();
    }
    /**
     * Set cursor safety even string has "\r\n". Will forward 1 char or backward
     * 1 char when cursor between '\r' and '\n'.
     * 
     * @param cursor
     *            The new cursor
     * 
     * @param fallbackChar
     *            When between '\r' and '\n' will cause fallback 1 char
     */
    public void setCursorSafety(int cursor, boolean fallbackChar)
    {
        if (cursor < 0)
        {
            cursor = 0;
        }
        if (cursor > text.length())
        {
            cursor = text.length();
        }
        if (cursor < text.length() && text.charAt(cursor) == '\n')
        {
            if (cursor - 1 >= 0 && text.charAt(cursor - 1) == '\r')
            {
                if (!fallbackChar)
                {
                    this.cursor = cursor + 1;
                }
                else
                {
                    this.cursor = cursor - 1;
                }
            }
            else
            {
                this.cursor = cursor;
            }
        }
        else
        {
            this.cursor = cursor;
        }
        checkCursorDisplay();
    }
    /**
     * Forward cursor 1 char. Safe even string has "\r\n".
     */
    public void forwardCursor()
    {
        if (cursor < text.length())
        {
            int offset = text.offsetByCodePoints(cursor, 1) - cursor;
            cursor = cursor + offset;
            if (cursor - offset >= 0 && cursor - offset < text.length() && text.charAt(cursor - offset) == '\r')
            {
                if (cursor >= 0 && cursor < text.length() && text.charAt(cursor) == '\n')
                {
                    cursor++;
                }
            }
        }
        checkCursorDisplay();
    }
    /**
     * backward cursor 1 char. Safe even string has "\r\n".
     */
    public void backwardCursor()
    {
        if (cursor > 0)
        {
            int offset = text.offsetByCodePoints(cursor, -1) - cursor;
            cursor = cursor + offset;
            if (cursor >= 0 && cursor < text.length() && text.charAt(cursor) == '\n')
            {
                if (cursor + offset >= 0 && cursor + offset < text.length() && text.charAt(cursor + offset) == '\r')
                {
                    cursor--;
                }
            }
        }
        checkCursorDisplay();
    }
    public void checkCursorDisplay()
    {
        int lineX = getLineX();
        int lineY = getLineY();
        String cursorAtLine = StringUtils.getLines(text)[lineY];
        int horizontalPos = 0;
        int verticalPos = 0;
        horizontalPos = FONT_SPACTING_LEFT;
        for (int i = 0; i < lineX; i++)
        {
            char c = cursorAtLine.charAt(i);
            horizontalPos += getCharWidth(c) + FONT_SPACTING_H;
        }
        verticalPos = FONT_SPACTING_UP + (FONT_HEIGHT + FONT_SPACTING_V) * lineY;
        int cursorMinHorizontalPos = horizontalPos - FONT_SPACTING_H / 2;
        int cursorMinVerticalPos = verticalPos - FONT_SPACTING_V / 2;
        int cursorMaxHorizontalPos = horizontalPos - FONT_SPACTING_H / 2 + 1;
        int cursorMaxVerticalPos = verticalPos + FONT_HEIGHT + FONT_SPACTING_V / 2;
        int scrollH = scollBoard.getScrollH();
        int scrollV = scollBoard.getScrollV();
        int maxScrollH = getMultipleLineTextMaxWidth(text);
        int maxScrollV = getMultipleLineTextMaxHeigth(text);
        if (maxScrollH < width - 2 - 14)
        {
            maxScrollH = 0;
        }
        if (maxScrollV < height - 2 - 14)
        {
            maxScrollV = 0;
        }
        int transformScrollH = scrollH - (int) (((float) scrollH / (float) maxScrollH) * (width - 16));
        int transformScrollV = scrollV - (int) (((float) scrollV / (float) maxScrollV) * (height - 16));
        int transformMaxScrollH = maxScrollH - (width - 16);
        int transformMaxScrollV = maxScrollV - (height - 16);
        if (transformMaxScrollH < 0)
        {
            transformMaxScrollH = 0;
        }
        if (transformMaxScrollV < 0)
        {
            transformMaxScrollV = 0;
        }
        scollBoard.setMaxScroll(maxScrollH, maxScrollV);
        if (cursorMinHorizontalPos < transformScrollH)
        {
            scollBoard.setScrollH((int) (((float) (cursorMinHorizontalPos) / (float) transformMaxScrollH) * maxScrollH));
        }
        if (cursorMinVerticalPos < transformScrollV)
        {
            scollBoard.setScrollV((int) (((float) (cursorMinVerticalPos) / (float) transformMaxScrollV) * maxScrollV));
        }
        if (cursorMaxHorizontalPos - (width - 16) > transformScrollH)
        {
            scollBoard.setScrollH((int) (((float) (cursorMaxHorizontalPos - (width - 16)) / (float) transformMaxScrollH) * maxScrollH));
        }
        if (cursorMaxVerticalPos - (height - 16) > transformScrollV)
        {
            scollBoard.setScrollV((int) (((float) (cursorMaxVerticalPos - (height - 16)) / (float) transformMaxScrollV) * maxScrollV));
        }
    }
    @Override
    public void keyReleased(char character, int key)
    {
        scollBoard.keyReleased(character, key);
    }
    @Override
    public void keyPressed(char character, int key)
    {
        if (isFocused && isInteractive)
        {
            if (Keyboard.KEY_LEFT == key)
            {
                if (isShiftDown())
                {
                    backwardCursor();
                    selectedTo = cursor;
                }
                else
                {
                    backwardCursor();
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
            }
            else if (Keyboard.KEY_RIGHT == key)
            {
                if (isShiftDown())
                {
                    forwardCursor();
                    selectedTo = cursor;
                }
                else
                {
                    forwardCursor();
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
            }
            else if (Keyboard.KEY_UP == key)
            {
                if (isShiftDown())
                {
                    setLineY(getLineY() - 1);
                    selectedTo = cursor;
                }
                else
                {
                    setLineY(getLineY() - 1);
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
            }
            else if (Keyboard.KEY_DOWN == key)
            {
                if (isShiftDown())
                {
                    setLineY(getLineY() + 1);
                    selectedTo = cursor;
                }
                else
                {
                    setLineY(getLineY() + 1);
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
            }
            else if (Keyboard.KEY_HOME == key)
            {
                if (isShiftDown())
                {
                    setCursor(0);
                    selectedTo = cursor;
                }
                else
                {
                    setCursor(0);
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
            }
            else if (Keyboard.KEY_END == key)
            {
                if (isShiftDown())
                {
                    setCursor(text.length());
                    selectedTo = cursor;
                }
                else
                {
                    setCursor(text.length());
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
            }
            else if (Keyboard.KEY_DELETE == key && isEditable)
            {
                if (selectedFrom != selectedTo)
                {
                    int start = Math.min(selectedFrom, selectedTo);
                    int end = Math.max(selectedFrom, selectedTo);
                    historyManager.addHistory(start, end, "", getSelectedtext(start, end));
                    text = (text.substring(0, start) + text.substring(end, text.length()));
                    setCursorSafety(start, false);
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
                else
                {
                    if (cursor < text.length())
                    {
                        int start = cursor;
                        int end = text.offsetByCodePoints(cursor, 1);
                        if (cursor >= 0 && cursor < text.length() && text.charAt(cursor) == '\r')
                        {
                            if (cursor + 1 >= 0 && cursor + 1 < text.length() && text.charAt(cursor + 1) == '\n')
                            {
                                end++;
                            }
                        }
                        historyManager.addHistory(start, end, "", getSelectedtext(start, end));
                        text = (text.substring(0, start) + text.substring((end <= text.length() ? end : text.length()), text.length()));
                    }
                }
            }
            else if (Keyboard.KEY_BACK == key && isEditable)
            {
                if (selectedFrom != selectedTo)
                {
                    int start = Math.min(selectedFrom, selectedTo);
                    int end = Math.max(selectedFrom, selectedTo);
                    historyManager.addHistory(start, end, "", getSelectedtext(start, end));
                    text = (text.substring(0, start) + text.substring(end, text.length()));
                    setCursorSafety(start, false);
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
                else
                {
                    if (cursor > 0)
                    {
                        int start = text.offsetByCodePoints(cursor, -1);
                        int end = cursor;
                        if (start >= 0 && start < text.length() && text.charAt(start) == '\n')
                        {
                            if (start - 1 >= 0 && start - 1 < text.length() && text.charAt(start - 1) == '\r')
                            {
                                start--;
                            }
                        }
                        historyManager.addHistory(start, end, "", getSelectedtext(start, end));
                        text = (text.substring(0, start) + text.substring((end <= text.length() ? end : text.length()), text.length()));
                        setCursorSafety(start, false);
                        selectedFrom = start;
                        selectedTo = start;
                    }
                }
            }
            else if (isCtrlDown() && key == Keyboard.KEY_A)
            {
                setCursor(text.length());
                selectedFrom = 0;
                selectedTo = text.length();
            }
            else if (isCtrlDown() && key == Keyboard.KEY_C)
            {
                GuiScreen.setClipboardString(getSelectedtextSafety());
            }
            else if (isCtrlDown() && key == Keyboard.KEY_X)
            {
                GuiScreen.setClipboardString(getSelectedtextSafety());
                int start = Math.min(selectedFrom, selectedTo);
                int end = Math.max(selectedFrom, selectedTo);
                historyManager.addHistory(start, end, "", getSelectedtext(start, end));
                text = text.substring(0, start) + text.substring(end, text.length());
                setCursor(start);
                selectedFrom = start;
                selectedTo = start;
            }
            else if (isCtrlDown() && key == Keyboard.KEY_V && isEditable)
            {
                this.insertText(GuiScreen.getClipboardString());
            }
            else if (isCtrlDown() && key == Keyboard.KEY_Z && isEditable)
            {
                HistoryEntry entry = historyManager.undoHistory();
                if (entry != null)
                {
                    try
                    {
                        text = entry.undo(text);
                        setCursor(entry.getFrom() + entry.getOldText().length());
                        selectedFrom = entry.getFrom();
                        selectedTo = entry.getFrom() + entry.getOldText().length();
                    }
                    catch (HistoryApplyException e)
                    {
                        e.printStackTrace();
                        historyManager.clearHistory();
                    }
                }
            }
            else if (isCtrlDown() && key == Keyboard.KEY_Y && isEditable)
            {
                HistoryEntry entry = historyManager.redoHistory();
                if (entry != null)
                {
                    try
                    {
                        text = entry.redo(text);
                        setCursor(entry.getFrom() + entry.getNewText().length());
                        selectedFrom = entry.getFrom();
                        selectedTo = entry.getFrom() + entry.getNewText().length();
                    }
                    catch (HistoryApplyException e)
                    {
                        e.printStackTrace();
                        historyManager.clearHistory();
                    }
                }
            }
            else if (key == Keyboard.KEY_RETURN && isEditable)
            {
                insertText(System.lineSeparator());
            }
            else if (key == Keyboard.KEY_TAB && isEditable)
            {
                insertChar('\t');
            }
            else if (ChatAllowedCharacters.isAllowedCharacter(character) && isEditable)
            {
                insertChar(character);
            }
        }
        scollBoard.keyPressed(character, key);
    }
    @Override
    public void mousePressed(int key, int mouseX, int mouseY)
    {
        scollBoard.setFocused(isFocused);
        if (isFocused && isInteractive)
        {
            if (key == 0 && mouseX >= xPos + 2 && mouseY >= yPos + 2 && mouseX < xPos + width - 14 && mouseY < yPos + height - 14)
            {
                setCursorByMousePos(mouseX, mouseY);
                if (isShiftDown())
                {
                    selectedTo = cursor;
                }
                else
                {
                    selectedFrom = cursor;
                    selectedTo = cursor;
                }
            }
        }
        scollBoard.mousePressed(key, mouseX, mouseY);
    }
    @Override
    public void mouseReleased(int key, int mouseX, int mouseY)
    {
        scollBoard.mouseReleased(key, mouseX, mouseY);
    }
    @Override
    public void mouseWheel(int wheel)
    {
        scollBoard.mouseWheel(wheel);
    }
    @Override
    public void mouseMove(int mouseX, int mouseY, int offsetX, int offsetY)
    {
        if (isFocused && isInteractive)
        {
            if (Mouse.isButtonDown(0) && !scollBoard.isScrolling())
            {
                setCursorByMousePos(mouseX, mouseY);
                selectedTo = cursor;
            }
        }
        scollBoard.mouseMove(mouseX, mouseY, offsetX, offsetY);
    }
    public void setCursorByMousePos(int mouseX, int mouseY)
    {
        int transformHorizontalOffset = scollBoard.getScrollH() - (int) (((float) scollBoard.getScrollH() / (float) getMultipleLineTextMaxWidth(text)) * (width - 16));
        int transformVerticalOffset = scollBoard.getScrollV() - (int) (((float) scollBoard.getScrollV() / (float) getMultipleLineTextMaxHeigth(text)) * (height - 16));
        int mouseTextPosX = mouseX - (xPos + 2) + transformHorizontalOffset - FONT_SPACTING_LEFT;
        int mouseTextPosY = mouseY - (yPos + 2) + transformVerticalOffset - FONT_SPACTING_UP;
        int lineY = (mouseTextPosY) / (FONT_HEIGHT + FONT_SPACTING_V);
        if (lineY < 0)
        {
            lineY = 0;
        }
        String[] strarray = StringUtils.getLines(text);
        if (lineY >= strarray.length - 1)
        {
            lineY = strarray.length - 1;
        }
        String line = strarray[lineY];
        int lineX = line.length();
        if (line.length() != 0)
        {
            int cPos = getCharWidth(line.charAt(0));
            for (int i = 0; i < line.length(); i++)
            {
                if (cPos >= mouseTextPosX)
                {
                    lineX = i;
                    break;
                }
                if ((i + 1) < line.length())
                {
                    cPos += (getCharWidth(line.charAt(i + 1)) + FONT_SPACTING_H);
                }
            }
        }
        int fromIndex = 0;
        for (int i = 0; i < lineY; i++)
        {
            fromIndex = StringUtils.indexOfLineEndIncludeCRLN(text, fromIndex) + 1;
        }
        setCursorSafety(fromIndex + lineX, false);
    }
    public void drawMultipleLineTextField()
    {
        int maxScrollH = getMultipleLineTextMaxWidth(text);
        int maxScrollV = getMultipleLineTextMaxHeigth(text);
        if (maxScrollH < width - 2 - 14)
        {
            maxScrollH = 0;
        }
        if (maxScrollV < height - 2 - 14)
        {
            maxScrollV = 0;
        }
        drawRect(xPos, yPos, xPos + width - 14, yPos + height - 14, backgroundColor);
        scollBoard.drawScrollBoard(maxScrollH, maxScrollV);
        int transformHorizontalOffset = scollBoard.getScrollH() - (int) (((float) scollBoard.getScrollH() / (float) maxScrollH) * (width - 16));
        int transformVerticalOffset = scollBoard.getScrollV() - (int) (((float) scollBoard.getScrollV() / (float) maxScrollV) * (height - 16));
        drawMultipleLineTextWithMask(text, transformHorizontalOffset, transformVerticalOffset, xPos + 2, yPos + 2, width - 14 - 2, height - 14 - 2);
    }
    public int getMultipleLineTextMaxHeigth(String str)
    {
        int heigth = FONT_HEIGHT + FONT_SPACTING_UP + FONT_SPACTING_DOWN;
        heigth += ((FONT_HEIGHT + FONT_SPACTING_V) * StringUtils.countLines(str));
        return heigth;
    }
    public int getMultipleLineTextMaxWidth(String str)
    {
        int width = FONT_SPACTING_LEFT + FONT_SPACTING_RIGHT;
        String[] strarray = StringUtils.getLines(str);
        for (int lineIndex = 0; lineIndex < strarray.length; lineIndex++)
        {
            String line = strarray[lineIndex];
            int currentLineWidth = FONT_SPACTING_LEFT + FONT_SPACTING_RIGHT;
            for (int index = 0; index < line.length(); index++)
            {
                char c = line.charAt(index);
                currentLineWidth += getCharWidth(c);
                if (index != line.length() - 1)
                {
                    currentLineWidth += FONT_SPACTING_H;
                }
            }
            if (currentLineWidth > width)
            {
                width = currentLineWidth;
            }
        }
        return width;
    }
    public void drawCursor(int horizontalPosTmp, int verticalPosTmp, int scrollH, int scrollV, int maskX, int maskY, int maskW, int maskH)
    {
        boolean drawCursor = this.isFocused && isInteractive && this.cursorCounter / 6 % 2 == 0;
        if (drawCursor)
        {
            if ((maskX - scrollH + horizontalPosTmp - FONT_SPACTING_H / 2 >= maskX) && (maskY - scrollV + verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2 >= maskY) && (maskX - scrollH + horizontalPosTmp - FONT_SPACTING_H / 2 - 1 < maskX + maskW) && (maskY - scrollV + verticalPosTmp - FONT_SPACTING_V / 2 < maskY + maskH))
            {
                int topCut = ((verticalPosTmp - FONT_SPACTING_V / 2 - scrollV) < 0) ? -(verticalPosTmp - FONT_SPACTING_V / 2 - scrollV) : 0;
                int buttomCut = (((verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2) - (scrollV + maskH)) > 0) ? ((verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2) - (scrollV + maskH)) : 0;
                int leftCut = ((horizontalPosTmp - FONT_SPACTING_H / 2 - scrollH) < 0) ? -(horizontalPosTmp - FONT_SPACTING_H / 2 - scrollH) : 0;
                int rightCut = (((horizontalPosTmp - FONT_SPACTING_H / 2 + 1) - (scrollH + maskW)) > 0) ? ((horizontalPosTmp - FONT_SPACTING_H / 2 + 1) - (scrollH + maskW)) : 0;
                drawRect(maskX - scrollH + horizontalPosTmp - FONT_SPACTING_H / 2 + leftCut, maskY - scrollV + verticalPosTmp - FONT_SPACTING_V / 2 + topCut, maskX - scrollH + horizontalPosTmp - FONT_SPACTING_H / 2 + 1 - rightCut, maskY - scrollV + verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2 - buttomCut, cursouColor);
            }
        }
    }
    public void drawMultipleLineTextWithMask(String text, int scrollH, int scrollV, int maskX, int maskY, int maskW, int maskH)
    {
        int horizontalPos = FONT_SPACTING_LEFT;
        int verticalPos = FONT_SPACTING_UP;
        int horizontalPosTmp = 0;
        int verticalPosTmp = 0;
        for (int i = 0; i <= text.length(); i++)
        {
            horizontalPosTmp = horizontalPos;
            verticalPosTmp = verticalPos;
            if (i == text.length())
            {
                if (cursor == i)
                {
                    drawCursor(horizontalPosTmp, verticalPosTmp, scrollH, scrollV, maskX, maskY, maskW, maskH);
                }
                break;
            }
            char theChar = text.charAt(i);
            if (theChar == '\n' || theChar == '\r')
            {
                int newlineIndex = i;
                if (theChar == '\r' && i + 1 < text.length() && text.charAt(i + 1) == '\n')
                {
                    i++;
                }
                horizontalPos = FONT_SPACTING_LEFT;
                verticalPos += FONT_HEIGHT + FONT_SPACTING_V;
                if (cursor == newlineIndex)
                {
                    drawCursor(horizontalPosTmp, verticalPosTmp, scrollH, scrollV, maskX, maskY, maskW, maskH);
                }
            }
            else
            {
                int charWidth = getCharWidth(theChar);
                boolean drawSelection = ((i >= selectedFrom) && (i < selectedTo)) || ((i >= selectedTo) && (i < selectedFrom));
                if (drawSelection)
                {
                    if ((maskX - scrollH + horizontalPosTmp + charWidth + FONT_SPACTING_H / 2 >= maskX) && (maskY - scrollV + verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2 >= maskY) && (maskX - scrollH + horizontalPosTmp - FONT_SPACTING_H / 2 < maskX + maskW) && (maskY - scrollV + verticalPosTmp - FONT_SPACTING_V / 2 < maskY + maskH))
                    {
                        int topCut = ((verticalPosTmp - FONT_SPACTING_V / 2 - scrollV) < 0) ? -(verticalPosTmp - FONT_SPACTING_V / 2 - scrollV) : 0;
                        int buttomCut = (((verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2) - (scrollV + maskH)) > 0) ? ((verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2) - (scrollV + maskH)) : 0;
                        int leftCut = ((horizontalPosTmp - FONT_SPACTING_H / 2 - scrollH) < 0) ? -(horizontalPosTmp - FONT_SPACTING_H / 2 - scrollH) : 0;
                        int rightCut = (((horizontalPosTmp + charWidth + FONT_SPACTING_H / 2) - (scrollH + maskW)) > 0) ? ((horizontalPosTmp + charWidth + FONT_SPACTING_H / 2) - (scrollH + maskW)) : 0;
                        drawRect(maskX - scrollH + horizontalPosTmp - FONT_SPACTING_H / 2 + leftCut, maskY - scrollV + verticalPosTmp - FONT_SPACTING_V / 2 + topCut, maskX - scrollH + horizontalPosTmp + charWidth + FONT_SPACTING_H / 2 - rightCut, maskY - scrollV + verticalPosTmp + FONT_HEIGHT + FONT_SPACTING_V / 2 - buttomCut, selectionColor);
                    }
                }
                if (horizontalPos >= (scrollH - charWidth) && verticalPos >= (scrollV - 8) && horizontalPos < (scrollH + maskW) && verticalPos < (scrollV + maskH))
                {
                    int x = horizontalPos - scrollH + maskX;
                    int y = verticalPos - scrollV + maskY;
                    int topCut = ((verticalPos - scrollV) < 0) ? -(verticalPos - scrollV) : 0;
                    int buttomCut = (((verticalPos + FONT_HEIGHT) - (scrollV + maskH)) > 0) ? ((verticalPos + this.fontRenderer.FONT_HEIGHT) - (scrollV + maskH)) : 0;
                    int leftCut = ((horizontalPos - scrollH) < 0) ? -(horizontalPos - scrollH) : 0;
                    int rightCut = (((horizontalPos + charWidth) - (scrollH + maskW)) > 0) ? ((horizontalPos + getCharWidth(theChar)) - (scrollH + maskW)) : 0;
                    int fontColor = this.fontColor;
                    if (drawSelection)
                    {
                        fontColor = selectedFontColor;
                    }
                    renderAdvancedCharAtPos(theChar, x, y, topCut, buttomCut, leftCut, rightCut, fontColor);
                }
                if (cursor == i)
                {
                    drawCursor(horizontalPosTmp, verticalPosTmp, scrollH, scrollV, maskX, maskY, maskW, maskH);
                }
                horizontalPos += charWidth + FONT_SPACTING_H;
            }
        }
    }
    public void renderAdvancedCharAtPos(char theChar, int xPos, int yPos, int topCut, int buttomCut, int leftCut, int rightCut, int color)
    {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float blue = (float) (color >> 8 & 255) / 255.0F;
        float green = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, blue, green, alpha);
        if (glyphWidth[theChar] == 0 || theChar == ' ' || theChar == '\t')
        {
            return;
        }
        else
        {
            int var3 = theChar / 256;
            loadGlyphTexture(var3);
            int var4 = glyphWidth[theChar] >>> 4;
            int var5 = glyphWidth[theChar] & 15;
            double var6 = (double) var4;
            double var7 = (double) (var5 + 1);
            double var8 = (double) (theChar % 16 * 16) + var6;
            double var9 = (double) ((theChar & 255) / 16 * 16);
            double var10 = var7 - var6;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(
                    xPos + leftCut,
                    yPos + topCut,
                    0.0D).tex(
                            var8 / 256.0D + leftCut / 256.0D + 0.004D * leftCut,
                            var9 / 256.0D + topCut / 256.0D + 0.004D * topCut)
                    .endVertex();
            worldRenderer.pos(
                    xPos + leftCut,
                    yPos + 8.0D - buttomCut,
                    0.0D).tex(
                            var8 / 256.0D + leftCut / 256.0D + 0.004D * leftCut,
                            (var9 + 16.0D) / 256.0D - buttomCut / 256.0D - 0.004D * buttomCut)
                    .endVertex();
            worldRenderer.pos(
                    xPos + var10 / 2.0D - rightCut,
                    yPos + topCut,
                    0.0D).tex(
                            (var8 + var10) / 256.0D - rightCut / 256.0D - 0.004D * rightCut,
                            var9 / 256.0D + topCut / 256.0D + 0.004D * topCut)
                    .endVertex();
            worldRenderer.pos(
                    xPos + var10 / 2.0D - rightCut,
                    yPos + 8.0D - buttomCut,
                    0.0D).tex(
                            (var8 + var10) / 256.0D - rightCut / 256.0D - 0.004D * rightCut,
                            (var9 + 16.0D) / 256.0D - buttomCut / 256.0D - 0.004D * buttomCut)
                    .endVertex();
            tessellator.draw();
        }
    }
    public void loadGlyphTexture(int par1)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.getUnicodePageLocation(par1));
    }
    public ResourceLocation getUnicodePageLocation(int par1)
    {
        if (unicodePageLocations[par1] == null)
        {
            unicodePageLocations[par1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[]
            {
                    Integer.valueOf(par1)
            }));
        }
        return unicodePageLocations[par1];
    }
    public int getCharWidth(char par1)
    {
        if (par1 == ' ')
        {
            return 1;
        }
        else if (par1 == '\t')
        {
            return getCharWidth(' ') * 4 + FONT_SPACTING_H * 3;
        }
        else
        {
            if (glyphWidth[par1] != 0)
            {
                int j = glyphWidth[par1] >>> 4;
                int k = glyphWidth[par1] & 15;
                if (k > 7)
                {
                    k = 15;
                    j = 0;
                }
                ++k;
                return (k - j) / 2;
            }
            else
            {
                return 0;
            }
        }
    }
    public int getStringWidth(String str)
    {
        int width = 0;
        for (int index = 0; index < str.length(); index++)
        {
            char c = str.charAt(index);
            width += getCharWidth(c);
            if (index != str.length() - 1)
            {
                width += FONT_SPACTING_H;
            }
        }
        return width;
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
    public boolean isInteractive()
    {
        return isInteractive;
    }
    public void setInteractive(boolean isInteractive)
    {
        this.isInteractive = isInteractive;
    }
    public boolean isEditable()
    {
        return isEditable;
    }
    public void setEditable(boolean isEditable)
    {
        this.isEditable = isEditable;
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
        cursorCounter++;
        this.scollBoard.tick();
    }
    public int getBackgroundColor()
    {
        return backgroundColor;
    }
    public void setBackgroundColor(int backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }
    public int getFontColor()
    {
        return fontColor;
    }
    public void setFontColor(int fontColor)
    {
        this.fontColor = fontColor;
    }
    public int getCursouColor()
    {
        return cursouColor;
    }
    public void setCursouColor(int cursouColor)
    {
        this.cursouColor = cursouColor;
    }
    public int getSelectionColor()
    {
        return selectionColor;
    }
    public void setSelectionColor(int selectionColor)
    {
        this.selectionColor = selectionColor;
    }
    public int getSelectedFontColor()
    {
        return selectedFontColor;
    }
    public void setSelectedFontColor(int selectedFontColor)
    {
        this.selectedFontColor = selectedFontColor;
    }
    
    static
    {
        InputStream inputstream = null;
        try
        {
            inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
            IOUtils.readFully(inputstream, glyphWidth);
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        finally
        {
            IOUtils.closeQuietly(inputstream);
        }
    }
}