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
	
	
	public DeadCraftAdminPacket(int x, int y, int z, ArrayList<String> allowed,  boolean isLocked) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.locked = isLocked;		
		this.allowed = allowed;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		
		
		if(!this.allowed.isEmpty()) {
			buf.writeInt(this.allowed.size());
			for (int i = 0; i < this.allowed.size(); i++)
				ByteBufUtils.writeUTF8String(buf, this.allowed.get(i));
		}
		
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
 
		
	}
	@Override
	public void handleClientSide(EntityPlayer player) {
		return; //packet come from client interface		
	}
	@Override
	public void handleServerSide(EntityPlayer player) {
		World world = player.worldObj;
		TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
		if(te == null) return;
		if(!te.isManagable()) return;
		te.setAllowedUser(allowed);
		te.setLocked(locked);
	}
	
	
	
	
	

}
