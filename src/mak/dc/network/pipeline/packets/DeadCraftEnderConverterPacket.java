package mak.dc.network.pipeline.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mak.dc.DeadCraft;
import mak.dc.common.tileEntities.TileEntityEnderConverter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DeadCraftEnderConverterPacket extends AbstractPacket {
    
    private int x, y, z;
    private int stackID;
    private int stackSize;
    private int stackMeta;
    
    public DeadCraftEnderConverterPacket() {
    }
    
    public DeadCraftEnderConverterPacket(TileEntityEnderConverter te) {
        x = te.xCoord;
        y = te.yCoord;
        z = te.zCoord;
        ItemStack is = te.getStackInSlot(0);
        stackID = is == null ? 0 : Item.getIdFromItem(is.getItem());
        stackMeta = is == null ? 0 : is.getItemDamage();
        stackSize = is == null ? 0 : is.stackSize;
    }
    
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        stackID = buf.readInt();
        stackSize = buf.readInt();
        stackMeta = buf.readInt();
    }
    
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(stackID);
        buf.writeInt(stackSize);
        buf.writeInt(stackMeta);
        
    }
    
    @Override
    public void handleClientSide(EntityPlayer player) {
        World world = DeadCraft.proxy.getClientWorld();
        TileEntityEnderConverter te = (TileEntityEnderConverter) world.getTileEntity(x, y, z);
        if (te == null) return;
        if (stackID != 0) {
            te.setInventorySlotContents(0, new ItemStack(Item.getItemById(stackID), stackSize, stackMeta));
        } else {
            te.setInventorySlotContents(0, null);
        }
    }
    
    @Override
    public void handleServerSide(EntityPlayer player) {
    }
    
}
