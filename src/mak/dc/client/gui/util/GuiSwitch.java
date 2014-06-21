package mak.dc.client.gui.util;

import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiSwitch extends GuiRectangle {
    
    private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);
    
    private boolean active;
    private final int id;
    private final boolean isVertical;
    
    public GuiSwitch(GuiCustom gui, int x, int y, int id, boolean initState, boolean isVertical) {
        super(gui, x, y, 10 + (isVertical ? 0 : 9), 10 + (isVertical ? 9 : 0));
        
        this.id = id;
        active = initState;
        this.isVertical = isVertical;
    }
    
    public void draw(GuiCustom gui) {
        if (shouldDisplay()) {
            GL11.glColor4f(1, 1, 1, 1);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            if (!isVertical) {
                gui.drawTexturedModalRect(gui.getLeft() + getX() + (active ? 0 : 9), gui.getTop() + getY(), 18, 166, 10, 10);
                gui.drawTexturedModalRect(gui.getLeft() + getX() + (active ? 9 : 0), gui.getTop() + getY(), 9, 174, 10, 10);
            } else if (isVertical) {
                gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY() + (active ? 0 : 9), 18, 166, 10, 10);
                gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY() + (active ? 9 : 0), 9, 174, 10, 10);
            }
            
            drawState(gui);
        }
    }
    
    private void drawState(GuiCustom gui) {
        if (!isVertical) {
            gui.drawTexturedModalRect(gui.getLeft() + getX() + 4, gui.getTop() + getY() + 2, 1, 176, 2, 6);
            gui.drawTexturedModalRect(gui.getLeft() + getX() + 11, gui.getTop() + getY() + 2, 3, 176, 6, 6);
        } else if (isVertical) {
            gui.drawTexturedModalRect(gui.getLeft() + getX() + 4, gui.getTop() + getY() + 2, 1, 176, 2, 6);
            gui.drawTexturedModalRect(gui.getLeft() + getX() + 2, gui.getTop() + getY() + 11, 3, 176, 6, 6);
        }
    }
    
    @Override
    public boolean hasMouseClicked(int x, int y, int id) {
        super.hasMouseClicked(x, y, id);
        if (inRect(x, y) && shouldDisplay()) {
            setActiveState(!active);
            return true;
        }
        return false;
        
    }
    
    public boolean isActive() {
        return active;
    }
    
    private void setActiveState(boolean par) {
        active = par;
    }
    
}
