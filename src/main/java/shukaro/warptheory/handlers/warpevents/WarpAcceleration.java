package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWorldTickWarpEvent;

public class WarpAcceleration extends IWorldTickWarpEvent {

    public WarpAcceleration(int minWarp) {
        super("acceleration", minWarp, world -> 6000 + world.rand.nextInt(12000));
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        world.setWorldTime(world.getWorldTime() + 2);
        return 1;
    }
}
