package mak.dc.items;

// TODO rework the functions

import java.util.List;

import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDeadWand extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons = { null, null };

    public ItemDeadWand () {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName(IBTInfos.ITEM_DEADWAND_UNLOCALIZED_NAME);
        this.setHasSubtypes(false);
        this.setMaxDamage(1000);

    }

    @Override
    public boolean onLeftClickEntity (ItemStack stack, EntityPlayer player, Entity entity) {
        if (!player.worldObj.isRemote && entity.isEntityAlive()) {

            float pv = ((EntityLivingBase) entity).getHealth();
            int dmg = stack.getItemDamage();
            boolean survivalMode = !player.capabilities.isCreativeMode;

            entity.attackEntityFrom(new EntityDamageSource("player", player), pv);

            if (!isCharged(dmg) && !hasPlayerCrystal(player, (int) pv) && survivalMode) {
                if (entity instanceof EntityPlayer) {
                    player.attackEntityFrom(DamageSource.magic, pv);
                } else {
                    player.attackEntityFrom(DamageSource.magic, pv / 2);
                }
            }

            if (!isCharged(dmg)) {
                if (!hasPlayerCrystal(player, (int) pv) && survivalMode) {

                    player.addPotionEffect(new PotionEffect(9, (int) pv * 10, (int) pv, false));
                    player.addPotionEffect(new PotionEffect(17, (int) pv * 10, (int) pv, false));

                } else {

                    ItemStack crystal = getCrystal(player);
                    crystal.setItemDamage((int) (crystal.getItemDamage() + pv));

                }
            } else if (isCharged(dmg)) {

                player.addPotionEffect(new PotionEffect(1, 1000, 5, false));
                player.addPotionEffect(new PotionEffect(3, 1000, 5, false));
                player.addPotionEffect(new PotionEffect(5, 1000, 5, false));
                player.addPotionEffect(new PotionEffect(8, 1000, 5, false));
                player.addPotionEffect(new PotionEffect(10, 1000, 5, false));
                stack.setItemDamage(getMaxDamage());
                return false;
            }

            if (!isCharged(dmg)) {
                stack.setItemDamage((int) (dmg - pv * 3 / 2));
            }

            return false;
        }
        return false;
    }

    @SideOnly (Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister registerIcon) {
        for (int i = 0; i < 2; i++)
            icons[i] = registerIcon.registerIcon(Textures.DEADWAND_TEXT_LOC[i]);

    }

    @SideOnly (Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int dmg) {
        return isCharged(dmg) ? icons[1] : icons[0];

    }

    @SideOnly (Side.CLIENT)
    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List info, boolean par4) {
        if (isCharged(stack.getItemDamage())) {
            info.add("wand is Charged ! ");
        } else {
            info.add("make " + (stack.getItemDamage()) + " damage to charge !");
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
        	
        }else{
        	info.add(EnumChatFormatting.YELLOW + "-- Press " +EnumChatFormatting.ITALIC + "Shift" +EnumChatFormatting.RESET + "" + EnumChatFormatting.YELLOW +  " for more Information --" );
        }
    }

    @Override
    public boolean isRepairable () {
        return false;
    }

    @Override
    public boolean isDamageable () {
        return true;
    }

    private boolean isCharged (int dmg) {
        return dmg == 0;
    }

    private boolean hasPlayerCrystal (EntityPlayer player, int pv) {
        InventoryPlayer inv = player.inventory;

        for (int slot = 0; slot < inv.getSizeInventory() - 1; slot++) {
            ItemStack slotStack = player.inventory.getStackInSlot(slot);

            if (slotStack != null && slotStack.getItem() instanceof ItemLifeCrystal) {
                ItemStack crystal = inv.getStackInSlot(slot);
                if (crystal.getItemDamage() < crystal.getMaxDamage() - pv) return true;
            }
        }
        return false;
    }

    private ItemStack getCrystal (EntityPlayer player) {
        InventoryPlayer inv = player.inventory;

        for (int slot = 0; slot < inv.getSizeInventory() - 1; slot++) {

            ItemStack slotStack = player.inventory.getStackInSlot(slot);

            if (slotStack != null && slotStack.getItem() instanceof ItemLifeCrystal) {
                ItemStack crystal = inv.getStackInSlot(slot);
                if (crystal.getItemDamage() < crystal.getMaxDamage()) return crystal;
            }
        }
        return null;
    }

    public void resetStack (ItemStack stack) {
        stack.setItemDamage(getMaxDamage());
    }
}
