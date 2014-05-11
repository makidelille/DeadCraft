package mak.dc.client.gui.util;

import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiDrawHelper extends GuiScreen{

	private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);
	private static final float p = 1f/256;
	
	public GuiDrawHelper() {}
	
	public static void drawLeftRect(GuiCustom gui, int top, int right, int width, int height){
						
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glColor4f(1f, 1f, 1f, 1f);		
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV(gui.getLeft() + right + 0, gui.getTop() + top + height, gui.getzLevel(), 0, 165 *p);
	        tessellator.addVertexWithUV(gui.getLeft() + right + width, gui.getTop() + top + height, gui.getzLevel(), 100 *p, 165*p);
	        tessellator.addVertexWithUV(gui.getLeft() + right + width, gui.getTop() + top + 0, gui.getzLevel(), 100 * p, 0);
	        tessellator.addVertexWithUV(gui.getLeft() + right + 0,gui.getTop() + top + 0,gui.getzLevel(), 0, 0);
        tessellator.draw();
	}
	
	public static void drawRightRect(GuiCustom gui, int top, int left, int width, int height){
		
		//TODO later
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glRotatef(180f, 0, 1f, 0);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV(gui.getLeft() + left + 0, gui.getTop() + top + height, gui.getzLevel(), 100*p, 165 *p);
	        tessellator.addVertexWithUV(gui.getLeft() + left - width, gui.getTop() + top + height, gui.getzLevel(), 000 *p, 165*p);
	        tessellator.addVertexWithUV(gui.getLeft() + left - width, gui.getTop() + top + 0, gui.getzLevel(), 000 * p, 0);
	        tessellator.addVertexWithUV(gui.getLeft() + left + 0,gui.getTop() + top + 0,gui.getzLevel(), 100 * p, 0);
        tessellator.draw();
        GL11.glRotatef(180, 0, -1f, 0);
		
		
	}
		
	public static void drawHorizontalLine(GuiCustom gui, int startX, int startY, int width) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		gui.drawTexturedModalRect(gui.getLeft() + startX, gui.getTop() + startY, 0, 165, width, 1);
	}

	
	public static int getColor(String color) {	
		switch(color) {
			case "red" : return 0xFF0000;
			case "brown" : return 0xA52A2A;
			case "chocolate" : return 0xD2691E;
			case "blue" : return 0x0000FF;		
			case "aqua" : return 0x00FFFF;
			case "blueviolet" :	return 0x8A2BE2;
			case "green" : return 0x00FF00;
			case "white" : return 0xFFFFFF;
			case "black" : return 0x000000;
			case "dark golden" : return 0xB8860B;
			case "dark magenta" : return 0x8B008B;
			case "dark green" :	return 0x006400;	
			case "dark blue" : 	return 0x00008B;
			case "light gray" : return 0xD3D3D3;
			case "light blue": return 0xADD8E6;
			case "light green" : return 0x90EE90;
			}
		return 0x404040;
		}
	
}
