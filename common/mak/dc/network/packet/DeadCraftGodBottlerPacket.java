package mak.dc.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mak.dc.DeadCraft;
import mak.dc.proxy.ClientProxy;
import mak.dc.tileEntities.TileEntityGodBottler;
import mak.dc.tileEntities.TileEntityGodBottler.EnumBuildError;
import mak.dc.util.AbstractPacket;

public class DeadCraftGodBottlerPacket extends AbstractPacket {

	int x,y,z;
	int workTime;
	int[] buildErrors;
	
	public DeadCraftGodBottlerPacket(){}
	public DeadCraftGodBottlerPacket(TileEntityGodBottler te) {
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		
		this.workTime = te.getWorkedTime();
		this.buildErrors = EnumBuildError.getIdsWithBuilError(te.getBuildErrors());
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		
		buf.writeInt(workTime);
		
		buf.writeInt(buildErrors.length);
		for(int i = 0; i< buildErrors.length; i++) {
			buf.writeByte(buildErrors[i]);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();

		workTime  = buf.readInt();
		
		buildErrors = new int[buf.readInt()];
		for(int i = 0; i< buildErrors.length; i++)
			buildErrors[i] = buf.readByte();
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		World world = DeadCraft.proxy.getClientWorld();
		TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
		if(te == null) return;
		te.setWorkedTime(workTime);
		te.setBuildErrors(EnumBuildError.getBuildErrorsWithIds(buildErrors));
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		return;

	}

}
