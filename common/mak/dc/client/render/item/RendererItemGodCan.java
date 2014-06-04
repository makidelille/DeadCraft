package mak.dc.client.render.item;

import mak.dc.client.model.ModelGodCan;
import mak.dc.util.Lib;
import mak.dc.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RendererItemGodCan implements IItemRenderer {
	
	private static final ResourceLocation text = new ResourceLocation(Lib.MOD_ID , Textures.GODCAN_MODEL_TEXT_LOC);
	private ModelGodCan model;

	public RendererItemGodCan() {
		this.model = new ModelGodCan();
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type) {
		case ENTITY: return true;
		case EQUIPPED : return true;
		case EQUIPPED_FIRST_PERSON : return true;
		case INVENTORY : return true;
		case FIRST_PERSON_MAP : return false;
		default : return false;
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(text);
		float scale;
		switch(type) {
		case ENTITY:
			scale = 0.05f;
			GL11.glTranslatef(0f, 0.2f, 0f);
			GL11.glScalef(scale , scale, scale);	
			break;
		case EQUIPPED : 
			scale = 0.025f;
			GL11.glTranslatef(0.7f, 0.3f, 0f);
			GL11.glScalef(scale , scale, scale);
			GL11.glRotatef(-145f, 0f, 0f, 1f);
			GL11.glRotatef(-85f, 0f, 1f, 0f);
			GL11.glTranslatef(-0.5f, 2f, 3f);
			break;
		case EQUIPPED_FIRST_PERSON :
			GL11.glTranslatef(5f, -1f, 4f);
			GL11.glRotatef(45, 0, 1, 0);
			GL11.glRotatef(135, 1, 0, 0);
			GL11.glRotatef(180, 0, 1, 0);
			scale = 0.35f;
			GL11.glScalef(scale, scale, scale);
			break;
		case INVENTORY : 
			GL11.glTranslatef(8f, +8f, 0f);
			GL11.glRotatef(45, 0, 1, 0);
			GL11.glRotatef(45, 0, 0, 1);
			break;
		case FIRST_PERSON_MAP : 
			break;
		default : break;
		}
		
		model.render(null, 0, 0, 0, 0, 0, 0.625F);
		GL11.glPopMatrix();

	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
		return false;
	}

}
