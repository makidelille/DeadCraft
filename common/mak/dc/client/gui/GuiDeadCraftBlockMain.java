package mak.dc.client.gui;

import java.util.ArrayList;

import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.client.gui.util.GuiSlider;
import mak.dc.client.gui.util.GuiSwitch;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.network.PacketHandler;
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
    private String s;
    private boolean hasInit = false;

    private static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.DEADCRAFTMAIN_GUI_TEXT_LOC);

    public GuiDeadCraftBlockMain (InventoryPlayer invPlayer, TileEntityDeadCraft te, int iD) {
        super(new ContainerDeadCraft(invPlayer, te, false), iD);

        this.te = te;
        this.user = invPlayer.player.username;
        this.allowed = te.getAllowedUser();

        xSize = 176;
        ySize = 166;
        
        this.s = "";

        names = new GuiRectangle(7, 10, 120, 100);
        scrollSlider = new GuiSlider(108, 25, 49, 0, true);
        scrollSlider.hide();
        lock = new GuiSwitch(123, 57, 0, te.isLocked(), true); //TODO BUG


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

        //System.out.println(allowed);
        
        for (int i = 0; i < allowed.size(); i++) {
            System.out.println(allowed.get(i).toString());
           names.drawString(this, allowed.get(i).toString(), 7, 10 + 10 * i, 110);
        }

        scrollSlider.draw(this);
        lock.draw(this);
        
        
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int x, int y) {
        lock.drawString(this, "lock :", 134 ,58,50, "gray");
        lock.drawString(this, lock.isActive() ? "private" : "public" , 134, 65, 50, lock.isActive() ? "red" : "green");
        this.entername.drawTextBox();
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
    }


    @Override
    protected void mouseClicked (int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        scrollSlider.mouseClicked(this,par1,par2,par3);
        lock.mouseClicked(this, par1, par2,par3);
  
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
        b0.enabled =false;
        GuiButton b1 = new GuiButton(1, guiLeft + 145, guiTop + 10, 16, 10, "-");
        b1.enabled = false;
        
        this.buttonList.add(b0);
        this.buttonList.add(b1);

        this.entername = new GuiTextField(fontRenderer, 7,10, 112, 12); //BUG
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
        System.out.println(button.id);
        String s = entername.getText(); 
        System.out.println(s);
        if(s != "") {
            PacketHandler.sendInterfaceStringPacket(this.id, button.id, s);
            this.hasChanged = true;
        }

        
    }
    
    

}
