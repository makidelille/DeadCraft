package mak.dc.items;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGodCan extends Item{
	
	private enum EFFECTS {
		INACTIVE(-1),
		FLY(0),
		ENV_INVUNERABILITY(1),
		MON_INVULNERABILITY(2);
		
		private int id;
		
		private EFFECTS(int id) {
			this.id = id;
		}
		
		private ItemStack createIS(ItemStack is) {
			is.setItemDamage(id);			
			return is;
		}
		
		private void addEffect(EntityPlayer player) {
			
		}
		
		
	}
	
	public ItemGodCan(){
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(3);
		this.setNoRepair();
		
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
		if(!world.isRemote) {
			
		}
		
		return is;
	}

}
