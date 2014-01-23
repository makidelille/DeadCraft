package mak.dc.client.gui.util;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiCustom extends GuiContainer {

    private Container container;
    public int id;
    
	public GuiCustom(Container par1Container, int id) {
		super(par1Container);
		this.container = par1Container;
		this.id=id;
	}

	public int getLeft() {
		return guiLeft;
	}
	
	public int getTop() {
		return guiTop;
	}
	
	protected void drawHoverString(List lst, int x, int y) {
		drawHoveringText(lst, x, y, fontRenderer);
	}	
	
	protected FontRenderer getFontRenderer() {
		return fontRenderer;
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
}
