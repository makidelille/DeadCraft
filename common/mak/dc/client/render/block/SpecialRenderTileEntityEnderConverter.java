package mak.dc.client.render.block;

import org.lwjgl.opengl.GL11;

import mak.dc.client.IInventoryRenderer;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityEnderConverter;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class SpecialRenderTileEntityEnderConverter extends TileEntitySpecialRenderer implements IInventoryRenderer{

	public static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID , Textures.ENDERCONVERTER_TEXT_LOC);
	private static final float hp = 1f/32f;
	private static final float vp = 1f/96f;
	private static final float p = 1f / 16f;
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float t) {
		TileEntityEnderConverter te = (TileEntityEnderConverter) tile;
		
		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1f);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glTranslated(x + 0.5f, y, z + 0.5f);
		
		for (int i = 0; i< 4 ; i++) {
			GL11.glRotatef(90f, 0, 1f, 0f);
			renderSide();
		}
		
		renderTop();
		renderBottom();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		
	}

	@Override
	public void renderInventory(double x, double y, double z) {
		this.renderTileEntityAt(null, x, y, z, 0f);
	}
	
	private void renderSide() {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(0.5f, 1, -0.5, 0,0);
		tess.addVertexWithUV(0.5f, 0, -0.5, 0,32 * vp );
		tess.addVertexWithUV(-0.5f, 0, -0.5, 32 * hp, 32 * vp);
		tess.addVertexWithUV(-0.5f, 1, -0.5, 32 * hp, 0);
		
		tess.draw();
	}
	
	private void renderTop() {
		float yt = 32 * vp;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(0.5f, 1, 0.5,0,0 + yt);
		tess.addVertexWithUV(0.5f, 1, -0.5, 0,32 * vp  + yt);
		tess.addVertexWithUV(-0.5f, 1, -0.5, 32 * hp, 32 * vp + yt);
		tess.addVertexWithUV(-0.5f, 1, 0.5, 32 * hp, 0 +yt);
		
		tess.draw();
	}
	
	private void renderBottom() {
		float yt = 64 * vp;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glRotatef(180f, 1f, 0f, 0f);
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(0.5f, 0, 0.5,0,0 + yt);
		tess.addVertexWithUV(0.5f, 0, -0.5, 0,32 * vp  + yt);
		tess.addVertexWithUV(-0.5f, 0, -0.5, 32 * hp, 32 * vp + yt);
		tess.addVertexWithUV(-0.5f, 0, 0.5, 32 * hp, 0 +yt);
		
		tess.draw();
		
	}

}
