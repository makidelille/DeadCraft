package mak.dc.common.blocks;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import mak.dc.DeadCraft;
import mak.dc.common.tileEntities.TileEntityCompressor;
import mak.dc.network.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCompressor extends BlockDeadCraft {
    
    protected BlockCompressor() {
        super(Material.iron);
        this.setBlockTextureName("stone");
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player != null) {
            FMLNetworkHandler.openGui(player, DeadCraft.instance, 4, world, x, y, z);
        }
        return true;
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityCompressor te = (TileEntityCompressor) world.getTileEntity(x, y, z);
        if (te == null) return;
        ItemStack buf = te.getTempbuffer();
        if (buf != null) {
            float spawnX = x + world.rand.nextFloat();
            float spawnY = y + world.rand.nextFloat();
            float spawnZ = z + world.rand.nextFloat();
            
            EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, buf);
            world.spawnEntityInWorld(droppedItem);
        }
        super.breakBlock(world, x, y, z, block, meta);
        
    }
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityCompressor();
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return ClientProxy.renderInventoryTESRId;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random ran) {
        TileEntityCompressor te = (TileEntityCompressor) world.getTileEntity(x, y, z);
        if (te == null) return;
        float prog = ((float) te.getProgress()) / te.BUILDTIME;
        float amount = -80f * (prog - 0.60f) * (prog - 0.30f);
        for (int i = 0; i < amount * 50; i++) {
            float dx = 0.55f + ran.nextFloat() / 100;
            float dy = 2 / 16f;
            float dz = 0.55f + ran.nextFloat() / 100;
            
            float vx = (ran.nextFloat() - 0.5f) / 10f;
            float vy = -0.00008f;
            float vz = (ran.nextFloat() - 0.5f) / 10f;
            
            EntityReddustFX fx = new EntityReddustFX(world, x + dx, y + dy, z + dz, vx, vy, vz);
            fx.setRBGColorF(0.8f, 0.8f, 0.8f);
            fx.setVelocity(vx, vy, vz);
            fx.multipleParticleScaleBy(0.05f);
            Minecraft.getMinecraft().effectRenderer.addEffect(fx);
        }
        
    }
    
}
