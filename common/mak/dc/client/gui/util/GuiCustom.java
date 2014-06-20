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
    protected ArrayList<GuiRectangle> subRect = new ArrayList();
    protected boolean haschange;;
    
    public GuiCustom(Container par1Container, int id) {
        super(par1Container);
        container = par1Container;
        this.id = id;
        defineSubRect();
    }
    
    @Override
    public void actionPerformed(GuiButton button) {
    }
    
    public void close() {
        mc.thePlayer.closeScreen();
    }
    
    protected abstract void defineSubRect();
    
    @Override
    protected abstract void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY);
    
    @Override
    protected abstract void drawGuiContainerForegroundLayer(int x, int y);
    
    protected void drawHoverRect(int startX, int startY, int endX, int endY) {
        int width = Math.abs(endX - startX);
        int height = Math.abs(endY - startY);
        
        int j1 = -267386864;
        drawGradientRect(startX - 3, startY - 4, startX + width + 3, startY - 3, j1, j1);
        drawGradientRect(startX - 3, startY + height + 3, startX + width + 3, startY + height + 4, j1, j1);
        drawGradientRect(startX - 3, startY - 3, startX + width + 3, startY + height + 3, j1, j1);
        drawGradientRect(startX - 4, startY - 3, startX - 3, startY + height + 3, j1, j1);
        drawGradientRect(startX + width + 3, startY - 3, startX + width + 4, startY + height + 3, j1, j1);
        
        int k1 = 1347420415;
        int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
        drawGradientRect(startX - 3, startY - 3 + 1, startX - 3 + 1, startY + height + 3, k1, l1);
        drawGradientRect(startX + width + 2, startY - 3 + 1, startX + width + 3, startY + height + 3 - 1, k1, l1);
        drawGradientRect(startX - 3, startY - 3, startX + width + 3, startY - 3 + 1, k1, k1);
        drawGradientRect(startX - 3, startY + height + 2, startX + width + 3, startY + height + 3, l1, l1);
    }
    
    protected void drawHoverString(List lst, int x, int y) {
        drawHoveringText(lst, x, y, fontRendererObj);
    }
    
    protected void drawInfoPanel(String infoStr, String header, int x, int y, int max) {
        int height = fontRendererObj.listFormattedStringToWidth(infoStr, max).size() + 1;
        if (infoStr.isEmpty()) {
            height = 1;
        }
        drawHoverRect(x, y, x + max - 3, y + height * fontRendererObj.FONT_HEIGHT);
        drawInfoString(infoStr, header, x, y, max);
    }
    
    protected void drawInfoString(String infoStr, String header, int x, int y, int maxSize) {
        FontRenderer ft = getFontRenderer();
        drawCenteredString(getFontRenderer(), header, x + maxSize / 2, y, GuiDrawHelper.getColor("red"));
        ft.drawSplitString(infoStr, x, y + 10, maxSize, 0xBBBBBB);
    }
    
    public List getButtonList() {
        return buttonList;
    }
    
    public Container getContainer() {
        return container;
    }
    
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }
    
    public int getLeft() {
        return guiLeft;
    }
    
    public int getTop() {
        return guiTop;
    }
    
    public float getzLevel() {
        return zLevel;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        for (GuiRectangle rect : subRect) {
            if (rect.hasMouseClicked(par1, par2, par3)) {
                haschange = true;
            }
        }
    }
    
    @Override
    protected void mouseClickMove(int par1, int par2, int par3, long par4) {
        super.mouseClickMove(par1, par2, par3, par4);
        for (GuiRectangle rect : subRect) {
            if (rect.hasMouseClickMove(par1, par2, par3, par4)) {
                haschange = true;
            }
        }
    }
    
    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3) {
        super.mouseMovedOrUp(par1, par2, par3);
        for (GuiRectangle rect : subRect) {
            if (rect.hasMouseMovedOrUp(par1, par2, par3)) {
                haschange = true;
            }
        }
        
    }
    
    protected void sendPacket() {
        if (haschange) {
            haschange = false;
        } else return;
    }
    
}
