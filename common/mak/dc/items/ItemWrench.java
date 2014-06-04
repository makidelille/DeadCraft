package mak.dc.items;

import java.util.Arrays;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.blocks.BlockDeadCraft;
import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.util.IPowerReceiver;
import mak.dc.util.IPowerSender;
import mak.dc.util.Lib.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
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

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWrench extends Item {

    private static final String[] version = {StatCollector.translateToLocal("dc.wrench.info.name.base"),StatCollector.translateToLocal("dc.wrench.info.name.admin"),StatCollector.translateToLocal("dc.wrench.info.name.info")};
    private IIcon[] icons = {null,null,null};

    public ItemWrench () {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }


    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    	return hasBlockCoord(par1ItemStack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack is, EntityPlayer player, List list, boolean par4) {
    	if(this.hasBlockCoord(is)){
    		int[] coords = getBlockCoord(is);
    		String head = StatCollector.translateToLocal("dc.wrench.info.coord.header") + " :";
    		String coord = "x: " + coords[0] + ", " +"y: " + coords[1] + ", " + "z: " + coords[2] ;
    		

    		list.add(head);
    		list.add(coord);
    		
    	}
    	if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
    		list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.info.holdShift"));
    	}
    	if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
	    	String infos0 = EnumChatFormatting.GREEN +StatCollector.translateToLocal("dc.wrench.info.change");
	    	list.add(infos0);
	    	
	    	
    	}
    	if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
    		String infos1 = EnumChatFormatting.GRAY + StatCollector.translateToLocal("dc.wrench.info.wrenchMode") + " : "+EnumChatFormatting.YELLOW + version[is.getItemDamage()];
	        list.add(infos1);
	        String infos2 = getDescription(is.getItemDamage());
			List<String> strs = Arrays.asList(infos2.split("\n"));
			for(int i= 0; i < strs.size(); i++) {
				list.add(strs.get(i));
			}
		}
       
        
      
    }

    @SideOnly(Side.CLIENT)
    private String getDescription (int itemDamage) {
        String re = "";
        switch(itemDamage) {
            case 0 :
                re = EnumChatFormatting.YELLOW+ StatCollector.translateToLocal("dc.wrench.info.ifAllowed")  +" :\n-" + StatCollector.translateToLocal("dc.wrench.info.doBasics");
                break;
            case 1 :
                re = EnumChatFormatting.RED + StatCollector.translateToLocal("dc.wrench.info.ifAdmin")+" :\n-" + StatCollector.translateToLocal("dc.wrench.info.editSec");
                break;
            case 2:
                re = EnumChatFormatting.RED +StatCollector.translateToLocal("dc.wrench.info.ifAdmin") + " :\n-" + StatCollector.translateToLocal("dc.wrench.info.seeSec") + "\n" +
                		EnumChatFormatting.YELLOW+StatCollector.translateToLocal("dc.wrench.info.ifAllowed") + " :\n-"+ StatCollector.translateToLocal("dc.wrench.info.seeInfo");
                break;
                default : break;
        }
        return re;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister) {
        for(int i=0; i < version.length; i++)
            icons[i]= iconRegister.registerIcon(Textures.CONTROLLER_TEXT_LOC[i]);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int par1) {
        return par1 <= 2 && par1 >= 0 ? icons[par1] : icons[0];
    }
    


    @Override
    public boolean onItemUseFirst (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
            String username = player.getCommandSenderName();
            if (!player.isSneaking()) {
                switch (stack.getItemDamage()) {
                    case 0:
                    	if(world.getBlock(x, y, z) instanceof BlockDeadCraft && te.isUserAllowed(username)) {
                    		if(te instanceof IPowerReceiver) {
                    			((IPowerReceiver) te).updatePowerSource(this.getBlockCoord(stack));
                    			this.removeBlockCoord(stack);
                    		}else if(te instanceof IPowerSender) {
                    			this.saveBlockCoord(stack, x, y, z);                    		}
                    		if(player.isSneaking()) ((BlockDeadCraft)world.getBlock(x, y, z)).onWrenched(world, x, y, z,player, side, hitX, hitY, hitZ);
                    	}
                        break;
                    case 1: 
                    	if(te.isUserCreator(username)) FMLNetworkHandler.openGui(player, DeadCraft.instance, 0, world, x, y, z);
                        break;
                    case 2: 
                        showData(te,player);
                        if(te.isUserCreator(username)) showAdminData(te, player);
                        break;
                    default:
                        return false;
                }
                return true;
                
            }else if(player.isSneaking()){
                stack.setItemDamage(stack.getItemDamage() < 2 ? (stack.getItemDamage() + 1) : 0);
            }else{
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.block.notOwner")));
            }
        }
        return false;
    }

    private void showAdminData (TileEntityDeadCraft te, EntityPlayer player) {
    	player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "====" + StatCollector.translateToLocal("dc.wrench.header.owner") +"===="));
    	player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.block.owner") + " : " + te.getowner()));
    	player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.block.lockState") + " : " + (te.isLocked()  ? StatCollector.translateToLocal("dc.private") : StatCollector.translateToLocal("dc.public"))));
    	player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.block.allowed") + " : " + (te.getAllowedUser().size() > 0 ? te.getAllowedUser().toString() : StatCollector.translateToLocal("dc.none"))));
    	
    }
    
    private void showData(TileEntityDeadCraft te, EntityPlayer player) {
    	player.addChatComponentMessage(new ChatComponentText(""));
    	player.addChatComponentMessage(new ChatComponentText(""));

    	player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA  + "" +EnumChatFormatting.BOLD + te.getBlockType().getLocalizedName()));
    	player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW  +"====" + StatCollector.translateToLocal("dc.wrench.header.allowed") +"===="));

    	List<String> infos = te.getInfo();
    	if(infos == null || infos.size() == 0) return;
    	for(int i = 0; i< infos.size(); i++) {
    		player.addChatComponentMessage(new ChatComponentText(infos.get(i)));
    	}
    }



    @Override
    public ItemStack onItemRightClick (ItemStack is, World world, EntityPlayer player) {
        if(!world.isRemote && player.isSneaking()) {
            is.setItemDamage(is.getItemDamage() < 2 ? (is.getItemDamage() + 1) : 0);
        }else if(!world.isRemote) {
        	if(hasBlockCoord(is)) removeBlockCoord(is);
        }
        return is;
    }
    public static boolean hasBlockCoord(ItemStack is) {
    	NBTTagCompound tag = is.getTagCompound();
    	return tag == null ? false : tag.hasKey("coord");
    }
    
    public static void removeBlockCoord(ItemStack is) {
    	NBTTagCompound tag = is.getTagCompound();
    	if(tag == null) return;
    	if(tag.hasKey("coord")) tag.removeTag("coord");
    	is.setTagCompound(tag);
    }
    
    
    public static void saveBlockCoord(ItemStack is, int x, int y, int z) {
    	NBTTagCompound coord = new NBTTagCompound();
    	NBTTagCompound tag = is.getTagCompound();
    	if(tag == null) {
    		tag = new NBTTagCompound();
    	}if(tag.hasKey("coord")) {
    		tag.removeTag("coord");
    	}
    	coord.setInteger("x", x);
    	coord.setInteger("y", y);
    	coord.setInteger("z", z);
    	
    	tag.setTag("coord", coord);
    	
    	is.setTagCompound(tag);
    }
    
    public static int[] getBlockCoord(ItemStack is) {
    	int[] re = new int[3];
    	NBTTagCompound tag = is.getTagCompound();
    	if(tag == null || !tag.hasKey("coord")) return re;
    	NBTTagCompound coord = tag.getCompoundTag("coord");
    	
    	re[0] = coord.getInteger("x");
    	re[1] = coord.getInteger("y");
    	re[2] = coord.getInteger("z");
    	
    	return re;
    }
}
