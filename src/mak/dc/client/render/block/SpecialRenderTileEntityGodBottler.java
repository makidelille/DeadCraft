package mak.dc.client.render.block;

import mak.dc.client.model.ModelGodBottler;
import mak.dc.client.model.ModelGodCan;
import mak.dc.client.render.IInventoryRenderer;
import mak.dc.common.tileEntities.TileEntityGodBottler;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class SpecialRenderTileEntityGodBottler extends TileEntitySpecialRenderer implements IInventoryRenderer {
    
    private final ModelGodBottler model = new ModelGodBottler();
    private final ModelGodCan can = new ModelGodCan();
    private static final ResourceLocation textLoc = new ResourceLocation(Lib.MOD_ID, Textures.GODBOTTLER_MODEL_TEXT_LOC);
    private static final ResourceLocation canTextLoc = new ResourceLocation(Lib.MOD_ID, Textures.GODCAN_MODEL_TEXT_LOC);
    
    public SpecialRenderTileEntityGodBottler() {
        func_147497_a(TileEntityRendererDispatcher.instance);
    }
    
    @Override
    public void renderInventory(double x, double y, double z) {
        renderTileEntityAt(null, x, y, z, 0.0f);
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        TileEntityGodBottler te = (TileEntityGodBottler) tile;
        
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslated(x + 0.5F, y + 1.5F, z + 0.5F);
        GL11.glRotatef(180f, 0f, 0f, 1.0F);
        if (te == null) { // inventory render
            GL11.glRotatef(90, 0f, 1.0f, 0f);
            GL11.glScalef(0.60f, 0.60f, 0.60f);
            GL11.glTranslated(0.5F, +1.5F, 0.5F);
        } else if (te.isTop()) {
            GL11.glPopMatrix();
            return;
        } else {
            GL11.glRotatef(te.getDirection() * 90F, 0.0F, 1.0F, 0.0F);
            if (te.getClientTick() >= 0) {
                float t = 1f - (float) te.getClientTick() / TileEntityGodBottler.ANIMATIONTIME;
                model.Plateau.offsetY = (float) (0.35 * t);
                model.BrasMid.offsetY = -(0.33f * t);
                model.BrasBot.offsetY = -(0.35f * t);
            }
            if (te.CLIENThasCan()) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glTranslatef(0, 0.79f, 0);
                float scale = 0.15f;
                GL11.glScalef(scale, scale, scale);
                bindTexture(canTextLoc);
                can.render(null, 0, 0, 0, 0, 0, 0.0625f);
                GL11.glPopMatrix();
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        bindTexture(textLoc);
        model.render(0.0625F);
        GL11.glPopMatrix();
    }
    
}
