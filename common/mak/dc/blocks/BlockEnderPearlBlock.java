package mak.dc.blocks;

import java.util.Random;

import mak.dc.lib.BlockInfo;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEnderPearlBlock extends BlockDeadCraft {


    public static boolean _IS_CREATIVE_MOVED; //if true, creatives players will be moved


    public BlockEnderPearlBlock() {
        super(Material.rock);
        this.setBlockName(BlockInfo.ENDERPEARLBLOCK_UNLOACLIZED_NAME);
        this.setHardness(3.0F);
        this.setResistance(1.5F);
        this.setTickRandomly(true);

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Textures.ENDERBLOCK_TEXT_LOC);		
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return blockIcon;

    }	

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        if(!world.isRemote) {
            if(!((EntityPlayer)entity).capabilities.isCreativeMode || (((EntityPlayer)entity).capabilities.isCreativeMode && _IS_CREATIVE_MOVED))		
                teleportEntity(world, x, y, z, entity);
        }
    }


    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack is) {
        super.onBlockPlacedBy(world, x, y, z, ent, is);
        if(!world.isRemote) {

            ((TileEntityDeadCraft)(world.getTileEntity(x, y, z))).setUnManagable();


            if(!((EntityPlayer)ent).capabilities.isCreativeMode || (((EntityPlayer)ent).capabilities.isCreativeMode && _IS_CREATIVE_MOVED))
                teleportEntity(world,x,y,z,ent);
        }
    }



    private void teleportEntity(World world, double x, double y, double z, Entity ent) {
        if(!world.isRemote) {

            for(int i=0; i<16; i++) {
                double newX = x + world.rand.nextInt(16) - world.rand.nextInt(16) +0.5D;
                double newY = y + world.rand.nextInt(16) - world.rand.nextInt(16) +0.5D;
                double newZ = z + world.rand.nextInt(16) - world.rand.nextInt(16) +0.5D;

                if ((world.isAirBlock((int)newX, (int)newY, (int)newZ) && world.isAirBlock((int)newX, (int)newY + 1 , (int)newZ)) && ent != null) {
                    if(!(ent instanceof EntityPlayerMP)){       
                        ent.setPosition(newX, newY, newZ);                  	
                        return ;
                    }else{
                        EntityPlayerMP playerMP = (EntityPlayerMP)ent;
                        if(playerMP != null)
                            playerMP.setPositionAndUpdate(newX, newY, newZ);
                        return ;
                    }}}

        }else {

            Random rand = world.rand;

            for( int i=0; i < 250; i++) {
                float xFx = (float) (x + rand.nextFloat() );
                float yFx = (float) (y + 2 + 0.5 * rand.nextFloat());
                float zFx = (float) (z + rand.nextFloat());

                float vectX = 0;
                float vectY = (-1) - rand.nextFloat();
                float vectZ = 0;

                world.spawnParticle("portal", xFx, yFx, zFx, vectX, vectY, vectZ);			
            }
        }
    }






}