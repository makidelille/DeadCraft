package mak.dc.client.gui.util;

import org.lwjgl.opengl.GL11;

import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiSlider extends GuiRectangle {

    private int                     size;
    private int                     cursorPos;
    private int                     intiId;
    private boolean                 selected;

    private static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);

    public GuiSlider (int posX, int posY, int size, int initId) {
        super(posX, posY, 4, 6);

        this.size = size;
        this.intiId = initId;
    }

    public void draw (GuiCustom gui) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY(), 1, 166, 2, 6);
        for (int i = 0; i < size; i++) {
            gui.drawTexturedModalRect(gui.getLeft() + getX() + 2 + i, gui.getTop() + getY(), 3, 166, 2, 6);
        }

        drawSlider(gui, getCursorPos());

    }

    private void drawSlider (GuiCustom gui, int pos) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        gui.drawTexturedModalRect(gui.getLeft() + getX() + pos + 1, gui.getTop() + getY(), 5, 166, 1, 6);
    }

    private int getCursorPos () {
        cursorPos = (cursorPos <= size && cursorPos >= 0) ? cursorPos : 0;
        return cursorPos;
    }

    private void setCursorPos (int pos) {
        cursorPos = (pos <= size && pos >= 0) ? pos : 0;
    }



    private void mouseDragged(GuiCustom gui , int mouseX, int mouseY) {
        if(selected) {
            if(mouseX > cursorPos + getX()) setCursorPos(cursorPos++);
            else if(mouseX < cursorPos + getX()) setCursorPos(cursorPos--);
        }

    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int type)
    {
        System.out.println(inRect(mouseX, mouseY));
        if(type == 0 || type == 1 )
          this.selected = false;
        else if (type == -1)
            if(mouseX > cursorPos + getX()) setCursorPos(cursorPos++);
            else if(mouseX < cursorPos + getX()) setCursorPos(cursorPos--);

    }
    
    @Override
    protected void mouseClicked(int x, int y, int par3) {
        System.out.println("click");
        if(inRect(x, y)) this.selected = true;
        
    }

    
    private boolean inRect(int mouseX, int mouseY) {
        return (mouseX == getCursorPos()+ getX() && mouseY >= getY() && mouseY <= getY() + 6);
    }
    
    @Override
    public void updateScreen () {
        this.handleInput();
    }
    
    





}
