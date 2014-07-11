package mak.dc.common.util;

import mak.dc.network.proxy.ClientProxy;
import mak.dc.network.proxy.CommonProxy;

public final class Lib {
    
    public static final String MOD_NAME = "Dead Craft";
    public static final String MOD_ID = "deadcraft";
    public static final String MOD_VERSION = "1.0";
    public static final String PROXY_CLIENT = "mak.dc.network.proxy.ClientProxy";
    public static final String PROXY_SERVER = "mak.dc.network.proxy.CommonProxy";
    
    public static class IBTInfos {
        
        public static final String ITEM_DEADWAND_KEY = "deadwand";
        public static final String ITEM_CRYSTAL_KEY = "crystal";
        public static final String ITEM_MINDCONTROLLER_KEY = "mindcontroller";
        public static final String ITEM_WRENCH_KEY = "wrench";
        public static final String ITEM_GODCAN_KEY = "godcan";
        public static final String ITEM_COMPACTED_KEY = "compact";
        
        public static final String BLOCK_EGGSPAWNER_KEY = "dragoneggspawner";
        public static final String BLOCK_ENDERPEARLBLOCK_KEY = "enderpearlblock";
        public static final String BLOCK_BOTTLER_KEY = "godbottler";
        public static final String BLOCK_ENDERCONVERTER_KEY = "enderconverter";
        public static final String BLOCK_COMPRESSOR_KEY = "compressor";
        
        public static final String TILE_EGGSPANWER_KEY = "eggspawnertileentity";
        public static final String TILE_BOTTLER_KEY = "godbottlertileentity";
        public static final String TILE_ENDERCONVERTER_KEY = "enderconvertertileentity";
        public static final String TILE_COMPRESSOR_KEY = "compressortileentity";        
    }
    
    public static class Textures {
        
        private static final String _TEXT_LOC = Lib.MOD_ID + ":";
        
        private static final String PATH_MODEL = "textures/models/";
        public static final String PATH_GUI = "textures/gui/";
        
        /** Blocks textures localization */
        public static final String EGGSPAWNER_TEXT_LOC = _TEXT_LOC + "dragoneggspawner";
        public static final String ENDERBLOCK_TEXT_LOC = _TEXT_LOC + "enderpearlblock";
        public static final String NETHERWOOD_LOG_TOP_TEXT_LOC = _TEXT_LOC + "netherWood_top";
        public static final String NETHERWOOD_LOG_SIDE_TEXT_LOC = _TEXT_LOC + "netherWood_side";
        public static final String NETHERWOOD_PLANK_TEXT_LOC = _TEXT_LOC + "netherWood_plank";
        
        /** Items textures localization */
        public static final String DEADWAND_TEXT_LOC = _TEXT_LOC + "deadwand";
        public static final String MINDCONTROLLER_TEXT_LOC = _TEXT_LOC + "mindcontroller";
        public static final String[] CONTROLLER_TEXT_LOC = { _TEXT_LOC + "controller_base", _TEXT_LOC + "controller_lock", _TEXT_LOC + "controller_info" };
        public static final String GODCAN_TEXT_LOC = _TEXT_LOC + "godcan";
        public static final String CRYSTAL_TEXT_LOC = _TEXT_LOC + "crystal";
        public static final String COMPACTED_TEXT_LOC = _TEXT_LOC + "compacted";
        public static final String NETHERSTARNUGGET_TEXT_LOC = _TEXT_LOC + "netherstar_nugget";
        
        /** Models Textures localization */
        public static final String MINDCONTROLLER_MODEL_TEXT_LOC = PATH_MODEL + "mindcontrollermodeltexturemap.png";
        public static final String GODBOTTLER_MODEL_TEXT_LOC = PATH_MODEL + "godbottlertexturemap.png";
        public static final String GODCAN_MODEL_TEXT_LOC = PATH_MODEL + "godcantexturemap.png";
        public static final String COMPRESSOR_MODEL_TEXT_LOC = PATH_MODEL + "compressormap.png";
        public static final String ENDERCONVERTER_TEXT_LOC = PATH_MODEL + "enderconvertertexturemap.png";
        
        /** Gui */
        public static final String EGGSPAWNER_GUI_TEXT_LOC = PATH_GUI + "eggspawner.png";
        public static final String UTIL_GUI_TEXT_LOC = PATH_GUI + "util.png";
        public static final String DEADCRAFTMAIN_GUI_TEXT_LOC = PATH_GUI + "deadcraftmain.png";
        public static final String GODBOTTLER_GUI_TEXT_LOC = PATH_GUI + "godbottler.png";
        public static final String ENDERCONVERTER_GUI_TEXT_LOC = PATH_GUI + "enderconverter.png";
        public static final String COMPRESSOR_GUI_TEXT_LOC = PATH_GUI + "compressor.png";
        public static final String ITEMCHARGE_GUI_TEXT_LOC = PATH_GUI + "itemcharge.png";
        
    }
    
    public static class GuiLib {
    	
    	public static final int ID_GUI_BLOCK_DC = 0;
    	public static final int ID_GUI_BLOCK_EGGSPAWN = 1;
    	public static final int ID_GUI_BLOCK_BOTTLER = 2;
    	public static final int ID_GUI_BLOCK_CONVERTER = 3;
    	public static final int ID_GUI_BLOCK_COMPRESSOR = 4;
    	public static final int ID_INV_POWERITEM = 5;
    	
    }
    
    
}
