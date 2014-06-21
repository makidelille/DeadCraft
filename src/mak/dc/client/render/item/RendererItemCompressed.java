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
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        // RenderBlocks renderBlock = new RenderBlocks();
        // if (!ForgeHooksClient.renderEntityItem(new
        // EntityItem(te.getWorldObj(), te.xCoord, te.yCoord + 2, te.zCoord,
        // is), is, 0f, 0f, te.getWorldObj().rand,
        // Minecraft.getMinecraft().renderEngine, renderBlock, is.stackSize)) {
        // GL11.glRotatef(a, 0, 1f, 0);
        //
        // if (is.getItem() instanceof ItemBlock &&
        // RenderBlocks.renderItemIn3d(Block.getBlockFromItem(is.getItem()).getRenderType()))
        // {
        // GL11.glTranslatef(0f, 0.5f, 0f);
        // GL11.glScalef(0.3f, 0.3f, 0.3f);
        // renderBlock.renderBlockAsItem(Block.getBlockFromItem(is.getItem()),
        // is.getItemDamage(), 1f);
        // } else {
        // GL11.glScalef(0.4f, 0.4f, 0.4f);
        // GL11.glTranslatef(-0.475f, 0.6f, 0.05f);
        //
        // for (int renderPass = 0; renderPass <
        // is.getItem().getRenderPasses(is.getItemDamage()); renderPass++) {
        // IIcon icon = is.getItem().getIcon(is, renderPass);
        // if (icon != null) {
        // int rgb = is.getItem().getColorFromItemStack(is, renderPass);
        // float r = (rgb >> 16 & 255) / 255f;
        // float g = (rgb >> 8 & 255) / 255f;
        // float b = (rgb & 255) / 255f;
        // GL11.glColor3f(r, g, b);
        // float f = icon.getMinU();
        // float f1 = icon.getMaxU();
        // float f2 = icon.getMinV();
        // float f3 = icon.getMaxV();
        // ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3,
        // icon.getIconWidth(), icon.getIconHeight(), 1f / 16f);
        // }
        // }
        // GL11.glRotatef(0f, 0f, 1f, 0f);
        //
        // }
        // }
        
        IIcon icon = item.getItem().getIcon(item, item.getItem().getRenderPasses(item.getItemDamage()));
        RenderItem renderItem = (RenderItem) RenderManager.instance.entityRenderMap.get(EntityItem.class);
        GL11.glColor3f(1, 1, 1);
        if (icon != null) {
            ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1 / 16f);
        }
        
        if (type.equals(ItemRenderType.INVENTORY)) {
            renderItem.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, item, 0, 0);
        }
        GL11.glScalef(0.9f, 0.9f, 0.9f);
        GL11.glTranslatef(0, 1/16f, 0);
        Item it = ItemCompacted.getItem(item);
        ItemStack is = new ItemStack(it);
        if(ItemCompacted.getTag(item) != null) is.setTagCompound(ItemCompacted.getTag(item));
        if (it != null) {
            
            IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(is, type);
            if (renderer != null && renderer.handleRenderType(is, ItemRenderType.INVENTORY)) { // special render
                if(!type.equals(ItemRenderType.EQUIPPED)){
                    GL11.glScalef(0.05f, 0.05f, 0.05f);
                }
                renderer.renderItem(ItemRenderType.INVENTORY, is, data);
            } else if (it instanceof ItemBlock) {
                RenderBlocks renderBlock = new RenderBlocks();
                renderBlock.renderBlockAsItem(Block.getBlockFromItem(it), 0, 0);
            } else {
                if (type.equals(ItemRenderType.INVENTORY)) {
                    renderItem.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, is, 0, 0);
                } else {
                    for (int renderPass = 0; renderPass < is.getItem().getRenderPasses(is.getItemDamage()); renderPass++) {
                        icon = is.getItem().getIcon(is, renderPass);
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
                            ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1 / 16f);
                        }
                    }
                }
                
            }
        }
        
    }
    
}
