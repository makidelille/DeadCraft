package mak.dc.client.gui;

import java.util.ArrayList;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.client.gui.util.GuiSlider;
import mak.dc.client.gui.util.GuiSwitch;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.network.DeadCraftAdminPacket;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiDeadCraftBlockMain extends GuiCustom {

    private GuiRectangle            names;
    private GuiSwitch               lock;
    private GuiTextField            entername;
    private GuiSlider               scrollSlider;
    private TileEntityDeadCraft     te;
    private String                  user;
    private ArrayList               allowed;
    private boolean hasChanged;
    private boolean hasToSend;
    private String s;
    private boolean hasInit = false;
	private boolean isLocked;

    private static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.DEADCRAFTMAIN_GUI_TEXT_LOC);

    public GuiDeadCraftBlockMain (InventoryPlayer invPlayer, TileEntityDeadCraft te, int iD) {
        super(new ContainerDeadCraft(invPlayer, te, false), iD);

        this.te = te;
        this.user = invPlayer.player.getCommandSenderName();
        this.allowed = te.getAllowedUser(); //TODO there is a bug somewher :(
        this.isLocked = this.te.isLocked();
                
        xSize = 176;
        ySize = 166;
        
        this.s = "";

        names = new GuiRectangle(7, 10, 120, 100);
        scrollSlider = new GuiSlider(108, 25, 49, 0, true);
        scrollSlider.hide();
        lock = new GuiSwitch(123, 57, 0, this.isLocked, true); //TODO BUG
        
        System.out.println("init");
        

    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float f, int mouseX, int mouseY) {
        if(!hasInit ) initGui();
        if(hasChanged) {
            allowed = te.getAllowedUser();
            hasChanged = false;
        }
        GL11.glColor4f(1, 1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);   
        

        scrollSlider.draw(this);
        lock.draw(this);
        
        
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int x, int y) {
        lock.drawString(this, "lock :", 134 ,58,50, "gray");
        lock.drawString(this, lock.isActive() ? "private" : "public" , 134, 65, 50, lock.isActive() ? "red" : "green");
        this.entername.drawTextBox();
        for (int i = 0; i < allowed.size(); i++) {
            names.drawString(this, allowed.get(i).toString(), 9, 25 + 10 * i, 110);
         }
    }
    
    @Override
    public void drawScreen (int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
       
    }

    @Override
    public void updateScreen () {
        super.updateScreen();
        this.entername.updateCursorCounter();
        scrollSlider.updateScreen();
        if(hasToSend) sendPacket();
    }


    @Override
    protected void mouseClicked (int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        scrollSlider.mouseClicked(this,par1,par2,par3);
        lock.mouseClicked(this, par1, par2,par3);
        this.isLocked = lock.isActive();
        this.hasToSend = true;
  
    }


    private void sendPacket() {
    	DeadCraftAdminPacket pkt = new DeadCraftAdminPacket(te.xCoord, te.yCoord, te.zCoord, this.allowed, this.isLocked);
    	System.out.println(pkt.toString());
    	DeadCraft.packetPipeline.sendToServer(pkt);
        this.hasChanged = true;
        this.hasToSend = false;
	}

	@Override
    protected void mouseClickMove (int par1, int par2, int par3, long par4) {
        super.mouseClickMove(par1, par2, par3, par4);
        scrollSlider.mouseClickMove(this, par1, par2);
    }

    @Override
    protected void mouseMovedOrUp (int par1, int par2, int par3) {
        super.mouseMovedOrUp(par1, par2, par3);
        scrollSlider.mouseMovedOrUp(this,par1,par2,par3);
    }



    @Override
    public void initGui () {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        GuiButton b0 = new GuiButton(0, guiLeft + 125, guiTop + 10, 16, 10, "+");
        b0.enabled = false;
        GuiButton b1 = new GuiButton(1, guiLeft + 145, guiTop + 10, 16, 10, "-");
        b1.enabled = false;
        
        this.buttonList.add(b0);
        this.buttonList.add(b1);

        this.entername = new GuiTextField(getFontRenderer(), 7,10, 112, 12); //BUG
        this.entername.setFocused(true);
        this.entername.setEnabled(true);
        this.entername.setCanLoseFocus(true);

        this.hasInit = true;
        
    }

    @Override
    public void onGuiClosed () {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped (char par1, int par2) {
        if (par2 == 28 || par2 == 156) {
           this.actionPerformed((GuiButton) this.buttonList.get(0));
        } else if (par2 == 1) this.close();
        if(!entername.isFocused()) entername.setFocused(true);
        this.entername.textboxKeyTyped(par1, par2);
        ((GuiButton) this.buttonList.get(0)).enabled = this.entername.getText().trim().length() > 0;
        ((GuiButton) this.buttonList.get(1)).enabled = this.entername.getText().trim().length() > 0;
        
    }

    @Override
    public void actionPerformed (GuiButton button) {
        String s = entername.getText();        
        if(s == "") return;
        if(button.id == 0){
        	if(allowed.contains(s)) return;        	
        	allowed.add(s);
        }else if(button.id == 1 ) {
        	if(!allowed.contains(s)) return;
        	allowed.remove(s);
        }
        this.hasToSend = true;
        
    }

	@Override
	protected void defineSubRect() {
		// TODO Auto-generated method stub
		
	}
	
	private String[] getDisplayedName(int start) {
		String[] re = new String[5];
		int size = this.allowed.size();
		if(size >= start + 5)
			for(int i =0; i < 5; i++)
				re[i] = (String) this.allowed.get(start + i);
		else if(size < start + 5) {
			int dif = start - size + 5;
			if(dif <0) return null;
			for(int i=0; i < dif; i++)
				re[i] = (String) this.allowed.get(start + i);
		}
		
		return re;
	}
    
    

}
