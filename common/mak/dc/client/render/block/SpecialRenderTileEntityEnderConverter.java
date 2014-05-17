package mak.dc.client.render.block;

import mak.dc.blocks.BlockEnderConverter;
import mak.dc.client.IInventoryRenderer;
import mak.dc.lib.Lib;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityEnderConverter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

public class SpecialRenderTileEntityEnderConverter extends TileEntitySpecialRenderer implements IInventoryRenderer{

	public static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID , Textures.ENDERCONVERTER_TEXT_LOC);
	private static final float hp = 1f/32f;
	private static final float vp = 1f/192f;
	private static final float p = 1f / 16f;
	private TileEntityEnderConverter te;
	private float a;
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float t) {
		this.te = (TileEntityEnderConverter) tile;
		
		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1f);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslated(x + 0.5f, y, z + 0.5f);
		
		if(te == null) { //inventory render
			GL11.glDisable(GL11.GL_BLEND);
			for (int i = 0; i< 4 ; i++) {
				GL11.glRotatef(90f, 0, 1f, 0f);
				renderSide();
				if(BlockEnderConverter.renderPass == 1) renderAlpha();
			}
			renderTop();
			renderBottom();
			GL11.glEnable(GL11.GL_BLEND);
			
		}else{
			renderInside();
			for (int i = 0; i< 4 ; i++) {
				GL11.glRotatef(90f, 0, 1f, 0f);
				renderSide();
				if(BlockEnderConverter.renderPass == 1) renderAlpha();
			}
	
			renderTop();
			renderBottom();

		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		
		if(a < 360) a +=0.3f;
		else a =0;
		
	}

	private void renderAlpha() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		
		float yt = 160 * vp;
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(3.5f*p, 11.5f*p, -8 * p, 9*hp ,9*vp + yt);
		tess.addVertexWithUV(3.5f*p, 4.5f*p, -8 * p, 9*hp,23 * vp + yt );
		tess.addVertexWithUV(-3.5f*p, 4.5f*p, -8 * p, 23 * hp, 23 * vp +yt);
		tess.addVertexWithUV(-3.5f*p, 11.5f*p, -8 * p, 23 * hp, 9 *vp+yt);
		

		tess.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void renderInventory(double x, double y, double z) {
		this.renderTileEntityAt(null, x, y, z, 0f);
	}
	
	private void renderSide() {
		GL11.glDisable(GL11.GL_LIGHTING);
		Tessellator tess;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		tess= Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(0.5f, 1, -0.5, 0,0);
		tess.addVertexWithUV(0.5f, 0, -0.5, 0,32 * vp );
		tess.addVertexWithUV(-0.5f, 0, -0.5, 32 * hp, 32 * vp);
		tess.addVertexWithUV(-0.5f, 1, -0.5, 32 * hp, 0);
		
		tess.draw();
		
		
		float yt = 128 * vp;
		tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(0.5f, 1, 4 * p, 0 ,0 + yt);
		tess.addVertexWithUV(0.5f, 0, 4 * p, 0,32 * vp + yt );
		tess.addVertexWithUV(-0.5f, 0, 4 * p, 32 * hp, 32 * vp +yt);
		tess.addVertexWithUV(-0.5f, 1, 4 * p, 32 * hp, 0+yt);
		

		tess.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
	
	}
	
	private void renderTop() {
		GL11.glDisable(GL11.GL_LIGHTING);
		float yt = 32 * vp;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(0.5f, 1, 0.5,0,0 + yt);
		tess.addVertexWithUV(0.5f, 1, -0.5, 0,32 * vp  + yt);
		tess.addVertexWithUV(-0.5f, 1, -0.5, 32 * hp, 32 * vp + yt);
		tess.addVertexWithUV(-0.5f, 1, 0.5, 32 * hp, 0 +yt);
		
		
		yt = 96 * vp;
		
		tess.addVertexWithUV(0.5f, 4*p, 0.5,0 ,0 + yt);
		tess.addVertexWithUV(0.5f, 4*p, -0.5, 0 ,32 * vp  + yt);
		tess.addVertexWithUV(-0.5f, 4*p, -0.5, 32 * hp , 32 * vp + yt);
		tess.addVertexWithUV(-0.5f, 4*p, 0.5, 32 * hp, 0 +yt);
		
		
		tess.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	private void renderBottom() {
		GL11.glDisable(GL11.GL_LIGHTING);
		float yt = 64 * vp;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glRotatef(180f, 1f, 0f, 0f);
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		tess.addVertexWithUV(0.5f, 0, 0.5,0,0 + yt);
		tess.addVertexWithUV(0.5f, 0, -0.5, 0,32 * vp  + yt);
		tess.addVertexWithUV(-0.5f, 0, -0.5, 32 * hp, 32 * vp + yt);
		tess.addVertexWithUV(-0.5f, 0, 0.5, 32 * hp, 0 +yt);
		
		yt = 96 * vp;
		
		tess.addVertexWithUV(0.5f, 4*p - 1, 0.5,0,0 + yt);
		tess.addVertexWithUV(0.5f, 4*p - 1, -0.5, 0,32 * vp  + yt);
		tess.addVertexWithUV(-0.5f, 4*p -1, -0.5, 32 * hp, 32 * vp + yt);
		tess.addVertexWithUV(-0.5f, 4*p -1, 0.5, 32 * hp, 0 +yt);
		
		tess.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	private void renderInside() {
		if(te.getStackInSlot(0) == null) return;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		ItemStack is = te.getStackInSlot(0);
		Minecraft.getMinecraft().renderEngine.bindTexture(is.getItem() instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
		
		RenderBlocks renderBlock = new RenderBlocks();
		if(!ForgeHooksClient.renderEntityItem(new EntityItem(te.getWorldObj(), te.xCoord, te.yCoord +2 , te.zCoord ,is ), is, 0f, 0f, te.getWorldObj().rand,Minecraft.getMinecraft().renderEngine, renderBlock  , is.stackSize)) {
			GL11.glRotatef(a, 0, 1f, 0);
			
	        if (is.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(is.getItem()).getRenderType())) {
	            GL11.glTranslatef(0f, 0.5f, 0f);
	            GL11.glScalef(0.3f, 0.3f, 0.3f);
	        	renderBlock.renderBlockAsItem(Block.getBlockFromItem(is.getItem()), is.getItemDamage(), 1f);
	        }else {
	        	GL11.glScalef(0.4f, 0.4f, 0.4f);
	        	GL11.glTranslatef(-0.5f, 0.6f ,0.05f);
	        	
	            for (int renderPass = 0; renderPass < is.getItem().getRenderPasses(is.getItemDamage()); renderPass++) {
	                IIcon icon = is.getItem().getIcon(is, renderPass);
	                if (icon != null) {
	                    int rgb = is.getItem().getColorFromItemStack(is, renderPass);
	                    float r = (rgb >> 16 & 255) / 255f;
	                    float g = (rgb >> 8 & 255) / 255f;
	                    float b = (rgb & 255) / 255f;
	                    GL11.glColor3f(r, g, b);
	                    float f = icon.getMinU();
	                    float f1 = icon.getMaxU();
	                    float f2 = icon.getMinV();
	                    float f3 = icon.getMaxV();
	                    ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1f / 16f);
	                }
	            }
	            GL11.glRotatef(0f, 0f, 1f, 0f);
	           
        	}
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

}
