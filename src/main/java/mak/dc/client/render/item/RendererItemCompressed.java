package mak.dc.client.render.item;

import org.lwjgl.opengl.GL11;

import mak.dc.common.items.ItemCompacted;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class RendererItemCompressed implements IItemRenderer {
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch (type) {
            case ENTITY:
                return true;
            case EQUIPPED:
                return true;
            case EQUIPPED_FIRST_PERSON:
                return true;
            case FIRST_PERSON_MAP:
                return false;
            case INVENTORY:
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (!(stack.getItem() instanceof ItemCompacted)) return;
        
        Item itemInStack = ItemCompacted.getItem(stack);
        int meta = ItemCompacted.getDmg(stack);
        ItemStack fakeStack = new ItemStack(itemInStack, 1, meta);
        if (ItemCompacted.getTag(stack) != null) fakeStack.setTagCompound(ItemCompacted.getTag(stack));
        if (fakeStack.getItem() == null) return;
        IItemRenderer specialRender = MinecraftForgeClient.getItemRenderer(fakeStack, type);
        
        if (type.equals(ItemRenderType.INVENTORY)) {
            GL11.glDisable(GL11.GL_LIGHTING);
            RenderItem renderItem = new RenderItem();
            renderItem.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, 0, 0);
            GL11.glTranslated(0f, 0f, -25f);
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            GL11.glTranslatef(2.5f, 2.5f, 0);
            if (specialRender != null && specialRender.handleRenderType(fakeStack, ItemRenderType.INVENTORY)) {
                specialRender.renderItem(ItemRenderType.INVENTORY, fakeStack, data);
            } else {
                renderItem.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, fakeStack, 0, 0);
            }
            GL11.glEnable(GL11.GL_LIGHTING);
        } else if (type.equals(ItemRenderType.ENTITY) || type.equals(ItemRenderType.EQUIPPED) || type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON)) {
            GL11.glTranslatef(-0.5f, 0, 0);
            if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON)) {
                GL11.glTranslatef(0.70f, -0.20f, 0.20f);
                GL11.glRotatef(25f, 0, 1f, 0);
            } else if (type.equals(ItemRenderType.EQUIPPED)) {
                GL11.glTranslatef(0.5f, -0.1f, 0);
                GL11.glRotatef(10f, 0, 0, 1);
                GL11.glRotatef(25f, 1, 0, 1);
                GL11.glScalef(0.8f, 0.8f, 0.8f);
            } else if (type.equals(ItemRenderType.ENTITY)) {
                GL11.glTranslated(0.5f, 0, 0);
                EntityItem ent = (EntityItem) data[1];
                float rot = (float) ((float)ent.age/100 * 360);
                GL11.glRotatef(rot, 0, 1, 0);
                GL11.glTranslated(-0.5f, 0, 0);
                GL11.glTranslated(0, Math.cos(rot/360d)*0.05d + 0.05d, 0);
            }
            
            IIcon icon = stack.getIconIndex();
            if (icon != null) {
                ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1 / 16f);
            }
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            GL11.glTranslatef(0.65f, 0.20f, 0);
            if (specialRender != null && specialRender.handleRenderType(fakeStack, ItemRenderType.ENTITY)) {
                specialRender.renderItem(ItemRenderType.ENTITY, fakeStack, data);
            } else {
                if (itemInStack instanceof ItemBlock) {
                    Block block = Block.getBlockFromItem(itemInStack);
                    int render = block.getRenderType();
                    RenderBlocks rb = new RenderBlocks();
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                    if (RenderBlocks.renderItemIn3d(block.getRenderType())) {
                        GL11.glTranslatef(0f, 0.5f, -0.05f);
                        GL11.glScalef(0.5f, 0.5f, 0.5f);
                        GL11.glRotatef(45f, -0.25f, 1, 0);
                        rb.renderBlockAsItem(block, meta, 1f);
                    }
                } else {
                    GL11.glTranslatef(-0.5f, 0f, 0f);
                    for (int renderPass = 0; renderPass < fakeStack.getItem().getRenderPasses(fakeStack.getItemDamage()); renderPass++) {
                        icon = fakeStack.getItem().getIcon(fakeStack, renderPass);
                        if (icon != null) {
                            int rgb = fakeStack.getItem().getColorFromItemStack(fakeStack, renderPass);
                            float r = (rgb >> 16 & 255) / 255f;
                            float g = (rgb >> 8 & 255) / 255f;
                            float b = (rgb & 255) / 255f;
                            GL11.glColor3f(r, g, b);
                            float f = icon.getMinU();
                            float f1 = icon.getMaxU();
                            float f2 = icon.getMinV();
                            float f3 = icon.getMaxV();
                            ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1 / 16f);
                        }
                    }
                }
            }
            
        }
        
    }
    
}
