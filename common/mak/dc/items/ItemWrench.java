package mak.dc.items;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.blocks.BlockDeadCraft;
import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.tileEntities.TileEntityEggSpawner;
import mak.dc.tileEntities.TileEntityGodBottler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentStyle;
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



    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack is, EntityPlayer player, List list, boolean par4) {
    	if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("dc.info.holdShift"));
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
                    	if(world.getBlock(x, y, z) instanceof BlockDeadCraft && te.isUserAllowed(username)) ((BlockDeadCraft)world.getBlock(x, y, z)).onWrenched(world, x, y, z, side, hitX, hitY,hitZ);
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

    	if(te instanceof TileEntityGodBottler) {
    		if(((TileEntityGodBottler) te).isTop()) player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY +"" +EnumChatFormatting.ITALIC + StatCollector.translateToLocal("dc.wrench.isTop")));
    		player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.power")+ " : " + ((TileEntityGodBottler) te).getPower() + "/" +  ((TileEntityGodBottler) te).MAXPOWER));
    		player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.wrench.redstone")+ " : " + (((TileEntityGodBottler) te).isRSPowered() ? (EnumChatFormatting.GREEN + StatCollector.translateToLocal("dc.true")) : (EnumChatFormatting.RED + StatCollector.translateToLocal("dc.false")) )));
    	}
    	if(te instanceof TileEntityEggSpawner) {
    		player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.power")+ " : " + ((TileEntityEggSpawner) te).getPower() + "/" +  ((TileEntityEggSpawner) te).MAXPOWER));
    		player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dc.progress")+ " : " + ((TileEntityEggSpawner) te).getProgress() + "%"));
    	}
    }



    @Override
    public ItemStack onItemRightClick (ItemStack is, World world, EntityPlayer player) {
        if(!world.isRemote && player.isSneaking()) {
            is.setItemDamage(is.getItemDamage() < 2 ? (is.getItemDamage() + 1) : 0);
        }
        return is;
    }
}
