package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class WarpWandDrain extends IWorldTickWarpEvent {
    public WarpWandDrain(int minWarp) {
        super("wanddrain", minWarp, world -> 1 + world.rand.nextInt(3));
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // We'll play the message when we successfully drain vis.
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        // If we try to drain more vis than the player's wand has, it'll fail, so we'll have to
        // try to drain a little at a time.
        int successful = 0;
        while (successful < eventAmount) {
            AspectList aspectList = new AspectList();
            for (Aspect aspect : Aspect.getPrimalAspects()) {
                int amount = Math.max(0, world.rand.nextInt(10 * 100) - 100);
                aspectList.add(aspect, amount);
            }

            if (ThaumcraftApiHelper.consumeVisFromInventory(player, aspectList)) {
                successful++;
            } else {
                break;
            }
        }

        if (successful > 0) {
            world.playSoundAtEntity(player, "thaumcraft:zap", 1.0F, 1.0F);
            super.sendChatMessage(player);
        }
        return successful;
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % (10 * 20) != 0) {
            return;
        }

        super.onTick(e);
    }
}
