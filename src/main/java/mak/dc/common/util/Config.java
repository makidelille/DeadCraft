package mak.dc.common.util;

import java.io.File;

import mak.dc.DeadCraft;
import net.minecraftforge.common.config.Configuration;

public class Config {

	public static Config config;
	
	private Configuration cfg;
	
	public Config(File file){
		this.cfg = new Configuration(file);
		config = this;
	}
	
	public int getInt(String cat,String cfgName, int defValue){
		return cfg.get(cat, cfgName, defValue).getInt();
	}
	
	
	public void save(){
		cfg.save();
	}
	
	public static class ConfigLib{	
		/**
		 * ItemCrystal CFG
		 */
		private static final String CRYSTAL_CAT ="Crystal";
		public static final int CRYS_CRYSTALCOST = config.getInt(CRYSTAL_CAT, "crystal_cost", 100);
		public static final int CRYS_DEFMAXCHARGE = config.getInt(CRYSTAL_CAT, "base_maxCharge", 2500);
		
		/**
		 * ItemDeadWand CFG
		 */
		private static final String WAND_CAT = "DeadWand";
		public static final int WAND_MAXCHARGE = config.getInt(WAND_CAT, "maxCharge", 250);
		/**
		 * ItemMindController CFG
		 */		
		private static final String MIND_CAT = "MindController";
		public static final int MIND_MAX =config.getInt(MIND_CAT, "maxCharge", 4000);
		public static final int MIND_C_PASS =config.getInt(MIND_CAT, "cost_passive", 100);
		public static final int MIND_C_HOST =config.getInt(MIND_CAT, "cost_hostile", 250);
		
		/**
		 * general power cfg
		 */
		private static final String POWER_CAT = "power";
		public static final int MAX_CHARGESPEED =config.getInt(POWER_CAT, "max_charge_speed", 50);
		public static final int BASE_MAXCHARGE =config.getInt(POWER_CAT, "base_max_charge", 2 * CRYS_DEFMAXCHARGE);
		public static final int MIN_CONSO =config.getInt(POWER_CAT, "min_powerconso_speed", 5);
		/**
		 * TE compressor cfg
		 */
		private static final String COMPR_CAT="compressor";
		public static final int COMPRESS_TIME =config.getInt(COMPR_CAT, "comp_time", 250);
		/**
		 * TE eggspawner cfg
		 */
		private static final String EGGSP_CAT = "eggspawner";
		public static final int EGGS_TIME =config.getInt(EGGSP_CAT, "buildtime", 18000);
		/**
		 * TE godBottler cfg
		 */
		private static final String GODBOT_CAT = "godbottler";
		public static final int GODBOT_TIME =config.getInt(GODBOT_CAT, "buildtime", 100);
		
		
		
	}
	
}
