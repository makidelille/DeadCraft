package mak.dc.items.itemBlock;

import java.util.List;

import org.lwjgl.input.Keyboard;

import mak.dc.blocks.BlockDeadCraft;
import mak.dc.util.NBTTagCompoundDeadCraft;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockDeadCraft extends ItemBlock{

	private final BlockDeadCraft blockDC;

	public ItemBlockDeadCraft(Block block) {
		super(block);
		this.blockDC = (BlockDeadCraft) block;
		this.setUnlocalizedName("itemBlock." + block.getUnlocalizedName());
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List info, boolean par4) {
		if(is.getTagCompound() != null) {
			NBTTagCompound tag = is.getTagCompound();
			if(NBTTagCompoundDeadCraft.isDeadCraftCompound(tag)) {
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					List lis = NBTTagCompoundDeadCraft.getInfoListfordipslay(tag);
					for(int i= 0; i <lis.size(); i++) info.add(lis.get(i));
				}else{
					info.add(EnumChatFormatting.YELLOW +" -- Press Shift for info --");
				}
			}else{
				info.add((EnumChatFormatting.YELLOW +" -- has not been set for the first time --"));
			}
				
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			
				NBTTagCompound tag = is.getTagCompound();
				if(!player.capabilities.isCreativeMode)player.inventory.decrStackSize(player.inventory.currentItem, 1);
				switch (side) {
				case 0:
					if(this.blockDC.canBePlaced(world, x,y-1,z)) {
						world.setBlock(x, y-1, z, blockDC);
						blockDC.onBlockPlacedBy(world, x, y-1, z, player, is);
						return true;
					}
				case 1:	
					if(this.blockDC.canBePlaced(world, x,y+1,z)) {
						world.setBlock(x, y+1, z, blockDC);
						blockDC.onBlockPlacedBy(world, x, y+1, z, player, is);
						return true;
					}
				case 2:
					if(this.blockDC.canBePlaced(world, x,y,z-1)) {
						world.setBlock(x, y, z-1, blockDC);
						blockDC.onBlockPlacedBy(world, x, y, z-1, player, is);
						return true;
					}
				case 3:
					if(this.blockDC.canBePlaced(world,x,y,z+1)) {
						world.setBlock(x, y, z+1, blockDC);
						blockDC.onBlockPlacedBy(world, x, y, z+1, player, is);
						return true;
					}
				case 4:
					if(this.blockDC.canBePlaced(world,x-1,y,z)) {
						world.setBlock(x-1, y, z, blockDC);
						blockDC.onBlockPlacedBy(world, x-1, y, z, player, is);
						return true;
					}
				case 5 :
					if(this.blockDC.canBePlaced(world,x+1,y,z)) {
						world.setBlock(x+1, y, z, blockDC);
						blockDC.onBlockPlacedBy(world, x+1, y, z, player, is);
						return true;
					}
				}
		}
		
		
		return false;
	}
	
	
	

}
