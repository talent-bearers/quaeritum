package eladkay.quaeritum.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class ComponentTextField extends GuiComponent {
    private final FontRenderer fontRenderer;
    public int x;
    public int y;
    public int width;
    public int height;
    private String text = "";
    private int maxStringLength = 64;
    private int cursorCounter;
    private boolean canLoseFocus = true;
    private boolean isFocused;
    private boolean isEnabled = true;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor = 0xe0e0e0;
    private int disabledColor = 0x707070;

    private Consumer<String> editedCallback = null;
    private Consumer<String> enterCallback = null;

    public ComponentTextField(FontRenderer fontRenderer, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.fontRenderer = fontRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        BUS.hook(GuiComponentEvents.MouseDownEvent.class, (GuiComponentEvents.MouseDownEvent event) ->
                mouseClicked(event.getMousePos().getXi(), event.getMousePos().getYi(), event.getButton().getMouseCode()));
        BUS.hook(GuiComponentEvents.KeyDownEvent.class, (GuiComponentEvents.KeyDownEvent event) ->
                handleKeyTyped(event.getKey(), event.getKeyCode()));
        BUS.hook(GuiComponentEvents.ComponentTickEvent.class, (GuiComponentEvents.ComponentTickEvent event) ->
                updateCursorCounter());
    }

    @Override
    public void drawComponent(@NotNull Vec2d mousePos, float partialTicks) {
        drawTextBox();
    }

    public void setEditedCallback(Consumer<String> editedCallback) {
        this.editedCallback = editedCallback;
    }

    public void setEnterCallback(Consumer<String> enterCallback) {
        this.enterCallback = enterCallback;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void setText(String textIn) {
        if (textIn.length() > this.maxStringLength) {
            this.text = textIn.substring(0, this.maxStringLength);
        } else {
            this.text = textIn;
        }

        this.cursorToEnd();
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int start = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int end = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(start, end);
    }

    public void writeText(String textToWrite) {

        String allowed = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        int selectionStart = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int selectionEnd = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int remainingSpace = this.maxStringLength - this.text.length() - (selectionStart - selectionEnd);

        String build = this.text.isEmpty() ? "" : this.text.substring(0, selectionStart);

        int shiftBy;

        if (remainingSpace < allowed.length()) {
            build += allowed.substring(0, remainingSpace);
            shiftBy = remainingSpace;
        } else {
            build += allowed;
            shiftBy = allowed.length();
        }

        if (!this.text.isEmpty() && selectionEnd < this.text.length())
            build = build + this.text.substring(selectionEnd);

        this.text = build;
        this.shiftCursor(selectionStart - this.selectionEnd + shiftBy);

        if (editedCallback != null)
            editedCallback.accept(text);
    }

    public void deleteWords(int count) {
        if (!this.text.isEmpty())
            if (this.selectionEnd != this.cursorPosition)
                this.writeText("");
            else
                this.deleteFromCursor(this.getWordStartingFromCursor(count) - this.cursorPosition);
    }

    public void deleteFromCursor(int count) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition)
                this.writeText("");
            else {
                boolean backwards = count < 0;
                int start = backwards ? this.cursorPosition + count : this.cursorPosition;
                int end = backwards ? this.cursorPosition : this.cursorPosition + count;
                String build = start >= 0 ? this.text.substring(0, start) : "";

                if (end < this.text.length()) build += this.text.substring(end);

                this.text = build;

                if (backwards) this.shiftCursor(count);

                if (editedCallback != null)
                    editedCallback.accept(text);
            }
        }
    }

    public int getWordStartingFromCursor(int relativeIndex) {
        return this.getWord(relativeIndex, this.getCursorPosition());
    }

    public int getWord(int relativeIndex, int startingPos) {
        return this.getWordSkippingWhitespace(relativeIndex, startingPos, true);
    }

    public int getWordSkippingWhitespace(int relativeIndex, int startingPos, boolean skipWhitespace) {
        int pos = startingPos;

        boolean backwards = relativeIndex < 0;
        int toSearch = Math.abs(relativeIndex);

        for (int i = 0; i < toSearch; ++i) {
            if (!backwards) {
                int length = this.text.length();
                pos = this.text.indexOf(32, pos);

                if (pos == -1)
                    pos = length;
                else while (skipWhitespace && pos < length && Character.isWhitespace(this.text.charAt(pos)))
                    pos++;
            } else {
                while (skipWhitespace && pos > 0 && Character.isWhitespace(this.text.charAt(pos - 1)))
                    --pos;
                while (pos > 0 && Character.isWhitespace(this.text.charAt(pos - 1)))
                    pos--;
            }
        }

        return pos;
    }

    public void shiftCursor(int num) {
        this.setCursorPosition(this.selectionEnd + num);
    }

    public void setCursorPosition(int pos) {
        this.cursorPosition = pos;
        int i = this.text.length();
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
        this.setSelectionPosition(this.cursorPosition);
    }

    public void cursorToStart() {
        this.setCursorPosition(0);
    }

    public void cursorToEnd() {
        this.setCursorPosition(this.text.length());
    }

    public boolean handleKeyTyped(char input, int inputCode) {
        if (!this.isFocused) return false;
        else if (GuiScreen.isKeyComboCtrlA(inputCode)) {
            this.cursorToEnd();
            this.setSelectionPosition(0);
            return true;
        } else if (GuiScreen.isKeyComboCtrlC(inputCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        } else if (GuiScreen.isKeyComboCtrlV(inputCode)) {
            if (this.isEnabled)
                this.writeText(GuiScreen.getClipboardString());
            return true;
        } else if (GuiScreen.isKeyComboCtrlX(inputCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled)
                this.writeText("");
            return true;
        } else switch (inputCode) {
            case Keyboard.KEY_BACK:

                if (isEnabled)
                    if (GuiScreen.isCtrlKeyDown())
                        this.deleteWords(-1);
                    else
                        this.deleteFromCursor(-1);

                return true;
            case Keyboard.KEY_HOME:

                if (GuiScreen.isShiftKeyDown())
                    this.setSelectionPosition(0);
                else
                    this.cursorToStart();

                return true;
            case Keyboard.KEY_LEFT:
                if (GuiScreen.isShiftKeyDown())
                    if (GuiScreen.isCtrlKeyDown())
                        this.setSelectionPosition(this.getWord(-1, this.getSelectionEnd()));
                    else
                        this.setSelectionPosition(this.getSelectionEnd() - 1);
                else if (GuiScreen.isCtrlKeyDown())
                    this.setCursorPosition(this.getWordStartingFromCursor(-1));
                else
                    this.shiftCursor(-1);

                return true;
            case Keyboard.KEY_RIGHT:
                if (GuiScreen.isShiftKeyDown())
                    if (GuiScreen.isCtrlKeyDown())
                        this.setSelectionPosition(this.getWord(1, this.getSelectionEnd()));
                    else
                        this.setSelectionPosition(this.getSelectionEnd() + 1);
                else if (GuiScreen.isCtrlKeyDown())
                    this.setCursorPosition(this.getWordStartingFromCursor(1));
                else
                    this.shiftCursor(1);

                return true;
            case Keyboard.KEY_END:

                if (GuiScreen.isShiftKeyDown())
                    this.setSelectionPosition(this.text.length());
                else
                    this.cursorToEnd();

                return true;
            case Keyboard.KEY_DELETE:
                if (this.isEnabled)
                    if (GuiScreen.isCtrlKeyDown())
                        this.deleteWords(1);
                    else
                        this.deleteFromCursor(1);

                return true;
            case Keyboard.KEY_RETURN:
                if (enterCallback != null)
                    enterCallback.accept(text);
                return true;
            default:
                if (ChatAllowedCharacters.isAllowedCharacter(input)) {
                    if (this.isEnabled)
                        this.writeText(Character.toString(input));

                    return true;
                }
        }

        return false;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean withinBoundary = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;

        if (this.canLoseFocus)
            this.setFocused(withinBoundary);

        if (this.isFocused && withinBoundary && mouseButton == 0) {
            int xFromLeft = mouseX - this.x;

            String visible = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(visible, xFromLeft).length() + this.lineScrollOffset);
            return true;
        } else
            return false;
    }

    public void drawTextBox() {
        if (this.isVisible()) {
            int textColor = this.isEnabled ? this.enabledColor : this.disabledColor;
            int cursorRelativePosition = this.cursorPosition - this.lineScrollOffset;
            int selectionEndPosition = this.selectionEnd - this.lineScrollOffset;
            String visible = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth() - fontRenderer.getStringWidth("_"));
            boolean cursorVisible = cursorRelativePosition >= 0 && cursorRelativePosition <= visible.length();
            boolean cursorBlinkActive = this.isFocused && this.cursorCounter / 6 % 2 == 0 && cursorVisible;
            int offset = x;

            if (selectionEndPosition > visible.length())
                selectionEndPosition = visible.length();

            if (!visible.isEmpty()) {
                String toCursor = cursorVisible ? visible.substring(0, cursorRelativePosition) : visible;
                offset = this.fontRenderer.drawStringWithShadow(toCursor, offset, y, textColor);
            }

            boolean cursorInText = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int unselectedBound = offset;

            if (!cursorVisible)
                unselectedBound = cursorRelativePosition > 0 ? x + this.width - fontRenderer.getStringWidth("_") : x;
            else if (cursorInText)
                unselectedBound = --offset;

            if (!visible.isEmpty() && cursorVisible && cursorRelativePosition < visible.length())
                this.fontRenderer.drawStringWithShadow(visible.substring(cursorRelativePosition), offset, y, textColor);

            if (cursorBlinkActive) if (cursorInText) {
                Gui.drawRect(unselectedBound, y - 1, unselectedBound + 1, y + 2 + this.fontRenderer.FONT_HEIGHT, 0xffd0d0d0);
                GlStateManager.enableBlend();
            } else
                this.fontRenderer.drawStringWithShadow("_", unselectedBound, y + 1, textColor);

            if (selectionEndPosition != cursorRelativePosition) {
                int selectionX = x + this.fontRenderer.getStringWidth(visible.substring(0, selectionEndPosition));
                this.drawSelectionBox(unselectedBound, y, selectionX - 1, y + this.fontRenderer.FONT_HEIGHT);
            }

            GlStateManager.color(1f, 1f, 1f, 1f);
        }
    }

    private void drawSelectionBox(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        if (minX > this.x + this.width)
            minX = this.x + this.width;

        if (maxX > this.x + this.width)
            maxX = this.x + this.width;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0f, 0f, 1f, 1f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(maxX, minY, 0).endVertex();
        bufferbuilder.pos(minX, minY, 0).endVertex();
        bufferbuilder.pos(minX, maxY, 0).endVertex();
        bufferbuilder.pos(maxX, maxY, 0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public void setFocused(boolean isFocused) {
        if (isFocused && !this.isFocused)
            this.cursorCounter = 0;

        this.isFocused = isFocused;

        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen != null) currentScreen.setFocused(isFocused);
    }

    public boolean isFocused() {
        return isFocused;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getWidth() {
        return this.width;
    }

    public void setSelectionPosition(int position) {
        int length = this.text.length();

        if (position > length) position = length;

        if (position < 0) position = 0;

        this.selectionEnd = position;

        if (this.fontRenderer != null) {
            if (this.lineScrollOffset > length) {
                this.lineScrollOffset = length;
            }

            int boxWidth = this.getWidth() - fontRenderer.getStringWidth("_");
            String visible = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), boxWidth);
            int positionInOverall = visible.length() + this.lineScrollOffset;

            if (position == this.lineScrollOffset)
                this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, boxWidth, true).length();

            if (position > positionInOverall)
                this.lineScrollOffset += position - positionInOverall;
            else if (position <= this.lineScrollOffset)
                this.lineScrollOffset -= this.lineScrollOffset - position;

            this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, length);
        }
    }

    public void setCanLoseFocus(boolean canLoseFocusIn) {
        this.canLoseFocus = canLoseFocusIn;
    }

    public void setEnabledColor(int enabledColor) {
        this.enabledColor = enabledColor;
    }

    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
    }
}

