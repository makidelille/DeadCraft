package mak.dc.client.gui;

import java.util.ArrayList;

import mak.dc.DeadCraft;
import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.util.GuiCustom;
import mak.dc.client.gui.util.GuiSwitch;
import mak.dc.common.network.packet.DeadCraftAdminPacket;
import mak.dc.common.network.packet.DeadCraftClientToServerPacket;
import mak.dc.common.tileEntities.TileEntityDeadCraft;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
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
    
    private GuiSwitch lock;
    private GuiTextField entername;
    private TileEntityDeadCraft te;
    private String user;
    private ArrayList<String> allowed;
    
    private String s;
    private boolean hasToSend;
    private boolean hasInit = false;
    private boolean isLocked;
    
    private final int slotWidth = 100;
    private final int slotheight = 11;
    private final float p = 1.0f / 256;
    private int focused = 0;
    private int firstInList = 0;
    
    private final static ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.DEADCRAFTMAIN_GUI_TEXT_LOC);
    
    public GuiDeadCraftBlockMain(InventoryPlayer invPlayer, TileEntityDeadCraft te, int iD) {
        super(new ContainerDeadCraft(invPlayer, te, false), iD);
        
        this.te = te;
        user = invPlayer.player.getCommandSenderName();
        allowed = te.getAllowedUser();
        isLocked = this.te.isLocked();
        
        xSize = 176;
        ySize = 166;
        
        s = "";
        
        lock = new GuiSwitch(this, 123, 57, 0, isLocked, true);
        
    }
    
    @Override
    public void actionPerformed(GuiButton button) {
        String s = entername.getText();
        switch (button.id) {
            case 0:
                if (allowed.contains(s) || s == "") return;
                allowed.add(s);
                ((GuiButton) buttonList.get(2)).enabled = firstInList > 0;
                ((GuiButton) buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
                hasToSend = true;
                break;
            case 1:
                if (!allowed.contains(s) || s == "") return;
                allowed.remove(s);
                ((GuiButton) buttonList.get(2)).enabled = firstInList > 0;
                ((GuiButton) buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
                hasToSend = true;
                break;
            case 2:
                firstInList--;
                if (firstInList < 0) {
                    firstInList = 0;
                }
                if (firstInList >= allowed.size() - 1) {
                    firstInList = allowed.size() - 1;
                }
                ((GuiButton) buttonList.get(2)).enabled = firstInList > 0;
                ((GuiButton) buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
                break;
            case 3:
                firstInList++;
                if (firstInList < 0) {
                    firstInList = 0;
                }
                if (firstInList >= allowed.size() - 1) {
                    firstInList = allowed.size() - 1;
                }
                ((GuiButton) buttonList.get(2)).enabled = firstInList > 0;
                ((GuiButton) buttonList.get(3)).enabled = firstInList < allowed.size() - 1;
                break;
            case 4:
                DeadCraft.packetPipeline.sendToServer(new DeadCraftClientToServerPacket(-2, Minecraft.getMinecraft().thePlayer.getCommandSenderName(), 0, 0, 0, 0));
                break;
        }
        
        haschange = true;
        
    }
    
    @Override
    protected void defineSubRect() {
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        if (!hasInit) {
            initGui();
        }
        
        GL11.glColor4f(1, 1, 1, 1);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        lock.draw(this);
        
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        lock.drawString(this, StatCollector.translateToLocal("dc.block.gui.lock") + " :", 134, 58, 90, "gray");
        lock.drawString(this, lock.isActive() ? StatCollector.translateToLocal("dc.private") : StatCollector.translateToLocal("dc.public"), 134, 68, 50, lock.isActive() ? "red" : "green");
        entername.drawTextBox();
    }
    
    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        
        for (int i = firstInList; i < allowed.size(); i++) {
            drawSlot(i - firstInList, i, i == focused);
        }
        
    }
    
    private void drawSlot(float translation, int listNb, boolean isFocused) {
        if (translation * slotheight > 11 * 4f || translation < 0) return;
        
        GL11.glTranslated(0, (slotheight - 1) * translation, 0);
        
        GL11.glDisable(GL11.GL_LIGHTING);
        Tessellator tess = Tessellator.instance;
        mc.renderEngine.bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (isFocused) {
            tess.startDrawingQuads();
            {
                tess.addVertexWithUV(guiLeft + 8 + slotWidth - 1, guiTop + 24 + 1, 0, (xSize + 2) * p, 0);
                tess.addVertexWithUV(guiLeft + 8 + 1, guiTop + 24 + 1, 0, (xSize + 2) * p, 0);
                tess.addVertexWithUV(guiLeft + 8 + 1, guiTop + 24 + slotheight - 1, 0, (xSize + 2) * p, 0);
                tess.addVertexWithUV(guiLeft + 8 + slotWidth - 1, guiTop + 24 + slotheight - 1, 0, (xSize + 2) * p, 0);
            }
            tess.draw();
        } else {
            tess.startDrawingQuads();
            {
                tess.addVertexWithUV(guiLeft + 8 + slotWidth - 1, guiTop + 24 + 1, 0, xSize * p, 0);
                tess.addVertexWithUV(guiLeft + 8 + 1, guiTop + 24 + 1, 0, xSize * p, 0);
                tess.addVertexWithUV(guiLeft + 8 + 1, guiTop + 24 + slotheight - 1, 0, xSize * p, 0);
                tess.addVertexWithUV(guiLeft + 8 + slotWidth - 1, guiTop + 24 + slotheight - 1, 0, xSize * p, 0);
            }
            tess.draw();
        }
        
        drawString(getFontRenderer(), allowed.get(listNb), guiLeft + 8 + 1, guiTop + 24 + 2, 101010);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glTranslated(0, -(slotheight - 1) * translation, 0);
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (Mouse.getEventDWheel() / 120 > 0 && ((GuiButton) buttonList.get(2)).enabled) {
            actionPerformed((GuiButton) buttonList.get(2));
        }
        if (Mouse.getEventDWheel() / 120 < 0 && ((GuiButton) buttonList.get(3)).enabled) {
            actionPerformed((GuiButton) buttonList.get(3));
        }
        
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        
        GuiButton b0 = new GuiButton(0, guiLeft + 125, guiTop + 10, 16, 10, "+");
        b0.enabled = false;
        GuiButton b1 = new GuiButton(1, guiLeft + 145, guiTop + 10, 16, 10, "-");
        b1.enabled = false;
        
        GuiButton up = new GuiButton(2, guiLeft + 108, guiTop + 25, 10, 20, null);
        up.enabled = firstInList > 0;
        GuiButton down = new GuiButton(3, guiLeft + 108, guiTop + 55, 10, 20, null);
        down.enabled = firstInList < allowed.size() - 1;
        
        GuiButton sync = new GuiButton(4, guiLeft + 125, guiTop + 30, 40, 10, StatCollector.translateToLocal("dc.block.gui.sync"));
        
        buttonList.add(b0);
        buttonList.add(b1);
        buttonList.add(up);
        buttonList.add(down);
        buttonList.add(sync);
        
        entername = new GuiTextField(getFontRenderer(), 7, 10, 112, 12);
        entername.setFocused(true);
        entername.setEnabled(true);
        entername.setCanLoseFocus(true);
        
        hasInit = true;
        
    }
    
    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == 28 || par2 == 156) {
            actionPerformed((GuiButton) buttonList.get(0));
        } else if (par2 == 1) {
            close();
        }
        if (!entername.isFocused()) {
            entername.setFocused(true);
        }
        entername.textboxKeyTyped(par1, par2);
        ((GuiButton) buttonList.get(0)).enabled = entername.getText().trim().length() > 0;
        ((GuiButton) buttonList.get(1)).enabled = entername.getText().trim().length() > 0;
        
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        
        boolean state = lock.hasMouseClicked(par1, par2, par3);
        isLocked = lock.isActive();
        
        int x = par1 - guiLeft;
        int y = par2 - guiTop;
        
        if (x < 107 && x > 8 && y > 23 && y < 77) {
            focused = (y - 23) / slotheight + firstInList;
            if (focused < allowed.size()) {
                entername.setText(allowed.get(focused));
                ((GuiButton) buttonList.get(0)).enabled = entername.getText().trim().length() > 0;
                ((GuiButton) buttonList.get(1)).enabled = entername.getText().trim().length() > 0;
            }
        }
        
        if (state) {
            hasToSend = true;
            te.setLocked(isLocked);
        }
        
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void sendPacket() {
        DeadCraftAdminPacket pkt = new DeadCraftAdminPacket(te);
        DeadCraft.packetPipeline.sendToServer(pkt);
        haschange = true;
        hasToSend = false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        entername.updateCursorCounter();
        if (hasToSend) {
            sendPacket();
        }
        if (haschange) {
            allowed = te.getAllowedUser();
            haschange = false;
        }
    }
    
}
