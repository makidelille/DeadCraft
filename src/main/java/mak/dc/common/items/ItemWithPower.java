package mak.dc.common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import mak.dc.common.util.Lib;
import mak.dc.common.util.Lib.GuiLib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public abstract class ItemWithPower extends Item {
	protected static int discharge;
	protected enum EnumPowerUseProp {
		TICK(0), ONUSE(1), NULL(-1);

		private int id;

		private EnumPowerUseProp(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}

		public static EnumPowerUseProp getMatchingId(int id) {
			for (EnumPowerUseProp prop : EnumPowerUseProp.values()) {
				if (prop.getID() == id)
					return prop;
			}
			return NULL;
		}

		public static void writeProp(NBTTagCompound tag, EnumPowerUseProp prop) {
			NBTTagCompound propTag = tag.hasKey("prop") ? tag
					.getCompoundTag("prop") : new NBTTagCompound();
			propTag.setByte("power_use_type", (byte) prop.id);
			tag.setTag("prop", propTag);
		}

		public static EnumPowerUseProp getProp(NBTTagCompound tag) {
			if (tag == null
					|| !tag.hasKey("prop")
					|| !((NBTTagCompound) tag.getTag("prop"))
							.hasKey("power_use_type"))
				return NULL;
			return getMatchingId(((NBTTagCompound) tag.getTag("prop"))
					.getInteger("power_use_type"));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity ent, int slot,
			boolean par5) {
		super.onUpdate(stack, world, ent, slot, par5);
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		EnumPowerUseProp prop = EnumPowerUseProp
				.getProp(stack.getTagCompound());
		if (prop.equals(EnumPowerUseProp.TICK)) {
				use(stack, world, ent, (EntityPlayer) ent);
		} else if (prop.equals(EnumPowerUseProp.NULL)) {
			EnumPowerUseProp.writeProp(stack.getTagCompound(), getUseType());
		}
	}

	protected boolean discharge(ItemStack item,int amount) {
		if(!hasCrystal(item)) return false;
		ItemStack crys = getCrystal(item);
		if(ItemCrystal.canDischarge(crys, amount)) {
			ItemCrystal.dischargeItem(crys, amount);
			setCrystal(item, crys);
			return true;
		}
		return false;
	}
	
	/** method called on using the item energie.
	 * you have to call discharge(stack, amount) in it before making any action
	 * 
	 * @param stack
	 * @param world
	 * @param ent
	 * @param player
	 */
	protected abstract void use(ItemStack stack, World world, Entity ent,
			EntityPlayer player);
	protected abstract EnumPowerUseProp getUseType();

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player) {
		if (!world.isRemote && player.isSneaking()) {
			FMLNetworkHandler.openGui(player, Lib.MOD_ID,
					GuiLib.ID_INV_POWERITEM, world, (int) player.posX,
					(int) player.posY, (int) player.posZ);
		}
		return stack;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (hasCrystal(stack)) {
			ItemStack crys = getCrystal(stack);
			return 1d - (double) ItemCrystal.getCharge(crys)
					/ ItemCrystal.getMaxCharge(crys);
		}
		return 1d;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list,
			boolean par4) { //TODO add colors
		if(hasCrystal(stack)) {
			ItemStack crys = getCrystal(stack);
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if(ItemCrystal.isCreative(crys)) {
					list.add("creative spawned");
					list.add("charge : infinite");
				}else{
					list.add("charge : " + ItemCrystal.getCharge(crys) + "/" + ItemCrystal.getMaxCharge(crys));
					list.add("crystal tier : " + ItemCrystal.tiers[getCrystal(stack).getItemDamage()]);
				}
			}else{
				if(ItemCrystal.isCreative(crys))
					list.add("charge : infinite");
				else
					list.add("charge : " + ItemCrystal.getCharge(crys) + "/" + ItemCrystal.getMaxCharge(crys));
				
			}
		}else{
			list.add("no power source");
		}
	}

	public static void setCrystal(ItemStack item, ItemStack crys) {
		if (crys == null || !(crys.getItem() instanceof ItemCrystal)) {
			if (item.getTagCompound().hasKey("crystal"))
				item.getTagCompound().removeTag("crystal");
			return;
		}
		NBTTagCompound cryTag = crys.writeToNBT(new NBTTagCompound());
		item.getTagCompound().setTag("crystal", cryTag);
	}

	public static ItemStack getCrystal(ItemStack item) {
		if (hasCrystal(item)) {
			return ItemStack.loadItemStackFromNBT((NBTTagCompound) item
					.getTagCompound().getTag("crystal"));
		}
		return null;
	}

	public static boolean hasCrystal(ItemStack item) {
		if (item == null)
			return false;
		NBTTagCompound tag = item.getTagCompound();
		if (tag == null) {
			item.setTagCompound(new NBTTagCompound());
			return false;
		}
		return tag.hasKey("crystal") && tag.getTag("crystal") != null;
	}

}