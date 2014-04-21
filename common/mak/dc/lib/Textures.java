package mak.dc.lib;

public class Textures {

	private static final String _TEXT_LOC = Lib.MOD_ID + ":";
	
	private static final String PATH_MODEL = "textures/models/";
	public static final String PATH_GUI = "textures/gui/";
	
	
	/** Blocks textures localization*/
	public static final String EGGSPAWNER_TEXT_LOC = _TEXT_LOC + "dragoneggspawner";
	public static final String ENDERBLOCK_TEXT_LOC = _TEXT_LOC + "enderpearlblock";
	
	/** Items textures localization */
	public static final String[] DEADWAND_TEXT_LOC = { _TEXT_LOC + "deadwand",_TEXT_LOC + "deadwand_charged"};
	public static final String[] LIFECRYSTAL_TEXT_LOC = { _TEXT_LOC + "lifecrystal",_TEXT_LOC + "lifecrystal_charged"};
	public static final String[] MINDCONTROLLER_TEXT_LOC = { _TEXT_LOC + "mindcontroller_passive", _TEXT_LOC + "mindcontroller_active",_TEXT_LOC + "mindcontroller_creative" };
	public static final String[] CONTROLLER_TEXT_LOC = { _TEXT_LOC + "controller_base",_TEXT_LOC + "controller_lock",_TEXT_LOC + "controller_info" };
	public static final String GODCAN_TEXT_LOC = _TEXT_LOC + "godcan";
    
	/** Models Textures localization */
	public static final String[] MINDCONTROLLER_MODEL_TEXT_LOC = {PATH_MODEL + "mindcontrollermodeltexturemap_passive.png",PATH_MODEL + "mindcontrollermodeltexturemap_active.png",PATH_MODEL + "mindcontrollermodeltexturemap_creative.png"};
	public static final String GODBOTTLER_MODEL_TEXT_LOC = PATH_MODEL + "godbottlertexturemap.png";
	public static final String GODCAN_MODEL_TEXT_LOC = PATH_MODEL + "godcantexturemap.png";
	
	/**Gui*/
	public static final String EGGSPAWNER_GUI_TEXT_LOC = PATH_GUI + "eggspawner.png";
	public static final String UTIL_GUI_TEXT_LOC = PATH_GUI +  "util.png";
    public static final String DEADCRAFTMAIN_GUI_TEXT_LOC = PATH_GUI + "deadcraftmain.png";




 

	
}
