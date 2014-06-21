package mak.dc.network.pipeline.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.common.util.IPowerReceiver;
import mak.dc.common.util.IPowerSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class DeadCraftPowerSourcesPacket extends AbstractPacket {
    
    private int x, y, z;
    private ArrayList<int[]> coords;
    
    public DeadCraftPowerSourcesPacket() {
    }
    
    public DeadCraftPowerSourcesPacket(IPowerReceiver te, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        coords = new ArrayList<>();
        List<IPowerSender> pSource = te.getPowerSource();
        for (int i = 0; i < pSource.size(); i++) {
            int[] coord = new int[3];
            coord = pSource.get(i).getCoord();
            coords.add(coord);
        }
    }
    
    public DeadCraftPowerSourcesPacket(TileEntity te) {
        this((IPowerReceiver) te, te.xCoord, te.yCoord, te.zCoord);
    }
    
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        coords = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int cx = buf.readInt();
            int cy = buf.readInt();
            int cz = buf.readInt();
            coords.add(new int[] { cx, cy, cz });
        }
        
    }
    
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(coords.size());
        for (int i = 0; i < coords.size(); i++) {
            int[] coord = coords.get(i);
            buf.writeInt(coord[0]);
            buf.writeInt(coord[1]);
            buf.writeInt(coord[2]);
        }
        
    }
    
    @Override
    public void handleClientSide(EntityPlayer player) {
        World world = DeadCraft.proxy.getClientWorld();
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof IPowerReceiver) {
            ((IPowerReceiver) te).resetPowerSource();
            for (int i = 0; i < coords.size(); i++) {
                int[] coord = coords.get(i);
                ((IPowerReceiver) te).setPowerSource(coord);
            }
        }
    }
    
    @Override
    public void handleServerSide(EntityPlayer player) {
    }
    
}
