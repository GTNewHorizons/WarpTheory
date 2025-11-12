package shukaro.warptheory.handlers.warpevents;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.RandomBlockHelper;
import thaumcraft.common.config.ConfigBlocks;

public class WarpMushrooms extends IWorldTickWarpEvent {

    public WarpMushrooms(int minWarp) {
        super("biomeMushrooms", minWarp, world -> 16 + world.rand.nextInt(16));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canDo(World world, EntityPlayer player) {
        for (String n : (Set<String>) MiscHelper.getWarpTag(player).func_150296_c()) {
            if (n.startsWith("biome") && !n.equals(getName())) return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target = RandomBlockHelper
                .randomBlock(world, player, 16, block -> MiscHelper.hasNonSolidNeighbor(world, block));
        if (target == null) {
            return 0;
        }

        BlockCoord below = target.copy().offset(0);
        if (target.isAir(world) && below.isTopSolid(world)) {
            int mushroomRandom = world.rand.nextInt(99);
            if (mushroomRandom == 0) {
                world.setBlock(target.x, target.y, target.z, ConfigBlocks.blockCustomPlant, 5, 3);
            } else if (mushroomRandom % 2 == 0) {
                world.setBlock(target.x, target.y, target.z, Blocks.brown_mushroom);
            } else {
                world.setBlock(target.x, target.y, target.z, Blocks.red_mushroom);
            }
            world.playSoundEffect(target.x, target.y, target.z, "dig.grass", 1.0F, 1.0F);
            return 1;
        }

        BlockCoord above = target.copy().offset(1);
        if ((target.getBlock(world) == Blocks.grass || target.getBlock(world) == Blocks.dirt) && above.isAir(world)) {
            world.setBlock(target.x, target.y, target.z, Blocks.mycelium);
            world.playSoundEffect(target.x, target.y, target.z, "dig.grass", 1.0F, 1.0F);
            return 1;
        }

        AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(
                (double) target.x - 1,
                (double) target.y - 1,
                (double) target.z - 1,
                (double) target.x + 1,
                (double) target.y + 1,
                (double) target.z + 1);
        for (Entity entity : world.getEntitiesWithinAABB(EntityCow.class, boundingBox)) {
            // Check for exact class match, because we don't want to transform subclasses.
            if (entity.getClass() == EntityCow.class) {
                EntityMooshroom mooshroom = new EntityMooshroom(world);
                mooshroom.copyLocationAndAnglesFrom(entity);
                mooshroom.setGrowingAge(((EntityCow) entity).getGrowingAge());

                if (world.spawnEntityInWorld(mooshroom)) {
                    world.playSoundAtEntity(mooshroom, "dig.grass", 1.0F, 1.0F);
                    mooshroom.playLivingSound();
                    entity.setDead();
                    return 1;
                }
            }
        }

        return 0;
    }
}
