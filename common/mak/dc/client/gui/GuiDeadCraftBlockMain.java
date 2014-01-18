package mak.dc.client.gui;

import org.lwjgl.opengl.GL11;

import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDeadCraftBlockMain extends GuiCustom {

    private GuiRectangle names;

    private static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.DEADCRAFTMAIN_GUI_TEXT_LOC);

    public GuiDeadCraftBlockMain (InventoryPlayer invPlayer, TileEntityDeadCraft te) {
        super(new ContainerDeadCraft(invPlayer, te, false));
      
        xSize = 176;
        ySize = 166;
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float f, int mouseX, int mouseY) {
        GL11.glColor4f(1, 1, 1, 1);



        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int x, int y) {
        // TODO Auto-generated method stub

    }

}
