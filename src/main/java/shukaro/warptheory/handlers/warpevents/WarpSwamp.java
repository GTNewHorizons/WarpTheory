package shukaro.warptheory.handlers.warpevents;

import java.util.Set;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpSwamp extends IWorldTickWarpEvent {

    public WarpSwamp(int minWarp) {
        super("biomeSwamp", minWarp, world -> 256 + world.rand.nextInt(256));
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
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target = RandomBlockHelper
                .randomBlock(world, player, 8, block -> MiscHelper.hasNonSolidNeighbor(world, block));
        if (target == null) {
            return 0;
        }

        boolean grown = false;
        if (target.getBlock(world) == Blocks.water) {
            if (target.offset(1).isAir(world) && world.setBlock(target.x, target.y, target.z, Blocks.waterlily, 0, 3))
                grown = true;
        } else if (target.getBlock(world) == Blocks.sapling) {
            // Function that grows sapling into a tree
            ((BlockSapling) target.getBlock(world)).func_149878_d(world, target.x, target.y, target.z, world.rand);
            grown = true;
        } else if (target.getBlock(world).getMaterial() == Material.leaves || target.getBlock(world) == Blocks.log
                || target.getBlock(world) == Blocks.log2) {
                    for (int j = 0; j < 6; j++) {
                        int side = 2 + world.rand.nextInt(4);
                        if (Blocks.vine.canPlaceBlockOnSide(world, target.x, target.y, target.z, side)
                                && target.offset(side).isAir(world)) {
                            world.setBlock(
                                    target.x,
                                    target.y,
                                    target.z,
                                    Blocks.vine,
                                    1 << Direction.facingToDirection[Facing.oppositeSide[side]],
                                    3);
                            grown = true;
                            break;
                        }
                    }
                } else {
                    if (world.rand.nextBoolean() && target.getBlock(world).canSustainPlant(
                            world,
                            target.x,
                            target.y,
                            target.z,
                            ForgeDirection.UP,
                            (IPlantable) Blocks.sapling)) {
                        if (world.rand.nextBoolean()) {
                            if (target.offset(1).isAir(world) || (target.getBlock(world) instanceof IPlantable
                                    && target.getBlock(world) != Blocks.sapling))
                                world.setBlock(target.x, target.y, target.z, Blocks.sapling, world.rand.nextInt(6), 3);
                        } else if (world.rand.nextBoolean()) {
                            if (target.offset(1).isAir(world) && target.offset(0).getBlock(world) instanceof IGrowable)
                                ((IGrowable) target.getBlock(world))
                                        .func_149853_b(world, world.rand, target.x, target.y, target.z); // Bonemealing
                        } else {
                            if (target.offset(1).isAir(world) && target.offset(0).getBlock(world) == Blocks.grass)
                                world.setBlock(target.x, target.y, target.z, Blocks.dirt, 2, 3);
                        }
                        grown = true;
                    } else if (world.rand.nextBoolean() && MiscHelper.canTurnToSwampWater(world, target)) {
                        if (target.copy().offset(1).getBlock(world) == Blocks.log
                                || target.copy().offset(1).getBlock(world) == Blocks.log2)
                            world.setBlock(
                                    target.x,
                                    target.y,
                                    target.z,
                                    target.copy().offset(1).getBlock(world),
                                    target.copy().offset(1).getMeta(world),
                                    3);
                        else world.setBlock(target.x, target.y, target.z, Blocks.water, 0, 3);
                        grown = true;
                    } else if (WarpHandler.decayMappings
                            .containsKey(new NameMetaPair(target.getBlock(world), target.getMeta(world)))
                            && target.getBlock(world).isOpaqueCube()
                            && target.getBlock(world) != Blocks.log
                            && target.getBlock(world) != Blocks.log2) {
                                if (target.getBlock(world) != Blocks.dirt && target.getBlock(world) != Blocks.grass) {
                                    if (target.copy().offset(1).getBlock(world).isOpaqueCube())
                                        world.setBlock(target.x, target.y, target.z, Blocks.dirt, 0, 3);
                                    else if (world.rand.nextBoolean())
                                        world.setBlock(target.x, target.y, target.z, Blocks.grass, 0, 3);
                                    else world.setBlock(target.x, target.y, target.z, Blocks.dirt, 2, 3);
                                    grown = true;
                                }
                            }
                }

        if (grown) {
            return 1;
        } else {
            return 0;
        }
    }
}
