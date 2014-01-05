package mak.dc.config;

import java.io.File;

import mak.dc.blocks.BlockEnderPearlBlock;
import mak.dc.items.ItemLifeCrystal;
import mak.dc.lib.BlockInfo;
import mak.dc.lib.Config;
import mak.dc.lib.ItemInfo;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraftforge.common.Configuration;

public class ConfigHandler {

	public static void init(File ConfigurationFile) {
	
		Configuration config = new Configuration (ConfigurationFile);
			
		config.load();
		
		//load id for items
		ItemInfo.DEADWAND_ID = config.getItem(ItemInfo.DEADWAND_KEY, ItemInfo.DEADWAND_DEFAULT).getInt() - 256 ;
		ItemInfo.LIFECRYSTAL_ID = config.getItem(ItemInfo.LIFECRYSTAL_KEY,ItemInfo.LIFECRYSTAL_DEFAULT).getInt() - 256 ;
		ItemInfo.MINDCONTROLLER_ID = config.getItem(ItemInfo.MINDCONTROLLER_KEY, ItemInfo.MINDCONTROLLER_DEFFAULT).getInt() - 256;
		
		
		//load id for blocks
		BlockInfo.EGGSPAWNER_ID = config.getBlock(BlockInfo.EGGSPAWNER_KEY, BlockInfo.EGGSPAWNER_DEFAULT).getInt();
		BlockInfo.ENDERPEARLBLOCK_ID = config.getBlock(BlockInfo.ENDERPEARLBLOCK_KEY, BlockInfo.ENDERPEARLBLOCK_DFFAULT).getInt();
		
		
		//load properties for items
		ItemLifeCrystal._crystalCost = config.get(Config.CONFIG_CRYSTAL_NAME, Config.CONFIG_CRYSTAL_COST, Config.CONFIG_CRYSTAL_COST_DEFFAULT).getInt();
		ItemLifeCrystal._maxValue = config.get(Config.CONFIG_CRYSTAL_NAME, Config.CONFIG_CRYSTAL_MAX, Config.CONFIG_CRYSTAL_MAX_DEFFAULT).getInt();
		
		//load properties for blocks / tileentity
		TileEntityEggSpawner._maxBuildTime = config.get(Config.CONFIG_EGGSPAWNER_NAME,Config.CONFIG_EGGSPAWNER_DURATION, Config.CONFIG_EGGSPAWNER_MAX_DEFFAULT).getInt();
		BlockEnderPearlBlock._IS_CREATIVE_MOVED = config.get(Config.CONFIG_EGGSPAWNER_NAME, Config.CONFIG_ENDERPEARLBLOCK_CREATIVESNEAK, Config.CONFIG_ENDERPEARLBLOCK_CREATIVESNEAK_DEFFAULT).getBoolean(Config.CONFIG_ENDERPEARLBLOCK_CREATIVESNEAK_DEFFAULT);
		
			
		config.save();
		
	}

}
