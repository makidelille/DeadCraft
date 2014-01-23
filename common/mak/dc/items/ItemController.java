package mak.dc.items;

import java.util.ArrayList;
import java.util.List;

import mak.dc.DeadCraft;
import mak.dc.lib.ItemInfo;
import mak.dc.lib.Textures;
import mak.dc.network.PacketHandler;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemController extends Item {

    private static final String[] version = {"base","lock","info"};
    private Icon[] icons = {null,null,null};
    
    public ItemController (int id) {
        super(id);
        this.setUnlocalizedName(ItemInfo.CONTROLLER_KEY);
        this.setHasSubtypes(true);
    }

    

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack is, EntityPlayer player, List list, boolean par4) {
        // TODO

    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IconRegister iconRegister) {
        for(int i=0; i < version.length; i++)
        icons[i]= iconRegister.registerIcon(Textures.CONTROLLER_TEXT_LOC[i]);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage (int par1) {
        return par1 <= 2 && par1 >= 0 ? icons[par1] : icons[0];
    }

    @Override
    public void onUpdate (ItemStack is, World world, Entity ent, int par4, boolean par5) {
        super.onUpdate(is, world, ent, par4, par5);
        if (!world.isRemote) {
            if (!is.hasTagCompound()) {
                NBTTagCompound tag = new NBTTagCompound();
                is.setTagCompound(tag);
            }
        }

    }


    @Override
    public boolean onItemUseFirst (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote && world.blockHasTileEntity(x, y, z)
                && world.getBlockTileEntity(x, y, z) instanceof TileEntityDeadCraft) {
            TileEntityDeadCraft te = (TileEntityDeadCraft) world.getBlockTileEntity(x, y, z);
            String username = player.username;
            if (te.isUserCreator(username)) {
                switch (stack.getItemDamage()) {
                    case 0: // change autoratsation
                        FMLNetworkHandler.openGui(player, DeadCraft.instance, 0, world, x, y, z);
                        break;
                    case 1: // change stats (lock)
                        PacketHandler.sendInterfaceSwitchPacket((byte)0,(byte) 0, !(te.isLocked()));
                        player.addChatMessage("the block is now locked : " + te.isLocked());
                        break;
                    case 2: // show infos
                        break;
                    default:
                        return false;
                }
                return true;

            }else{
                player.addChatMessage("You're not the owner of the block");
            }
        }
        return false;
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

    public void onInterfaceEventRecieve () {

    }

}
