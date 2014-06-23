package mak.dc.client.gui.util;

import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiDrawHelper extends GuiScreen {

	private static final ResourceLocation texture = new ResourceLocation(
			Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);
	private static final float p = 1f / 256;

	public static void drawHorizontalLine(GuiCustom gui, int startX,
			int startY, int width) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		gui.drawTexturedModalRect(gui.getLeft() + startX,
				gui.getTop() + startY, 0, 165, width, 1);
	}

	public static void drawLeftRect(GuiCustom gui, int top, int right,
			int width, int height) {

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(gui.getLeft() + right + 0, gui.getTop()
				+ top + height, gui.getzLevel(), 0, 165 * p);
		tessellator.addVertexWithUV(gui.getLeft() + right + width, gui.getTop()
				+ top + height, gui.getzLevel(), 100 * p, 165 * p);
		tessellator.addVertexWithUV(gui.getLeft() + right + width, gui.getTop()
				+ top + 0, gui.getzLevel(), 100 * p, 0);
		tessellator.addVertexWithUV(gui.getLeft() + right + 0, gui.getTop()
				+ top + 0, gui.getzLevel(), 0, 0);
		tessellator.draw();
	}

	public static void drawRightRect(GuiCustom gui, int top, int left,
			int width, int height) {

		// TODO later

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glRotatef(180f, 0, 1f, 0);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(gui.getLeft() + left + 0, gui.getTop()
				+ top + height, gui.getzLevel(), 100 * p, 165 * p);
		tessellator.addVertexWithUV(gui.getLeft() + left - width, gui.getTop()
				+ top + height, gui.getzLevel(), 000 * p, 165 * p);
		tessellator.addVertexWithUV(gui.getLeft() + left - width, gui.getTop()
				+ top + 0, gui.getzLevel(), 000 * p, 0);
		tessellator.addVertexWithUV(gui.getLeft() + left + 0, gui.getTop()
				+ top + 0, gui.getzLevel(), 100 * p, 0);
		tessellator.draw();
		GL11.glRotatef(180, 0, -1f, 0);

	}

	public static int getColor(String color) {

		if (color == "red")
			return 0xFF0000;
		if (color == "brown")
			return 0xA52A2A;
		if (color == "chocolate")
			return 0xD2691E;
		if (color == "blue")
			return 0x0000FF;
		if (color == "aqua")
			return 0x00FFFF;
		if (color == "blueviolet")
			return 0x8A2BE2;
		if (color == "green")
			return 0x00FF00;
		if (color == "white")
			return 0xFFFFFF;
		if (color == "black")
			return 0x000000;
		if (color == "dark golden")
			return 0xB8860B;
		if (color == "dark magenta")
			return 0x8B008B;
		if (color == "dark green")
			return 0x006400;
		if (color == "dark blue")
			return 0x00008B;
		if (color == "light gray")
			return 0xD3D3D3;
		if (color == "light blue")
			return 0xADD8E6;
		if (color == "light green")
			return 0x90EE90;

		return 0x404040;
	}

	public GuiDrawHelper() {
	}

}
