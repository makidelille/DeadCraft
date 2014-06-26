package mak.dc;

import mak.dc.client.events.DeadCraftClientEvent;
import mak.dc.common.blocks.DeadCraftBlocks;
import mak.dc.common.event.DeadCraftCreationsEvent;
import mak.dc.common.event.DeadCraftEvents;
import mak.dc.common.items.DeadCraftItems;
import mak.dc.common.util.Config;
import mak.dc.common.util.Lib;
import mak.dc.common.util.PowerManager;
import mak.dc.common.util.cans.CanCraftingManager;
import mak.dc.network.pipeline.PacketPipeline;
import mak.dc.network.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Lib.MOD_ID, name = Lib.MOD_NAME, version = Lib.MOD_VERSION)
public class DeadCraft {
    
    @SidedProxy(clientSide = Lib.PROXY_CLIENT, serverSide = Lib.PROXY_SERVER)
    public static CommonProxy proxy;
    
    @Instance("deadcraft")
    public static DeadCraft instance;
    
    public static final Logger logger = LogManager.getLogger("DeadCraft");
    
    public static final PacketPipeline packetPipeline = new PacketPipeline();
    public static final CanCraftingManager canCraftingManager = new CanCraftingManager();
    public static final PowerManager powerManager = new PowerManager();
    public static Config config;
    
    @EventHandler
    public void Init(FMLInitializationEvent event) {
        /**
         * register world gen register entities register crafting register tile
         * entities register guiHandler
         */
        DeadCraftItems.registerCraftRecipe();
        DeadCraftBlocks.initTileEntity();
        DeadCraftBlocks.registerCraftRecipe();
        packetPipeline.initialise();
        canCraftingManager.initialise();
        powerManager.initialise();
        
        
    }
    
    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {
        DeadCraftItems.postInit();
        packetPipeline.postInitialise();
        canCraftingManager.postInitialise();
        MinecraftForge.EVENT_BUS.register(new DeadCraftEvents());
        MinecraftForge.EVENT_BUS.register(new DeadCraftCreationsEvent());
        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new DeadCraftClientEvent());
        }
        logger.log(Level.INFO, "DeadCraft is loaded");
    }
    
    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
        /**
         * Register blocs register configuration register item register
         * achievement register Render register sounds
         */
        
    	config = new Config(event.getSuggestedConfigurationFile());
    	
        DeadCraftItems.init();
        
        DeadCraftBlocks.init();
        
        proxy.init();
        
        
        config.save();
    }
}
