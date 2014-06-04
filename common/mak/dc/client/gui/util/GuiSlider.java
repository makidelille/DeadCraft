package mak.dc.client.gui.util;

import mak.dc.util.Lib;
import mak.dc.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiSlider extends GuiRectangle {

    private final int               size;
    private int                     cursorPos;
    private final int               sliderId;
    private int sizeBar;
    private boolean                 started;
    private final boolean           isVertical;
    private boolean                 isClientOnly = false;
    private boolean drawAmount;

    private final static ResourceLocation texture      = new ResourceLocation(Lib.MOD_ID, Textures.UTIL_GUI_TEXT_LOC);

    public GuiSlider (GuiCustom gui,int posX, int posY, int size,int sizeBar, int id, boolean isVertical) {
        super(gui, posX, posY, (!isVertical) ? (size >= 3 ? size * 2 : 2) : 10, (isVertical) ? (size >= 3 ? size * 2 : 2)
                : 10);

        this.size = size >= 3 ? size : 3;
        this.sizeBar = sizeBar < size - 2 ? sizeBar : size - 2;
        this.sliderId = id;
        this.isVertical = isVertical;
        this.drawAmount = true; //set to true to debug
        
        init();

    }

    public void draw () {
        if (this.shouldDisplay()) {
            GL11.glColor4f(1, 1, 1, 1);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            if (!isVertical) {
                parent.drawTexturedModalRect(getX(), getY(), 1, 166, 1, 10);
                for (int i = 0; i < size; i++) {
                    parent.drawTexturedModalRect(getX() + 1 + i, getY(), 5, 166, 2, 10);
                }
            } else if (isVertical) {
                parent.drawTexturedModalRect(getX(),getY(), 9, 166, 10, 1);
                for (int i = 0; i < size; i++) {
                    parent.drawTexturedModalRect(getX(),getY() + 1 + i, 9, 170, 10, 2);
                }
            }
            drawSlider(getCursorPos());
        }
    }


    private void drawSlider (int pos) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        for(int i =0 ; i < this.sizeBar; i++) {
	        if (!isVertical) {
	            parent.drawTexturedModalRect(getX() + pos + 1 + i, getY(), 7, 166, 2, 10);
	            
	        } else if (isVertical) {
	            parent.drawTexturedModalRect(getX(),getY() + pos + 1 +i, 9, 172, 10, 2);
	        }
        }
        if( drawAmount)
        	drawString(parent, "" + getRatio(),getX(), getY() - 10, 100);
        
    }

    private int getCursorPos () {
        cursorPos = (cursorPos <= size - 2 && cursorPos >= 0) ? cursorPos : 0;
        return cursorPos;
    }

    private void setCursorPos (int pos) {
        cursorPos = pos <= size - 1 - sizeBar ? (pos >= 0 ? pos : 0) : size - sizeBar - 1;
    }
    
    public void incCursorPos() {
    	setCursorPos((this.getCursorPos()) + 1);
    }
    
    public void decCursorpos() {
    	setCursorPos((this.getCursorPos()) - 1);
    }

    @Override
    public boolean hasMouseClicked (int x, int y, int button) {
        if (inRect(x, y) && display) {
            if (!isVertical) setCursorPos(x - parent.getLeft() - getX());
            else if (isVertical) setCursorPos(y - parent.getTop() - getY());
            this.started = true;
            return true;
        }
		return false;

    }
    @Override
    public boolean hasMouseClickMove (int mouseX, int mouseY, int par3, long par4) {
        if (started && this.shouldDisplay()) {
            if (!isVertical) setCursorPos(mouseX - parent.getLeft() - getX());
            else if (isVertical) setCursorPos(mouseY - parent.getTop() - getY());
        }
		return false;
    }
    @Override
    public boolean hasMouseMovedOrUp (int par1, int par2, int type) {
        if (type == 0 && this.shouldDisplay()) {
            started = false;
            if (!isClientOnly) return true;
        }return false;
    }

    public int getRatio () {
        return (int) ((float)(100f * (float)cursorPos / (float) (this.size-sizeBar - 1)));
    }
    
    public void init() {
    	this.buttonList.clear();
    	this.buttonList.add(new GuiButton(0, 0, 0, 0, 0, ""));
    	this.buttonList.add(new GuiButton(1, 0, 0, 0, 0, ""));
    }
    
    @Override
    protected void actionPerformed(GuiButton butt) {
    	switch(butt.id) {
    	case 0 : incCursorPos();
    	break;
    	case 1 : decCursorpos();
    	break;	
    	}
    }

	public int getSizeBar() {
		return this.sizeBar;
	}

	public int getSize() {
		return this.size;
	}

}
