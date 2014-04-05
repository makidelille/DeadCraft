package mak.dc.util;

import mak.dc.blocks.BlockDeadCraft;
import mak.dc.tileEntities.TileEntityDeadCraft;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTagCompoundDeadCraft extends NBTTagCompound {

	
	
	public NBTTagCompoundDeadCraft(TileEntityDeadCraft te) {
		super();
		te.writeToNBT(this);
	}
}
