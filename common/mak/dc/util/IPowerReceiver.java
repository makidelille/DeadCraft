package mak.dc.util;

public interface IPowerReceiver {

	public void recievePower(int amount);
	public void askPower(IPowerSender te, int amount);
	
}
