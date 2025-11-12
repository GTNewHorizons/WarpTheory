package shukaro.warptheory.util;

import java.util.ArrayList;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.entity.EntityDropParticleFX;
import shukaro.warptheory.handlers.WarpHandler;

public class MiscHelper {

    public static EntityPlayer getPlayerByName(String name) {
        for (EntityPlayer serverPlayer : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (serverPlayer.getCommandSenderName().equals(name)) return serverPlayer;
        }
        return null;
    }

    public static EntityPlayer getPlayerByEntityID(int id) {
        for (EntityPlayer serverPlayer : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (serverPlayer.getEntityId() == id) return serverPlayer;
        }
        return null;
    }

    public static boolean isOp(String name) {
        for (String n : MinecraftServer.getServer().getConfigurationManager().func_152606_n()) {
            if (n.equals(name)) return true;
        }
        return false;
    }

    public static NBTTagCompound getWarpTag(EntityPlayer player) {
        if (!player.getEntityData().hasKey(Constants.modID)) {
            NBTTagCompound tag = new NBTTagCompound();
            player.getEntityData().setTag(Constants.modID, tag);
            return tag;
        }
        return player.getEntityData().getCompoundTag(Constants.modID);
    }

    public static NBTTagCompound modEventInt(EntityPlayer player, String tagName, int amount) {
        NBTTagCompound tag = getWarpTag(player);
        tag.setInteger(tagName, tag.getInteger(tagName) + amount);
        return tag;
    }

    public static boolean hasNonSolidNeighbor(World world, BlockCoord coord) {
        for (BlockCoord n : coord.getNearby()) {
            if (n.isAir(world) || !n.getBlock(world).isOpaqueCube()) return true;
        }
        return false;
    }

    public static boolean canTurnToSwampWater(World world, BlockCoord coord) {
        NameMetaPair pair = new NameMetaPair(coord.getBlock(world), coord.getMeta(world));
        boolean contained = true;
        for (int i = 0; i < 6; i++) {
            if (i != 1 && (!coord.copy().offset(i).getBlock(world).isOpaqueCube()
                    && coord.copy().offset(i).getBlock(world) != Blocks.water))
                contained = false;
        }
        BlockCoord cover = coord.copy().offset(1);
        return contained && (coord.isAir(world) || WarpHandler.decayMappings.containsKey(pair))
                && (cover.isAir(world) || cover.getBlock(world) == Blocks.log
                        || cover.getBlock(world) == Blocks.log2
                        || cover.getBlock(world) instanceof IPlantable);
    }

    public static ArrayList<IInventory> getNearbyTileInventories(EntityPlayer player, int range) {
        ArrayList<IInventory> nearby = new ArrayList<IInventory>();
        for (TileEntity te : (ArrayList<TileEntity>) player.worldObj.loadedTileEntityList) {
            BlockCoord teCoord = new BlockCoord(te.xCoord, te.yCoord, te.zCoord);
            BlockCoord playerCoord = new BlockCoord((int) player.posX, (int) player.posY, (int) player.posZ);
            if (te instanceof IInventory && teCoord.getDistance(playerCoord) <= range) nearby.add((IInventory) te);
        }
        return nearby;
    }

    @SideOnly(Side.CLIENT)
    public static void spawnDripParticle(World world, int x, int y, int z, float r, float g, float b) {
        if (world.isAirBlock(x, y - 2, z) && !world.isAirBlock(x, y - 1, z)
                && world.getBlock(x, y - 1, z).getMaterial().blocksMovement()) {
            double px = x + world.rand.nextFloat();
            double py = y - 1.05D;
            double pz = z + world.rand.nextFloat();
            EntityFX fx = new EntityDropParticleFX(world, px, py, pz, r, g, b);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }
}
