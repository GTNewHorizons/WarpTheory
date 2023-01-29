package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;
import thaumcraft.common.entities.monster.EntityFireBat;

public class WarpFireBats extends IMultiWarpEvent {

    public WarpFireBats(int minWarp) {
        super("firebats", minWarp, 2, world -> 5 + world.rand.nextInt(10));
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;
        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomAirBlock(world, player, 16);
            if (target == null) {
                continue;
            }

            EntityFireBat bat = new EntityFireBat(world);
            bat.playLivingSound();
            RandomBlockHelper.setLocation(world, bat, target);

            if (eventLevel > 0) {
                switch (world.rand.nextInt(3)) {
                    case 2:
                        bat.setIsDevil(true);
                        break;
                    case 1:
                        bat.setIsExplosive(true);
                        break;
                    case 0:
                        // Do nothing.
                        break;
                }
            }

            if (world.spawnEntityInWorld(bat)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }
}
