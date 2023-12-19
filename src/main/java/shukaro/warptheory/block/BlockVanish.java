package shukaro.warptheory.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.util.Constants;

public class BlockVanish extends BlockContainer {

    public IIcon icon;

    protected BlockVanish() {
        super(Material.rock);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setStepSound(Block.soundTypeGlass);
        this.setLightLevel(0.7f);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setTickRandomly(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.icon = reg.registerIcon(Constants.modID.toLowerCase() + ":blank");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item i, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(i));
    }

    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist,
            Entity entity) {}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return Item.getItemById(0);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int i, int j, int k, ForgeDirection o) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return null;
    }
}
