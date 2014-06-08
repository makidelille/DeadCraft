package mak.dc.util;

import net.minecraft.entity.player.EntityPlayer;

public interface IPowerSender {
	
	public void sendPower();
	public void onAskedPower(IPowerReceiver te, int amount);
	public int[] getCoord();
	public void delete();
	public int getMaxTransfertRate();
}
