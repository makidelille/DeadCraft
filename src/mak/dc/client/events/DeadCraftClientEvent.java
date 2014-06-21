package mak.dc.client.events;

import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.common.items.ItemWrench;
import mak.dc.common.tileEntities.TileEntityGodBottler;
import mak.dc.common.util.DrawHelper;
import mak.dc.common.util.IPowerReceiver;
import mak.dc.common.util.IPowerSender;
import mak.dc.network.pipeline.packets.DeadCraftClientToServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.world.ChunkEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DeadCraftClientEvent {
    
    public void drawPowerConnections(World world, IPowerReceiver te, boolean isHighlighted, EntityPlayer player, double partialTicks) {
        if (te == null) return;
        
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        
        float alphaP = isHighlighted ? 0.8f : 0f;
        
        AxisAlignedBB bounds = ((TileEntity) te).getBlockType().getSelectedBoundingBoxFromPool(world, ((TileEntity) te).xCoord, ((TileEntity) te).yCoord, ((TileEntity) te).zCoord);
        bounds = bounds.copy().offset(-x, -y, -z);
        
        DrawHelper.drawBlockOutline(bounds, 5f, 0, 1, 0, 0.1f + alphaP);
        
        double blockMidX = bounds.maxX - (bounds.maxX - bounds.minX) / 2d;
        double blockMidY = bounds.maxY - (bounds.maxY - bounds.minY) / 2d;
        double blockMidZ = bounds.maxZ - (bounds.maxZ - bounds.minZ) / 2d;
        
        List<IPowerSender> sources = te.getPowerSource();
        for (IPowerSender source : sources) {
            
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glLineWidth(100.0f);
            GL11.glDepthMask(true);
            
            AxisAlignedBB sourcebounds = ((TileEntity) source).getBlockType().getSelectedBoundingBoxFromPool(world, ((TileEntity) source).xCoord, ((TileEntity) source).yCoord, ((TileEntity) source).zCoord); // .expand(-x,
                                                                                                                                                                                                               // -y,
                                                                                                                                                                                                               // -z);
            sourcebounds = sourcebounds.copy().offset(-x, -y, -z);
            
            DrawHelper.drawBlockOutline(sourcebounds, 5f, 1, 0, 0, 0.1f + alphaP);
            
            double sourceMidX = sourcebounds.maxX - (sourcebounds.maxX - sourcebounds.minX) / 2d;
            double sourceMidY = sourcebounds.maxY - (sourcebounds.maxY - sourcebounds.minY) / 2d;
            double sourceMidZ = sourcebounds.maxZ - (sourcebounds.maxZ - sourcebounds.minZ) / 2d;
            
            DrawHelper.drawLine(blockMidX, blockMidY, blockMidZ, sourceMidX, sourceMidY, sourceMidZ, 0, 0, 255, (int) (32 + alphaP * 96f / 0.8f), 50f);
            
            double dx = bounds.maxX - sourcebounds.maxX;
            double dz = bounds.maxZ - sourcebounds.maxZ;
            double dy = bounds.maxY - sourcebounds.maxY;
            double hyp = Math.sqrt(dx * dx + dz * dz);
            
            double roll = hyp != 0 ? Math.sin(dy / hyp) * 180 / Math.PI : 90d;
            double angle = dz != 0 ? Math.atan(dx / dz) * 180 / Math.PI : 90d;
            
            if (dx > 0 && dz >= 0 || dx <= 0 && dz >= 0) {
                angle += 180d;
            }
            
            double midX = blockMidX / 2d + sourceMidX / 2d;
            double midY = blockMidY / 2d + sourceMidY / 2d;
            double midZ = blockMidZ / 2d + sourceMidZ / 2d;
            
            float scale = 0.05f;
            
            String info = ((TileEntity) source).getBlockType().getLocalizedName();
            
            DrawHelper.drawFloatingString(info, midX, midY + 0.5, midZ, angle + 270, 0, roll, 0, 0, 0, 0, scale);
            DrawHelper.drawFloatingString(info, midX, midY + 0.5, midZ, angle + 90, 0, -roll, 0, 0, 0, 0, scale);
            
        }
    }
    
    @SubscribeEvent
    public void onBlockHighlightEvent(DrawBlockHighlightEvent e) {
        if (e.player.getHeldItem() != null && e.player.getHeldItem().getItem() instanceof ItemWrench) {
            World world = e.player.worldObj;
            for (int i = 0; i < world.loadedTileEntityList.size(); i++) {
                TileEntity te = (TileEntity) world.loadedTileEntityList.get(i);
                boolean flag = false;
                if (e.target.typeOfHit.equals(MovingObjectType.BLOCK)) if (world.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ) == null) {
                    flag = false;
                } else if (world.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ).equals(te)) {
                    flag = true;
                }
                if (te instanceof IPowerReceiver) {
                    if (te instanceof TileEntityGodBottler && ((TileEntityGodBottler) te).isTop()) {
                        te = ((TileEntityGodBottler) te).getPair();
                    }
                    drawPowerConnections(world, (IPowerReceiver) te, flag, e.player, e.partialTicks);
                    e.setCanceled(flag);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onChunckLoad(ChunkEvent.Load e) {
        if (DeadCraft.proxy.getClientWorld() != null && e.getChunk() != null && Minecraft.getMinecraft().thePlayer != null) {
            DeadCraft.packetPipeline.sendToServer(new DeadCraftClientToServerPacket(-1, Minecraft.getMinecraft().thePlayer.getCommandSenderName(), e.getChunk().xPosition, 0, e.getChunk().zPosition, 0));
        }
    }
    
}
