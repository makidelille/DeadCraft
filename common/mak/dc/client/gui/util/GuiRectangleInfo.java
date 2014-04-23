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


	public GuiRectangleInfo(GuiCustom gui) {
		super(gui,-100, 15, 100, 164);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	}
	
	public void drawTexturedLeftRect() {
		GL11.glColor4f(1, 1, 1, 1);		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		parent.drawTexturedModalRect(parent.getLeft() + getX(), parent.getTop() + getY(), 0, 0 , this.w , this.h);
		
	}
	
	public void drawTexturedRedState(int i , int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		parent.drawTexturedModalRect(x + 1 , y, 100, (i- 1) * 16 , 16, 16);
		
	}
	
	public void drawButtonRedState(int i , int x, int y) {
		drawButtonBasedOnState(i,  x,  y, activeRedState != (i - 1));
	}

	public void setActiveRedState(byte redstoneState) {
		activeRedState = redstoneState;
		
	}
	
	public void drawSeparatorH(int x, int y, int width) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		parent.drawTexturedModalRect(parent.getLeft() + x, parent.getTop() + y, 0, 165, width, 1);
	
	}
	public void drawSeparatorV(int x, int y, int width) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		parent.drawTexturedModalRect(parent.getLeft() + x, parent.getTop() + y, 0, 165, 1, width);
	
	}

	public void drawButtonBasedOnState(int i, int x, int y, boolean enabled) {
		GuiButton button = new GuiButton(i, parent.getLeft() + x, parent.getTop() + y , 18, 18 , "");
		button.enabled = enabled;
		
		parent.getButtonList().add(button);
		
	}

}
