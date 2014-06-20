package mak.dc;

import mak.dc.common.blocks.DeadCraftBlocks;
import mak.dc.common.event.DeadCraftClientEvent;
import mak.dc.common.event.DeadCraftEvents;
import mak.dc.common.items.DeadCraftItems;
import mak.dc.common.network.PacketPipeline;
import mak.dc.common.proxy.CommonProxy;
import mak.dc.common.util.Lib;
import mak.dc.common.util.PowerManager;
import mak.dc.common.util.cans.CanCraftingManager;
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
    
    @SidedProxy(clientSide = "mak.dc.common.proxy.ClientProxy", serverSide = "mak.dc.common.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @Instance("deadcraft")
    public static DeadCraft instance;
    
    public static final Logger logger = LogManager.getLogger("DeadCraft");
    
    public static final PacketPipeline packetPipeline = new PacketPipeline();
    public static final CanCraftingManager canCraftingManager = new CanCraftingManager();
    public static final PowerManager powerManager = new PowerManager();
    
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
        
//        ConfigHandler.init(event.getSuggestedConfigurationFile());
        
        DeadCraftItems.init();
        
        DeadCraftBlocks.init();
        
        proxy.init();
        
    }
}
