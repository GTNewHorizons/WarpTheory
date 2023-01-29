package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.entity.EntityPhantom;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpPhantoms extends IWorldTickWarpEvent {

    public WarpPhantoms(int minWarp) {
        super("phantoms", minWarp, world -> 1 + world.rand.nextInt(5));
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // No message for this one.
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        int successful = 0;

        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomBlock(world, player, 32, block -> isValid(world, block));
            if (target == null) {
                continue;
            }

            EntityPhantom phantom = new EntityPhantom(world);
            RandomBlockHelper.setLocation(world, phantom, target);
            if (world.spawnEntityInWorld(phantom)) {
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
