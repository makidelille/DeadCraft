package mak.dc.common.util;

import java.io.File;

import mak.dc.DeadCraft;
import net.minecraftforge.common.config.Configuration;

public class Config {

	private Configuration cfg;
	
	public Config(File file){
		this.cfg = new Configuration(file);
	}
	
	public static int getInt(String cat,String cfgName, int defValue){
		return DeadCraft.config.getConfig().get(cat, cfgName, defValue).getInt();
	}
	
	public Configuration getConfig(){
		return cfg;
	}
	
	public void save(){
		cfg.save();
	}
	
	public static class ConfigLib{	
		/**
		 * ItemCrystal CFG
		 */
		private static final String CRYSTAL_CAT ="Crystal";
		public static final int CRYS_CRYSTALCOST = getInt(CRYSTAL_CAT, "crystal_cost", 100);
		public static final int CRYS_DEFMAXCHARGE = getInt(CRYSTAL_CAT, "base_maxCharge", 2500);
		
		/**
		 * ItemDeadWand CFG
		 */
		private static final String WAND_CAT = "DeadWand";
		public static final int WAND_MAXCHARGE = getInt(WAND_CAT, "maxCharge", 250);
		/**
		 * ItemMindController CFG
		 */		
		private static final String MIND_CAT = "MindController";
		public static final int MIND_MAX = getInt(MIND_CAT, "maxCharge", 4000);
		public static final int MIND_C_PASS = getInt(MIND_CAT, "cost_passive", 100);
		public static final int MIND_C_HOST = getInt(MIND_CAT, "cost_hostile", 250);
		
		/**
		 * general power cfg
		 */
		private static final String POWER_CAT = "power";
		public static final int MAX_CHARGESPEED = getInt(POWER_CAT, "max_charge_speed", 50);
		public static final int BASE_MAXCHARGE = getInt(POWER_CAT, "base_max_charge", 2 * CRYS_DEFMAXCHARGE);
		public static final int MIN_CONSO = getInt(POWER_CAT, "min_powerconso_speed", 5);
		/**
		 * TE compressor cfg
		 */
		private static final String COMPR_CAT="compressor";
		public static final int COMPRESS_TIME = getInt(COMPR_CAT, "comp_time", 250);
		/**
		 * TE eggspawner cfg
		 */
		private static final String EGGSP_CAT = "eggspawner";
		public static final int EGGS_TIME = getInt(EGGSP_CAT, "buildtime", 18000);
		/**
		 * TE godBottler cfg
		 */
		private static final String GODBOT_CAT = "godbottler";
		public static final int GODBOT_TIME = getInt(GODBOT_CAT, "buildtime", 100);
		
		
		
	}
	
}
