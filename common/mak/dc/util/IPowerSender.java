package mak.dc.util;

import net.minecraft.tileentity.TileEntity;

public interface IPowerSender {
	
	public void sendPower();
	public void onAskedPower(IPowerReceiver te, int amount);
	
}
