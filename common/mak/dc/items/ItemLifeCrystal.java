package mak.dc.items;

// TODO add the func to change the charge of crystal here and not in the other
// files /D

import mak.dc.lib.ItemInfo;
import mak.dc.lib.Textures;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLifeCrystal extends ItemFood {

    // private static boolean DEBUG = Lib.DEBUG;

    /** handle by config */

    public static int _crystalCost;
    public static int _maxValue;

    @SideOnly (Side.CLIENT)
    private Icon[]    icons = { null, null };

    public ItemLifeCrystal (int id) {
        super(id, 0, false);
        this.setMaxDamage(_maxValue);
        this.setUnlocalizedName(ItemInfo.LIFECRYSTAL_UNLOCALIZED_NAME);
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
        this.setAlwaysEdible();
        this.isDamageable();
    }

    @Override
    public boolean onItemUseFirst (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                for (int slot = 0; slot < player.inventory.getSizeInventory() - 1; slot++) {

                    ItemStack slotStack = player.inventory.getStackInSlot(slot);

                    if (slotStack != null && slotStack.getItem() instanceof ItemDeadWand) {

                        ItemStack wand = player.inventory.getStackInSlot(slot);
                        int wandDmg = wand.getMaxDamage() - wand.getItemDamage();
                        int dmg = stack.getItemDamage();

                        if (wandDmg > 0 && !isCharged(dmg)) {
                            chargeItem(stack, wandDmg);

                            ItemDeadWand wandItem = (ItemDeadWand) wand.getItem();
                            wandItem.resetStack(wand);
                        }
                        return true;
                    }
                }
            }

        }
        return false;
    }

    @Override
    public ItemStack onEaten (ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {

            ItemStack newStack = stack;
            int dmg = stack.getItemDamage();
            if (dmg < _crystalCost) {
                dischargeItem(newStack, _crystalCost);
                player.addChatMessage("you've been healed and feed");
                player.heal(500);
                player.getFoodStats().setFoodSaturationLevel(20);
                player.getFoodStats().setFoodLevel(20);
            }
            return newStack;
        }
        return stack;
    }

    @SideOnly (Side.CLIENT)
    @Override
    public void registerIcons (IconRegister registerIcon) {
        for (int i = 0; i < 2; i++)
            icons[i] = registerIcon.registerIcon(Textures.LIFECRYSTAL_TEXT_LOC[i]);
    }

    @SideOnly (Side.CLIENT)
    @Override
    public Icon getIconFromDamage (int dmg) {
        return dmg == _maxValue ? icons[0] : icons[1];
    }

    private boolean isCharged (int dmg) {
        return dmg == 0;
    }

    public void chargeItem (ItemStack stack, int chargeAmount) {
        stack.setItemDamage(stack.getItemDamage() - chargeAmount);
    }

    public void dischargeItem (ItemStack stack, int chargeAmount) {
        stack.setItemDamage(stack.getItemDamage() + chargeAmount);
    }

}
