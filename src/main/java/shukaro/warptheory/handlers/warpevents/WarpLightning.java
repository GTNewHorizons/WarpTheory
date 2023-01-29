package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWorldTickWarpEvent;

public class WarpLightning extends IWorldTickWarpEvent {

    public WarpLightning(int minWarp) {
        super("lightning", minWarp, world -> 5 + world.rand.nextInt(10));
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        int x = (int) player.posX + world.rand.nextInt(3) - world.rand.nextInt(3);
        int y = (int) player.posY;
        int z = (int) player.posZ + world.rand.nextInt(3) - world.rand.nextInt(3);
        if (world.rand.nextInt(100) == 0 && world.canBlockSeeTheSky(x, y, z)) {
            EntityLightningBolt bolt = new EntityLightningBolt(world, x, y, z);
            world.addWeatherEffect(bolt);
            return 1;
        }

        return 0;
    }
}
