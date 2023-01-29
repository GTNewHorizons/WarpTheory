package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpMessage extends IWarpEvent {

    private static final int NUM_MESSAGES = 9;

    public WarpMessage(int minWarp) {
        super("message", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        int message = world.rand.nextInt(NUM_MESSAGES);
        sendChatMessage(player, String.format("%s.%d", name, message));
        return true;
    }
}
