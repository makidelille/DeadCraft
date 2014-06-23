package mak.dc.client.gui.util;

import java.util.Arrays;

import net.minecraft.client.gui.GuiScreen;

public class GuiRectangle extends GuiScreen {
        
    private int x;
    private int y;
    protected int w;
    protected int h;
    protected boolean display = true;
    
    protected GuiCustom parent;
    
    public GuiRectangle(GuiCustom gui, int x, int y, int width, int height) {
        
        setX(x);
        setY(y);
        w = width;
        h = height;
        parent = gui;
    }
    
    public void draw(int srcX, int srcY) {
        drawTexturedModalRect(parent.getLeft() + x, parent.getTop() + y, srcX, srcY, w, h);
    }
    
    public void drawHoverString(int mouseX, int mouseY, String str) {
        if (inRect(mouseX, mouseY)) {
            parent.drawHoverString(Arrays.asList(str.split("\n")), mouseX - parent.getLeft(), mouseY - parent.getTop());
        }
    }
    
    public void drawString(GuiCustom gui, String str, int x, int y, int maxSize) {
        drawString(gui, str, x, y, maxSize, "");
    }
    
    public void drawString(GuiCustom gui, String str, int x, int y, int maxSize, String color) {
        int colorCoded = GuiDrawHelper.getColor(color);
        gui.getFontRenderer().drawSplitString(str, x, y, maxSize, colorCoded);
    }
    
    public int getHeight() {
        return h;
    }
    
    public int getWidth() {
        return w;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean hasMouseClicked(int x, int y, int id) {
        mouseClicked(x, y, id);
        return false;
        
    }
    
    public boolean hasMouseClickMove(int par1, int par2, int par3, long par4) {
        mouseClickMove(par1, par2, par3, par4);
        return false;
    }
    
    public boolean hasMouseMovedOrUp(int par1, int par2, int par3) {
        mouseMovedOrUp(par1, par2, par3);
        return false;
    }
    
    public void hide() {
        display = false;
    }
    
    public boolean inRect(int mouseX, int mouseY) {
        mouseX -= parent.getLeft();
        mouseY -= parent.getTop();
        
        return getX() <= mouseX && mouseX <= getX() + w && getY() <= mouseY && mouseY <= getY() + h;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    protected boolean shouldDisplay() {
        return display;
    }
    
    public void show() {
        display = true;
    }
    
    @Override
    public void updateScreen() {
    }
}
