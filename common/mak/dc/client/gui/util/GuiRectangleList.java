package mak.dc.client.gui.util;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiRectangleList extends GuiRectangle {

	private GuiSlider  scrollSlider;
	
	private int sliderPos;
	private ArrayList list;
	
	private final static ResourceLocation texture      = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);

	private int _MAXSIZE;
	private int selected = 0;


	
	// new GuiRectangleList(this, 7, 10, 120, 100, allowed, 108, 25, 49, 30 , 0, true)
	
	public GuiRectangleList(GuiCustom gui,int x, int y, int width, int height, ArrayList list, int sliderId, boolean isSliderVertical) {
		super(gui, x, y, width, height);
		
		 scrollSlider = new GuiSlider(gui, x + width - 12, y + 2, height - 5 ,15, sliderId, isSliderVertical);  
	     this.list = list;
	     this._MAXSIZE =  (int) (-1 + (int) (width /5));
	     System.out.println(_MAXSIZE);
	
	}
	
	
	
	@Override
	public void draw(int srcX, int srcY) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawBody();
        drawBox();
        
        drawMask();
		scrollSlider.draw();
	}
		
	private void drawMask() {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
	}
//TODO finish see teselator and GL11
	private void drawBox() {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		int start = getPadding();
		
		
		
		for(int index =0 ; index < list.size(); index++) {
			System.out.println(index);
			String str = getStringToDisplay(index);
			int size = (1+str.length() / _MAXSIZE) * 9;
			for(int i =0; i < this.w - 15; i++) 
				for(int j = 0; j < size;j++ )
					this.drawTexturedModalRect(getX() + 2 + i, getY() + j - start + index * size, isSelected(index) ? 32 : 31, 166, 1, 1);
				this.drawString(parent, str, getX() + 3, getY() - start + index * size, (int) (_MAXSIZE * 4.5));
		}
	}


	private boolean isSelected(int index) {
		return index == selected;
	}



	private void drawBody() {
		for(int k = 0; k < this.w; k++) {
			for (int l = 0 ; l < this.h; l++)
				this.drawTexturedModalRect(getX() + k, getY() + l, 30, 166, 1, 1);
		}
		for(int i = 0; i < this.w; i++) {
			this.drawTexturedModalRect(getX() + i, getY(), 32, 166, 1, 1);
			this.drawTexturedModalRect(getX() + i, getY() + h-1, 28, 166, 1, 1);
		}
		for (int j = 0 ; j < this.h; j++) {
			this.drawTexturedModalRect(getX(), getY() + j, 32, 166, 1, 1);
			this.drawTexturedModalRect(getX() + w - 1, getY() + j, 28, 166, 1, 1);
		}
		this.drawTexturedModalRect(getX(), getY() + h-1, 31, 166, 1, 1);
		this.drawTexturedModalRect(getX() + w - 1, getY(), 31, 166, 1, 1);
		
		
		
	}
	
	@Override
	public boolean hasMouseClicked(int x, int y, int id) {
		super.mouseClicked( x, y, id);
		boolean re =false;
		scrollSlider.hasMouseClicked( x, y, id);
		
		
		return re;
	}
	
	@Override
	public boolean hasMouseClickMove(int par1, int par2, int par3, long par4) {
		super.mouseClickMove(par1, par2, par3, par4);
		scrollSlider.hasMouseClickMove(par1, par2,par3,par4);
		return false;
	}
	
	@Override
	public boolean hasMouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);
        scrollSlider.hasMouseMovedOrUp(par1,par2,par3);
        return false;
	}
	
	private String getStringToDisplay(int index) {
		if(list.size() >= index)		
			return list.get(index).toString();
		else
			return "NULL";
	}
	
	private int getPadding() {
		//System.out.println(scrollSlider.getMult());
		return (int)(scrollSlider.getMult()  * (scrollSlider.getRatio() * h ) /(100));
	}



	public void updateList(ArrayList list) {
		this.list = list;
		
	}
	
	

}
