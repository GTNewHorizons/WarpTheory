package shukaro.warptheory.entity;

import java.lang.reflect.Field;

import javax.annotation.Nullable;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.apache.commons.lang3.reflect.FieldUtils;

import shukaro.warptheory.WarpTheory;

/**
 * A creeper that explodes, but the explosion doesn't do any damage.
 */
public class EntityFakeCreeper extends EntityCreeper {

    /**
     * The fake creeper will be passive until this many ticks has elapsed.
     */
    public static final int ARMING_TIME = 120;

    /**
     * {@link Field} object for the {@link EntityCreeper#explosionRadius} private field.
     *
     * <p>
     * Code must gracefully handle this being null, which will be the case if something went wrong when trying to fetch
     * the field.
     */
    @Nullable
    public static final Field explosionRadiusField;

    static {
        Field explosionRadius = null;
        try {
            explosionRadius = FieldUtils.getField(EntityCreeper.class, "field_82226_g", true);
            if (explosionRadius == null) {
                explosionRadius = FieldUtils.getField(EntityCreeper.class, "explosionRadius", true);
            }
        } catch (Exception e) {
            WarpTheory.logger.error("Got exception trying to access explosionRadius for EntityFakeCreeper", e);
        }
        explosionRadiusField = explosionRadius;
    }

    protected int armingTimeRemaining;
    protected boolean errorState;

    public EntityFakeCreeper(World world) {
        super(world);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.setHealth(100.0f);

        armingTimeRemaining = ARMING_TIME;
        errorState = false;
        setExplosionRadius();
    }

    /**
     * Tries to set {@link EntityCreeper#explosionRadius} to 0; if unsuccessful, sets {@code errorState} to true.
     *
     * <p>
     * Unfortunately, EntityCreeper's explosion method is private, so we cannot override it. We will set the creeper's
     * explosion radius to 0, instead. If we are unable to do so, we'll set {@code errorState} to true, which will
     * disarm the creeper so that we don't accidentally set off a real explosion.
     */
    private void setExplosionRadius() {
        if (explosionRadiusField != null) {
            try {
                explosionRadiusField.setInt(this, 0);
            } catch (Exception e) {
                WarpTheory.logger.error("Got exception trying to set explosionRadius for EntityFakeCreeper", e);
                errorState = true;
            }
        } else {
            errorState = true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        armingTimeRemaining--;
        if (armingTimeRemaining < 0) {
            armingTimeRemaining = 0;
        }
    }

    @Override
    public int getCreeperState() {
        // If something went wrong, force override the creeper state to be negative to prevent
        // accidentally setting off a real explosion.
        if (armingTimeRemaining > 0 || errorState) {
            return -1;
        }

        return super.getCreeperState();
    }

    @Override
    public boolean allowLeashing() {
        return !getLeashed();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("armingTimeRemaining", armingTimeRemaining);
        tagCompound.setBoolean("errorState", errorState);
        // Force overwrite the explosion radius value from the super method, just in case.
        tagCompound.setByte("ExplosionRadius", (byte) 0);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);

        if (tagCompound.hasKey("armingTimeRemaining", 99)) {
            armingTimeRemaining = tagCompound.getInteger("armingTimeRemaining");
        }

        if (tagCompound.getBoolean("errorState") || tagCompound.getByte("ExplosionRadius") > 0) {
            errorState = true;
        }
    }
}
