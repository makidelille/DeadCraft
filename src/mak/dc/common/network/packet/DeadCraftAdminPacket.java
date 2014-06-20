package mak.dc.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

import mak.dc.common.tileEntities.TileEntityDeadCraft;
import mak.dc.common.util.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class DeadCraftAdminPacket extends AbstractPacket {
    int x, y, z;
    
    boolean locked;
    ArrayList<String> allowed;
    
    public DeadCraftAdminPacket() {
    }
    
    public DeadCraftAdminPacket(TileEntityDeadCraft te) {
        x = te.xCoord;
        y = te.yCoord;
        z = te.zCoord;
        
        locked = te.isLocked();
        allowed = te.getAllowedUser();
    }
    
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        
        int size = buf.readInt();
        if (size != 0) {
            ArrayList<String> newAllowed = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                newAllowed.add(ByteBufUtils.readUTF8String(buf));
            }
            allowed = newAllowed;
        }
        locked = buf.readBoolean();
        
    }
    
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        
        buf.writeInt(allowed.size());
        if (!allowed.isEmpty()) {
            for (int i = 0; i < allowed.size(); i++) {
                ByteBufUtils.writeUTF8String(buf, allowed.get(i));
            }
        }
        buf.writeBoolean(locked);
        
    }
    
    @Override
    public void handleClientSide(EntityPlayer player) {
        return;
    }
    
    @Override
    public void handleServerSide(EntityPlayer player) {
        World world = player.worldObj;
        if (!world.isRemote) {
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
            if (te == null || !(te instanceof TileEntityDeadCraft)) return;
            te.setAllowedUser(allowed);
            te.setLocked(locked);
        }
    }
    
    @Override
    public String toString() {
        return "coord : " + x + " " + y + " " + z + "\n locked : " + locked + "\n allowed : " + allowed;
    }
    
}
