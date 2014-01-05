package mak.dc.client.gui.util;

import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiRectangleInfo extends GuiRectangle {
	
	private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID , Textures.UTIL_GUI_TEXT_LOC);
	private byte activeRedState;


	public GuiRectangleInfo() {
		super(-100, 15, 100, 164);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	}
	
	public void drawTexturedLeftRect(GuiCustom gui) {
		GL11.glColor4f(1, 1, 1, 1);		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY(), 0, 0 , this.w , this.h);
		
	}
	
	public void drawTexturedRedState(GuiCustom gui , int i , int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		gui.drawTexturedModalRect(x + 1 , y, 100, (i- 1) * 16 , 16, 16);
		
	}
	
	public void drawButtonRedState(GuiCustom gui , int i , int x, int y) {
		drawButtonBasedOnState(gui,  i,  x,  y, activeRedState != (i - 1));
	}

	public void setActiveRedState(byte redstoneState) {
		activeRedState = redstoneState;
		
	}
	
	public void drawSeparatorH(GuiCustom gui, int x, int y, int width) {
		gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, 0, 165, width, 1);
	
	}
	public void drawSeparatorV(GuiCustom gui, int x, int y, int width) {
		gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, 0, 165, 1, width);
	
	}

	public void drawButtonBasedOnState(GuiCustom gui, int i, int x, int y, boolean enabled) {
		GuiButton button = new GuiButton(i, gui.getLeft() + x, gui.getTop() + y , 18, 18 , "");
		button.enabled = enabled;
		
		gui.getButtonList().add(button);
		
	}

}
