package shukaro.warptheory.entity;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Interface for entities that want to listen for {@link LivingHurtEvent}.
 */
public interface IHurtable {

    void onHurt(LivingHurtEvent e);
}
