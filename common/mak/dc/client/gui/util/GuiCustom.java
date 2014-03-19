package mak.dc.client.gui.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiCustom extends GuiContainer {

    private Container container;
    public int id;
    protected ArrayList<GuiRectangle> subRect = new ArrayList();;
    
	public GuiCustom(Container par1Container, int id) {
		super(par1Container);
		this.container = par1Container;
		this.id=id;
		defineSubRect();
	}

	public int getLeft() {
		return guiLeft;
	}
	
	public int getTop() {
		return guiTop;
	}
	
	protected void drawHoverString(List lst, int x, int y) {
		drawHoveringText(lst, x, y, fontRendererObj);
	}	
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();

	}
		
	

	public List getButtonList() {
		return buttonList;
	}

	
	@Override
	public void actionPerformed(GuiButton button) {}
	
	public void close() {
		 mc.thePlayer.closeScreen();
	}
	
	public Container getContainer() {
	    return this.container;
	}
	
	

	@Override
    protected abstract void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY);
	@Override
    protected abstract void drawGuiContainerForegroundLayer(int x, int y);
	protected abstract void defineSubRect();

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		for(GuiRectangle rect : subRect) {
			rect.mouseClicked(this, par1, par2, par3);
		}
	}
	
	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}
}
