package mak.dc.client.gui;

import org.lwjgl.opengl.GL11;

import mak.dc.client.gui.container.ContainerItemWithPower;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.common.inventory.InventoryItemWithPower;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiItemCharge extends GuiCustom{

	private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.ITEMCHARGE_GUI_TEXT_LOC);
	
	public GuiItemCharge(EntityPlayer player,InventoryItemWithPower itemInv, int id) {
		super(new ContainerItemWithPower(player, itemInv), id);
		this.xSize = 176;
		this.ySize = 165;
	}

	@Override
	protected void defineSubRect() {}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX,	int mouseY) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(texture);
        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {}

}
