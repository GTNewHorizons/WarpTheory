package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.IMultiWarpEvent;

public class WarpGregTechFakeSound extends IMultiWarpEvent {

    /** How many times to play the alarm sound. */
    private static final int ALARM_PLAY_TIMES = 8;

    private static final ImmutableList<String> SOUNDS = ImmutableList
            .of("ic2:tools.Wrench", "ic2:machines.InterruptOne", "nuclearcontrol:alarm-default");

    public WarpGregTechFakeSound(int minWarp) {
        super("gtfakesound", minWarp, SOUNDS.size(), world -> ALARM_PLAY_TIMES);
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // No message for this one.
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        world.playSoundEffect(player.posX, player.posY, player.posZ, SOUNDS.get(eventLevel), 1.0f, 1.0f);

        // A little hackery to allow us to play the alarm sound multiple times, but the others once.
        return eventLevel == 2 ? 1 : ALARM_PLAY_TIMES;
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 40 != 0) {
            return;
        }

        super.onTick(e);
    }
}
