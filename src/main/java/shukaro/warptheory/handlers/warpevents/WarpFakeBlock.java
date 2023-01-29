package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpFakeBlock extends IWarpEvent {

    public WarpFakeBlock(int minWarp) {
        super("fakeblock", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        return false;
    }
}
