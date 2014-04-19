package mak.dc.client.render.block;

import mak.dc.client.IRenderer;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.model.ModelGodBottler;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class SpecialRenderTileEntityGodBottler extends TileEntitySpecialRenderer implements IRenderer {

	private final ModelGodBottler model = new ModelGodBottler();
	public static final ResourceLocation textLoc = new ResourceLocation(Lib.MOD_ID,Textures.GODBOTTLER_MODEL_TEXT_LOC);
	
	public SpecialRenderTileEntityGodBottler() {	
		this.func_147497_a(TileEntityRendererDispatcher.instance);
	}
	
	
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float tick) {
			this.renderTileEntityGodBottlerAt((TileEntityGodBottler) te, x, y, z, tick);
	}

	@Override
	public void renderInventory(double x, double y, double z) {
		this.renderTileEntityAt(null, x, y, z, 0.0f);
	}
	
	public void renderTileEntityGodBottlerAt(TileEntityGodBottler te, double x, double y, double z, float tick) {	
		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glTranslated(x + 0.5F, y + 1.5F, z + 0.5F);
		GL11.glRotatef(180f, 0f, 0f, 1.0F);
		int face = 0;
		if(te == null) {
			face = 1;
			GL11.glScalef(0.75f, 0.75f, 0.75f);
		}
		if(te.isTop()) return;
		face = te.getFacing();
		GL11.glRotatef((face * 90F), 0.0F, 1.0F, 0.0F);
		this.bindTexture(textLoc);
		this.model.render(0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderBlock(TileEntity tileEntity, int x, int y, int z) {
		this.renderTileEntityAt(tileEntity, x, y, z, 0F);
	}

}
