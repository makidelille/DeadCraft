package mak.dc.util;

import net.minecraft.tileentity.TileEntity;

public interface IPowerSender {
	
	public void sendPowerTo (IPowerReceiver te);
	public void onAskedPower(int amount);
	
}
