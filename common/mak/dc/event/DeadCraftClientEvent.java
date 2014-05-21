package mak.dc.event;

import java.util.List;

import mak.dc.blocks.BlockDeadCraft;
import mak.dc.items.ItemWrench;
import mak.dc.tileEntities.TileEntityGodBottler;
import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
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
			Block block = world.getBlock(e.target.blockX, e.target.blockY, e.target.blockZ);
			if(block instanceof BlockDeadCraft && e.player.getHeldItem() != null && e.player.getHeldItem().getItem() instanceof ItemWrench) {
				TileEntity te = world.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ);
				if(te instanceof TileEntityGodBottler && ((TileEntityGodBottler) te).isTop()) te = ((TileEntityGodBottler) te).getPair();
				double x = e.player.lastTickPosX + (e.player.posX - e.player.lastTickPosX) * e.partialTicks;
				double y = e.player.lastTickPosY + (e.player.posY - e.player.lastTickPosY) * e.partialTicks;
				double z = e.player.lastTickPosZ + (e.player.posZ - e.player.lastTickPosZ) * e.partialTicks;
				
				if(te instanceof IPowerReceiver) {
					e.setCanceled(true);
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glLineWidth(100.0f);
					GL11.glDepthMask(true);
					
					AxisAlignedBB bounds = block.getSelectedBoundingBoxFromPool(world, e.target.blockX, e.target.blockY, e.target.blockZ);
					
					GL11.glColor4f(0f, 1f, 0f, 1f); 
					RenderGlobal.drawOutlinedBoundingBox(bounds.copy().expand(0.0075, 0.0075, 0.0075).offset(-x, -y, -z),-1);
					bounds = bounds.expand(-x, -y, -z);
					
					if(te instanceof IPowerReceiver) {
						List<IPowerSender> sources = ((IPowerReceiver)te).getPowerSource();
						for(IPowerSender source : sources){
							
							AxisAlignedBB sourcebounds = block.getSelectedBoundingBoxFromPool(world, ((TileEntity)source).xCoord, ((TileEntity)source).yCoord, ((TileEntity)source).zCoord); //.expand(-x, -y, -z);
							
							GL11.glColor4f(1f, 0, 0, 0.4f);
							RenderGlobal.drawOutlinedBoundingBox(sourcebounds.copy().expand(0.0075, 0.0075, 0.0075).offset(-x, -y, -z),-1);
							sourcebounds = sourcebounds.expand(-x, -y, -z);
							
							GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.4f);
							
							Tessellator tess = Tessellator.instance;
							
							tess.startDrawing(GL11.GL_LINES);
							tess.setColorRGBA(0, 0, 255, 156);
						
							tess.addVertex(bounds.maxX -0.5d, bounds.maxY - 0.5d, bounds.maxZ-0.5d);
							tess.addVertex(sourcebounds.maxX - 0.5d, sourcebounds.maxY-0.5d, sourcebounds.maxZ-0.5d);
							
							tess.draw();
						}	
						GL11.glDepthMask(false);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_BLEND);
					}
				}else if(te instanceof IPowerSender) {
					//TODO 
				}
			}
			
		}
	}
	
	
	private void drawBlockBoundsInColor(AxisAlignedBB bounds, int color) {
		RenderGlobal.drawOutlinedBoundingBox(bounds, color);
	}

}
