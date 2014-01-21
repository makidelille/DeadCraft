package mak.dc.client.gui;

import java.util.ArrayList;

import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiRectangle;
import mak.dc.client.gui.util.GuiSlider;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
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
    private GuiRectangle            infos;
    private GuiTextField            entername;
    private GuiSlider               sliderTest;
    private TileEntityDeadCraft     te;
    private String                  user;
    private ArrayList               allowed;

    private static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.DEADCRAFTMAIN_GUI_TEXT_LOC);

    public GuiDeadCraftBlockMain (InventoryPlayer invPlayer, TileEntityDeadCraft te) {
        super(new ContainerDeadCraft(invPlayer, te, false));

        xSize = 176;
        ySize = 166;

        infos = new GuiRectangle(121, 53, 50, 24);
        names = new GuiRectangle(7, 10, 120, 100);
        sliderTest = new GuiSlider(123, 62, 40, 0);

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

        sliderTest.draw(this);
        initGui();

    }

    @Override
    protected void drawGuiContainerForegroundLayer (int x, int y) {
        this.entername.drawTextBox();

    }

    public void updateScreen () {
        this.entername.updateCursorCounter();
        sliderTest.updateScreen();
    }

    @Override
    public void initGui () {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(0, guiLeft + 125, guiTop + 10, 16, 10, "+"));
        this.buttonList.add(new GuiButton(1, guiLeft + 145, guiTop + 10, 16, 10, "-"));

        this.entername = new GuiTextField(fontRenderer, 7, 10, 112, 10);
        this.entername.setFocused(true);

    }

    @Override
    public void onGuiClosed () {
        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped (char par1, int par2) {
        this.entername.textboxKeyTyped(par1, par2);
        ((GuiButton) this.buttonList.get(0)).enabled = this.entername.getText().trim().length() > 0;

        if (par2 == 28 || par2 == 156) {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        } else if (par2 == 1) this.close();
    }

}
