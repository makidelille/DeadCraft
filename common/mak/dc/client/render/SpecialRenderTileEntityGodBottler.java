package mak.dc.client.render;

import mak.dc.client.IInventoryRenderer;
import mak.dc.model.ModelGodBottler;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class SpecialRenderTileEntityGodBottler extends TileEntitySpecialRenderer implements IInventoryRenderer {

	private final ModelGodBottler model = new ModelGodBottler();
	public static final ResourceLocation textLoc = new ResourceLocation("");
	
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
		if(te == null) return;
		if(te.isTop()) return;
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5F, y + 1.5F, z + 0.5F);
		GL11.glRotatef(180f, 0f, 0f, 1.0F);
		GL11.glRotatef((te.getFacing() * 90F), 0.0F, 1.0F, 0.0F);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		this.bindTexture(textLoc);
		byte face = te.getFacing(); //TODO understand what is happening here
		System.out.println(face);
		
		//0 = +Z
        //1 = -X
        //2 = -Z
        //3 = +X
		
//		switch(face) {
//		case 0:
//			GL11.glRotatef(0F, 180F, 0.0F, 1.0F);
//			break;
//		case 1 :
//			GL11.glRotatef(0F, -90F, 0.0F, 1.0F);
//			System.out.println("test2");
//			break;
//		case 2: 
//			GL11.glRotatef(0F, 180F, 0.0F, 1.0F);
//			break;
//		case 3: 
//			GL11.glRotatef(0F, 90F, 0.0F, 1.0F);
//			break;		
//		}
//		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		this.model.render(0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderBlock(TileEntity tileEntity, int x, int y, int z) {
		this.renderTileEntityAt(tileEntity, x, y, z, 0f);
	}

}
