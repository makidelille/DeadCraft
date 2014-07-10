package mak.dc.common.items;

import java.util.List;

import mak.dc.common.entity.ai.EntityAIAvoidAPlayer;
import mak.dc.common.entity.ai.EntityAITemptMindController;
import mak.dc.common.util.Config.ConfigLib;
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

public class ItemMindController extends ItemWithPower {
    
    /** handle by config */
    private static int MAXCHARGE = ConfigLib.MIND_MAX;
    private static int CostForPassiveEntityUse = ConfigLib.MIND_C_PASS; 
    private static int CostForHostileEntityUse = ConfigLib.MIND_C_HOST;
     
    public ItemMindController() {
        super();
        setMaxStackSize(1);
        setNoRepair();
        setFull3D();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List info, boolean par4) {
    	super.addInformation(is, player, info, par4);
    	NBTTagCompound tag = is.getTagCompound();
        
        if (tag != null) {
            String plrName = tag.getString("player");
            if (plrName == "") {
                plrName = "none";
            }
            
            String ListInfo = EnumChatFormatting.AQUA + StatCollector.translateToLocal("dc.mindcontroller.info.creator") + " : " + EnumChatFormatting.ITALIC + plrName + EnumChatFormatting.RESET;
            
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                info.add(ListInfo);
            }
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                info.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.info.holdShift"));
            }
            
        } else {
            String listInfo = EnumChatFormatting.RED + "" + EnumChatFormatting.ITALIC + "" + EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("dc.mindcontroller.info.activate");
            info.add(listInfo);
            
        }
        
    }
    
    private boolean checkEntity(EntityLiving entLive) {
        return entLive.isCreatureType(EnumCreatureType.monster, true) && entLive instanceof EntityCreature && !(entLive instanceof IBossDisplayData);
    }
       
    private int getEntityCost(EntityLiving entLive) {
        float pv = entLive.getHealth();
        int armor = entLive.getTotalArmorValue();
        return (int) ((pv + armor)/10 * CostForHostileEntityUse);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return itemIcon;
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
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity ent) {
        if (ent != null && !ent.worldObj.isRemote) {
            if (!(ent instanceof EntityPlayer) && isUserCreator(stack, player)) {
            	this.tryToUse(stack, player.worldObj, ent, player);
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
	protected int getCost() {
		return 100;
	}

	@Override
	protected void use(ItemStack stack, World world, Entity ent, EntityPlayer player) {
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
            
            return;
        }
        if (checkEntity(entLive)) {
            System.out.println("new ai");
        	entLive.tasks.addTask(1, new EntityAIAvoidAPlayer((EntityCreature) entLive, player, 4F, 1.2D, 2D));
            player.worldObj.playSoundAtEntity(player, "fireworks.blast_far", 1f, 0.5f);
            return;
        }
        if (entLive instanceof EntitySlime ) {
            player.worldObj.spawnEntityInWorld(new EntityLightningBolt(entLive.worldObj, entLive.posX, entLive.posY, entLive.posZ));
        }
	}

	@Override
	protected EnumPowerUseProp getUseType() {
		return EnumPowerUseProp.ONUSE;
	}
}
