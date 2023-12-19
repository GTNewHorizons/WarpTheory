package shukaro.warptheory.handlers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import shukaro.warptheory.util.MiscHelper;

/**
 * A world tick warp event that can set timers. Override {@link #onTick(TickEvent.WorldTickEvent)} to change the tick
 * interval.
 */
public abstract class ITimerWarpEvent extends IWarpEvent {

    protected final Function<World, Integer> incrementFunction;
    /**
     * Map from timer prefix to full timer tag.
     */
    protected final ImmutableMap<String, String> timers;

    protected ITimerWarpEvent(String name, int minWarp, Function<World, Integer> incrementFunction, String... timers) {
        super(name, minWarp);
        this.incrementFunction = incrementFunction;

        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        for (String timer : timers) {
            builder.put(timer, name + "-" + timer);
        }
        this.timers = builder.build();

        FMLCommonHandler.instance().bus().register(this);
    }

    /**
     * Returns the number of successful triggers of the event. This method will not be called while any timers are still
     * active.
     */
    public abstract int triggerEvent(int eventAmount, World world, EntityPlayer player);

    /**
     * Called each time any timer ticks.
     */
    public void timerTick(World world, EntityPlayer player, String timer, int timerCount) {}

    /**
     * Returns {@code 0} if the player does not have the timer tag.
     */
    public int getTimer(EntityPlayer player, String timer) {
        return MiscHelper.getWarpTag(player).getInteger(timers.get(timer));
    }

    /**
     * Call this with {@code 0} to cancel the timer.
     */
    public void setTimer(EntityPlayer player, String timer, int timerCount) {
        MiscHelper.getWarpTag(player).setInteger(timers.get(timer), timerCount);
    }

    public ImmutableMap<String, String> getTimers() {
        return timers;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        sendChatMessage(player);
        MiscHelper.modEventInt(player, name, incrementFunction.apply(world));
        return true;
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER) return;

        for (EntityPlayer player : (List<EntityPlayer>) e.world.playerEntities) {
            boolean hasTimer = false;
            for (Map.Entry<String, String> entry : timers.entrySet()) {
                if (MiscHelper.getWarpTag(player).hasKey(entry.getValue())) {
                    int count = decreaseTag(player, entry.getValue(), 1);
                    timerTick(e.world, player, entry.getKey(), count);

                    // Don't count expired timers.
                    if (count > 0) {
                        hasTimer = true;
                    }
                }
            }

            if (!hasTimer && MiscHelper.getWarpTag(player).hasKey(name)) {
                int eventAmount = MiscHelper.getWarpTag(player).getInteger(name);
                int decrement = triggerEvent(eventAmount, e.world, player);
                decreaseTag(player, name, decrement);
            }
        }
    }
}
