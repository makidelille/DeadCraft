package mak.dc.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mak.dc.DeadCraft;
import mak.dc.common.tileEntities.TileEntityGodBottler;
import mak.dc.common.tileEntities.TileEntityGodBottler.EnumBuildError;
import mak.dc.common.util.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DeadCraftGodBottlerPacket extends AbstractPacket {
    
    int x, y, z;
    int workTime;
    int[] buildErrors;
    
    public DeadCraftGodBottlerPacket() {
    }
    
    public DeadCraftGodBottlerPacket(TileEntityGodBottler te) {
        x = te.xCoord;
        y = te.yCoord;
        z = te.zCoord;
        
        workTime = te.getWorkedTime();
        buildErrors = EnumBuildError.getIdsWithBuilError(te.getBuildErrors());
    }
    
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        
        workTime = buf.readInt();
        
        buildErrors = new int[buf.readInt()];
        for (int i = 0; i < buildErrors.length; i++) {
            buildErrors[i] = buf.readByte();
        }
        
    }
    
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        
        buf.writeInt(workTime);
        
        buf.writeInt(buildErrors.length);
        for (int buildError : buildErrors) {
            buf.writeByte(buildError);
        }
    }
    
    @Override
    public void handleClientSide(EntityPlayer player) {
        World world = DeadCraft.proxy.getClientWorld();
        TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
        if (te == null) return;
        te.setWorkedTime(workTime);
        te.setBuildErrors(EnumBuildError.getBuildErrorsWithIds(buildErrors));
        
    }
    
    @Override
    public void handleServerSide(EntityPlayer player) {
        return;
        
    }
    
}
