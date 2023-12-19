package shukaro.warptheory.handlers.warpevents;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
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
        // In order to avoid this event being too punishing, here's how the trigger works:
        //
        // * We'll drain a small amount of vis from random aspects, up to all of them, based on the
        // event amount. If event amount > # of primal aspects, the excess is ignored.
        // * If we were able to drain any vis at all, we'll clear the event entirely, to avoid the
        // situation where players accumulate a massive queue of wand drain events while not
        // holding a wand for a while.
        boolean successful = false;
        List<Aspect> primalAspects = Aspect.getPrimalAspects();
        Collections.shuffle(primalAspects);

        for (int i = 0; i < primalAspects.size() && i < eventAmount; i++) {
            AspectList aspectList = new AspectList();
            int amount = Math.max(0, world.rand.nextInt(10 * 100) - 100);
            aspectList.add(primalAspects.get(i), amount);

            if (ThaumcraftApiHelper.consumeVisFromInventory(player, aspectList)) {
                successful = true;
            }
        }

        if (successful) {
            world.playSoundAtEntity(player, "thaumcraft:zap", 1.0F, 1.0F);
            super.sendChatMessage(player);
            return eventAmount;
        } else {
            return 0;
        }
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
