package shukaro.warptheory.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * A creeper that explodes, but the explosion doesn't do any damage.
 */
public class EntityFakeCreeper extends EntityCreeper {
    /**
     * The fake creeper will be passive until this many ticks has elapsed.
     */
    public static final int ARMING_TIME = 120;

    protected int armingTimeElapsed;
    protected boolean errorState;

    public EntityFakeCreeper(World world) {
        super(world);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.setHealth(100.0f);

        armingTimeElapsed = 0;
        // Unfortunately, EntityCreeper's explosion method is private, so we cannot override it.
        // We will set the creeper's explosion radius to 0, instead. If we are unable to do so,
        // we'll set errorState to true, which will disarm the creeper so that we don't accidentally
        // set off a real explosion.
        errorState = false;
        try {
            // EntityCreeper.explosionRadius
            FieldUtils.writeField(this, "field_82226_g", 0, true);
        } catch (IllegalArgumentException | IllegalAccessException ignored) {
            errorState = true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        armingTimeElapsed++;
        if (armingTimeElapsed > ARMING_TIME) {
            armingTimeElapsed = ARMING_TIME;
        }
    }

    @Override
    public int getCreeperState() {
        // If something went wrong, force override the creeper state to be negative to prevent
        // accidentally setting off a real explosion.
        if (armingTimeElapsed < ARMING_TIME || errorState) {
            return -1;
        }

        return super.getCreeperState();
    }

    @Override
    public boolean allowLeashing() {
        return !getLeashed();
    }
}
