package mak.dc.client.gui;

import java.util.ArrayList;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiSwitch;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.network.packet.DeadCraftAdminPacket;
import mak.dc.network.packet.DeadCraftDebugForcePakcet;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiDeadCraftBlockMain extends GuiCustom {

    private GuiSwitch               lock;
    private GuiTextField            entername;
    private TileEntityDeadCraft     te;
    private String                  user;
    private ArrayList<String>               allowed;
    
    
    private String s;
    private boolean hasToSend;
    private boolean hasInit = false;
	private boolean isLocked;

	private final int slotWidth = 100;
	private final int slotheight = 11;
	private final float p = 1.0f/256;
	private int focused = 0;
	private int firstInList = 0;
	
    private final  static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.DEADCRAFTMAIN_GUI_TEXT_LOC);

    public GuiDeadCraftBlockMain (InventoryPlayer invPlayer, TileEntityDeadCraft te, int iD) {
        super(new ContainerDeadCraft(invPlayer, te, false), iD);

        this.te = te;
        this.user = invPlayer.player.getCommandSenderName();
        this.allowed = te.getAllowedUser();
        this.isLocked = this.te.isLocked();
                
        xSize = 176;
        ySize = 166;
        
        this.s = "";
      
        
        lock = new GuiSwitch(this,123, 57, 0, this.isLocked, true);
        

    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float f, int mouseX, int mouseY) {
        if(!hasInit ) initGui();
        
        GL11.glColor4f(1, 1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);   
        


        lock.draw(this);
        
        
        
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int x, int y) {
        lock.drawString(this, StatCollector.translateToLocal("dc.block.gui.lock") + " :", 134 ,58,90, "gray");
        lock.drawString(this, lock.isActive() ? StatCollector.translateToLocal("dc.private") : StatCollector.translateToLocal("dc.public") , 134, 68, 50, lock.isActive() ? "red" : "green");
        this.entername.drawTextBox();
    }
    
    @Override
    public void drawScreen (int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
                       
        for (int i = firstInList; i < allowed.size(); i++) {
        	this.drawSlot(i - firstInList, i, i == focused);
        }
        
       
       

       
    }

    private void drawSlot(float translation, int listNb, boolean isFocused) {
    	if(translation * slotheight > 11 * 4f || translation  < 0) return;	
    	
    	GL11.glTranslated(0, (slotheight - 1) * translation, 0);
    	
		GL11.glDisable(GL11.GL_LIGHTING);
		Tessellator tess = Tessellator.instance;
		mc.renderEngine.bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(isFocused) {
			tess.startDrawingQuads();
			{
				tess.addVertexWithUV(this.guiLeft + 8 + slotWidth-1, this.guiTop + 24 +1, 0, (this.xSize + 2)*p  ,  0);
				tess.addVertexWithUV(this.guiLeft + 8 +1, this.guiTop + 24 +1, 0, (this.xSize + 2)*p  ,  0);
				tess.addVertexWithUV(this.guiLeft + 8 +1, this.guiTop + 24  + slotheight -1 , 0, (this.xSize + 2)*p  ,  0);
				tess.addVertexWithUV(this.guiLeft + 8 + slotWidth-1 , this.guiTop + 24 + slotheight -1, 0, (this.xSize + 2)*p  ,  0);
			}		
			tess.draw();
	    }else {
	    	tess.startDrawingQuads();
			{
				tess.addVertexWithUV(this.guiLeft + 8 + slotWidth-1, this.guiTop + 24 +1, 0, (this.xSize)*p  ,  0);
				tess.addVertexWithUV(this.guiLeft + 8 +1, this.guiTop + 24 +1, 0, (this.xSize)*p  ,  0);
				tess.addVertexWithUV(this.guiLeft + 8 +1, this.guiTop + 24  + slotheight -1 , 0, (this.xSize )*p  ,  0);
				tess.addVertexWithUV(this.guiLeft + 8 + slotWidth-1 , this.guiTop + 24 + slotheight -1, 0, (this.xSize)*p  ,  0);
			}		
			tess.draw();
	    }
		
		
		this.drawString(getFontRenderer(), (String) allowed.get(listNb),this.guiLeft + 8 + 1, this.guiTop +  24 + 2, 101010);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(0,- (slotheight - 1) * translation, 0);
    }

	@Override
    public void updateScreen () {
        super.updateScreen();
        this.entername.updateCursorCounter();
        if(hasToSend) sendPacket();
        if(haschange) {
            allowed = te.getAllowedUser();
            haschange = false;
        }
    }


    @Override
    protected void mouseClicked (int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        
        boolean state = lock.hasMouseClicked(par1, par2, par3);
        this.isLocked = lock.isActive();
        
        int x = par1 - this.guiLeft;
        int y = par2 - this.guiTop;
        
        if(x < 107 && x > 8 && y >23 && y <77){
        	focused = (y - 23) / slotheight + firstInList;
        	if(focused < allowed.size()) {
        		entername.setText(allowed.get(focused));
        		((GuiButton) this.buttonList.get(0)).enabled = this.entername.getText().trim().length() > 0;
                ((GuiButton) this.buttonList.get(1)).enabled = this.entername.getText().trim().length() > 0;
        	}
        }
        
        if(state) {
        	this.hasToSend = true;
        	te.setLocked(isLocked);
        }
        
  
    }
    @Override
    public void handleMouseInput() {
    	super.handleMouseInput();
    	if(Mouse.getEventDWheel() / 120 > 0 && ((GuiButton) buttonList.get(2)).enabled) this.actionPerformed((GuiButton) buttonList.get(2));
    	if(Mouse.getEventDWheel() / 120 < 0 && ((GuiButton) buttonList.get(3)).enabled) this.actionPerformed((GuiButton) buttonList.get(3));

    }
    
    
    




    @Override
    protected void sendPacket() {
    	DeadCraftAdminPacket pkt = new DeadCraftAdminPacket(te);
    	DeadCraft.packetPipeline.sendToServer(pkt);
    	this.haschange = true;
    	this.hasToSend = false;
    }


    @Override
    public void initGui () {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        GuiButton b0 = new GuiButton(0, guiLeft + 125, guiTop + 10, 16, 10, "+");
        b0.enabled = false;
        GuiButton b1 = new GuiButton(1, guiLeft + 145, guiTop + 10, 16, 10, "-");
        b1.enabled = false;
        
        GuiButton up = new GuiButton(2, guiLeft + 108, guiTop + 25, 10,20, null);
        up.enabled = firstInList > 0 ;
        GuiButton down = new GuiButton(3,guiLeft + 108, guiTop + 55, 10,20, null);
        down.enabled = firstInList < allowed.size() - 1;
        
        GuiButton sync = new GuiButton(4, guiLeft + 125, guiTop + 30, 40, 10,"Sync");
        
        this.buttonList.add(b0);
        this.buttonList.add(b1);
        this.buttonList.add(up);
        this.buttonList.add(down);
        this.buttonList.add(sync);

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
        switch (button.id) {
        case 0 : 
        	if(allowed.contains(s) || s == "") return;        	
        	allowed.add(s);
        	((GuiButton) this.buttonList.get(2)).enabled = firstInList > 0 ;
        	((GuiButton) this.buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
        	hasToSend = true;
        	break;
        case 1 :
        	if(!allowed.contains(s) || s == "") return;
        	allowed.remove(s);
        	((GuiButton) this.buttonList.get(2)).enabled = firstInList > 0 ;
        	((GuiButton) this.buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
        	hasToSend = true;
        	break;
        case 2 :
        	firstInList--;
        	if(firstInList < 0 ) firstInList = 0;
        	if(firstInList >= allowed.size() - 1) firstInList = allowed.size() - 1;
        	((GuiButton) this.buttonList.get(2)).enabled = firstInList > 0 ;
        	((GuiButton) this.buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
        	break;
        case 3 :
        	firstInList++;
        	if(firstInList < 0 ) firstInList = 0;
        	if(firstInList >= allowed.size() - 1) firstInList = allowed.size() - 1;
        	((GuiButton) this.buttonList.get(2)).enabled = firstInList > 0 ;
        	((GuiButton) this.buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
        	break;
        case 4 :
        	DeadCraft.packetPipeline.sendToServer(new DeadCraftDebugForcePakcet(te.xCoord, te.yCoord, te.zCoord));
        	break;
        }
       
        this.haschange = true;
        
        
    }

	@Override
	protected void defineSubRect() {}
    
    

}
