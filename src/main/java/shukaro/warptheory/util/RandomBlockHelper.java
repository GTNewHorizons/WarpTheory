package shukaro.warptheory.util;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class RandomBlockHelper {

    private RandomBlockHelper() {}

    /**
     * Returns a random air block within a cube of the given "radius", centered on the player, or null if the random
     * block was not an air block.
     */
    @Nullable
    public static BlockCoord randomAirBlock(World world, EntityPlayer player, int radius) {
        return randomBlock(world, player, radius, block -> block.isAir(world));
    }

    /**
     * Returns a random air block within a cube of the given "radius", centered on the player, with the block above also
     * being air; or returns null if the random block did not satisfy those conditions.
     */
    @Nullable
    public static BlockCoord randomDoubleAirBlock(World world, EntityPlayer player, int radius) {
        return randomBlock(
                world,
                player,
                radius,
                block -> block.isAir(world) && world.isAirBlock(block.x, block.y + 1, block.z));
    }

    /**
     * Returns a random air block within a cube of the given "radius", centered on the player, with the two blocks above
     * also being air; or returns null if the random block did not satisfy those conditions.
     */
    @Nullable
    public static BlockCoord randomTripleAirBlock(World world, EntityPlayer player, int radius) {
        return randomBlock(
                world,
                player,
                radius,
                block -> block.isAir(world) && world.isAirBlock(block.x, block.y + 1, block.z)
                        && world.isAirBlock(block.x, block.y + 2, block.z));
    }

    /**
     * Returns a random block within a cube of the given "radius", centered on the player, or null if the random block
     * doesn't pass {@code isValid}.
     */
    @Nullable
    public static BlockCoord randomBlock(World world, EntityPlayer player, int radius, Predicate<BlockCoord> isValid) {
        int x = (int) player.posX + world.rand.nextInt(radius) - world.rand.nextInt(radius);
        int y = (int) player.posY + world.rand.nextInt(radius) - world.rand.nextInt(radius);
        int z = (int) player.posZ + world.rand.nextInt(radius) - world.rand.nextInt(radius);

        BlockCoord block = new BlockCoord(x, y, z);
        return isValid.test(block) ? block : null;
    }

    public static void setLocation(World world, Entity entity, BlockCoord location) {
        entity.setLocationAndAngles(
                location.x + world.rand.nextDouble(),
                location.y + world.rand.nextDouble(),
                location.z + world.rand.nextDouble(),
                360.0f * world.rand.nextFloat(),
                180.0f * world.rand.nextFloat() - 90.0f);
    }
}
