package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.ITimerWarpEvent;
import shukaro.warptheory.util.ChatHelper;

public class WarpCountdownBomb extends ITimerWarpEvent {

    private static final String COUNTDOWN = "timer";

    public WarpCountdownBomb(int minWarp) {
        super("countdownbomb", minWarp, world -> 1, COUNTDOWN);
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // No message for this one.
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        setTimer(player, COUNTDOWN, 11);
        return 1;
    }

    @Override
    public void timerTick(World world, EntityPlayer player, String timer, int timerCount) {
        if (timer.equals(COUNTDOWN)) {
            EnumChatFormatting color = timerCount > 5 ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.RED;
            EnumChatFormatting format = timerCount > 2 ? EnumChatFormatting.ITALIC : EnumChatFormatting.BOLD;
            ChatHelper.sendTranslatedToPlayer(
                    player,
                    String.format("chat.warptheory.%s.%d", name, timerCount),
                    color,
                    format);

            // Spawn some particles
            int numParticles = 8 * (10 - timerCount);
            double particleSpeed = 0.3;
            ((WorldServer) world).func_147487_a(
                    "fireworksSpark",
                    player.posX,
                    player.posY,
                    player.posZ,
                    numParticles,
                    0.0d,
                    0.0d,
                    0.0d,
                    particleSpeed);

            if (timerCount > 0) {
                world.playSoundAtEntity(player, "game.tnt.primed", 1.0F, 1.0F);
            } else {
                world.createExplosion(null, player.posX, player.posY, player.posZ, 3.0F, true);
            }
        }
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
