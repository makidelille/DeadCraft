package mak.dc.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mak.dc.DeadCraft;
import mak.dc.tileEntities.TileEntityEggSpawner;
import mak.dc.tileEntities.TileEntityGodBottler;
import mak.dc.util.AbstractPacket;

public class DeadCraftGodBottlerPacket extends AbstractPacket {
	
	
	private int x,y,z;
	
	private int facing;
	private boolean isTop;
	private boolean isPowered;
	
	public DeadCraftGodBottlerPacket() {}
	
	public DeadCraftGodBottlerPacket(TileEntityGodBottler te) {
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.facing = te.direction;
		this.isTop = te.isTop();
		this.isPowered = te.isRSPowered();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(facing);
		buf.writeBoolean(isTop);
		buf.writeBoolean(isPowered);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.facing = buf.readInt();
		this.isTop = buf.readBoolean();
		this.isPowered = buf.readBoolean();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		World worldObj = DeadCraft.proxy.getClientWorld();
		TileEntityGodBottler te = (TileEntityGodBottler) worldObj.getTileEntity(x, y, z);
		if(te == null) return;
		if(this.isTop) te.setTop();
		te.setDirection(facing);
		te.setRSPowered(isPowered);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {

	}

}
