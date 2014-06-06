package mak.dc.event;

import java.util.List;

import mak.dc.blocks.BlockDeadCraft;
import mak.dc.items.ItemWrench;
import mak.dc.tileEntities.TileEntityGodBottler;
import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DeadCraftClientEvent {
		
	@SubscribeEvent
	public void onBlockHighlightEvent(DrawBlockHighlightEvent e) {
		if(e.target.typeOfHit.equals(MovingObjectType.BLOCK)) {
			World world = e.player.worldObj;
			EntityPlayer player = e.player;
			Block block = world.getBlock(e.target.blockX, e.target.blockY, e.target.blockZ);
			if(e.player.getHeldItem() != null && e.player.getHeldItem().getItem() instanceof ItemWrench) {
				if(block instanceof BlockDeadCraft ) {
					TileEntity te = world.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ);
					if(te instanceof TileEntityGodBottler && ((TileEntityGodBottler) te).isTop()) te = ((TileEntityGodBottler) te).getPair();
					double x = e.player.lastTickPosX + (e.player.posX - e.player.lastTickPosX) * e.partialTicks;
					double y = e.player.lastTickPosY + (e.player.posY - e.player.lastTickPosY) * e.partialTicks;
					double z = e.player.lastTickPosZ + (e.player.posZ - e.player.lastTickPosZ) * e.partialTicks;
					
					if(te instanceof IPowerReceiver) {
						e.setCanceled(true);
						
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glLineWidth(100.0f);
						GL11.glDepthMask(true);

						OpenGlHelper.glBlendFunc(770, 771, 1, 0);
						
						AxisAlignedBB bounds = block.getSelectedBoundingBoxFromPool(world, e.target.blockX, e.target.blockY, e.target.blockZ);
						
						GL11.glColor4f(0f, 1f, 0f, 1f); 
						RenderGlobal.drawOutlinedBoundingBox(bounds.copy().expand(0.0075, 0.0075, 0.0075).offset(-x, -y, -z),-1);
						bounds = bounds.copy().offset(-x, -y, -z);
						
						double blockMidX =  bounds.maxX - (bounds.maxX - bounds.minX)/2d;
						double blockMidY =  bounds.maxY - (bounds.maxY - bounds.minY)/2d;
						double blockMidZ =  bounds.maxZ - (bounds.maxZ - bounds.minZ)/2d;
						
						List<IPowerSender> sources = ((IPowerReceiver)te).getPowerSource();
						for(IPowerSender source : sources){
							GL11.glDisable(GL11.GL_LIGHTING);
							GL11.glEnable(GL11.GL_BLEND);
							GL11.glDisable(GL11.GL_TEXTURE_2D);
							GL11.glLineWidth(100.0f);
							GL11.glDepthMask(true);
							
							AxisAlignedBB sourcebounds = ((TileEntity) source).getBlockType().getSelectedBoundingBoxFromPool(world, ((TileEntity)source).xCoord, ((TileEntity)source).yCoord, ((TileEntity)source).zCoord); //.expand(-x, -y, -z);
							GL11.glPushMatrix();
							GL11.glColor4f(1f, 0, 0, 0.4f);
							RenderGlobal.drawOutlinedBoundingBox(sourcebounds.copy().expand(0.0075, 0.0075, 0.0075).offset(-x, -y, -z),-1);
							sourcebounds = sourcebounds.copy().offset(-x, -y, -z);
							
							double sourceMidX = sourcebounds.maxX - (sourcebounds.maxX - sourcebounds.minX)/2d;
							double sourceMidY = sourcebounds.maxY - (sourcebounds.maxY - sourcebounds.minY)/2d;
							double sourceMidZ = sourcebounds.maxZ - (sourcebounds.maxZ - sourcebounds.minZ)/2d;
							
							GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.4f);
							
							Tessellator tess = Tessellator.instance;
							
							tess.startDrawing(GL11.GL_LINES);
								tess.setColorRGBA(0, 0, 255, 156);
							
								tess.addVertex(blockMidX, blockMidY, blockMidZ);
								tess.addVertex(sourceMidX, sourceMidY, sourceMidZ);
							tess.draw();
							GL11.glPopMatrix();
							
							//TODO create a Draw Helper to simplify and reuse the code elsewhere
							
							double dx = bounds.maxX - sourcebounds.maxX;
							double dz = bounds.maxZ - sourcebounds.maxZ;
							
							double angle =(Math.atan(dx/dz) * 180 / Math.PI);
																
							if(dx > 0 && dz >= 0){
								angle += 180d;
							}else if(dx <= 0 && dz >= 0) {
								angle += 180d;
							}
							
							
							double pitch = e.player.rotationPitch;
							double yaw = e.player.rotationYaw;
							
							double midX = blockMidX/2d + sourceMidX/2d;
							double midY = blockMidY/2d + sourceMidY/2d;
							double midZ = blockMidZ/2d + sourceMidZ/2d;
							
							double dist = Math.sqrt(dx * dx + dz * dz);
							
							float playerYaw = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw)*e.partialTicks;
							float playerPitch = player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch)*e.partialTicks;

							float scale =  (0.1f * (float)dist / 10f);
							FontRenderer ftRenderer = Minecraft.getMinecraft().fontRenderer;
							GL11.glPushMatrix();
							GL11.glTranslated(blockMidX, blockMidY, blockMidZ);
							GL11.glRotated(angle +180, 0.0F, 1.0F, 0.0F);
							GL11.glTranslated(0f, 0.5d, -(0.7d * dist) + 1d);
							GL11.glScalef(scale, -scale, scale);
							GL11.glRotated(angle +90d, 0.0F, -1.0F, 0.0F);
							GL11.glRotated(-yaw +90d, 0F, 1.0F, 0.0F);
							GL11.glRotated(pitch, 1.0F, 0.0F, 0.0F);
							GL11.glDisable(GL11.GL_LIGHTING);
							GL11.glDepthMask(false);
				            GL11.glDisable(GL11.GL_DEPTH_TEST);
				            GL11.glEnable(GL11.GL_BLEND);
				            GL11.glDisable(GL11.GL_TEXTURE_2D);
				            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_LINES, GL11.GL_POINTS);
				           				            
				            tess = Tessellator.instance;
				            tess.startDrawingQuads();
				            	tess.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
					            tess.addVertex(+10, +5, 0);
					            tess.addVertex(+10, -5, 0);
					            tess.addVertex(-10, -5, 0);
					            tess.addVertex(-10, +5, 0);
							tess.draw();
							
							GL11.glEnable(GL11.GL_TEXTURE_2D);
				            ftRenderer.drawString("test", -10, -4, 553648127); //TODO strings
				            GL11.glEnable(GL11.GL_DEPTH_TEST);
				            GL11.glDepthMask(true);
				            ftRenderer.drawString("test", -10, -4, -1);
							
							GL11.glPopMatrix();		
							GL11.glEnable(GL11.GL_LIGHTING);
							GL11.glDepthMask(true);
				            GL11.glEnable(GL11.GL_DEPTH_TEST);
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							
							
							//TODO add a text with the power transfert rate 
						}	
						GL11.glDepthMask(false);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_LIGHTING);
						e.setCanceled(true);
					}else if(te instanceof IPowerSender) {
						//TODO Later
					}
				}
			}
			
		}
	}
	
	
	private void drawBlockBoundsInColor(AxisAlignedBB bounds, int color) {
		RenderGlobal.drawOutlinedBoundingBox(bounds, color);
	}

}
