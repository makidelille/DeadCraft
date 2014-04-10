package mak.dc.util;

import java.util.ArrayList;
import java.util.List;

import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class NBTTagCompoundDeadCraft {

	public static boolean isDeadCraftCompound(NBTTagCompound tag) {
		if(!tag.hasKey("owner")) return false; 
		if(!tag.hasKey("locked")) return false;
		return true;
	}

	public static List getInfoList(NBTTagCompound tag) {
		ArrayList lis = new ArrayList();
		lis.add(0,tag.getString("owner"));
		lis.add(1,tag.getBoolean("locked"));
		if(tag.hasKey("nbAllowed")) {
			lis.add(2,tag.getInteger("nbAllowed"));
			int nb = tag.getInteger("nbAllowed");
			for(int i= 0; i < nb ; i++) {
				lis.add((3+i), tag.getString("allowed [" +i+ "]"));
			}
		}
		return lis;
	}

	public static List<String> getInfoListfordipslay(NBTTagCompound tag) {
		ArrayList lis = new ArrayList();
		lis.add(EnumChatFormatting.AQUA + "Creator : " +EnumChatFormatting.ITALIC + tag.getString("owner") + EnumChatFormatting.RESET);
		lis.add(EnumChatFormatting.DARK_BLUE + "State : " +EnumChatFormatting.BOLD + (tag.getBoolean("locked")? "locked" : "open") + EnumChatFormatting.RESET);
		if(tag.hasKey("nbAllowed") && tag.getInteger("nbAllowed") > 0 ) lis.add(EnumChatFormatting.GRAY +""+ EnumChatFormatting.UNDERLINE + "has an allowed list" + EnumChatFormatting.RESET);
		return lis;
	}
}
