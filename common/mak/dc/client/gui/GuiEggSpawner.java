package mak.dc.client.gui;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerEggSpawner;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.client.gui.util.GuiRectangleInfo;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.network.packet.DeadCraftEggSpawnerPacket;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

//TODO render bug depending if there is a crystal in inv

public class GuiEggSpawner extends GuiCustom{

	private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID , Textures.EGGSPAWNER_GUI_TEXT_LOC);
	private TileEntityEggSpawner te;
	
	public GuiEggSpawner(InventoryPlayer inventory, TileEntityEggSpawner te, int iD) {
		super(new ContainerEggSpawner(inventory, te), iD);
		
		this.te = te;
		
		xSize = 184;
		ySize = 189;	
		
		
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1, 1, 1, 1);
		
		
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int powerBar;
		int durationBarLenght;
		
		powerBar = (int) (50 *  (float) te.getPower() / (float) te.MAXPOWER);
		subRect.set(1, new GuiRectangle(this, 29, 18 + 50 - powerBar, 16, powerBar));
		subRect.get(1).draw(185, 64 + 50 - powerBar);
		
		durationBarLenght =  (int)(te.getProgress() * 1.60);
		subRect.set(0, new GuiRectangle (this, 11, 8, durationBarLenght, 3));
		subRect.get(0).draw(0, ySize);
		
		((GuiRectangleInfo) subRect.get(2)).drawTexturedLeftRect();	
		
		((GuiRectangleInfo) subRect.get(3)).drawSeparatorH(-85, 80, 75);
		
		
		initGui();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1, 1, 1, 1);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		getFontRenderer().drawSplitString("Dragon Egg Spawner", 122, 22, 46, 0x404040);
		
		String str = null;
		
		str = ("State: " + (te.hasStarted() ? "active" : "inactive"));
		getFontRenderer().drawSplitString(str, 65, 57, 110, (te.hasStarted() ? 0x00FF00 : 0xFF0000));
		
		
		str = ("Progress: " + te.getProgress() + "%");
		getFontRenderer().drawSplitString(str, 65, 67, 110, 0x404040);
				
		str = ("Power: " + te.getPower() + "/" + te.MAXPOWER);
		getFontRenderer().drawSplitString(str, 65, 77, 110, 0x404040);

		str = ("Egg in stock: " + te.getEggInStock());
		getFontRenderer().drawSplitString(str, 65, 87, 110, 0x404040);
		
		redGuiDisplay();
		prodGuiDisplay();
		GL11.glEnable(GL11.GL_LIGHTING);
				
	}
		
	

	@Override
	public void initGui() {
		super.initGui();
		
		drawButtonStartStop();
		
		for (int i = 0 ; i < 3; i++) {
			((GuiRectangleInfo) subRect.get(2)).drawButtonRedState(i + 1, -22, 20 + i * 20);
		}
		
		for (int i = 4 ; i < 6 ; i++) {
			((GuiRectangleInfo) subRect.get(3)).drawButtonBasedOnState(i, -22, 83 + (i - 4) * 20 , te.isRepeatOn(i - 4)); //change to a switch gui
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
		DeadCraft.packetPipeline.sendToServer(new DeadCraftEggSpawnerPacket(te,(byte) button.id));
	}

		
	private void redGuiDisplay() {
		String str = "";
		
		for (int i = 0 ; i < 3; i++) {
			((GuiRectangleInfo) subRect.get(2)).drawTexturedRedState(i + 1, -22, 20 + i * 20);
		}
		
		((GuiRectangleInfo) subRect.get(2)).setActiveRedState(te.getRedstoneState());
		
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
		
		((GuiRectangleInfo) subRect.get(2)).drawString(this, str, - 90, 25, 70);
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
		
		((GuiRectangleInfo) subRect.get(3)).drawString(this, str, -90,  85, 70);
	}

	@Override
	protected void defineSubRect() {
		subRect.add(new GuiRectangle (this, 11, 8, 0, 3)); //TOP BAR
		subRect.add(new GuiRectangle(this, 29, 18, 16, 0)); //powerbar
		subRect.add(new GuiRectangleInfo(this));
		subRect.add(new GuiRectangleInfo(this));
		
	}
}