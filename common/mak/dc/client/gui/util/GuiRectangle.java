package mak.dc.client.gui.util;

import java.util.Arrays;

import net.minecraft.client.gui.GuiScreen;

public class GuiRectangle extends GuiScreen{
		
	private int x;
	private int y;
	protected int w;
	protected int h;
	protected boolean display = true;
		
	public GuiRectangle(int x, int y, int width, int height) {
			
			this.setX(x);
			this.setY(y);
			this.w = width;
			this.h = height;
	}
	
			
	public boolean inRect(GuiCustom gui, int mouseX, int mouseY) {
		mouseX -= gui.getLeft();
		mouseY -= gui.getTop();
		
		return getX() <= mouseX && mouseX <= getX() + w && getY() <= mouseY && mouseY <= getY() + h;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
	
	public void draw(GuiCustom gui, int srcX, int srcY) {
		gui.drawTexturedModalRect(gui.getLeft() + getX(), gui.getTop() + getY(), srcX, srcY, w, h);
	}
	
	public void drawHoverString(GuiCustom gui, int mouseX, int mouseY, String str) {
		if (inRect(gui, mouseX, mouseY)) {
			gui.drawHoverString(Arrays.asList(str.split("\n")), mouseX - gui.getLeft(), mouseY - gui.getTop());
		}
	}
	
	public void drawString(GuiCustom gui, String str, int x, int y,int maxSize) {
		drawString(gui, str, x, y, maxSize, "");
	}
	
	public void drawString(GuiCustom gui, String str, int x, int y,int maxSize, String color) {
		int colorCoded = getColor(color);
		gui.getFontRenderer().drawSplitString(str, x, y, maxSize, colorCoded);
	}
	
	
	
	private int getColor(String color) {
		int re = 0x404040;
		
		switch(color) {
		case "red" : 
			re = 0xFF0000;
			break;
		case "brown" :
			re = 0xA52A2A;
			break;
		case "chocolate" :
			re = 0xD2691E;
			break;
		case "blue" :
			re = 0x0000FF;
			break;
		case "aqua" :
			re = 0x00FFFF;
			break;
		case "blueviolet" :
		re = 0x8A2BE2;
			break;
		case "green" :
			re = 0x00FF00;
			break;
		case "white" :
			re = 0xFFFFFF;
			break;
		case "black" :
			re = 0x000000;
			break;
		case "dark golden" :
			re = 0xB8860B;
			break;
		case "dark magenta" :
			re = 0x8B008B;
			break;
		case "dark green" :
			re = 0x006400;
			break;
		case "dark blue" : 
			re = 0x00008B;
			break;
		case "light gray" : 
			re = 0xD3D3D3;
			break;
		case "light blue":
			break;
		case "light green" :
			re = 0x90EE90;
			break;
			
		}
		return re;
	}
	
	public void hide() {
	    this.display = false;
	}
	
	public void show() {
	    this.display = true;
	}
	
	protected boolean shouldDisplay() {
	    return this.display;
	}


	public void mouseClicked(GuiCustom gui, int x, int y, int id) {}


   

    
	
}

