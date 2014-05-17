package mak.dc.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mak.dc.DeadCraft;
import mak.dc.tileEntities.TileEntityEnderConverter;
import mak.dc.util.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DeadCraftEnderConverterPacket extends AbstractPacket{

	private int x,y,z;
	private int stackID;
	private int stackMeta;
	
	public DeadCraftEnderConverterPacket() {}
	public DeadCraftEnderConverterPacket(TileEntityEnderConverter te){
		this.x=te.xCoord;
		this.y=te.yCoord;
		this.z=te.zCoord;
		ItemStack is=te.getStackInSlot(0);
		stackID = is == null ? 0 :Item.getIdFromItem(is.getItem());
		stackMeta = is == null ? 0 : is.getItemDamage();
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stackID);
		buf.writeInt(stackMeta);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		x=buf.readInt();
		y=buf.readInt();
		z=buf.readInt();
		stackID=buf.readInt();
		stackMeta= buf.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		World world = DeadCraft.proxy.getClientWorld();
		TileEntityEnderConverter te = (TileEntityEnderConverter) world.getTileEntity(x, y, z);
		if(te == null) return;
		if(stackID != 0 )te.setInventorySlotContents(0, new ItemStack(Item.getItemById(stackID),1, stackMeta ));
		else te.setInventorySlotContents(0, null);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
