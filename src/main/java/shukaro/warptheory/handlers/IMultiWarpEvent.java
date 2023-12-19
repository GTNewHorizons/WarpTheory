package shukaro.warptheory.handlers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import shukaro.warptheory.util.MiscHelper;

/**
 * A world tick warp event that has multiple "levels" depending on player warp.
 */
public abstract class IMultiWarpEvent extends IWarpEvent {

    /**
     * This bi-map will iterate from least to greatest integer.
     */
    protected final ImmutableBiMap<Integer, String> eventLevels;

    /**
     * A list of the entries in {@code eventLevels}, in reversed order, so we can iterate from greatest to least
     * integer.
     */
    protected final ImmutableList<Map.Entry<Integer, String>> reversedEventLevels;

    protected final Function<World, Integer> incrementFunction;

    protected IMultiWarpEvent(String name, int minWarp, int numLevels, Function<World, Integer> incrementFunction) {
        super(name, minWarp);
        this.incrementFunction = incrementFunction;

        ImmutableBiMap.Builder<Integer, String> builder = ImmutableBiMap.builder();
        builder.put(0, name);
        for (int i = 1; i < numLevels; i++) {
            builder.put(i, name + i);
        }
        this.eventLevels = builder.build();
        this.reversedEventLevels = ImmutableList.copyOf(Lists.reverse(Lists.newArrayList(eventLevels.entrySet())));

        FMLCommonHandler.instance().bus().register(this);
    }

    /**
     * Returns the number of successful triggers of the event. {@code eventLevel} is the {@code 0}-indexed event level.
     */
    public abstract int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player);

    public ImmutableBiMap<Integer, String> getEventLevels() {
        return eventLevels;
    }

    protected int calculateEventLevel(World world, EntityPlayer player) {
        if (!ConfigHandler.allowMultiWarpEffects) {
            return 0;
        }

        int eventLevel = 0;
        int totalWarp = WarpHandler.getTotalWarp(player);

        /*
         * The math here is as follows: 1. We start at event level 0. 2. For each event level i > 0, the chance of the
         * event getting "promoted" to that warp level scales linearly with player warp: 3. The promotion chance starts
         * at 0% with warp <= (i + 1) * minWarp 4. The promotion chance maxes out at 50% with warp >= (i + 2) * minWarp
         * 5. Events must successfully promote past all prior levels to promote to the next level. So with infinite
         * warp, the chance of level 0 is 50% (failing to promote to level 1), the chance of level 1 is 25%, and so on.
         */
        for (int i = 1; i < eventLevels.size(); i++) {
            int promotionWarp = MathHelper.clamp_int(totalWarp - (i + 1) * minWarp, 0, minWarp);

            if (world.rand.nextInt(minWarp + promotionWarp) >= minWarp) {
                eventLevel++;
            } else {
                break;
            }
        }

        return eventLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        int eventLevel = calculateEventLevel(world, player);
        return doEvent(eventLevels.get(eventLevel), world, player);
    }

    /**
     * Debug method that force-triggers this event with the specified tag.
     */
    public boolean doEvent(String tagName, World world, EntityPlayer player) {
        sendChatMessage(player);
        MiscHelper.modEventInt(player, tagName, incrementFunction.apply(world));
        return true;
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER) return;

        for (EntityPlayer player : (List<EntityPlayer>) e.world.playerEntities) {
            for (Map.Entry<Integer, String> entry : reversedEventLevels) {
                String tag = entry.getValue();

                if (MiscHelper.getWarpTag(player).hasKey(tag)) {
                    int eventAmount = MiscHelper.getWarpTag(player).getInteger(tag);
                    int decrement = triggerEvent(entry.getKey(), eventAmount, e.world, player);
                    decreaseTag(player, tag, decrement);
                    break; // Just do one event at a time.
                }
            }
        }
    }
}
