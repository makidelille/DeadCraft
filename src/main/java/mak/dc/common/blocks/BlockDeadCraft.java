package mak.dc.common.blocks;

import mak.dc.common.items.ItemWrench;
import mak.dc.common.tileEntities.TileEntityDeadCraft;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockDeadCraft extends Block implements ITileEntityProvider {
    
    public BlockDeadCraft(Material par2Material) {
        super(par2Material);
        setBlockUnbreakable();
        setResistance(-1F);
        
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!world.isRemote) {
            ItemStack is = new ItemStack(block);
            is.stackSize = 1;
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
            if (te != null) {
                NBTTagCompound info = te.writeNBTData();
                is.setTagCompound(info);
                if (te.hasInventory()) {
                    IInventory teinv = (IInventory) te;
                    for (int i = 0; i < teinv.getSizeInventory(); i++) {
                        ItemStack stack = teinv.getStackInSlotOnClosing(i);
                        
                        if (stack != null) {
                            float spawnX = x + world.rand.nextFloat();
                            float spawnY = y + world.rand.nextFloat();
                            float spawnZ = z + world.rand.nextFloat();
                            
                            EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
                            world.spawnEntityInWorld(droppedItem);
                        }
                    }
                }
                world.spawnEntityInWorld(new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, is));
                world.removeTileEntity(x, y, z);
            }
            world.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }
    
    public boolean canBePlaced(World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z);
    }
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityDeadCraft(true);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
            if (!((TileEntityDeadCraft) world.getTileEntity(x, y, z)).isUserAllowed(player.getCommandSenderName())) return false;
        }
        return false;
    }
    
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.isRemote) {
            if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
                if (!((TileEntityDeadCraft) world.getTileEntity(x, y, z)).isUserCreator(player.getCommandSenderName())) return;
                
                if (player.isSneaking() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWrench) {
                    breakBlock(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
                    return;
                }
            }
        }
        return;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack is) {
        super.onBlockPlacedBy(world, x, y, z, ent, is);
        if (is.getTagCompound() != null && world.getTileEntity(x, y, z) != null && ent instanceof EntityPlayer) {
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
            te.readNBTData(is.getTagCompound());
        } else if (world.getTileEntity(x, y, z) != null && ent instanceof EntityPlayer) {
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
            te.setOwner(((EntityPlayer) ent).getCommandSenderName());
        }
        
    }
    
    public void onWrenched(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    }
    
    @Override
    public BlockDeadCraft setBlockName(String name) {
        return (BlockDeadCraft) super.setBlockName(name);
    }
    
}
