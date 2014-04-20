package mak.dc.items.itemBlock;

import mak.dc.blocks.DeadCraftBlocks;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBlockGodBottler extends ItemBlockDeadCraft {

	public ItemBlockGodBottler(Block block) {
		super(block);	
	}
		
	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}
	

}
