package shukaro.warptheory.entity;

import java.lang.reflect.Field;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import thaumcraft.common.entities.monster.EntityTaintSheep;

/** A version of the tainted sheep that cannot spread taint. */
public class EntitySafeTaintSheep extends EntityTaintSheep {

    private final EntityAIEatGrass eatGrass = new EntityAIEatGrass(this);
    private final Field sheepTimerField;

    public EntitySafeTaintSheep(World world) {
        super(world);
        this.tasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
        this.tasks.addTask(5, this.eatGrass);
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));

        try {
            sheepTimerField = EntityTaintSheep.class.getDeclaredField("sheepTimer");
            sheepTimerField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();

        try {
            sheepTimerField.set(this, eatGrass.func_151499_f());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eatGrassBonus() {
        this.setSheared(false);
    }
}
