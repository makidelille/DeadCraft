package mak.dc.items;

//TODO handle dmg of the stack

import java.util.List;

import mak.dc.entity.ai.EntityAIAvoidAPlayer;
import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import mak.dc.util.DamageSourceDeadCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMindController extends Item {

    /** handle by config */
    private static int                 _maxCharge        = 60000;
    private static int                 _maxDamage        = 12000;
    private static int                 _costForEntityUse = 100; //Config
    private static final String[]      version           = new String[] { "passive", "active", "creative" };

    @SideOnly (Side.CLIENT)
    private IIcon[]                     icons             = { null, null, null };

    public ItemMindController () {
        super();
        this.setUnlocalizedName(IBTInfos.ITEM_MINDCONTROLLER_UNLOCALIZED_NAME);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setFull3D();
    }

    @SideOnly (Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister) {
        for (int i = 0; i < version.length; i++) {
            icons[i] = iconRegister.registerIcon(Textures.MINDCONTROLLER_TEXT_LOC[i]);
        }
    }

    @SideOnly (Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int meta) {
        return icons[meta];
    }

    @SideOnly (Side.CLIENT)
    @Override
    public void addInformation (ItemStack is, EntityPlayer player, List info, boolean par4) { //TODO add more beauty to info panel
        NBTTagCompound tag = is.getTagCompound();

        if (tag != null) {
            int dmg = tag.getInteger("damage");
            int charge = tag.getInteger("charge");
            String plrName = tag.getString("player");

            String ListInfo = EnumChatFormatting.AQUA + "Creator : " +EnumChatFormatting.ITALIC +  plrName + EnumChatFormatting.RESET;
            String listInfo1 = "duarbility : " + EnumChatFormatting.ITALIC + "" + dmg + " / " + _maxDamage;
            String ListInfo2 = "charge : " + EnumChatFormatting.ITALIC + "" + charge + "/" + _maxCharge;
            String ListInfo3 =  (is.getItemDamage() == 1 ? EnumChatFormatting.GREEN : EnumChatFormatting.YELLOW)
                    + "state: " + EnumChatFormatting.ITALIC + "" + version[is.getItemDamage()] + "";

            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) info.add(ListInfo);
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) info.add(listInfo1);
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) info.add(ListInfo2);
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) info.add(ListInfo3);
            if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) info.add(EnumChatFormatting.YELLOW +" -- Press Shift for info --");

        } else {
            String listInfo = EnumChatFormatting.RED + "" + EnumChatFormatting.ITALIC + ""
                    + EnumChatFormatting.UNDERLINE + "right click to activate";

            info.add(listInfo);

        }
    }

    @Override
    public ItemStack onItemRightClick (ItemStack is, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (is.getItemDamage() == 0) {
                NBTTagCompound newTag = new NBTTagCompound();
                newTag.setInteger("damage", _maxDamage);
                if (player.capabilities.isCreativeMode) newTag.setInteger("charge", _maxCharge);
                else newTag.setInteger("charge", 0);
                newTag.setBoolean("creative", player.capabilities.isCreativeMode);
                newTag.setString("player", player.getCommandSenderName());

                is.setTagCompound(newTag);

                if (player.capabilities.isCreativeMode) {
                    is.setItemDamage(2); // TODO change after test
                } else {
                    is.setItemDamage(1);
                }
            } else if (player.isSneaking() && is.getItemDamage() != 0) checkCrystal(player, is);
        }
        return is;
    }

    private void checkCrystal (EntityPlayer player, ItemStack is) {
        // TODO work on the refresh of the icons BUG

        if (!player.worldObj.isRemote) {
            NBTTagCompound tag = is.getTagCompound();
            if (tag == null) return;
            if (is.getItemDamage() == 2) {
                tag.setInteger("charge", _maxCharge);
                is.setTagCompound(tag);
                return;
            }
            int charge = tag.getInteger("charge");
            int chargeEmpty = _maxCharge - charge;

            int i = 0;
            IInventory inv = player.inventory;

            while (chargeEmpty > 0 && i < inv.getSizeInventory()) {
                chargeEmpty = _maxCharge - charge;
                ItemStack stack = inv.getStackInSlot(i);

                if (stack != null && stack.getItem() instanceof ItemLifeCrystal) {
                    ItemLifeCrystal crsytalItem = (ItemLifeCrystal) stack.getItem();
                    int dmg = stack.getItemDamage();
                    int chargeCrystal = ItemLifeCrystal._maxValue - dmg;
                    if (chargeCrystal > 0) {
                        crsytalItem.dischargeItem(stack, chargeCrystal);
                        chargeCrystal = chargeItem(is, chargeCrystal);
                    }

                }
                charge = tag.getInteger("charge");
                i++;
            }
//          inv..onInventoryChanged();
        }

    }

    @Override
    public boolean onLeftClickEntity (ItemStack stack, EntityPlayer player, Entity ent) {
        if (ent != null && !ent.worldObj.isRemote) {
            if (!(ent instanceof EntityPlayer) && isUserCreator(stack, player)) {
                EntityLiving entLive = (EntityLiving) ent;
                if (entLive.isCreatureType(EnumCreatureType.creature, true)) {
                    entLive.tasks.addTask(0, new EntityAITempt((EntityCreature) entLive, 2D, this, false));
                    return true;
                }
                if (stack.getItemDamage() != 0 && checkEntity(entLive) && dischargeItem(stack, costEntity(entLive))) {
                    entLive.tasks.addTask(1, new EntityAIAvoidAPlayer((EntityCreature) entLive, player, 4F, 1.2D, 2D));
                    return true;
                }
            } else if (!isUserCreator(stack, player)) {
                player.worldObj.spawnEntityInWorld(new EntityLightningBolt(player.worldObj, player.posX, player.posY,
                        player.posZ));
                player.attackEntityFrom(DamageSourceDeadCraft.lightning, 100F);
//                player.addChatMessage((EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD
//                        + "do not use the items of the others" + EnumChatFormatting.RESET));
                return true;
            } else if (ent instanceof EntityPlayer || ent instanceof EntityVillager) {
                World world = player.worldObj;
                world.spawnEntityInWorld(new EntityLightningBolt(world, player.posX, player.posY, player.posZ));
                player.attackEntityFrom(DamageSourceDeadCraft.lightning, 100F);
//                player.addChatMessage((EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD
//                        + "do not use the mindController on people" + EnumChatFormatting.RESET));
                return true;
            }
        }

        return false;
    }

    private int costEntity (EntityLiving entLive) {
        float pv = entLive.getHealth();
        int armor = entLive.getTotalArmorValue();
        return (int) ((pv + armor) * _costForEntityUse);
    }

    /** amount should be positive or it will charge the item */
    private boolean dischargeItem (ItemStack stack, int amount) {
        if (stack.getItemDamage() == 2) return true;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            int charge = tag.getInteger("charge");
            if (charge <= amount) return false;
            charge -= amount;
            tag.setInteger("charge", charge);
            System.out.println(tag);
            stack.setTagCompound(tag);
            return true;
        }
        return false;
    }

    private int chargeItem (ItemStack stack, int amount) {
        NBTTagCompound tag = stack.getTagCompound();
        int re = 0;
        if (tag != null) {
            int charge = tag.getInteger("charge");
            if (charge + amount > _maxCharge) {
                charge = _maxCharge;
                re = charge + amount - _maxCharge;
            } else {
                charge += amount;
            }
            tag.setInteger("charge", charge);
            stack.setTagCompound(tag);
        }
        return re;
    }

    private boolean isUserCreator (ItemStack stack, EntityPlayer player) {
        if (!player.isClientWorld()) {
            NBTTagCompound tag = stack.stackTagCompound;
            if (tag == null || stack.getItemDamage() == 0) return false;
            String user = tag.getString("player");
            System.out.println(user);
            if (user.equalsIgnoreCase(player.getCommandSenderName())) return false;
        }
        return true;
    }

    private boolean checkEntity (EntityLiving entLive) {
        return entLive.isCreatureType(EnumCreatureType.monster, true) && entLive instanceof EntityCreature && !(entLive instanceof IBossDisplayData);
    }

    public int getMaxCharge () {
        return _maxCharge;
    }
}
