package mak.dc.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCompresor extends ModelBase {
    // fields
    private ModelRenderer Base;
    private ModelRenderer PistonBase;
    private ModelRenderer BotPress;
    private ModelRenderer BotPressFence1;
    private ModelRenderer BotPressFence2;
    private ModelRenderer BotPressFence3;
    private ModelRenderer BotPressFence4;
    
    public ModelRenderer PistonTop;
    public ModelRenderer Top;
    public ModelRenderer TopPress;
    public ModelRenderer Press;
    
    public ModelCompresor() {
        textureWidth = 64;
        textureHeight = 64;
        
        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(-8F, 0F, -8F, 16, 1, 16);
        Base.setRotationPoint(0F, 23F, 0F);
        Base.setTextureSize(64, 64);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        PistonBase = new ModelRenderer(this, 4, 0);
        PistonBase.addBox(-8F, -4F, -8F, 2, 7, 2);
        PistonBase.setRotationPoint(0F, 20F, 0F);
        PistonBase.setTextureSize(64, 64);
        PistonBase.mirror = true;
        setRotation(PistonBase, 0F, 0F, 0F);
        PistonTop = new ModelRenderer(this, 0, 0);
        PistonTop.addBox(-7.5F, -4F, -7.5F, 1, 7, 1);
        PistonTop.setRotationPoint(0F, 13F, 0F);
        PistonTop.setTextureSize(64, 64);
        PistonTop.mirror = true;
        setRotation(PistonTop, 0F, 0F, 0F);
        Top = new ModelRenderer(this, 0, 17);
        Top.addBox(-8F, 0F, -8F, 16, 1, 16);
        Top.setRotationPoint(0F, 8F, 0F);
        Top.setTextureSize(64, 64);
        Top.mirror = true;
        setRotation(Top, 0F, 0F, 0F);
        TopPress = new ModelRenderer(this, 48, 26);
        TopPress.addBox(-2F, 0F, -2F, 4, 3, 4);
        TopPress.setRotationPoint(0F, 9F, 0F);
        TopPress.setTextureSize(64, 64);
        TopPress.mirror = true;
        setRotation(TopPress, 0F, 0F, 0F);
        Press = new ModelRenderer(this, 52, 22);
        Press.addBox(-1.5F, 0F, -1.5F, 3, 1, 3);
        Press.setRotationPoint(0F, 12F, 0F);
        Press.setTextureSize(64, 64);
        Press.mirror = true;
        setRotation(Press, 0F, 0F, 0F);
        BotPress = new ModelRenderer(this, 44, 34);
        BotPress.addBox(-2.5F, 0F, -2.5F, 5, 2, 5);
        BotPress.setRotationPoint(0F, 21F, 0F);
        BotPress.setTextureSize(64, 64);
        BotPress.mirror = true;
        setRotation(BotPress, 0F, 0F, 0F);
        BotPressFence1 = new ModelRenderer(this, 0, 17);
        BotPressFence1.addBox(-2.5F, 0F, 0F, 5, 1, 1);
        BotPressFence1.setRotationPoint(0F, 20F, 1.5F);
        BotPressFence1.setTextureSize(64, 64);
        BotPressFence1.mirror = true;
        setRotation(BotPressFence1, 0F, 0F, 0F);
        BotPressFence2 = new ModelRenderer(this, 0, 19);
        BotPressFence2.addBox(-0.5F, 0F, -2F, 1, 1, 4);
        BotPressFence2.setRotationPoint(-2F, 20F, -0.5F);
        BotPressFence2.setTextureSize(64, 64);
        BotPressFence2.mirror = true;
        setRotation(BotPressFence2, 0F, 0F, 0F);
        BotPressFence3 = new ModelRenderer(this, 0, 24);
        BotPressFence3.addBox(0F, 0F, -2F, 1, 1, 4);
        BotPressFence3.setRotationPoint(1.5F, 20F, -0.5F);
        BotPressFence3.setTextureSize(64, 64);
        BotPressFence3.mirror = true;
        setRotation(BotPressFence3, 0F, 0F, 0F);
        BotPressFence4 = new ModelRenderer(this, 0, 29);
        BotPressFence4.addBox(-1.5F, 0F, 0F, 3, 1, 1);
        BotPressFence4.setRotationPoint(0F, 20F, -2.5F);
        BotPressFence4.setTextureSize(64, 64);
        BotPressFence4.mirror = true;
        setRotation(BotPressFence4, 0F, 0F, 0F);
    }
    
    public void render(float f5) {
        Base.render(f5);
        for (int i = 0; i < 4; i++) {
            GL11.glRotatef(90, 0f, 1f, 0f);
            PistonBase.render(f5);
            PistonTop.render(f5);
        }
        
        Top.render(f5);
        TopPress.render(f5);
        Press.render(f5);
        BotPress.render(f5);
        BotPressFence1.render(f5);
        BotPressFence2.render(f5);
        BotPressFence3.render(f5);
        BotPressFence4.render(f5);
    }
    
    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
    
}
