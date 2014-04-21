package mak.dc.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.util.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class DeadCraftAdminPacket extends AbstractPacket {
	int x ,y, z;
	
	boolean locked;
	ArrayList<String> allowed;
	
	public DeadCraftAdminPacket() {}
	
	
	public DeadCraftAdminPacket(TileEntityDeadCraft te) {
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		
		this.locked = te.isLocked();		
		this.allowed = te.getAllowedUser();
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		
		buf.writeInt(this.allowed.size());
		if(!this.allowed.isEmpty()) {
			for (int i = 0; i < this.allowed.size(); i++)
				ByteBufUtils.writeUTF8String(buf, this.allowed.get(i));
		}
		buf.writeBoolean(locked);
		
	}
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
        this.z = buf.readInt();
        
    	int size = buf.readInt();
    	if(size !=0) {
    		ArrayList<String> newAllowed = new ArrayList<String>();
    		for(int i = 0 ; i <size; i++) 
    			newAllowed.add(ByteBufUtils.readUTF8String(buf));
    		this.allowed = newAllowed;
    	}
    	this.locked = buf.readBoolean();
 
		
	}
	@Override
	public void handleClientSide(EntityPlayer player) {
		return;
	}
	@Override
	public void handleServerSide(EntityPlayer player) {
		World world = player.worldObj;
		if(!world.isRemote) {
			TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
			if(te == null || !(te instanceof TileEntityDeadCraft)) return;
			te.setAllowedUser(allowed);
			te.setLocked(locked);
		}
	}
	
	@Override
	public String toString() {
		return ("coord : " + x +" " + y + " " +z + "\n locked : " + locked + "\n allowed : " + allowed );
	}
	
	
	
	
	

}
