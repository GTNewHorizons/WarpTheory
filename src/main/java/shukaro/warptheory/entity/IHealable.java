package shukaro.warptheory.entity;

import net.minecraftforge.event.entity.living.LivingHealEvent;

/**
 * Interface for entities that want to listen for {@link LivingHealEvent}.
 */
public interface IHealable {

    void onHeal(LivingHealEvent e);
}
