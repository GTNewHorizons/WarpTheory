package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpWither extends IWorldTickWarpEvent {

    public WarpWither(int minWarp) {
        super("wither", minWarp, world -> 1);
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target = RandomBlockHelper.randomBlock(world, player, 4, block -> isValid(world, block));
        if (target == null) {
            return 0;
        }

        world.addWeatherEffect(new EntityLightningBolt(world, target.x, target.y, target.z));
        world.playSoundEffect(
                target.x,
                target.y,
                target.z,
                "random.explode",
                4.0F,
                (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        world.spawnParticle("hugeexplosion", target.x, target.y, target.z, 1.0D, 0.0D, 0.0D);
        EntityWither wither = new EntityWither(world);
        wither.setLocationAndAngles(
                target.x + 0.5D,
                target.y - 0.5D,
                target.z + 0.5D,
                world.rand.nextFloat(),
                world.rand.nextFloat());
        wither.func_82206_m();

        if (world.spawnEntityInWorld(wither)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean isValid(World world, BlockCoord block) {
        if (!world.getBlock(block.x, block.y - 1, block.z).getMaterial().blocksMovement()) return false;

        for (int xb = block.x - 1; xb < block.x + 1; xb++) {
            for (int yb = block.y; yb < block.y + 2; yb++) {
                for (int zb = block.z - 1; zb < block.z + 1; zb++) {
                    if (world.getBlock(xb, yb, zb).getMaterial().blocksMovement()) return false;
                }
            }
        }

        return true;
    }
}
