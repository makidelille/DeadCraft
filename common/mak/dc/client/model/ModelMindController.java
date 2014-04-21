package mak.dc.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMindController extends ModelBase
{
  //fields
    ModelRenderer Controller;
    ModelRenderer Antenna;
    ModelRenderer Button;
  
  public ModelMindController()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Controller = new ModelRenderer(this, 0, 0);
      Controller.addBox(-4F, -0.5F, -5F, 8, 2, 10);
      Controller.setRotationPoint(0F, 0F, 0F);
      Controller.setTextureSize(64, 32);
      Controller.mirror = true;
      setRotation(Controller, 0F, 0F, 0F);
      Antenna = new ModelRenderer(this, 0, 0);
      Antenna.addBox(-0.5F, -0.5F, -2F, 1, 1, 4);
      Antenna.setRotationPoint(3F, 0.5F, -7F);
      Antenna.setTextureSize(64, 32);
      Antenna.mirror = true;
      setRotation(Antenna, 0F, 0F, 0F);
      Button = new ModelRenderer(this, 0, 6);
      Button.addBox(-1F, -0.5F, -1F, 2, 1, 2);
      Button.setRotationPoint(2F, -0.5F, -2F);
      Button.setTextureSize(64, 32);
      Button.mirror = true;
      setRotation(Button, 0F, 0F, 0F);
  }
  
  @Override
public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Controller.render(f5);
    Antenna.render(f5);
    Button.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  @Override
public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5,Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
