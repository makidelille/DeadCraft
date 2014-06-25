package mak.dc.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class DrawHelper {
    
    public static void drawBlockOutline(AxisAlignedBB bounds, float lineWidth, float R, float G, float B, float A) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glLineWidth(lineWidth);
        GL11.glDepthMask(true);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        
        GL11.glColor4f(R, G, B, A);
        
        RenderGlobal.drawOutlinedBoundingBox(bounds.copy().expand(0.0075, 0.0075, 0.0075), -1);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glPopMatrix();
    }
    
    public static void drawFloatingString(String s, double x, double y, double z, double yaw, double pitch, double roll, int R, int G, int B, int A, float size) {
        FontRenderer ftRenderer = Minecraft.getMinecraft().fontRenderer;
        drawFloatingString(s, x, y, z, ftRenderer.getStringWidth(s), ftRenderer.FONT_HEIGHT + 1, yaw, pitch, roll, R, G, B, A, size);
    }
    //TODO later split string
    public static void drawFloatingString(String s, double x, double y, double z, int width, int height, double yaw, double pitch, double roll, int R, int G, int B, int A, float size) {
        FontRenderer ftRenderer = Minecraft.getMinecraft().fontRenderer;
        
        GL11.glPushMatrix();
        
        GL11.glTranslated(x, y, z);
        GL11.glRotated(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(size, -size, size);
        GL11.glRotated(pitch, 1.0F, 0.0F, 0.0F);
        GL11.glRotated(roll, 0, 0, 1f);
        
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_LINES, GL11.GL_POINTS);
        
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tess.addVertex(+width / 2, +height / 2, 0);
        tess.addVertex(+width / 2, -height / 2, 0);
        tess.addVertex(-width / 2, -height / 2, 0);
        tess.addVertex(-width / 2, +height / 2, 0);
        tess.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        ftRenderer.drawString(s, -width / 2, -height / 2 + 1, 553648127);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        ftRenderer.drawString(s, -width / 2, -height / 2 + 1, -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        
        
        GL11.glPopMatrix();
    }
    
    public static void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, int R, int G, int B, int alpha, float width) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        GL11.glLineWidth(width);
        Tessellator tess = Tessellator.instance;
        tess.startDrawing(GL11.GL_LINES);
        tess.setColorRGBA(R, G, B, alpha);
        tess.addVertex(startX, startY, startZ);
        tess.addVertex(endX, endY, endZ);
        tess.draw();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    
}
