package mak.dc.client.gui;

import mak.dc.client.gui.container.ContainerEggSpawner;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.client.gui.util.GuiRectangleInfo;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.network.PacketHandler;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiEggSpawner extends GuiCustom{

	private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID , Textures.EGGSPAWNER_GUI_TEXT_LOC);

	private GuiRectangle durationBar;
	private GuiRectangle[] lifeBar = {null,null};
	private GuiRectangleInfo infoRed = new GuiRectangleInfo();
	private GuiRectangleInfo infoProd = new GuiRectangleInfo(); 

	private TileEntityEggSpawner te;
	
	public GuiEggSpawner(InventoryPlayer inventory, TileEntityEggSpawner te) {
		super(new ContainerEggSpawner(inventory, te));
		
		this.te = te;
		
		xSize = 184;
		ySize = 189;
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		GL11.glColor4f(1, 1, 1, 1);
		
		
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int[] lifeBarHeight = {0,0};
		int durationBarLenght;
		
		for(int i =0; i < lifeBar.length; i++) {
			lifeBarHeight[i] = (int) (te.getLifeBar(i) * 6.3F) ;
			lifeBar[i] = new GuiRectangle(27 + i * 18, 81 - lifeBarHeight[i], 2, lifeBarHeight[i]);
			lifeBar[i].draw(this, xSize, 63 - lifeBarHeight[i]);
		}
		durationBarLenght =  (int)(te.getProgress() * 1.60);
		durationBar = new GuiRectangle (11, 8, durationBarLenght, 3);
		durationBar.draw(this, 0, ySize);
		
		infoRed.drawTexturedLeftRect(this);	
		
		infoRed.drawSeparatorH(this, -85, 80, 75);
		
		
		initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawSplitString("Dragon Egg Spawner", 122, 19, 46, 0x404040);
		
		String str = null;
		
		str = ("State : " + (te.hasStarted() ? "active" : "inactive"));
		fontRenderer.drawSplitString(str, 65, 56, 110, (te.hasStarted() ? 0x00FF00 : 0xFF0000));
		
		
		str = ("Progress : " + te.getProgress() + "%");
		fontRenderer.drawSplitString(str, 65, 66, 110, 0x404040);
		
		
		str = ("Life multiplier : " + te.getLifeMultiplier() + "%");
		fontRenderer.drawSplitString(str, 65, 76, 110, 0x404040);
		
		str = ("Egg in stock : " + te.getEggInStock());
		fontRenderer.drawSplitString(str, 65, 86, 110, 0x404040);
		
		redGuiDisplay();
		prodGuiDisplay();
				
	}
		
	

	@Override
	public void initGui() {
		super.initGui();
		
		drawButtonStartStop();
		
		for (int i = 0 ; i < 3; i++) {
			infoRed.drawButtonRedState(this, i + 1, -22, 20 + i * 20);
		}
		
		for (int i = 4 ; i < 6 ; i++) {
			infoProd.drawButtonBasedOnState(this, i, -22, 83 + (i - 4) * 20 , te.isRepeatOn(i - 4));
		}
	
	
		
	}
		
	private void drawButtonStartStop() {
		GuiButton button = new GuiButton(6, guiLeft + 90, guiTop - 20 , 60, 20 , "Stop");
		button.enabled = te.hasStarted();
		buttonList.add(button);
		
		button = new GuiButton(0, guiLeft + 30, guiTop - 20, 60, 20, "Start");
		button.enabled = !te.hasStarted();
		buttonList.add(button);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		
		PacketHandler.sendInterfacePacket((byte) 1,(byte) button.id);
	}

		
	private void redGuiDisplay() {
		String str = "";
		
		for (int i = 0 ; i < 3; i++) {
			infoRed.drawTexturedRedState(this, i + 1, -22, 20 + i * 20);
		}
		
		infoRed.setActiveRedState(te.getRedstoneState());
		
		switch (te.getRedstoneState()) {
		case 0:
			str = "emit a short pulse ";
			break;
		case 1:
			str = "stop emitting ";
			break;
		case 2 :
			str = "does nothing ";
			break;
		}
		
		str = str + "when a dragon egg spawn";
		
		infoRed.drawString(this, str, - 90, 25, 70);
	}
	

	private void prodGuiDisplay( ) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			
		String str = "";
					
		for (int i = 0 ; i < 2 ; i++) {
			drawTexturedModalRect(-21, 84 + i * 20, xSize + 2, i * 16, 16, 16);
			}
		
		switch(te.getMode()) {
		case 1 :
			str = "the spawner is set to create only one egg";
			break;
		case 0 :
			str = "the spawner is in loop mode, it create eggs until you clicked ths 'Stop' button, or change mode";
		}
		
		infoProd.drawString(this, str, -90,  85, 70);
	}
}