package mak.dc.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.util.AbstractPacket;

public class DeadCraftDebugForcePakcet extends AbstractPacket {

	private int x,y,z;
	
	public DeadCraftDebugForcePakcet(){}
	public DeadCraftDebugForcePakcet(int x, int y,int z){
		this.x =  x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileEntityDeadCraft) ((TileEntityDeadCraft) te).syncWithplayer((EntityPlayerMP) player);
		

	}

}
