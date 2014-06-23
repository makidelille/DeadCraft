package mak.dc.common.util;


public interface IPowerSender {
    
    public void delete();
    
    public int[] getCoord();
    
    public int getMaxTransfertRate();
    
    public void onAskedPower(IPowerReceiver te, int amount);
    
    public void sendPower();
}
