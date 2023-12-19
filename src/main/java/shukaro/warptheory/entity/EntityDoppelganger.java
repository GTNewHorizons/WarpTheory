package shukaro.warptheory.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

public class EntityDoppelganger extends EntityCreature implements IHealable, IHurtable {

    /**
     * The number of ticks we will wait between each attempt to find our player.
     */
    protected static final int FIND_PLAYER_WAIT_TICKS = 20;

    /**
     * The number of ticks we will wait between each heal tick.
     */
    protected static final int HEAL_WAIT_TICKS = 40;

    protected static final int UUID_DATA_WATCHER_ID = 16;
    protected static final String UUID_NBT_TAG = "playerUuid";

    // This will only be populated on the client.
    protected static final Map<UUID, GameProfile> gameProfileCache = new HashMap<>();

    protected int healWait;

    public EntityDoppelganger(World world) {
        super(world);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
        tasks.addTask(3, new EntityAIWander(this, 0.8D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(5, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));

        healWait = HEAL_WAIT_TICKS;
    }

    /**
     * Should be called once shortly after construction, to initialize things like HP and name.
     */
    public void initialize(EntityPlayer player) {
        this.dataWatcher.updateObject(UUID_DATA_WATCHER_ID, player.getUniqueID().toString());

        String name = StatCollector
                .translateToLocalFormatted("chat.warptheory.doppelganger.name", player.getDisplayName());
        this.setCustomNameTag(name);

        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(player.getMaxHealth());
        this.setHealth(player.getHealth());
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @SuppressWarnings("unchecked")
    public ResourceLocation getPlayerSkin() {
        String uuidString = dataWatcher.getWatchableObjectString(UUID_DATA_WATCHER_ID);
        if (uuidString.isEmpty()) {
            return null;
        }
        UUID uuid = UUID.fromString(uuidString);

        GameProfile gameProfile = gameProfileCache.get(uuid);
        if (gameProfile == null) {
            gameProfile = Minecraft.getMinecraft().func_152347_ac()
                    .fillProfileProperties(new GameProfile(uuid, null), true);
            gameProfileCache.put(uuid, gameProfile);
        }

        SkinManager skinManager = Minecraft.getMinecraft().func_152342_ad();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textureMap = skinManager.func_152788_a(gameProfile);

        if (textureMap.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return skinManager.func_152792_a(
                    textureMap.get(MinecraftProfileTexture.Type.SKIN),
                    MinecraftProfileTexture.Type.SKIN);
        }

        return null;
    }

    @Override
    public void onHeal(LivingHealEvent e) {
        if (e.amount <= 0.5f) {
            // The doppelgänger's passive regen also counts as healing, but we don't want to pass
            // that on to the player. So check for larger than 0.5f healing amount.
            return;
        }

        float currentDamage = getMaxHealth() - getHealth();
        float amount = Math.min(e.amount, currentDamage);
        if (amount <= 0f) {
            return;
        }

        Optional<EntityPlayerMP> player = findPlayer();
        if (!player.isPresent()) {
            return;
        }

        EntityPlayer entityPlayer = player.get();
        if (entityPlayer.getHealth() < entityPlayer.getMaxHealth()) {
            entityPlayer.heal(amount);
            ChatHelper.sendToPlayer(
                    entityPlayer,
                    FormatCodes.Purple.code + FormatCodes.Italic.code
                            + StatCollector.translateToLocal("chat.warptheory.doppelganger.heal"));
        }
    }

    @Override
    public void onHurt(LivingHurtEvent e) {
        Optional<EntityPlayerMP> player = findPlayer();
        if (!player.isPresent()) {
            return;
        }

        EntityPlayer entityPlayer = player.get();
        DamageSource damageSource = DamageSource.causeIndirectMagicDamage(this, this);
        float damage = Math.min(e.ammount, getHealth());
        entityPlayer.attackEntityFrom(damageSource, damage);

        if (getHealth() > e.ammount) {
            ChatHelper.sendToPlayer(
                    entityPlayer,
                    FormatCodes.Purple.code + FormatCodes.Italic.code
                            + StatCollector.translateToLocal("chat.warptheory.doppelganger.hurt"));
        } else {
            ChatHelper.sendToPlayer(
                    entityPlayer,
                    FormatCodes.Purple.code + FormatCodes.Italic.code
                            + StatCollector.translateToLocal("chat.warptheory.doppelganger.die"));
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(UUID_DATA_WATCHER_ID, "");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (getHealth() < getMaxHealth()) {
            if (healWait > 0) {
                healWait--;
            } else {
                healWait = HEAL_WAIT_TICKS;
                // If you increase the healing amount here, you may also need to modify onHeal()
                // to prevent passing this healing on to the player.
                heal(0.5f);
            }
        } else {
            healWait = HEAL_WAIT_TICKS;
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25d);
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return true;
    }

    @Override
    protected Item getDropItem() {
        return Items.rotten_flesh;
    }

    @Override
    protected void dropRareDrop(int par1) {
        Optional<EntityPlayerMP> player = findPlayer();
        if (!player.isPresent()) {
            return;
        }

        ItemStack head = new ItemStack(Items.skull, 1, 2);
        head.setItemDamage(3);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("SkullOwner", player.get().getDisplayName());
        head.setTagCompound(nbt);

        entityDropItem(head, 0.0f);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        String uuid = dataWatcher.getWatchableObjectString(UUID_DATA_WATCHER_ID);
        if (!uuid.isEmpty()) {
            nbt.setString(UUID_NBT_TAG, uuid);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey(UUID_NBT_TAG)) {
            dataWatcher.updateObject(UUID_DATA_WATCHER_ID, nbt.getString(UUID_NBT_TAG));
        }
    }

    /** Searches the server's list of connected players for the targeted player. */
    private Optional<EntityPlayerMP> findPlayer() {
        if (worldObj.isRemote) {
            return Optional.empty();
        }

        String uuidString = dataWatcher.getWatchableObjectString(UUID_DATA_WATCHER_ID);
        if (uuidString.isEmpty()) {
            return Optional.empty();
        }
        UUID uuid = UUID.fromString(uuidString);

        @SuppressWarnings("unchecked")
        List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP entityPlayer : players) {
            if (entityPlayer.getUniqueID().equals(uuid)) {
                return Optional.of(entityPlayer);
            }
        }
        return Optional.empty();
    }
}
