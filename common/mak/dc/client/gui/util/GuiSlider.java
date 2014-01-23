package mak.dc.client.gui.util;

import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiSlider extends GuiRectangle {

    private int                     size;
    private int                     cursorPos;
    private int                     sliderId;
    private boolean                 started;
    private boolean                 isVertical;
    private boolean                 display      = true;
    private boolean                 isClientOnly = false;

    private static ResourceLocation texture      = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);

    public GuiSlider (int posX, int posY, int size, int id, boolean isVertical) {
        super(posX, posY, (!isVertical) ? (size >= 3 ? size * 2 : 2) : 10, (isVertical) ? (size >= 3 ? size * 2 : 2)
                : 10);

        this.size = size >= 3 ? size : 3;
        this.sliderId = id;
        this.isVertical = isVertical;

    }

    public void draw (GuiCustom gui) {
        if (display) {
            GL11.glColor4f(1, 1, 1, 1);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            if (!isVertical) {
                gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY(), 1, 166, 1, 10);
                for (int i = 0; i < size; i++) {
                    gui.drawTexturedModalRect(gui.getLeft() + getX() + 1 + i, gui.getTop() + getY(), 5, 166, 2, 10);
                }
            } else if (isVertical) {
                gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY(), 9, 166, 10, 1);
                for (int i = 0; i < size; i++) {
                    gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY() + 1 + i, 9, 170, 10, 2);
                }
            }
            drawSlider(gui, getCursorPos());
        }
    }

    private void drawSlider (GuiCustom gui, int pos) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        if (!isVertical) {
            gui.drawTexturedModalRect(gui.getLeft() + getX() + pos + 1, gui.getTop() + getY(), 7, 166, 2, 10);
            drawString(gui, "" + getRatio(), gui.getLeft() + getX(), gui.getTop() + getY() - 10, 100);
        } else if (isVertical) {
            gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY() + pos + 1, 9, 172, 10, 2);
        }
    }

    private int getCursorPos () {
        cursorPos = (cursorPos <= size - 2 && cursorPos >= 0) ? cursorPos : 0;
        return cursorPos;
    }

    private void setCursorPos (int pos) {
        cursorPos = pos <= size - 2 ? (pos >= 0 ? pos : 0) : size - 2;
    }

    public void mouseClicked (GuiCustom gui, int x, int y, int button) {
        if (inRect(gui, x, y) && display) {
            if (!isVertical) setCursorPos(x - gui.getLeft() - getX());
            else if (isVertical) setCursorPos(y - gui.getTop() - getY());
            this.started = true;
        }

    }

    public void mouseClickMove (GuiCustom gui, int mouseX, int mouseY) {
        if (started && display) {
            if (!isVertical) setCursorPos(mouseX - gui.getLeft() - getX());
            else if (isVertical) setCursorPos(mouseY - gui.getTop() - getY());
        }
    }

    public void mouseMovedOrUp (GuiCustom gui, int par1, int par2, int type) {
        if (type == 0 && display) {
            started = false;
            if(!isClientOnly) PacketHandler.sendInterfaceSliderPacket((byte) gui.id, (byte) this.sliderId, (int) getRatio());
        }
    }


    public void wheel(GuiCustom gui,int x, int y) { //TODO
        int dx = Mouse.getDWheel();
        if(inRect(gui, x, y) && display) setCursorPos(cursorPos + dx);
    }

    public int getRatio () {
        return (int) (100 * ((float) cursorPos / (float) (this.size - 2)));
    }

    public void hide () {
        this.display = false;
    }

    public void show () {
        this.display = true;
    }

    

}
