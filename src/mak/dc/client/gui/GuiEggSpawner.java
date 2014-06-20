package mak.dc.client.gui;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerEggSpawner;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiDrawHelper;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.common.network.packet.DeadCraftEggSpawnerPacket;
import mak.dc.common.tileEntities.TileEntityEggSpawner;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiEggSpawner extends GuiCustom {
    
    private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.EGGSPAWNER_GUI_TEXT_LOC);
    private TileEntityEggSpawner te;
    
    public GuiEggSpawner(InventoryPlayer inventory, TileEntityEggSpawner te, int iD) {
        super(new ContainerEggSpawner(inventory, te), iD);
        
        this.te = te;
        
        xSize = 184;
        ySize = 189;
        
    }
    
    @Override
    public void actionPerformed(GuiButton button) {
        DeadCraft.packetPipeline.sendToServer(new DeadCraftEggSpawnerPacket(te, (byte) button.id));
    }
    
    @Override
    protected void defineSubRect() {
        subRect.add(new GuiRectangle(this, 11, 8, 0, 3)); // TOP BAR
        subRect.add(new GuiRectangle(this, 29, 18, 16, 0)); // powerbar
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1, 1, 1, 1);
        
        GuiDrawHelper.drawLeftRect(this, 15, -80, 80, 95);
        GuiDrawHelper.drawHorizontalLine(this, -75, 60, 70);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        int powerBar;
        int durationBarLenght;
        
        powerBar = (int) (50 * (float) te.getPower() / TileEntityEggSpawner.MAXPOWER);
        subRect.set(1, new GuiRectangle(this, 29, 18 + 50 - powerBar, 16, powerBar));
        subRect.get(1).draw(185, 64 + 50 - powerBar);
        
        durationBarLenght = (int) (te.getProgress() * 1.60);
        subRect.set(0, new GuiRectangle(this, 11, 8, durationBarLenght, 3));
        subRect.get(0).draw(0, ySize);
        
        initGui();
        
        GL11.glEnable(GL11.GL_LIGHTING);
        
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        GL11.glDisable(GL11.GL_LIGHTING);
        // GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        getFontRenderer().drawSplitString(te.getInventoryName(), 122, 22, 46, 0x404040);
        
        String str = null;
        
        str = StatCollector.translateToLocal("dc.block.eggSpawner.gui.status") + " : " + (te.hasStarted() ? StatCollector.translateToLocal("dc.active") : StatCollector.translateToLocal("dc.inactive"));
        getFontRenderer().drawSplitString(str, 65, 57, 110, te.hasStarted() ? 0x00FF00 : 0xFF0000);
        
        str = StatCollector.translateToLocal("dc.progress") + " : " + te.getProgress() + "%";
        getFontRenderer().drawSplitString(str, 65, 67, 110, 0x404040);
        
        str = StatCollector.translateToLocal("dc.power") + " : " + te.getPower() + "/" + TileEntityEggSpawner.MAXPOWER / 1000 + StatCollector.translateToLocal("dc.kilo");
        getFontRenderer().drawSplitString(str, 65, 77, 110, 0x404040);
        
        str = StatCollector.translateToLocal("dc.block.eggSpawner.gui.eggInStock") + " : " + te.getEggInStock();
        getFontRenderer().drawSplitString(str, 65, 87, 110, 0x404040);
        
        redGuiDisplay();
        prodGuiDisplay();
        
        String infoStr = StatCollector.translateToLocal("dc.block.eggSpawner.gui.info.info");
        
        if (guiLeft - 70 <= x && x <= guiLeft - 70 + 18 && guiTop + 30 + 18 >= y && y >= guiTop + 30) {
            infoStr = StatCollector.translateToLocal("dc.block.eggSpawner.gui.info.redstone.normal");
        }
        if (guiLeft - 70 + 20 <= x && x <= guiLeft - 70 + 20 + 18 && guiTop + 30 + 18 >= y && y >= guiTop + 30) {
            infoStr = StatCollector.translateToLocal("dc.block.eggSpawner.gui.info.redstone.inverted");
        }
        if (guiLeft - 70 + 40 <= x && x <= guiLeft - 70 + 40 + 18 && guiTop + 30 + 18 >= y && y >= guiTop + 30) {
            infoStr = StatCollector.translateToLocal("dc.block.eggSpawner.gui.info.redstone.off");
        }
        if (guiLeft - 65 <= x && x <= guiLeft - 65 + 18 && guiTop + 72 <= y && y <= guiTop + 72 + 18) {
            infoStr = StatCollector.translateToLocal("dc.block.eggSpawner.gui.info.single");
        }
        if (guiLeft - 65 + 22 <= x && x <= guiLeft - 65 + 18 + 22 && guiTop + 72 <= y && y <= guiTop + 72 + 18) {
            infoStr = StatCollector.translateToLocal("dc.block.eggSpawner.gui.info.loop");
        }
        drawInfoPanel(infoStr, StatCollector.translateToLocal("dc.block.eggSpawner.gui.info.header"), -76, 115, 76);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        
    }
    
    @Override
    public void initGui() {
        super.initGui();
        int redstoneState = te.getRedstoneState();
        for (int i = 0; i < 3; i++) {
            GuiButton button = new GuiButton(1 + i, guiLeft - 70 + 20 * i, guiTop + 30, 18, 18, "");
            button.enabled = redstoneState != i;
            buttonList.add(button);
        }
        
        GuiButton button = new GuiButton(6, guiLeft + 90, guiTop - 20, 60, 20, StatCollector.translateToLocal("dc.stop"));
        button.enabled = te.hasStarted();
        buttonList.add(button);
        
        button = new GuiButton(0, guiLeft + 30, guiTop - 20, 60, 20, StatCollector.translateToLocal("dc.start"));
        button.enabled = !te.hasStarted();
        buttonList.add(button);
        
        for (int i = 0; i < 2; i++) {
            GuiButton button1 = new GuiButton(4 + i, guiLeft - 65 + 22 * i, guiTop + 72, 18, 18, "");
            button1.enabled = te.getMode() == i;
            buttonList.add(button1);
        }
        
    }
    
    private void prodGuiDisplay() {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        for (int i = 0; i < 2; i++) {
            drawTexturedModalRect(-64 + 22 * i, +73, xSize + 2, 0 + 16 * i, 18, 18);
        }
        getFontRenderer().drawSplitString(EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("dc.block.eggSpawner.gui.run.mode") + " : ", -62, 62, 70, 0x404040);
        
        String str = te.isRepeatOn() ? EnumChatFormatting.BLUE + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("dc.block.eggSpawner.gui.run.loop") : EnumChatFormatting.YELLOW + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("dc.block.eggSpawner.gui.run.single");
        
        getFontRenderer().drawSplitString(str, -70, 92, 70, 0x404040);
        
    }
    
    private void redGuiDisplay() {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LIGHTING);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        IIcon[] icons = { Items.redstone.getIconFromDamage(0), Blocks.redstone_torch.getIcon(0, 0), Items.gunpowder.getIconFromDamage(0) };
        drawTexturedModelRectFromIcon(-70 + 1, 30 + 1, Items.redstone.getIconFromDamage(1), 16, 16);
        drawTexturedModelRectFromIcon(-70 + 40 + 1, 30 + 1, Items.gunpowder.getIconFromDamage(1), 16, 16);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        drawTexturedModelRectFromIcon(-70 + 20 + 1, 30, Blocks.redstone_torch.getIcon(0, 0), 16, 16);
        
        String str = EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("dc.block.eggSpawner.gui.redstone.mode") + " : ";
        getFontRenderer().drawSplitString(str, -68, 20, 70, GuiDrawHelper.getColor("chocolate"));
        
        str = EnumChatFormatting.ITALIC + "";
        switch (te.getRedstoneState()) {
            case 0:
                str = EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("dc.block.eggSpawner.gui.redstone.normal");
                break;
            case 1:
                str = EnumChatFormatting.RED + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("dc.block.eggSpawner.gui.redstone.inverted");
                break;
            case 2:
                str = EnumChatFormatting.BLACK + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("dc.block.eggSpawner.gui.redstone.disable");
                break;
        }
        getFontRenderer().drawSplitString(str, -65, 50, 70, 0x404040);
        
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    
}
