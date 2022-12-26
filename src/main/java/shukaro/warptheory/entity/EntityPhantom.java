package shukaro.warptheory.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EntityPhantom extends EntityLiving implements IHurtable {
    public static final ImmutableList<ResourceLocation> SKINS = ImmutableList.of(
            // Add more textures here if you want more varied phantoms.
            new ResourceLocation("warptheory", "textures/entities/phantom.png"),
            new ResourceLocation("textures/entity/steve.png"));

    protected static final int SKIN_DATA_WATCHER_ID = 16;
    protected static final String SKIN_NBT_TAG = "skin";
    protected static final int LIFETIME_DATA_WATCHER_ID = 17;
    protected static final String LIFETIME_NBT_TAG = "lifetime";

    public EntityPhantom(World world) {
        super(world);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(256);
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0d);
        setHealth(256);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(SKIN_DATA_WATCHER_ID, (byte) worldObj.rand.nextInt(SKINS.size()));
        dataWatcher.addObject(LIFETIME_DATA_WATCHER_ID, (short) (100 + worldObj.rand.nextInt(60 * 20)));
    }

    public int getSkinIndex() {
        return dataWatcher.getWatchableObjectByte(SKIN_DATA_WATCHER_ID);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        short lifetime = dataWatcher.getWatchableObjectShort(LIFETIME_DATA_WATCHER_ID);
        if (lifetime <= 0) {
            setDead();
        }

        EntityPlayer nearestPlayer = worldObj.getClosestPlayerToEntity(this, 16.0f);
        if (nearestPlayer != null && nearestPlayer.canEntityBeSeen(this)) {
            lifetime *= 0.5;
        }
        lifetime--;

        dataWatcher.updateObject(LIFETIME_DATA_WATCHER_ID, lifetime);
    }

    @Override
    public void onHurt(LivingHurtEvent e) {
        setDead();
    }

    @Override
    protected String getHurtSound() {
        return "none";
    }

    @Override
    protected String getDeathSound() {
        return "none";
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte(SKIN_NBT_TAG, dataWatcher.getWatchableObjectByte(SKIN_DATA_WATCHER_ID));
        nbt.setShort(LIFETIME_NBT_TAG, dataWatcher.getWatchableObjectShort(LIFETIME_DATA_WATCHER_ID));
    }

    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        dataWatcher.updateObject(SKIN_DATA_WATCHER_ID, nbt.getByte(SKIN_NBT_TAG));
        dataWatcher.updateObject(LIFETIME_DATA_WATCHER_ID, nbt.getShort(LIFETIME_NBT_TAG));
    }
}
