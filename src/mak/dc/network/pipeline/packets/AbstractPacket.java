package mak.dc.network.pipeline.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractPacket {
    
    public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf buf);
    
    public abstract void encodeInto(ChannelHandlerContext ctx, ByteBuf buf);
    
    public abstract void handleClientSide(EntityPlayer player);
    
    public abstract void handleServerSide(EntityPlayer player);
    
}
