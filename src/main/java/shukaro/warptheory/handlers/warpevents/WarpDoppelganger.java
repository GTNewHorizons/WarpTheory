package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.entity.EntityDoppelganger;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpDoppelganger extends IWorldTickWarpEvent {

    public WarpDoppelganger(int minWarp) {
        super("doppelganger", minWarp, world -> 1);
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        int successful = 0;

        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomBlock(world, player, 4, block -> isValid(world, block));
            if (target == null) {
                continue;
            }

            EntityDoppelganger doppelganger = new EntityDoppelganger(world);
            doppelganger.initialize(player);
            doppelganger.playLivingSound();
            RandomBlockHelper.setLocation(world, doppelganger, target);
            if (world.spawnEntityInWorld(doppelganger)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }

    private static boolean isValid(World world, BlockCoord block) {
        BlockCoord below = block.copy().offset(0);
        BlockCoord above = block.copy().offset(1);

        return below.isTopSolid(world) && block.isAir(world) && above.isAir(world);
    }
}
