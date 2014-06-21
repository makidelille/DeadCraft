package mak.dc.client.render.block;

import org.lwjgl.opengl.GL11;

import mak.dc.client.model.ModelCompresor;
import mak.dc.client.render.IInventoryRenderer;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.common.tileEntities.TileEntityEnderConverter;
import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.Textures;
import mak.dc.network.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class SpecialRenderTileEntityCompressor extends TileEntitySpecialRenderer implements IInventoryRenderer{
    
    private static final ResourceLocation texture = new ResourceLocation(Lib.MOD_ID, Textures.COMPRESSOR_MODEL_TEXT_LOC);
    private static final ModelCompresor model = new ModelCompresor();
    
    public SpecialRenderTileEntityCompressor(){
        func_147497_a(TileEntityRendererDispatcher.instance);
    }
    
    
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        TileEntityCompressor te = (TileEntityCompressor) tile;
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1f);
        
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslated(x + 0.5f, y+1.5f, z + 0.5f);
        GL11.glRotatef(180f, 1f, 0f, 0f);
        float off = 0;
        if(te != null){
            float prog = ((float) te.getProgress()) / te.BUILDTIME; 
            off = -4 * prog * (prog -1)  * 7/16f;
        }
        model.PistonTop.offsetY = off;
        model.Top.offsetY = off;
        model.Press.offsetY = off;
        model.TopPress.offsetY = off;
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.render(0.0625F);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    public void renderInventory(double x, double y, double z) {
        renderTileEntityAt(null, x, y, z, 0f);
        
    }
    
}
