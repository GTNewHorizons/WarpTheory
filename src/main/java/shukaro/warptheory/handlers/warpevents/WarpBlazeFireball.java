package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.ITimerWarpEvent;

public class WarpBlazeFireball extends ITimerWarpEvent {

    private static final String FIREBALL_TIMER = "timer";

    public WarpBlazeFireball(int minWarp) {
        super("blazefireball", minWarp, world -> 1, FIREBALL_TIMER);
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // We'll play the message when we start the timer.
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        world.playSoundAtEntity(player, "mob.blaze.breathe", 1.0F, 1.0F);
        super.sendChatMessage(player);
        setTimer(player, FIREBALL_TIMER, 7);
        return 1;
    }

    @Override
    public void timerTick(World world, EntityPlayer player, String timer, int timerCount) {
        // Triple fireball at 2, 1, 0
        if (timer.equals(FIREBALL_TIMER) && timerCount <= 2) {
            EntitySmallFireball fireball = new EntitySmallFireball(world, player, 0.0f, 0.0f, 0.0f);
            // The fireball constructor introduces some randomness.
            // Copy over vector from an ender pearl projectile, to remove this randomness.
            EntityEnderPearl enderPearl = new EntityEnderPearl(world, player);
            fireball.posY += player.getEyeHeight();
            fireball.accelerationX = enderPearl.motionX / 10.0d;
            fireball.accelerationY = enderPearl.motionY / 10.0d;
            fireball.accelerationZ = enderPearl.motionZ / 10.0d;

            if (world.spawnEntityInWorld(fireball)) {
                world.playSoundAtEntity(player, "mob.ghast.fireball", 1.0F, 1.0F);
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 10 != 0) {
            return;
        }

        super.onTick(e);
    }
}
