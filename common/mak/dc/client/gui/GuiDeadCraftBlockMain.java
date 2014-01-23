package mak.dc.client.gui;

import java.util.ArrayList;

import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.client.gui.util.GuiSlider;
import mak.dc.client.gui.util.GuiSwitch;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSmallButton;
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

    private static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.DEADCRAFTMAIN_GUI_TEXT_LOC);

    public GuiDeadCraftBlockMain (InventoryPlayer invPlayer, TileEntityDeadCraft te, int iD) {
        super(new ContainerDeadCraft(invPlayer, te, false), iD);

        xSize = 176;
        ySize = 166;

        names = new GuiRectangle(7, 10, 120, 100);
        scrollSlider = new GuiSlider(108, 25, 49, 0, true);
        scrollSlider.hide();
        lock = new GuiSwitch(123, 57, 0, te.isLocked(), true); //bug on the init

        this.te = te;
        this.user = invPlayer.player.username;

        this.allowed = te.getAllowedUser();

    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float f, int mouseX, int mouseY) {
        GL11.glColor4f(1, 1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        for (int i = 0; i < allowed.size(); i++) {
            names.drawString(this, allowed.get(i).toString(), 7, 10 + 10 * i, 110);
        }

        scrollSlider.draw(this);
        lock.draw(this);
        initGui();

    }

    @Override
    protected void drawGuiContainerForegroundLayer (int x, int y) {
        this.entername.drawTextBox();
        lock.drawString(this, "lock :", 134 ,58,50, "gray");
        lock.drawString(this, lock.isActive() ? "private" : "public" , 134, 65, 50, lock.isActive() ? "red" : "green");
    }

    @Override
    public void updateScreen () {
        this.entername.updateCursorCounter();
        scrollSlider.updateScreen();
    }


    @Override
    protected void mouseClicked (int par1, int par2, int par3) {
        scrollSlider.mouseClicked(this,par1,par2,par3);
        lock.mouseClicked(this, par1, par2,par3);
    }

   
    @Override
    protected void mouseClickMove (int par1, int par2, int par3, long par4) {
       scrollSlider.mouseClickMove(this, par1, par2);
    }
    
    @Override
    protected void mouseMovedOrUp (int par1, int par2, int par3) {
        scrollSlider.mouseMovedOrUp(this,par1,par2,par3);
    }
    
    

    @Override
    public void initGui () {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiSmallButton(0, guiLeft + 125, guiTop + 10, 16, 10, "+"));
        this.buttonList.add(new GuiSmallButton(1, guiLeft + 145, guiTop + 10, 16, 10, "-"));

        this.entername = new GuiTextField(fontRenderer, 7, 10, 112, 10);
        this.entername.setFocused(true);

    }

    @Override
    public void onGuiClosed () {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped (char par1, int par2) {
        this.entername.textboxKeyTyped(par1, par2);
        ((GuiButton) this.buttonList.get(0)).enabled = this.entername.getText().trim().length() > 0;

        if (par2 == 28 || par2 == 156) {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        } else if (par2 == 1) this.close();
    }

}
