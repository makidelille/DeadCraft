package mak.dc.common.proxy;

import mak.dc.client.gui.GuiHandler;
import net.minecraft.world.World;

public class CommonProxy {
    
    public World getClientWorld() {
        return null;
    }
    
    public void init() {
        registerRender();
        registerTileEntityRender();
        registerSounds();
        new GuiHandler();
        
    }
    
    public void registerRender() {
        
    }
    
    public void registerSounds() {
        
    }
    
    public void registerTileEntityRender() {
    }
    
}
