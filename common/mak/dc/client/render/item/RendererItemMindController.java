package mak.dc.client.render.item;

import mak.dc.client.model.ModelMindController;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RendererItemMindController implements IItemRenderer {
    
    protected ModelMindController model;
    protected static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.MINDCONTROLLER_MODEL_TEXT_LOC);
    
    public RendererItemMindController() {
        model = new ModelMindController();
    }
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch (type) {
            case EQUIPPED:
                return true;
            case EQUIPPED_FIRST_PERSON:
                return true;
            case INVENTORY:
                return false;
            case FIRST_PERSON_MAP:
                return false;
            default:
                return false;
        }
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        
        switch (type) {
            case EQUIPPED: {
                
                double scale = -0.05D;
                float angle = 90;
                
                GL11.glPushMatrix();
                
                Minecraft.getMinecraft().renderEngine.bindTexture(texture);
                
                GL11.glRotatef(angle, 0, -1, 0);
                angle = 90;
                GL11.glRotatef(angle, -1, 0, 0);
                angle = 180;
                GL11.glRotatef(angle, 0, 0, 1);
                angle = 35;
                GL11.glRotatef(angle, -1, 0, 0);
                
                GL11.glTranslatef(0.01F, -0.75F, 0.05F);
                
                GL11.glScaled(scale, scale, scale);
                
                model.render((Entity) data[1], 0, 0, 0, 0, 0, 0.625F);
                
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                double scale = 0.3D;
                
                GL11.glPushMatrix();
                
                Minecraft.getMinecraft().renderEngine.bindTexture(texture);
                
                float angle = 90;
                GL11.glRotatef(angle, -1, -1, 0);
                angle = 180;
                GL11.glRotatef(angle, 1, 0, 0);
                GL11.glRotatef(angle, 0, 0, 1);
                angle = 75;
                GL11.glRotatef(angle, 0, 0, 1);
                angle = 15;
                GL11.glRotatef(angle, 1, 0, 0);
                
                GL11.glTranslatef(-0.5F, 2F, 0.5F);
                
                GL11.glScaled(scale, scale, scale);
                model.render((Entity) data[1], 0, 0, 0, 0, 0, 0.625F);
                
                GL11.glPopMatrix();
                break;
            }
            default:
                break;
        
        }
    }
    
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }
    
}
