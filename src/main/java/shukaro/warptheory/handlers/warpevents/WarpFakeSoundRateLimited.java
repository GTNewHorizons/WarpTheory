package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class WarpFakeSoundRateLimited extends WarpFakeSound {

    private static final String LAST_PLAYED_KEY = "WarpTheoryFakeSoundLastPlayed_%s";
    // 86400 seconds = 1 day
    private static final long DELAY_IN_SECONDS = 86400;

    private final String nbtKey;

    public WarpFakeSoundRateLimited(String name, int minWarp, String sound) {
        super(name, minWarp, sound);
        nbtKey = String.format(LAST_PLAYED_KEY, name);
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        final long now = System.currentTimeMillis() / 1000;

        final long lastTriggered;
        if (player.getEntityData().hasKey(nbtKey)) {
            lastTriggered = player.getEntityData().getLong(nbtKey);
        } else {
            lastTriggered = 0;
        }

        if (now - lastTriggered < DELAY_IN_SECONDS) {
            return eventAmount;
        }

        final int count = super.triggerEvent(eventAmount, world, player);
        if (count > 0) {
            player.getEntityData().setLong(nbtKey, now);
        }

        return count;
    }
}
