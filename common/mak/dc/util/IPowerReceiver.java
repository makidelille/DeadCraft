package mak.dc.util;

import java.util.List;

import cpw.mods.fml.relauncher.Side;

public interface IPowerReceiver {

	public void recievePower(int amount);
	public void askPower(int amount);
	public void setPowerSource(int[] coord);
	public void updatePowerSource(int[] sourceCoord);
	public List<IPowerSender> getPowerSource();
	public void setSourceChange();
	public void resetPowerSource();
	

	
}
