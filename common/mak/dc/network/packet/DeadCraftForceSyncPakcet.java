package mak.dc.network.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.util.AbstractPacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class DeadCraftForceSyncPakcet extends AbstractPacket {

	private int x,y,z;
	private String player;
	private boolean syncWithAll;
	
	public DeadCraftForceSyncPakcet(){}
	public DeadCraftForceSyncPakcet(int x, int y,int z, String playerName){
		this.x = x;
		this.y = y;
		this.z = z;
		this.player = playerName;
		this.syncWithAll = false;
	}
	public DeadCraftForceSyncPakcet(int x, int y,int z, EntityPlayer player){
		this(x,y,z,player.getCommandSenderName());
	}
	
	public DeadCraftForceSyncPakcet(EntityPlayer thePlayer) {
		this.player = thePlayer.getCommandSenderName();
		this.syncWithAll = true;
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		buf.writeBoolean(syncWithAll);
		if(!syncWithAll){
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
		}
		ByteBufUtils.writeUTF8String(buf, player);

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		syncWithAll = buf.readBoolean();
		if(!syncWithAll) {
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
		}
		player = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		World world = player.worldObj;
		if(!world.isRemote) {
			EntityPlayerMP sync = (EntityPlayerMP) world.getPlayerEntityByName(this.player);
			if(sync == null) return;
			if(!syncWithAll){
				TileEntity te = world.getTileEntity(x, y, z);
				if(te instanceof TileEntityDeadCraft) ((TileEntityDeadCraft) te).syncWithplayer(sync);
			}else{
				List tes = world.loadedTileEntityList;
				for (int i = 0; i<tes.size(); i++) {
					if(tes.get(i) instanceof TileEntityDeadCraft) {
						System.out.println(tes.get(i));
						((TileEntityDeadCraft)tes.get(i)).syncWithplayer(sync);
					}
				}
			}
		}
		

	}

}
