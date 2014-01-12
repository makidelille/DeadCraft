package mak.dc.client.gui;

import mak.dc.client.gui.container.ContainerEnterName;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEnterName extends GuiCustom {

    private static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.ENTERNAME_TEXT_LOC);
    private GuiTextField            theGuiTextField;
    private String                  name;
    private ContainerEnterName container;

    public GuiEnterName (InventoryPlayer inv, ItemStack itemStack) {
        super(new ContainerEnterName(inv, itemStack));
        this.container =  (ContainerEnterName) this.getContainer();
        this.xSize = 176;
        this.ySize = 166;
        this.name = "";

    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float f, int mouseX, int mouseY) {
        GL11.glColor4f(1, 1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
       

        this.name = theGuiTextField.getText();
        initGui();
    }
    
    @Override
    public void drawScreen (int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        this.theGuiTextField.drawTextBox();
      
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int x, int y) {
       
    }

    @Override
    public void initGui () {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        this.theGuiTextField = new GuiTextField(fontRenderer, this.guiLeft + 8,  this.guiTop + 35, 160, 20);
        this.theGuiTextField.setFocused(true);
        this.theGuiTextField.setText(this.name);
        this.theGuiTextField.setEnabled(true);
        
        this.buttonList.add(new GuiButton(0, this.guiLeft + 5, this.guiTop + 60, this.xSize - 10, 20, "OK"));
    }
    
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen () {
        this.theGuiTextField.updateCursorCounter();
    }

    @Override
    protected void keyTyped (char par1, int par2) {
        this.theGuiTextField.textboxKeyTyped(par1, par2);
        ((GuiButton) this.buttonList.get(0)).enabled = this.theGuiTextField.getText().trim().length() > 0;
        if (par2 == 1) {
            this.close();
        } else if (par2 == 28 || par2 == 156) {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked (int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.theGuiTextField.mouseClicked(par1, par2, par3);
    }

    @Override
    public void actionPerformed (GuiButton button) {
        switch(button.id) {
            case 0 : 
                PacketHandler.sendInterfaceSpecialPacket(1, 0, name);
                this.close();
                break;
        }
    }

    

}
