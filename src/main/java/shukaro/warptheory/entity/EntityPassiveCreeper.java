package shukaro.warptheory.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class EntityPassiveCreeper extends EntityCreeper {

    public EntityPassiveCreeper(World world) {
        super(world);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.setHealth(100.0f);
    }

    @Override
    public int getCreeperState() {
        return -1;
    }

    @Override
    public boolean allowLeashing() {
        return !getLeashed();
    }
}
