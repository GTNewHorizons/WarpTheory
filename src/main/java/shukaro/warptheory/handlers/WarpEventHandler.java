package shukaro.warptheory.handlers;

import static thaumcraft.common.config.Config.potionWarpWardID;
import static thaumcraft.common.config.Config.wuss;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import shukaro.warptheory.entity.IHealable;
import shukaro.warptheory.entity.IHurtable;

public class WarpEventHandler {

    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent e) {
        if (!(e.entity instanceof EntityPlayer player)) return;

        World world = player.worldObj;
        if (world.isRemote || player.ticksExisted <= 0) return;

        boolean canApplyWarp = !player.isPotionActive(potionWarpWardID) && !wuss && !player.capabilities.isCreativeMode
                || WarpHandler.getUnavoidableCount(player) > 0;

        if (!canApplyWarp || !ConfigHandler.allowWarpEffects) return;

        int ticks = player.ticksExisted;
        int warpCounter = WarpHandler.Knowledge.getWarpCounter(player.getCommandSenderName());
        Random rand = world.rand;

        if (ticks % 2000 == 0 && warpCounter > 0 && rand.nextInt(100) <= Math.sqrt(warpCounter)) {
            int effectiveWarp = (WarpHandler.getTotalWarp(player) * 2 + warpCounter) / 3;
            effectiveWarp -= WarpHandler.faceplateReduction(player);
            IWarpEvent queuedEvent = WarpHandler.queueOneEvent(player, effectiveWarp);
            if (queuedEvent != null) {
                int tempWarp = WarpHandler.getIndividualWarps(player)[2];
                if (tempWarp > 0) {
                    int cost = Math.min(queuedEvent.getCost(), tempWarp);
                    WarpHandler.removeWarp(player, cost);
                }
            }
        }

        if (ticks % 20 == 0 && rand.nextBoolean()) {
            IWarpEvent nextEvent = WarpHandler.dequeueEvent(player);
            WarpHandler.addUnavoidableCount(player, -1);

            if (nextEvent != null && nextEvent.canDo(world, player)) {
                nextEvent.doEvent(world, player);
            }
        }
    }

    @SubscribeEvent
    public void livingHeal(LivingHealEvent e) {
        if (e.entityLiving instanceof IHealable) {
            ((IHealable) e.entityLiving).onHeal(e);
        }
    }

    @SubscribeEvent
    public void livingHurt(LivingHurtEvent e) {
        if (e.entityLiving instanceof IHurtable) {
            ((IHurtable) e.entityLiving).onHurt(e);
        }
    }
}
