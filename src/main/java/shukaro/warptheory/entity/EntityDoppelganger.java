package shukaro.warptheory.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.util.ChatHelper;

public class EntityDoppelganger extends EntityCreature {

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
    protected static final String NAME_KEY = "chat.warptheory.doppelganger.name";

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

        // Store only the player name; the visible name is translated per client in func_145748_c_
        this.setCustomNameTag(player.getDisplayName());

        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(player.getMaxHealth());
        this.setHealth(player.getHealth());
    }

    @Override
    public String getCommandSenderName() {
        return StatCollector.translateToLocalFormatted(NAME_KEY, getCustomNameTag());
    }

    @Override
    public IChatComponent func_145748_c_() {
        // Used by the name tag renderer and death messages, so each client translates it
        return new ChatComponentTranslation(NAME_KEY, getCustomNameTag());
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    public ResourceLocation getPlayerSkin() {
        String uuidString = dataWatcher.getWatchableObjectString(UUID_DATA_WATCHER_ID);
        if (uuidString.isEmpty()) {
            return null;
        }
        UUID uuid = UUID.fromString(uuidString);

        GameProfile gameProfile = gameProfileCache.get(uuid);
        if (gameProfile == null) {
            gameProfile = new GameProfile(uuid, null);
            gameProfileCache.put(uuid, gameProfile);
            CompletableFuture
                    .supplyAsync(
                            () -> Minecraft.getMinecraft().func_152347_ac()
                                    .fillProfileProperties(new GameProfile(uuid, null), true))
                    .thenAccept(p -> Minecraft.getMinecraft().func_152344_a(() -> gameProfileCache.put(uuid, p)));
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
    public void heal(float amount) {
        float initialHealth = getHealth();
        super.heal(amount);

        float healthRestored = getHealth() - initialHealth;
        if (healthRestored <= 0f) return;

        Optional<EntityPlayerMP> player = findPlayer();
        if (!player.isPresent()) return;

        EntityPlayerMP entityPlayer = player.get();
        if (entityPlayer.getHealth() < entityPlayer.getMaxHealth()) {
            entityPlayer.heal(healthRestored);
            ChatHelper
                    .sendTranslatedToPlayer(entityPlayer, "chat.warptheory.doppelganger.heal", ChatHelper.warpStyle());
        }
    }

    @Override
    protected void damageEntity(DamageSource source, float amount) {
        Optional<EntityPlayerMP> player = findPlayer();
        if (player.isPresent()) {
            EntityPlayer entityPlayer = player.get();
            DamageSource damageSource = DamageSource.causeIndirectMagicDamage(this, this);
            float damage = Math.min(amount, getHealth());
            entityPlayer.attackEntityFrom(damageSource, damage);
        }

        super.damageEntity(source, amount);

        if (player.isPresent()) {
            ChatHelper.sendTranslatedToPlayer(
                    player.get(),
                    isEntityAlive() ? "chat.warptheory.doppelganger.hurt" : "chat.warptheory.doppelganger.die",
                    ChatHelper.warpStyle());
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
                super.heal(0.5f);
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

        List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP entityPlayer : players) {
            if (entityPlayer.getUniqueID().equals(uuid)) {
                return Optional.of(entityPlayer);
            }
        }
        return Optional.empty();
    }
}
