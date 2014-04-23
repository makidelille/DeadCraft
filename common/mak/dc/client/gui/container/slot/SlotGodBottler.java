package mak.dc.client.gui.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotGodBottler extends Slot {

	private byte i;

	public SlotGodBottler(IInventory par1iInventory, int par2, int par3,int par4) {
		super(par1iInventory, par2, par3, par4);
		this.i = (byte) par2;
	}

}
