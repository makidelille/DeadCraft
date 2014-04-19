package mak.dc.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGodBottler extends ModelBase
{
  //fields
    ModelRenderer BBot;
    ModelRenderer BTop;
    ModelRenderer BBack;
    ModelRenderer BLeft;
    ModelRenderer BRight;
    ModelRenderer Plateau;
    ModelRenderer Support;
    ModelRenderer Brastop;
    ModelRenderer Brasmid;
    ModelRenderer BrasBot;
    ModelRenderer PanelBot;
    ModelRenderer PanelTop;
  
  public ModelGodBottler()
  {
    textureWidth = 128;
    textureHeight = 128;
    
      BBot = new ModelRenderer(this, 0, 38);
      BBot.addBox(-8F, -2F, -8F, 16, 4, 16);
      BBot.setRotationPoint(0F, 22F, 0F);
      BBot.setTextureSize(128, 128);
      BBot.mirror = true;
      setRotation(BBot, 0F, 0F, 0F);
      BTop = new ModelRenderer(this, 0, 58);
      BTop.addBox(-8F, -2F, -8F, 16, 4, 16);
      BTop.setRotationPoint(0F, -6F, 0F);
      BTop.setTextureSize(128, 128);
      BTop.mirror = true;
      setRotation(BTop, 0F, 0F, 0F);
      BBack = new ModelRenderer(this, 0, 0);
      BBack.addBox(-8F, -12F, -1F, 16, 24, 2);
      BBack.setRotationPoint(0F, 8F, 7F);
      BBack.setTextureSize(128, 128);
      BBack.mirror = true;
      setRotation(BBack, 0F, 0F, 0F);
      BLeft = new ModelRenderer(this, 36, 0);
      BLeft.addBox(-8F, -12F, -1F, 16, 24, 2);
      BLeft.setRotationPoint(7F, 8F, 0F);
      BLeft.setTextureSize(128, 128);
      BLeft.mirror = true;
      setRotation(BLeft, 0F, 1.570796F, 0F);
      BRight = new ModelRenderer(this, 72, 0);
      BRight.addBox(-8F, -12F, -1F, 16, 24, 2);
      BRight.setRotationPoint(-7F, 8F, 0F);
      BRight.setTextureSize(128, 128);
      BRight.mirror = true;
      setRotation(BRight, 0F, -1.570796F, 0F);
      Plateau = new ModelRenderer(this, 26, 26);
      Plateau.addBox(-5F, 0F, -5F, 10, 1, 10);
      Plateau.setRotationPoint(0F, 13F, 0F);
      Plateau.setTextureSize(128, 128);
      Plateau.mirror = true;
      setRotation(Plateau, 0F, 0F, 0F);
      Support = new ModelRenderer(this, 26, 26);
      Support.addBox(-1F, -3F, -1F, 2, 6, 2);
      Support.setRotationPoint(0F, 17F, 0F);
      Support.setTextureSize(128, 128);
      Support.mirror = true;
      setRotation(Support, 0F, 0F, 0F);
      Brastop = new ModelRenderer(this, 108, 0);
      Brastop.addBox(-2F, -4F, -2F, 4, 8, 4);
      Brastop.setRotationPoint(0F, 0F, 0F);
      Brastop.setTextureSize(128, 128);
      Brastop.mirror = true;
      setRotation(Brastop, 0F, 0F, 0F);
      Brasmid = new ModelRenderer(this, 56, 26);
      Brasmid.addBox(-1F, -3F, -1F, 2, 6, 2);
      Brasmid.setRotationPoint(0F, 7F, 0F);
      Brasmid.setTextureSize(128, 128);
      Brasmid.mirror = true;
      setRotation(Brasmid, 0F, 0F, 0F);
      BrasBot = new ModelRenderer(this, 108, 12);
      BrasBot.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
      BrasBot.setRotationPoint(0F, 10F, 0F);
      BrasBot.setTextureSize(128, 128);
      BrasBot.mirror = true;
      setRotation(BrasBot, 0F, 0F, 0F);
      PanelBot = new ModelRenderer(this, 0, 26);
      PanelBot.addBox(-6F, -2F, 0F, 12, 4, 1);
      PanelBot.setRotationPoint(0F, 18F, -8F);
      PanelBot.setTextureSize(128, 128);
      PanelBot.mirror = true;
      setRotation(PanelBot, 0F, 0F, 0F);
      PanelTop = new ModelRenderer(this, 0, 31);
      PanelTop.addBox(-6F, -3F, 0F, 12, 6, 1);
      PanelTop.setRotationPoint(0F, -1F, -8F);
      PanelTop.setTextureSize(128, 128);
      PanelTop.mirror = true;
      setRotation(PanelTop, 0F, 0F, 0F);
  }
  
  public void render(float f)
  {
    BBot.render(f);
    BTop.render(f);
    BBack.render(f);
    BLeft.render(f);
    BRight.render(f);
    Plateau.render(f);
    Support.render(f);
    Brastop.render(f);
    Brasmid.render(f);
    BrasBot.render(f);
    PanelBot.render(f);
    PanelTop.render(f);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5,Entity ent)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5,ent);
  }

}
