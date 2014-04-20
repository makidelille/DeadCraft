package mak.dc.client.render.block;

import mak.dc.client.IInventoryRenderer;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.model.ModelGodBottler;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class SpecialRenderTileEntityGodBottler extends TileEntitySpecialRenderer implements IInventoryRenderer {

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
		this.bindTexture(textLoc);
		if(te == null) { //inventory render
			GL11.glRotatef(90, 0f, 1.0f, 0f);
			GL11.glScalef(0.60f, 0.60f, 0.60f);
			GL11.glTranslated(0.5F, + 1.5F, 0.5F);
		}else if(te.isTop()){
			GL11.glPopMatrix();
			return;
		}else{
			GL11.glRotatef((te.getDirection() * 90F), 0.0F, 1.0F, 0.0F);  
			if(te.getClientTick() > 0) {
				tick = te.getClientTick();
				this.model.Plateau.offsetY = -(float) (0.1* Math.log((float)(tick/te.cycleTime)));
				this.model.BrasMid.offsetY = (float) (0.12f * Math.log((float)(tick/te.cycleTime)));
				this.model.BrasBot.offsetY = (float) (0.135f * Math.log((float)(tick/te.cycleTime)));
			}
		}		
		this.model.render(0.0625F);
		GL11.glPopMatrix();
	}

}
