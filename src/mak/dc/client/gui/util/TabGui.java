package mak.dc.client.gui.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class TabGui extends GuiRectangle {
    
    public int id;
    
    private String name;
    
    public TabGui(GuiCustom gui, String name, int id, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
        
        this.name = name;
        this.id = id;
    }
    
    public abstract void drawBackground(GuiCustom gui, int x, int y);
    
    public abstract void drawForeground(GuiCustom gui, int x, int y);
    
    public String getName() {
        return name;
    }
    
    public boolean hasMouseClick(GuiCustom gui, int x, int y, int button) {
        return false;
    }
    
    public boolean hasMouseMoveClick(GuiCustom gui, int x, int y, int button, long timeSinceClicked) {
        return false;
    }
    
    public boolean hasMouseReleased(GuiCustom gui, int x, int y, int button) {
        return false;
    }
    
}
