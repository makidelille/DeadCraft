package mak.dc.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mak.dc.tileEntities.TileEntityEggSpawner;
import mak.dc.util.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public class DeadCraftEggSpawnerPacket extends AbstractPacket {

	private int x, y, z;

	private byte buttonId;
	
	public DeadCraftEggSpawnerPacket() {
		
	}
	
	public DeadCraftEggSpawnerPacket(int x, int y, int z, byte id) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.buttonId = id;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
			buf.writeByte(buttonId);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.buttonId = buf.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
	
		World worldObj = player.worldObj;
		TileEntityEggSpawner te = (TileEntityEggSpawner) worldObj.getTileEntity(x, y, z);

		if (!worldObj.isRemote) {
			switch (buttonId) {
			case 0:
				if (!te.hasStarted()) {
					if (te.isInventoryComplete() && te.getProgress() == 0) {
						te.decrStackCreation();
						te.setStarted((byte) 1);
					} else if (te.getProgress() > 0) {
						te.setStarted((byte) 1);
					}
				}
				break;
			case 1:
				if (te.getRedstoneState() != 0) {
					te.setRedstoneState((byte) 0);
				}
				break;
			case 2:
				if (te.getRedstoneState() != 1) {
					te.setRedstoneState((byte) 1);
				}
				break;
			case 3:
				if (te.getRedstoneState() != 2) {
					te.setRedstoneState((byte) 2);
				}
				break;
			case 4:
				if (te.getMode() != 1)
					te.setMode((byte) 1);
				break;
			case 5:
				if (te.getMode() != 0)
					te.setMode((byte) 0);
				break;
			case 6:
				te.setStarted((byte) 0);
			}

		}

	}

}
