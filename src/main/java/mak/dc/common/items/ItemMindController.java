package mak.dc.common.items;

import java.util.List;

import mak.dc.common.entity.ai.EntityAIAvoidAPlayer;
import mak.dc.common.entity.ai.EntityAITemptMindController;
import mak.dc.common.util.DamageSourceDeadCraft;
import mak.dc.common.util.Lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMindController extends Item {
    
    /** handle by config */
    private static int MAXCHARGE = 4000;
    private static int CostForPassiveEntityUse = 100;  // Config
    private static int CostForHostileEntityUse = 25;   // Config
    
    /**
     * @param stack
     * @param therorical Cost
     * @return if it can be taken of
     */
    private static boolean canDischargeItem(ItemStack stack, int cost) {
        if (!stack.hasTagCompound()) return false;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getBoolean("creativeSpawn")) return true;
        int curentCharge = tag.getInteger("charge");
        if (curentCharge <= 0) return false;
        if (curentCharge - cost < 0) return false;
        else return true;
    }
    
    /**
     * @param stack
     * @param chargeAmount
     * @return how much of the charge amount given is left after charge of the
     *         crystal
     */
    public static int chargeItem(ItemStack stack, int chargeAmount) {
        if (!stack.hasTagCompound()) return chargeAmount;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getBoolean("creative")) {
            tag.setInteger("charge", MAXCHARGE);
            stack.setTagCompound(tag);
            return 0;
        }
        int curentCharge = tag.getInteger("charge");
        if (curentCharge >= MAXCHARGE) return chargeAmount;
        if (curentCharge + chargeAmount > MAXCHARGE) {
            tag.setInteger("charge", MAXCHARGE);
            stack.setTagCompound(tag);
            return curentCharge + chargeAmount - MAXCHARGE;
        } else {
            tag.setInteger("charge", curentCharge + chargeAmount);
            stack.setTagCompound(tag);
            return 0;
        }
    }
    
    /**
     * @param stack
     * @param dischargeAmount
     * @return how much of the discharge amount given is left after discharge of
     *         the crystal
     */
    public static int dischargeItem(ItemStack stack, int dischargeAmount) {
        if (!stack.hasTagCompound()) return dischargeAmount;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getBoolean("creative")) {
            tag.setInteger("charge", MAXCHARGE);
            stack.setTagCompound(tag);
            return 0;
        }
        int curentCharge = tag.getInteger("charge");
        if (curentCharge <= 0) return dischargeAmount;
        if (curentCharge - dischargeAmount < 0) {
            tag.setInteger("charge", 0);
            stack.setTagCompound(tag);
            return dischargeAmount - curentCharge;
        } else {
            tag.setInteger("charge", curentCharge - dischargeAmount);
            stack.setTagCompound(tag);
            return 0;
        }
    }
    
    public static int getEmptyCharge(ItemStack stack) {
        if (!stack.hasTagCompound()) return 0;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getBoolean("creativeSpawn")) return 0;
        int curentCharge = tag.getInteger("charge");
        return MAXCHARGE - curentCharge;
        
    }
    
    public ItemMindController() {
        super();
        setMaxStackSize(1);
        setNoRepair();
        setFull3D();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List info, boolean par4) {
        NBTTagCompound tag = is.getTagCompound();
        
        if (tag != null) {
            int charge = tag.getInteger("charge");
            String plrName = tag.getString("player");
            if (plrName == "") {
                plrName = "none";
            }
            
            String ListInfo = EnumChatFormatting.AQUA + StatCollector.translateToLocal("dc.mindcontroller.info.creator") + " : " + EnumChatFormatting.ITALIC + plrName + EnumChatFormatting.RESET;
            String ListInfo2 = StatCollector.translateToLocal("dc.charge") + " : " + EnumChatFormatting.ITALIC + "" + EnumChatFormatting.YELLOW + (tag.getBoolean("creative") ? StatCollector.translateToLocal("dc.infinite") : charge + "/" + MAXCHARGE);
            
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                info.add(ListInfo);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && tag.getBoolean("creative")) {
                info.add(StatCollector.translateToLocal("dc.info.isCreative"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                info.add(ListInfo2);
            }
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                info.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.info.holdShift"));
            }
            
        } else {
            String listInfo = EnumChatFormatting.RED + "" + EnumChatFormatting.ITALIC + "" + EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("dc.mindcontroller.info.activate");
            info.add(listInfo);
            
        }
    }
    
    private void chargeController(EntityPlayer player, ItemStack is) {
        ItemStack cry = getCrystal(player);
        int chargeAmount = getEmptyCharge(is);
        int amountLeft = 0;
        while (cry != null && chargeAmount > 0) {
            amountLeft = ItemCrystal.dischargeItem(cry, chargeAmount);
            chargeItem(is, chargeAmount - amountLeft);
            cry = getCrystal(player);
            chargeAmount = getEmptyCharge(is);
        }
        
    }
    
    private boolean checkEntity(EntityLiving entLive) {
        return entLive.isCreatureType(EnumCreatureType.monster, true) && entLive instanceof EntityCreature && !(entLive instanceof IBossDisplayData);
    }
    
    private ItemStack getCrystal(EntityPlayer player) {
        ItemStack re = null;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.getItem() instanceof ItemCrystal) {
                if (ItemCrystal.isFullyCharged(is)) return is;
                if (ItemCrystal.isEmpty(is)) {
                    continue;
                }
                re = is;
            }
        }
        return re;
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return 0;
        return 1d - (double) tag.getInteger("charge") / MAXCHARGE;
    }
    
    private int getEntityCost(EntityLiving entLive) {
        float pv = entLive.getHealth();
        int armor = entLive.getTotalArmorValue();
        return (int) ((pv + armor) * CostForHostileEntityUse);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return itemIcon;
    }
    
    public int getMaxCharge() {
        return MAXCHARGE;
    }
    
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {
        ItemStack is = new ItemStack(item, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        
        tag.setBoolean("creative", false);
        tag.setInteger("charge", 0);
        is.setTagCompound(tag);
        l.add(is);
        
        is = new ItemStack(item, 1, 0);
        tag = new NBTTagCompound();
        tag.setBoolean("creative", false);
        tag.setInteger("charge", MAXCHARGE);
        is.setTagCompound(tag);
        l.add(is);
        
        is = new ItemStack(item, 1, 0);
        tag = new NBTTagCompound();
        tag.setBoolean("creative", true);
        tag.setInteger("charge", MAXCHARGE);
        is.setTagCompound(tag);
        l.add(is);
        
    }
    
    private boolean isUserCreator(ItemStack stack, EntityPlayer player) {
        if (player.isClientWorld()) {
            NBTTagCompound tag = stack.stackTagCompound;
            if (tag == null) return false;
            if (!tag.hasKey("player")) {
                tag.setString("player", player.getCommandSenderName());
                stack.setTagCompound(tag);
            }
            String user = tag.getString("player");
            if (user.equalsIgnoreCase(player.getCommandSenderName())) return true;
        }
        return false;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (!world.isRemote) {
            NBTTagCompound tag = is.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
                tag.setInteger("charge", 0);
                tag.setBoolean("creative", true);
            }
            if (!tag.hasKey("player")) {
                tag.setString("player", player.getCommandSenderName());
            }
            if (!isUserCreator(is, player)) return is;
            
            is.setTagCompound(tag);
            
        } else if (player.isSneaking()) {
            chargeController(player, is);
        }
        return is;
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity ent) {
        if (ent != null && !ent.worldObj.isRemote) {
            if (!(ent instanceof EntityPlayer) && isUserCreator(stack, player) && canDischargeItem(stack, CostForPassiveEntityUse)) {
                dischargeItem(stack, CostForPassiveEntityUse);
                EntityLiving entLive = (EntityLiving) ent;
                if (entLive.isCreatureType(EnumCreatureType.creature, true)) {
                    EntityAITemptMindController ai = new EntityAITemptMindController((EntityCreature) entLive, 2D, false, player);
                    boolean flag = true;
                    for (int i = 0; i < entLive.tasks.taskEntries.size(); i++) {
                        EntityAITaskEntry task = (EntityAITaskEntry) entLive.tasks.taskEntries.get(i);
                        if (task.action instanceof EntityAITemptMindController) {
                            entLive.tasks.removeTask(task.action);
                            player.worldObj.playSoundAtEntity(player, "mob.chicken.plop", 1.0F, 0.5f);
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        entLive.tasks.addTask(0, ai);
                        player.worldObj.playSoundAtEntity(player, "mob.chicken.plop", 1.0F, 5f);
                    }
                    
                    return true;
                }
                if (checkEntity(entLive) && canDischargeItem(stack, getEntityCost(entLive))) {
                    entLive.tasks.addTask(1, new EntityAIAvoidAPlayer((EntityCreature) entLive, player, 4F, 1.2D, 2D));
                    dischargeItem(stack, getEntityCost(entLive));
                    player.worldObj.playSoundAtEntity(player, "fireworks.blast_far", 1f, 0.5f);
                    return true;
                }
                if (entLive instanceof EntitySlime && canDischargeItem(stack, MAXCHARGE / 4)) {
                    player.worldObj.spawnEntityInWorld(new EntityLightningBolt(entLive.worldObj, entLive.posX, entLive.posY, entLive.posZ));
                }
            } else if (!isUserCreator(stack, player)) {
                player.worldObj.spawnEntityInWorld(new EntityLightningBolt(player.worldObj, player.posX, player.posY, player.posZ));
                player.attackEntityFrom(DamageSourceDeadCraft.lightning, 100F);
                player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + StatCollector.translateToLocal("dc.mindcontroller.useNotCreat") + EnumChatFormatting.RESET));
                return true;
            } else if (ent instanceof EntityPlayer || ent instanceof EntityVillager) {
                World world = player.worldObj;
                world.spawnEntityInWorld(new EntityLightningBolt(world, player.posX, player.posY, player.posZ));
                player.attackEntityFrom(DamageSourceDeadCraft.lightning, 100F);
                player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + StatCollector.translateToLocal("dc.mindcontroller.usePlayer") + EnumChatFormatting.RESET));
                return true;
            }
        }
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Textures.MINDCONTROLLER_TEXT_LOC);
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound() && !stack.getTagCompound().getBoolean("creative");
    }
}
