package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpRain extends IWarpEvent {

    public WarpRain(int minWarp) {
        super("rain", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (!world.getWorldInfo().isThundering()) {
            sendChatMessage(player);
            world.getWorldInfo().setRaining(true);
            world.getWorldInfo().setThundering(true);
            return true;
        }
        return false;
    }
}
