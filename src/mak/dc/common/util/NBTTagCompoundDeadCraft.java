package mak.dc.common.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class NBTTagCompoundDeadCraft {
    
    public static List<String> getInfoListfordipslay(NBTTagCompound tag) {
        ArrayList lis = new ArrayList();
        lis.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("dc.block.owner") + " : " + EnumChatFormatting.ITALIC + tag.getString("owner") + EnumChatFormatting.RESET);
        lis.add(EnumChatFormatting.DARK_BLUE + StatCollector.translateToLocal("dc.block.lockState") + " : " + EnumChatFormatting.BOLD + (tag.getBoolean("locked") ? StatCollector.translateToLocal("dc.private") : StatCollector.translateToLocal("dc.public")) + EnumChatFormatting.RESET);
        if (tag.hasKey("nbAllowed") && tag.getInteger("nbAllowed") > 0) {
            lis.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("dc.block.info.hasAllowedList") + EnumChatFormatting.RESET);
        }
        return lis;
    }
    
    public static boolean isDeadCraftCompound(NBTTagCompound tag) {
        if (!tag.hasKey("owner")) return false;
        if (!tag.hasKey("locked")) return false;
        return true;
    }
}
