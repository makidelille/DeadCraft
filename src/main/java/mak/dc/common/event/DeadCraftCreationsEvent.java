package mak.dc.common.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;

public class DeadCraftCreationsEvent {

	@SubscribeEvent
	public void onStruckByLighting(EntityStruckByLightningEvent e) {
		World world = e.entity.worldObj;
		if (!world.isRemote) {
			if (e.entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) e.entity;
				ItemStack held = player.getHeldItem();
				if (held != null && held.getItem() instanceof ItemBlock) {
					Block block = Block.getBlockFromItem(held.getItem());
					if (Block.getIdFromBlock(block) == Block
							.getIdFromBlock(Blocks.diamond_block)) {
						ItemStack newStack = new ItemStack(Items.nether_star,
								0, held.stackSize);
						player.setCurrentItemOrArmor(0, newStack);
						player.attackEntityFrom(
								DamageSource.magic.setDamageIsAbsolute(),
								player.getHealth());
					}
				}

			}
		}
	}

}
