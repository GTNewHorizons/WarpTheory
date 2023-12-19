package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;

public class WarpInsomnia extends IWorldTickWarpEvent {

    public WarpInsomnia(int minWarp) {
        super("insomnia", minWarp, world -> 120 + world.rand.nextInt(240));
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        if (player.isPlayerSleeping()) {
            sendChatMessage(player, name + ".wakeup");
            player.wakeUpPlayer(true, true, false);
        }

        return 1;
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 20 != 0) {
            return;
        }

        super.onTick(e);
    }
}
