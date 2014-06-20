package mak.dc.client.gui;

import mak.dc.client.gui.container.ContainerCompressor;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCompressor extends GuiCustom {
    
    private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.COMPRESSOR_GUI_TEXT_LOC);
    
    public GuiCompressor(InventoryPlayer inv, TileEntityCompressor te, int id) {
        super(new ContainerCompressor(inv, te), id);
        xSize = 166;
        ySize = 176;
    }

    @Override
    protected void defineSubRect() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
}
