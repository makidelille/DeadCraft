package mak.dc.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import mak.dc.util.AbstractPacket;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;

public class PacketPipeline extends
		MessageToMessageCodec<FMLProxyPacket, AbstractPacket> {
	
	private EnumMap<Side,FMLEmbeddedChannel> channels;
	private LinkedList<Class <? extends AbstractPacket>> packets = new LinkedList<Class<? extends AbstractPacket>>();
	private boolean isPostInitialised = false;
	
	public boolean registerPacket (Class< ? extends AbstractPacket> clazz) {
		if(this.packets.size() > 256) return false; //log should be here
		if(this.packets.contains(clazz)) return false; //log should be here
		if(this.isPostInitialised) return false; //log should be here
		this.packets.add(clazz);
		return true;
	}

	// In line encoding of the packet, including discriminator setting
	@Override
	protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception {
		ByteBuf buf = Unpooled.buffer();
		Class<? extends AbstractPacket> clazz = msg.getClass();
		if(!this.packets.contains(clazz)) {
			throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
		}
		
		byte discriminatror = (byte) this.packets.indexOf(clazz);
		buf.writeByte(discriminatror);
		msg.encodeInto(ctx, buf);
		FMLProxyPacket proxyPacket = new FMLProxyPacket(buf.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
		out.add(proxyPacket);
	}

	
	 // In line decoding and handling of the packet
	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg,List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
