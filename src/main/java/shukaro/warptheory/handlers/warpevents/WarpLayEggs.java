package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.ITimerWarpEvent;

public class WarpLayEggs extends ITimerWarpEvent {

    /**
     * This timer does nothing, but as long as it's still ticking, this event won't fire again.
     */
    private static final String COOLDOWN_TIMER = "timer";

    public WarpLayEggs(int minWarp) {
        super("layeggs", minWarp, world -> 3 + world.rand.nextInt(6), COOLDOWN_TIMER);
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // No message for this one.
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        world.playSoundAtEntity(player, "mob.chicken.plop", 1.0F, 1.0F);
        player.dropItem(Items.egg, 1);

        setTimer(player, COOLDOWN_TIMER, world.rand.nextInt(5) + 1);
        return 1;
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % (30 * 20) != 0) {
            return;
        }

        super.onTick(e);
    }
}
