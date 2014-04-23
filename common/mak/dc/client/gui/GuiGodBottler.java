package mak.dc.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import mak.dc.client.gui.container.ContainerGodBottler;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityEggSpawner;
import mak.dc.tileEntities.TileEntityGodBottler;

public class GuiGodBottler extends GuiCustom {
	
	private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.GODBOTTLER_GUI_TEXT_LOC);
	private static final ResourceLocation textUtil = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);
	private TileEntityGodBottler te;



	public GuiGodBottler(InventoryPlayer inventory, TileEntityGodBottler te2,int iD) {
		super(new ContainerGodBottler(inventory, te2),iD);
		this.xSize = 176;
		this.ySize = 166;
		this.te = te2;
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX,	int mouseY) {
		GL11.glColor4f(1, 1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);   	
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        
        subRect.get(0).draw(xSize, 0);
        subRect.get(1).draw(xSize + 4, 0);
        subRect.get(2).draw(xSize + 8, 4);
        subRect.get(3).draw(xSize + 8, 0);
//        subRect.get(4).draw(xSize,22);

//        System.out.println(getSizeBar(0));
        subRect.add(0,new GuiRectangle(this, 71, 23, 4, getSizeBar(0))); // vertical l 
		subRect.add(1,new GuiRectangle(this, 139, 23, 4, getSizeBar(1))); // vertical r 
		subRect.add(2,new GuiRectangle(this, 71, 45, getSizeBar(2), 4)); // horizontal r 
		subRect.add(3,new GuiRectangle(this, 107, 45, getSizeBar(3), 4)); // horizontal l 
//		subRect.add(4,new GuiRectangle(this, 102, 49, 12, getSizeBar(4))); // fleche 
	
	
   	
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {

	}

	@Override
	protected void defineSubRect() {
		subRect.add(0,new GuiRectangle(this, 71, 23, 4, 0)); // vertical l 
		subRect.add(1,new GuiRectangle(this, 139, 23, 4, 0)); // vertical r 
		subRect.add(2,new GuiRectangle(this, 71, 45, 0, 4)); // horizontal r 
		subRect.add(3,new GuiRectangle(this, 107, 45, 0, 4)); // horizontal l 
//		subRect.add(4,new GuiRectangle(this, 102, 49, 12, 0)); // fleche 
	}
	
	private int getSizeBar(int i){
		int workedTime  = this.te.getWorkedTime();
		if(workedTime <= te.buildTime/3)
			switch(i){
			case 0 : return 22 * (3*workedTime)/te.buildTime; 
			case 1 : return 22 * (3*workedTime)/te.buildTime; 
			default : return 0;			
			}
		
		else if(workedTime <= 5 * te.buildTime/6 && workedTime > te.buildTime /3)
			switch(i) {
			case 0 : return 22;
			case 1 : return 22;
			case 2 : 
				System.out.println((float) (6*workedTime)/ (5* te.buildTime));
				
				return 36 * ((6*workedTime)/ (5* te.buildTime)); //TODO
//			case 3 : return 36 * (5*workedTime) / (6*te.buildTime);
			default : return 0;
			}
//		else if(workedTime <= te.buildTime && workedTime > 5* te.buildTime /6)
//			switch(i) {
//			case 0 : return 22;
//			case 1 : return 22;
//			case 2 : return 36;
//			case 3 : return 36;
//			case 4 : return subRect.get(i).height * (6*workedTime) /te.buildTime;
//			default : return 0;
//			}
		return 0; 
	}

}
