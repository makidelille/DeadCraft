package mak.dc.client.gui;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mak.dc.client.gui.container.ContainerGodBottler;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.items.ItemGodCan;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

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
		GL11.glDisable(GL11.GL_LIGHTING);
		
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);   	
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        
        subRect.get(0).draw(xSize, 0);
        subRect.get(1).draw(xSize + 4, 0);
        subRect.get(2).draw(xSize + 8, 4);
        subRect.get(3).draw(xSize + 8 +36 - getSizeBar(3), 0);
        subRect.get(4).draw(xSize,22);
        subRect.get(5).draw(xSize, 32 + 52 - getSizeBar(5));

        
        subRect.add(0,new GuiRectangle(this, 71, 23, 4, getSizeBar(0))); // vertical l 
		subRect.add(1,new GuiRectangle(this, 139, 23, 4, getSizeBar(1))); // vertical r 
		subRect.add(2,new GuiRectangle(this, 71, 45, getSizeBar(2), 4)); // horizontal r 
		subRect.add(3,new GuiRectangle(this, 107 + 36 - getSizeBar(3), 45, getSizeBar(3), 4)); // horizontal l 
		subRect.add(4,new GuiRectangle(this, 102, 49, 12, getSizeBar(4))); // fleche 
		subRect.add(5,new GuiRectangle(this, 16 , 7 + 52 - getSizeBar(5), 18, getSizeBar(5))); //power bar
	
	    GL11.glEnable(GL11.GL_LIGHTING);

	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		GL11.glDisable(GL11.GL_LIGHTING);
       GuiRectangle rect = new GuiRectangle(this, 16, 7, 18, 52);
       rect.drawHoverString(x, y, (StatCollector.translateToLocal("dc.power") + " :\n" + EnumChatFormatting.YELLOW + te.getPower() + "/" + te.MAXPOWER/1000f + StatCollector.translateToLocal("dc.kilo")));
       
       if(!te.hasStarted())
    	   this.drawInfoPanel(this.getProblems(), StatCollector.translateToLocal("dc.block.godBottler.gui.error.header"), -93, 8, 92);
       GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	protected void defineSubRect() {
		subRect.add(0,new GuiRectangle(this, 71, 23, 4, 0)); // vertical l 
		subRect.add(1,new GuiRectangle(this, 139, 23, 4, 0)); // vertical r 
		subRect.add(2,new GuiRectangle(this, 71, 45, 0, 4)); // horizontal r 
		subRect.add(3,new GuiRectangle(this, 107, 45, 0, 4)); // horizontal l 
		subRect.add(4,new GuiRectangle(this, 102, 49, 12, 0)); // fleche 
		subRect.add(5,new GuiRectangle(this, 16, 7, 18, 0)); // charge
	}
	
	private int getSizeBar(int i){
		if(i == 5) return (int) (52 * ((float)te.getPower()) / te.MAXPOWER);
		float workedTime  = this.te.getWorkedTime();
		float x = (float)this.te.getWorkedTime() /(float)this.te.BUILDTIME;
		if(workedTime <= te.BUILDTIME/3f)
			switch(i){
			case 0 : return (int) (22 * (3*x)); 
			case 1 : return (int) (22 * (3*x)); 
			default : return 0;			
			}
		
		else if(workedTime <= (5f* te.BUILDTIME)/6f && workedTime > te.BUILDTIME /3f)
			switch(i) {
			case 0 : return 22;
			case 1 : return 22;
			case 2 : return (int) (36* ((2*x) - (2f/3f)));
			case 3 : return (int) (36* ((2*x) - (2f/3f)));
			default : return 0;
			}
		else if(workedTime <= te.BUILDTIME && workedTime > 5f* te.BUILDTIME /6f)
			switch(i) {
			case 0 : return 22;
			case 1 : return 22;
			case 2 : return 36;
			case 3 : return 36;
			case 4 : return (int) (10 * (6f*x - 5f));
			default : return 0;
			}
		return 0; 
	}
	
	private String getProblems() {
		ArrayList errors = te.getBuildErrors(); 
		String re = "";
		for(int i=0; i< errors.size(); i++) {
			re += ("-"  + StatCollector.translateToLocal("dc.block.godBottler.gui.error." + errors.get(i).toString()) + "\n");
		}
		return re;		
	}

}
