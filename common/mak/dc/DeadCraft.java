package mak.dc;

import mak.dc.client.gui.GuiHandler;
import mak.dc.config.ConfigHandler;
import mak.dc.init.BlockInit;
import mak.dc.init.CreativeTab;
import mak.dc.init.ItemInit;
import mak.dc.lib.Lib;
import mak.dc.network.PacketHandler;
import mak.dc.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(modid= Lib.MOD_ID, name= Lib.MOD_NAME, version= Lib.MOD_VERSION)

@NetworkMod(channels = {Lib.MOD_ID}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)

public class DeadCraft {

	 @SidedProxy(clientSide = "mak.dc.proxy.ClientProxy", serverSide = "mak.dc.proxy.CommonProxy")
	    public static CommonProxy proxy;
	    
	    @Instance ("deadcraft")
	    public static DeadCraft instance;
		
		@EventHandler
		public void PreInit (FMLPreInitializationEvent event) {
			/**Register blocs
		    * register configuration
		    * register item
		    * register achievement
			* register Render
			* register sounds
			*/

			
			ConfigHandler.init(event.getSuggestedConfigurationFile());
			
			ItemInit.init();
			BlockInit.init();
			CreativeTab.init();
			
			proxy.registerRender();
			proxy.registerSounds();

		}
		
		@EventHandler
		public void Init (FMLInitializationEvent event) {
			/**register world gen
			* register entities
			* register crafting
			* register tile entities 
			* register guiHandler
			*/
			
			BlockInit.initTileEntity();
			
			BlockInit.registerCraftRecipe();
			ItemInit.registerCraftRecipe();
			
			new GuiHandler();
			
		

		}
		
		@EventHandler
		public void PostInit (FMLPostInitializationEvent event) {
			
		}
}
