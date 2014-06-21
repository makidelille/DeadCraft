package mak.dc.common.util;

import java.util.List;

public interface IPowerReceiver {
    
    public void askPower(int amount);
    
    public List<IPowerSender> getPowerSource();
    
    public void recievePower(int amount);
    
    public void resetPowerSource();
    
    public void setPowerSource(int[] coord);
    
    public void setSourceChange();
    
    public void updatePowerSource(int[] sourceCoord);
    
}
