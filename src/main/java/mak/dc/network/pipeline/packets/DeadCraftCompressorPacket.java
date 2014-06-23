package mak.dc.network.pipeline.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mak.dc.DeadCraft;
import mak.dc.common.tileEntities.TileEntityCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DeadCraftCompressorPacket extends AbstractPacket {
    
    private int x,y,z;
    private boolean wip;
    private boolean client;
    private boolean isInverted;
    
    public DeadCraftCompressorPacket(){}
    public DeadCraftCompressorPacket(TileEntityCompressor te){
        this.x = te.xCoord;
        this.y = te.yCoord;
        this.z = te.zCoord;
        this.wip = te.hasStarted();
        this.isInverted = te.isInverted();
        this.client = false;
    }
    
    public DeadCraftCompressorPacket(int xCoord, int yCoord, int zCoord, boolean isInverted) {
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
        this.client = true;
        this.isInverted = isInverted;
    
    }
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {    
        this.client = buf.readBoolean();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        if(!client){
            this.wip = buf.readBoolean();
            this.isInverted = buf.readBoolean();
        }
        else this.isInverted = buf.readBoolean();
    }
    
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.writeBoolean(client);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        if(!this.client) {
            buf.writeBoolean(wip);
            buf.writeBoolean(isInverted);
        }
        else buf.writeBoolean(isInverted);
        
    }
    
    @Override
    public void handleClientSide(EntityPlayer player) {
        World world= DeadCraft.proxy.getClientWorld();
        TileEntityCompressor te = (TileEntityCompressor) world.getTileEntity(x, y, z);
        if(te == null) return;
        te.setWorkInProgress(this.wip);
        te.setInverted(isInverted);
        
    }
    
    @Override
    public void handleServerSide(EntityPlayer player) {
        World world = player.worldObj;
        TileEntityCompressor te = (TileEntityCompressor) world.getTileEntity(x, y, z);
        if(te == null) return;
        te.setInverted(this.isInverted);     
    }
    
}
