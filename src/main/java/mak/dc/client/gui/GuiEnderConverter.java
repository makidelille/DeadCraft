package mak.dc.client.gui;

import mak.dc.client.gui.container.ContainerEnderConverter;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.common.tileEntities.TileEntityEnderConverter;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiEnderConverter extends GuiCustom {
    
    private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.ENDERCONVERTER_GUI_TEXT_LOC);
    private TileEntityEnderConverter te;
    
    public GuiEnderConverter(InventoryPlayer inv, TileEntityEnderConverter te, int id) {
        super(new ContainerEnderConverter(inv, te), id);
        
        xSize = 176;
        ySize = 166;
        
        this.te = te;
    }
    
    @Override
    protected void defineSubRect() {
        subRect.add(0, new GuiRectangle(this, 79, 6, 18, 52));
        
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(texture);
        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        int size = (int) (52 * (float) te.getPower() / TileEntityEnderConverter.MAXPOWER);
        
        subRect.add(0, new GuiRectangle(this, 79, 6 + 52 - size, 18, size));
        
        subRect.get(0).draw(xSize, 52 - size);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        int x = mx - guiLeft;
        int y = my - guiTop;
        if (x <= 80 + 16 && x >= 80 && y >= 61 && y <= 61 + 16) {
            drawInfoPanel("" + te.getPowerLeft(), StatCollector.translateToLocal("dc.block.enderConverter.gui.powerLeft") + " : ", 17, 60, 60);
        }
        if (x <= 79 + 18 && x >= 79 && y <= 6 + 52 && y >= 6) {
            drawInfoPanel("" + te.getPower() + "/" + TileEntityEnderConverter.MAXPOWER / 1000 + StatCollector.translateToLocal("dc.kilo"), StatCollector.translateToLocal("dc.power") + " : ", 17, y, 60);
        }
    }
    
}
