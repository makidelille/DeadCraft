package mak.dc.items;

import java.util.ArrayList;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.lib.IBTInfos;
import mak.dc.lib.Textures;
import mak.dc.network.DeadCraftAdminPacket;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemController extends Item {

    private static final String[] version = {"base","lock","info"};
    private IIcon[] icons = {null,null,null};

    public ItemController () {
        super();
        this.setUnlocalizedName(IBTInfos.ITEM_CONTROLLER_KEY);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }



    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack is, EntityPlayer player, List list, boolean par4) {
        String[] infos = {"","",""};
        infos[0] = EnumChatFormatting.UNDERLINE + "shift right click to change state";
        infos[1] = EnumChatFormatting.RESET + "wrench mode : " +EnumChatFormatting.ITALIC + "" +  (is.getItemDamage() == 0 ? "basic" : (is.getItemDamage() == 1 ? "lock" : (is.getItemDamage() == 2? "infos" : EnumChatFormatting.RED + "should not exist")));
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) infos[2] = getDescription(is.getItemDamage());
        
       
        
        for(int i=0; i<infos.length;i++){
            list.add(infos[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    private String getDescription (int itemDamage) {
        String re = "";
        switch(itemDamage) {
            case 0 :
                re = "you can change propeties of the block";
                break;
            case 1 :
                re = "you can lock the block";
                break;
            case 2:
                re  = "you get infos on the block";
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
    public boolean onItemUseFirst (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote && world.getTileEntity(x, y, z) != null
                && world.getTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getTileEntity(x, y, z);
            String username = player.getCommandSenderName();
            if (te.isUserCreator(username) && !player.isSneaking()) {
                switch (stack.getItemDamage()) {
                    case 0:
                        FMLNetworkHandler.openGui(player, DeadCraft.instance, 0, world, x, y, z);
                        break;
                    case 1: 
                    	DeadCraft.packetPipeline.sendToServer(new DeadCraftAdminPacket(x,y,z,te.getAllowedUser(),!te.isLocked()));
                        break;
                    case 2: 
                        showData(te,player);
                        break;
                    default:
                        return false;
                }
                return true;

            }else if(player.isSneaking()){
                stack.setItemDamage(stack.getItemDamage() < 2 ? (stack.getItemDamage() + 1) : 0);
            }else{
                showData(te,player);
                player.addChatComponentMessage(new ChatComponentText("You're not the owner of the block"));
            }
        }
        return false;
    }

    private void showData (TileEntityDeadCraft te, EntityPlayer player) {
        System.out.println("owner : " + te.getowner());
        System.out.println("state "  + (te.isLocked()  ? "private"  :"public"));
        System.out.println("allowed users : "  + te.getAllowedUser());
    }



    @Override
    public ItemStack onItemRightClick (ItemStack is, World world, EntityPlayer player) {
        if(!world.isRemote && player.isSneaking()) {
            is.setItemDamage(is.getItemDamage() < 2 ? (is.getItemDamage() + 1) : 0);
        }

        return is;
    }


    public ArrayList getAllowedList (ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        ArrayList allowed = new ArrayList();
        if (tag != null) {
            int nbersAll = tag.getInteger("nbAlllowed");
            NBTTagCompound tagAllowed = tag.getCompoundTag("allowed");
            for (int i = 0; i < nbersAll; i++) {

                allowed.add(i, tagAllowed.getString("allowed " + i));
            }
        }
        return allowed;
    }

    public String addUser (ItemStack is) {
        String re = "";
        NBTTagCompound tag = is.getTagCompound();
        if (tag != null) re = tag.getString("lastEntry");
        return re;
    }

    public void setName (ItemStack is, String s) {
        System.out.println(is);
        NBTTagCompound tag = is.getTagCompound();
        if (tag != null) {
            tag.setString("lastEntry", s); // TODO ??
            is.setTagCompound(tag);
        }
        System.out.println(is.getTagCompound());
    }
}
