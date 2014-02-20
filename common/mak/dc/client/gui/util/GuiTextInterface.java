package mak.dc.client.gui.util;

import org.lwjgl.opengl.GL11;

import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiTextInterface extends GuiRectangle{

    private final int id;
    private String text;
    private boolean isSelected;
    
    private static ResourceLocation texture      = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);

    
    public GuiTextInterface (int x, int y, int width, int id) {
        super(x, y, width, 12);
        this.id = id;
    }
    
    public void draw(GuiCustom gui) {
        if(this.shouldDisplay()) {
            GL11.glColor4f(1, 1, 1, 1);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
 //           gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY(),  srcX, srcY, width, 12);
            drawText(gui);
        }
    }
    
   
    public void drawText(GuiCustom gui) {
        this.drawString(gui, text, this.getX(), this.getY(), this.width, "black");
        
        
        drawCursor(gui);
    } 
    
    private void drawCursor (GuiCustom gui) {
        // TODO Auto-generated method stub
        
    }


}
