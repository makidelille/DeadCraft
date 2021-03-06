package mak.dc.common.blocks;

import java.util.Random;

import mak.dc.DeadCraft;
import mak.dc.common.tileEntities.TileEntityGodBottler;
import mak.dc.network.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGodBottler extends BlockDeadCraft {
    
    public BlockGodBottler() {
        super(Material.iron);
        setLightOpacity(0);
        setLightLevel(0.5f);
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!world.isRemote) {
            TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
            if (te == null || !(te instanceof TileEntityGodBottler)) return;
            if (te.isTop()) {
                te = (TileEntityGodBottler) world.getTileEntity(x, y - 1, z);
            }
            if (te != null && te instanceof TileEntityGodBottler) {
                world.removeTileEntity(te.xCoord, te.yCoord + 1, te.zCoord);
                world.setBlockToAir(te.xCoord, te.yCoord + 1, te.zCoord);
                super.breakBlock(world, te.xCoord, te.yCoord, te.zCoord, block, meta);
            }
        }
    }
    
    @Override
    public boolean canBePlaced(World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z);
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityGodBottler();
    }
    
    @Override
    public int damageDropped(int p_149692_1_) {
        return 0;
    }
    
    @Override
    public IIcon getIcon(int par1, int par2) {
        return Blocks.stone.getIcon(0, 0);
    }
    
    @Override
    public int getRenderType() {
        return ClientProxy.renderInventoryTESRId;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == 4 ? AxisAlignedBB.getBoundingBox(x, y - 1, z, x + 1, y + 1, z + 1) : AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 2, z + 1);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        switch (side) {
            case DOWN:
                return true;
            case UP:
                return true;
            default:
                break;
        }
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 4) {
            meta = world.getBlockMetadata(x, y - 1, z);
        }
        switch (meta) {
            case 1:
                return !(Math.abs(side.offsetX * 2 + side.offsetZ + 1) == 3);
            case 3:
                return !(Math.abs(side.offsetX * 2 + side.offsetZ + 1) == 1);
            default:
                return !(meta == Math.abs(side.offsetX * 2 + side.offsetZ + 1));
        }
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
            if (te.isTop()) {
                te = te.getPair();
                if (te == null) return false;
            }
            if (!super.onBlockActivated(world, te.xCoord, te.yCoord, te.zCoord, player, side, hitX, hitY, hitZ)) {
                if (player.getCurrentEquippedItem() == null) {
                    FMLNetworkHandler.openGui(player, DeadCraft.instance, 2, world, te.xCoord, te.yCoord, te.zCoord);
                    return true;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack is) {
        world.setBlock(x, y + 1, z, this, 4, 1 | 2);
        TileEntityGodBottler teBot = (TileEntityGodBottler) world.getTileEntity(x, y, z);
        byte face = (byte) (MathHelper.floor_double(ent.rotationYaw * 4.0F / 360.0F + 0.5D) & 3);
        world.setBlockMetadataWithNotify(x, y, z, face, 1 | 2);
        
        TileEntityGodBottler teTop = (TileEntityGodBottler) world.getTileEntity(x, y + 1, z);
        super.onBlockPlacedBy(world, teBot.xCoord, teBot.yCoord, teBot.zCoord, ent, is);
        teTop.setup(teBot);
        teBot.setPair(teTop);
    }
    
    @Override
    public void onWrenched(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntityGodBottler te = (TileEntityGodBottler) world.getTileEntity(x, y, z);
        if (world.getBlockMetadata(x, y, z) == 4) {
            te = te.getPair();
        }
        if (te == null) return;
        if (side <= 1) return;
        switch (side) {
            case 2:
                side = 0;
                break;
            case 3:
                side = 2;
                break;
            case 4:
                side = 3;
                break;
            case 5:
                side = 1;
                break;
        }
        world.setBlockMetadataWithNotify(te.xCoord, te.yCoord, te.zCoord, side, 1 | 2);
        world.notifyBlocksOfNeighborChange(te.xCoord, te.yCoord + 1, te.zCoord, this);
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random ran) {
        if (((TileEntityGodBottler) world.getTileEntity(x, y, z)).hasStarted()) {
            boolean flag = ((TileEntityGodBottler) world.getTileEntity(x, y, z)).isTop();
            for (int i = 0; i < 5; i++) {
                float coef = ran.nextFloat() / 10;
                float scale = ran.nextFloat();
                
                int a = ran.nextInt(90);
                int b = ran.nextInt(360);
                
                float vx = (float) (Math.cos(a / (2 * Math.PI)) * Math.cos(b / (2 * Math.PI)) * coef);
                float vy = (float) (Math.sin(a / (2 * Math.PI)) * coef);
                float vz = (float) (Math.cos(a / (2 * Math.PI)) * Math.sin(b / (2 * Math.PI)) * coef);
                float dx = 0.5f, dy = flag ? -0.35f : 0.65f, dz = 0.5f;
                
                EntityReddustFX fx = new EntityReddustFX(world, x + dx, y + dy + ran.nextFloat() / 10, z + dz, vx, vy, vz);
                if (ran.nextBoolean()) {
                    fx.setRBGColorF(0.65f + ran.nextFloat() / 10, 0.65f + ran.nextFloat() / 10, 1f);
                } else {
                    fx.setRBGColorF(1f, 0.1f + ran.nextFloat() / 10, 0.1f + ran.nextFloat() / 10);
                }
                fx.setVelocity(vx, vy, vz);
                Minecraft.getMinecraft().effectRenderer.addEffect(fx);
            }
        }
    }
    
    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        return;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
}
