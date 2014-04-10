package mak.dc.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockFake extends Block {

	protected BlockFake() {
		super(Material.glass);
		this.setBlockUnbreakable();
		this.setBlockTextureName("null");
		this.setBlockName("null");
		this.opaque = false;
	}

}
