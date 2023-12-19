package shukaro.warptheory.handlers.warpevents;

import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpBuff extends IWarpEvent {

    private final ImmutableList<PotionEffect> potionEffects;

    public WarpBuff(String name, int minWarp, PotionEffect... effects) {
        super(name, minWarp);
        this.potionEffects = ImmutableList.copyOf(effects);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        for (PotionEffect effect : potionEffects) {
            int id = effect.getPotionID();
            int duration = effect.getDuration();
            int level = effect.getAmplifier();

            if (player.isPotionActive(id)) {
                for (PotionEffect e : (Collection<PotionEffect>) player.getActivePotionEffects()) {
                    if (e.getPotionID() == id) {
                        effect = new PotionEffect(id, duration + e.getDuration(), level);
                        break;
                    }
                }
            } else {
                effect = new PotionEffect(id, duration, level);
            }

            effect.getCurativeItems().clear();
            player.addPotionEffect(effect);
        }

        sendChatMessage(player);
        return true;
    }
}
