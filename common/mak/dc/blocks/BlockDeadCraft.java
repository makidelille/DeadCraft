package mak.dc.blocks;

//TODO sinish switch

import mak.dc.items.ItemController;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDeadCraft extends BlockContainer{

    
    protected BlockDeadCraft (int par1, Material par2Material) {
        super(par1, par2Material);
    }

    public TileEntity createNewTileEntity (World world) {
        return new TileEntityDeadCraft();
    }
      
    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase ent,  ItemStack is) {
        super.onBlockPlacedBy(world, x, y, z, ent, is);
        if(!world.isRemote) {
            if(world.blockHasTileEntity(x, y, z) && ent instanceof EntityPlayer) {
                TileEntityDeadCraft te = (TileEntityDeadCraft) world.getBlockTileEntity(x, y, z);
                te.setOwner(((EntityPlayer)ent).username);
                System.out.println("valid");
            }
        }
    }
    
    @Override
    public void onBlockClicked (World world, int x, int y, int z, EntityPlayer player) {
        if(!world.isRemote && world.blockHasTileEntity(x, y, z)) {
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getBlockTileEntity(x, y, z);
            String username = player.username;
            if(te.isUserCreator(username)) {
                if(player.getCurrentEquippedItem().getItem() instanceof ItemController) {
                    ItemStack is = player.getCurrentEquippedItem();
                   ItemController controller = (ItemController)is.getItem();
                   switch(controller.getMode(is)) {
                       case 0 : //overwrite users list
                           te.setAllowedUser(controller.getAllowedList(is));
                           break;
                       case 1 : //add Allowed Users
                           te.addAllowedUser(controller.addUser(is));
                           break;
                       case 2 : //delete Allowed Users
                           break;
                       case 3 : //reset users
                           break;
                       case 4 : //change stats (lock)
                           break;
                       case 5 : //show infos
                           break;
                       default : break;
                   }
                }
                
            }
        }
    }

  
}
