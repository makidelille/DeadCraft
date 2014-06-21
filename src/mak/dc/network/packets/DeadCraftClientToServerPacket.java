package mak.dc.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mak.dc.common.tileEntities.TileEntityDeadCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.common.network.ByteBufUtils;

public class DeadCraftClientToServerPacket extends AbstractPacket {
    
    String username;
    
    int x, y, z;
    int type;
    int data;
    
    public DeadCraftClientToServerPacket() {
    }
    
    public DeadCraftClientToServerPacket(int type, String playerName, int blockX, int blockY, int blockZ, int data) {
        
        this.type = type;
        this.data = data;
        
        username = playerName;
        
        x = blockX;
        y = blockY;
        z = blockZ;
    }
    
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        username = ByteBufUtils.readUTF8String(buf);
        type = buf.readInt();
        switch (type) {
            case -2:
                break;
            case -1:
                x = buf.readInt();
                z = buf.readInt();
                break;
            case 0:
                x = buf.readInt();
                y = buf.readInt();
                z = buf.readInt();
                data = buf.readInt();
                break;
        
        }
        
    }
    
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, username);
        buf.writeInt(type);
        switch (type) {
            case -2:
                break;
            case -1:
                buf.writeInt(x);
                buf.writeInt(z);
                break;
            case 0:
                buf.writeInt(x);
                buf.writeInt(y);
                buf.writeInt(z);
                buf.writeInt(data);
                break;
        }
    }
    
    @Override
    public void handleClientSide(EntityPlayer player) {
    }
    
    @Override
    public void handleServerSide(EntityPlayer player) {
        World world = player.worldObj;
        EntityPlayer sync = world.getPlayerEntityByName(username);
        if (sync == null) return;
        switch (type) {
            case -2:
                List teList = world.loadedTileEntityList;
                for (int i = 0; i < teList.size(); i++) {
                    if (teList.get(i) instanceof TileEntityDeadCraft) {
                        ((TileEntityDeadCraft) teList.get(i)).syncWithplayer(sync);
                    }
                }
                break;
            case -1:
                Chunk chunkToSync = world.getChunkFromChunkCoords(x, z);
                Map tes = chunkToSync.chunkTileEntityMap;
                Iterator it = tes.keySet().iterator();
                while (it.hasNext()) {
                    TileEntity te = (TileEntity) tes.get(it.next());
                    if (te instanceof TileEntityDeadCraft) {
                        ((TileEntityDeadCraft) te).syncWithplayer(sync);
                    }
                    
                }
                break;
            case 0:
                break;
        }
        
    }
    
}
