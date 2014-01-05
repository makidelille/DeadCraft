package mak.dc.items;

import java.util.List;

import mak.dc.lib.ItemInfo;
import mak.dc.lib.Textures;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMindController extends Item {

	private static int _maxCharge = 60000;
	private static int _maxDamage = 12000;		

	private static final String[] version =  new String[] {"passive","active","creative"};


	@SideOnly(Side.CLIENT)
	private Icon[] icons = {null,null,null};


	public ItemMindController(int id) {
		super(id);
		this.setUnlocalizedName(ItemInfo.MINDCONTROLLER_UNLOCALIZED_NAME);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setMaxDamage(0);
		this.setFull3D();
	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister) {
		for (int i = 0; i < version.length; i++ ) {
			icons[i] = iconRegister.registerIcon(Textures.MINDCONTROLLER_TEXT_LOC[i]); 
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int meta) {
		return icons[meta];
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack is,	EntityPlayer player, List info, boolean par4) {
		NBTTagCompound tag = is.getTagCompound();

		if (tag != null) {
			int dmg = tag.getInteger("damage");
			int charge = tag.getInteger("charge");
			String plrName = tag.getString("player");

			String ListInfo = EnumChatFormatting.AQUA + " creator : " +plrName + EnumChatFormatting.RESET;
			String listInfo1 = "duarbility : " +  dmg  + " / " + _maxDamage;
			String ListInfo2 = "charge : " + charge + "/"  + _maxCharge ;
			String ListInfo3 = (is.getItemDamage() == 1 ? EnumChatFormatting.GREEN : EnumChatFormatting.YELLOW ) +  "state: " + version[is.getItemDamage()] + "";

			info.add(ListInfo);
			info.add(listInfo1);
			info.add(ListInfo2);
			info.add(ListInfo3);

		}else {
			String listInfo = EnumChatFormatting.RED + "" + EnumChatFormatting.ITALIC + "" + EnumChatFormatting.UNDERLINE + "right click to activate";

			info.add(EnumChatFormatting.RED +  version[is.getItemDamage()]);
			info.add(listInfo);

		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World world,	EntityPlayer player) {
		if(!world.isRemote) {
			if(is.getItemDamage() == 0) {						
				NBTTagCompound newTag = new NBTTagCompound();

				newTag.setInteger("damage", _maxDamage);
				if(player.capabilities.isCreativeMode)
					newTag.setInteger("charge", 0); //TODO don't forget to change after test 
				else
					newTag.setInteger("charge", 0);
				newTag.setBoolean("creative", player.capabilities.isCreativeMode);
				newTag.setString("player", player.username);

				is.setTagCompound(newTag);					

				if(player.capabilities.isCreativeMode) {
					is.setItemDamage(2);
				}else{
					is.setItemDamage(1);	
				}
			}else if(player.isSneaking() && is.getItemDamage()!= 0)
				checkCrystal(player,is);
		}
		return is;
	}
	

	private void checkCrystal(EntityPlayer player, ItemStack is) {

		//TODO debug :(
		// refresh the render of the item icon.
		//check the NBTtag of the is. 

		if(!player.worldObj.isRemote) {
			System.out.println("test");
			NBTTagCompound tag = is.getTagCompound();
			if(tag == null)
				return;
			long charge = tag.getInteger("charge");
			int chargeEmpty = (int) (_maxCharge - charge);

			int i=0;
			IInventory inv = player.inventory;


			while(chargeEmpty > 0 && i < inv.getSizeInventory()) {
				chargeEmpty = (int) (_maxCharge - charge);
				ItemStack stack = inv.getStackInSlot(i);

				if(stack != null && stack.getItem() instanceof ItemLifeCrystal) {
					System.out.println("init : " + charge);
					System.out.println(i);
					int dmg = stack.getItemDamage();
					int chargeCrystal = ItemLifeCrystal._maxValue - dmg;
					System.out.println(dmg + " " + chargeCrystal);
					if(chargeCrystal > 0) {
						//TODO calcul des charges
						//Bug I don't understand what happened here
						//the controller does'nt want to charge properly :(
						System.out.println("left" + chargeEmpty);

						if(chargeCrystal >= chargeEmpty) {
							System.out.println("chargeCrystal >= chargeEmpty");

							chargeCrystal =- chargeEmpty;
							charge =+ chargeEmpty;

							stack.setItemDamage(ItemLifeCrystal._maxValue - chargeCrystal);
						}else if(chargeCrystal < chargeEmpty) {
							System.out.println("chargeCrystal < chargeEmpty");

							charge =+ chargeCrystal;
							System.out.println("en charge "  + charge);
							chargeCrystal = 0;
							stack.setItemDamage(ItemLifeCrystal._maxValue);

						}
						System.out.println("charge "  + charge);

					}
				}
				i++;
			}
			System.out.println(charge);
			System.out.println(tag);
			tag.setInteger("charge", (int) charge);
			tag.setBoolean("charge done", true);
			is.setTagCompound(tag);
			System.out.println(is.getTagCompound());
		}

	}
	
	
	

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity ent) {
		if(ent != null && !ent.worldObj.isRemote) {

			//TODO inserer code de pascification en fonction de la charge et augmenter dmg
			//TODO ainsi que mettre les blacklists et ajouter une identifiactaion de l'hostile
			EntityLiving entLive = (EntityLiving) ent;
			if(entLive.isCreatureType(EnumCreatureType.creature, true)) {
				entLive.tasks.addTask(0, new EntityAITempt((EntityCreature) entLive, 2D ,this.itemID, false));
			}
			
			
			if(stack.getItemDamage() != 0 && entLive.isCreatureType(EnumCreatureType.monster, true)) {
				entLive.targetTasks.taskEntries.clear();
				entLive.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget((EntityTameable) entLive));
			}
		}


		return false;
	}


	public int getMaxCharge() {
		return _maxCharge;
	}
}
