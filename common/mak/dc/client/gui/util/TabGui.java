package mak.dc.client.gui.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class TabGui extends GuiRectangle {
	
	public int id;

	private String name;

	public TabGui(String name, int id, int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void drawBackground(GuiCustom gui, int x, int y);
	public abstract void drawForeground(GuiCustom gui, int x, int y);
	public void mouseClick(GuiCustom gui, int x, int y, int button) {}
	public void mouseMoveClick(GuiCustom gui, int x, int y, int button, long timeSinceClicked) {}
	public void mouseReleased(GuiCustom gui, int x, int y, int button) {}

	
}
