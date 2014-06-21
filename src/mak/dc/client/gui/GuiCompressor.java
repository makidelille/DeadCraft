package mak.dc.client.gui;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerCompressor;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.client.gui.util.GuiSwitch;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import mak.dc.network.pipeline.packets.DeadCraftCompressorPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCompressor extends GuiCustom {
    
    private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.COMPRESSOR_GUI_TEXT_LOC);
    private TileEntityCompressor te;
    
    public GuiCompressor(InventoryPlayer inv, TileEntityCompressor te, int id) {
        super(new ContainerCompressor(inv, te), id);
        xSize = 176;
        ySize = 166;
        this.te = te;
    }
    
    @Override
    protected void defineSubRect() {
        this.subRect.add(0, new GuiRectangle(this, 77, 33, 30, 18));
        this.subRect.add(1, new GuiRectangle(this, 7, 58, 18, 52));
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        this.subRect.get(0).draw(xSize + 18, 0);
        this.subRect.set(0, new GuiRectangle(this, 77, 33, getSize(0), 18));
        
        this.subRect.get(1).draw(xSize, 52 - getSize(1));
        this.subRect.add(1, new GuiRectangle(this, 7, 58-getSize(1), 18, getSize(1)));
            
        
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.drawString(getFontRenderer(), "inverted : " + te.isInverted(), 1, 1, 0x404040); //TODO strings
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0,guiLeft + 76, guiTop + 16, 30, 12, "button")); //TODO strings
    }
    
    @Override
    public void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:
                DeadCraft.packetPipeline.sendToServer(new DeadCraftCompressorPacket(te.xCoord,te.yCoord,te.zCoord, !te.isInverted()));
                break;
        }
    }
    
    
    private int getSize(int i) {
        switch (i) {
            case 0:
                return (int) (30 * ((float) te.getProgress()) / (float) te.BUILDTIME);
            case 1:
                return (int) (52 * ((float) te.getCharge()) / (float) te.getMaxPower());
        }
        return 0;
    }
    
    
}
