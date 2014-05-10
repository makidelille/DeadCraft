package mak.dc.items.itemBlock;

import java.util.List;

import org.lwjgl.input.Keyboard;

import mak.dc.blocks.BlockDeadCraft;
import mak.dc.blocks.BlockGodBottler;
import mak.dc.util.NBTTagCompoundDeadCraft;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
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
					info.add(EnumChatFormatting.YELLOW +StatCollector.translateToLocal("dc.info.holdShift"));
				}
			}
				
		}else{
			info.add((EnumChatFormatting.GRAY +StatCollector.translateToLocal("dc.block.info.notSet")));
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
			int dx = 0,dy = 0,dz = 0;
			
			switch(side) {
			case 0:
				dy = -1;
				break;
			case 1:
				dy = +1;
				break;
			case 2:
				dz = -1;
				break;
			case 3:
				dz = +1;
				break;
			case 4:
				dx = -1;
				break;
			case 5:
				dx = +1;
				break;
			}
			if(this.blockDC.canBePlaced(world, x+dx, y+dy, z+dz)){
				world.setBlock(x+dx, y+dy, z+dz, blockDC);
				blockDC.onBlockPlacedBy(world, x+dx, y+dy, z+dz, player, is);
				if(!world.isRemote && !player.capabilities.isCreativeMode) player.inventory.decrStackSize(player.inventory.currentItem, 1);
				return true;
			}
		
		
		return false;
	}
	
	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return  (blockDC instanceof BlockGodBottler);
	}
	
	
	

}
