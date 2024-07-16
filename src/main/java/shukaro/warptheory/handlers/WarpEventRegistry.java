package shukaro.warptheory.handlers;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Loader;
import shukaro.warptheory.handlers.warpevents.WarpAcceleration;
import shukaro.warptheory.handlers.warpevents.WarpBats;
import shukaro.warptheory.handlers.warpevents.WarpBlazeFireball;
import shukaro.warptheory.handlers.warpevents.WarpBlink;
import shukaro.warptheory.handlers.warpevents.WarpBlood;
import shukaro.warptheory.handlers.warpevents.WarpBuff;
import shukaro.warptheory.handlers.warpevents.WarpChests;
import shukaro.warptheory.handlers.warpevents.WarpCoin;
import shukaro.warptheory.handlers.warpevents.WarpCountdownBomb;
import shukaro.warptheory.handlers.warpevents.WarpDecay;
import shukaro.warptheory.handlers.warpevents.WarpDoppelganger;
import shukaro.warptheory.handlers.warpevents.WarpEars;
import shukaro.warptheory.handlers.warpevents.WarpEnderPearl;
import shukaro.warptheory.handlers.warpevents.WarpEndermen;
import shukaro.warptheory.handlers.warpevents.WarpEyeBlink;
import shukaro.warptheory.handlers.warpevents.WarpFakeRain;
import shukaro.warptheory.handlers.warpevents.WarpFakeSound;
import shukaro.warptheory.handlers.warpevents.WarpFakeSoundBehind;
import shukaro.warptheory.handlers.warpevents.WarpFakeSoundRateLimited;
import shukaro.warptheory.handlers.warpevents.WarpFall;
import shukaro.warptheory.handlers.warpevents.WarpFireBats;
import shukaro.warptheory.handlers.warpevents.WarpFriend;
import shukaro.warptheory.handlers.warpevents.WarpGregTechFakeSound;
import shukaro.warptheory.handlers.warpevents.WarpInsomnia;
import shukaro.warptheory.handlers.warpevents.WarpInventoryScramble;
import shukaro.warptheory.handlers.warpevents.WarpInventorySwap;
import shukaro.warptheory.handlers.warpevents.WarpJunk;
import shukaro.warptheory.handlers.warpevents.WarpLayEggs;
import shukaro.warptheory.handlers.warpevents.WarpLightning;
import shukaro.warptheory.handlers.warpevents.WarpLitmusPaper;
import shukaro.warptheory.handlers.warpevents.WarpLivestockRain;
import shukaro.warptheory.handlers.warpevents.WarpMessage;
import shukaro.warptheory.handlers.warpevents.WarpMushrooms;
import shukaro.warptheory.handlers.warpevents.WarpObsidian;
import shukaro.warptheory.handlers.warpevents.WarpPhantoms;
import shukaro.warptheory.handlers.warpevents.WarpPumpkin;
import shukaro.warptheory.handlers.warpevents.WarpRain;
import shukaro.warptheory.handlers.warpevents.WarpSnow;
import shukaro.warptheory.handlers.warpevents.WarpSwamp;
import shukaro.warptheory.handlers.warpevents.WarpTongue;
import shukaro.warptheory.handlers.warpevents.WarpWandDrain;
import shukaro.warptheory.handlers.warpevents.WarpWind;
import shukaro.warptheory.handlers.warpevents.WarpWither;

public enum WarpEventRegistry {

    // Default Warp Theory warp events
    BATS(WarpBats::new, "Bats", "spawn bats", true, 30, false, false),
    BLINK(WarpBlink::new, "Blink", "random teleport", true, 130, false, false),
    POISON(warp -> new WarpBuff("poison", warp, true, new PotionEffect(Potion.poison.id, 20 * 20)), "Poison", "poison",
            true, 65, false, false),
    NAUSEA(warp -> new WarpBuff("nausea", warp, true, new PotionEffect(Potion.confusion.id, 20 * 20)), "Nausea",
            "nausea", true, 45, false, false),
    JUMP(warp -> new WarpBuff("jump", warp, true, new PotionEffect(Potion.jump.id, 20 * 20, 20)), "Jump", "jump boost",
            true, 45, false, false),
    BLIND(warp -> new WarpBuff("blind", warp, true, new PotionEffect(Potion.blindness.id, 20 * 20)), "Blind",
            "blindness", true, 65, false, false),
    DECAY(WarpDecay::new, "Decay", "decay", true, 180, true, false),
    EARS(WarpEars::new, "Deaf", "ears (unable to read messages)", false, 75, false, false),
    SWAMP(WarpSwamp::new, "Swamp", "swamp (random trees)", true, 190, true, false),
    TONGUE(WarpTongue::new, "Mute", "tongue (unable to send messages)", false, 75, false, false),
    FRIEND(WarpFriend::new, "Friend", "friendly creeper", true, 40, false, false),
    LIVESTOCK_RAIN(WarpLivestockRain::new, "LiveStockRain", "livestock rain", true, 70, false, false),
    WIND(WarpWind::new, "Wind", "wind", true, 85, false, false),
    CHESTS(WarpChests::new, "Chest", "chest scramble", false, 175, true, false),
    BLOOD(WarpBlood::new, "Blood", "blood", true, 35, false, false),
    ACCELERATION(WarpAcceleration::new, "Acceleration", "acceleration", true, 140, true, false),
    LIGHTNING(WarpLightning::new, "Lightning", "lightning", true, 120, false, false),
    FALL(WarpFall::new, "WorldHole", "world hole", false, 150, true, true),
    RAIN(WarpRain::new, "Rain", "rain", true, 55, true, false),
    WITHER(WarpWither::new, "WitherSpawn", "spawn wither", true, 200, true, false),
    FAKE_EXPLOSION(warp -> new WarpFakeSound("fakeexplosion", warp, "random.explode", 8), "FakeBoom", "fake explosion",
            true, 10, false, false),
    FAKE_CREEPER(warp -> new WarpFakeSoundBehind("fakecreeper", warp, "creeper.primed", 2), "FakeBoomer",
            "fake creeper", true, 25, false, false),

    // GTNH warp effects
    BLAZE_FIREBALL(WarpBlazeFireball::new, "BlazeFireball", "blaze fireball", true, 95, false, false),
    COIN(WarpCoin::new, "Coin", "coin", true, 15, false, false),
    COUNTDOWN_BOMB(WarpCountdownBomb::new, "CountdownBomb", "countdown bomb", true, 160, true, false),
    DOPPELGANGER(WarpDoppelganger::new, "Doppelganger", "doppelganger", true, 75, false, false),
    ENDER_PEARL(WarpEnderPearl::new, "EnderPearl", "ender pearl", true, 90, false, false),
    ENDERMEN(WarpEndermen::new, "Endermen", "endermen", true, 80, false, false),
    EYE_BLINK(WarpEyeBlink::new, "EyeBlink", "eye blink", false, 5, false, false),
    FAKE_RAIN(WarpFakeRain::new, "FakeRain", "fake rain", true, 25, false, false),
    FIRE_BATS(WarpFireBats::new, "FireBats", "fire bats", true, 60, false, false),
    INSOMNIA(WarpInsomnia::new, "Insomnia", "insomnia", true, 50, false, false),
    INVENTORY_SCRAMBLE(WarpInventoryScramble::new, "InventoryScramble", "inventory scramble", true, 150, false, false),
    INVENTORY_SWAP(WarpInventorySwap::new, "InventorySwap", "inventory swap", true, 125, false, false),
    JUNK(WarpJunk::new, "Junk", "junk", true, 100, false, false),
    LAY_EGGS(WarpLayEggs::new, "LayEggs", "lay eggs", true, 20, false, false),
    LITMUS_PAPER(WarpLitmusPaper::new, "LitmusPaper", "litmus paper", true, 100, false, false),
    MESSAGE(WarpMessage::new, "Message", "message", true, 5, false, false),
    MUSHROOMS(WarpMushrooms::new, "Mushrooms", "mushrooms", true, 170, true, false),
    OBSIDIAN(WarpObsidian::new, "Obsidian", "obsidian", true, 110, true, false),
    PHANTOMS(WarpPhantoms::new, "Phantoms", "phantoms", true, 20, false, false),
    PUMPKIN(WarpPumpkin::new, "Pumpkin", "pumpkin", true, 15, false, false),
    SNOW(WarpSnow::new, "Snow", "snow", true, 175, true, false),
    WAND_DRAIN(WarpWandDrain::new, "WandDrain", "wand drain", true, 50, false, false),
    WITHER_POTION(
            warp -> new WarpBuff(
                    "witherpotion",
                    warp,
                    false,
                    new PotionEffect(Potion.wither.id, 30 * 20, 2),
                    new PotionEffect(Potion.hunger.id, 30 * 20, 2),
                    new PotionEffect(Potion.moveSlowdown.id, 30 * 20, 2)),
            "WitherPotion", "wither potion", true, 80, false, false),
    FAKE_ENDERMAN(warp -> new WarpFakeSound("fakeenderman", warp, "mob.endermen.stare", 0, 1.5f, 0.1f), "FakeEnderman",
            "fake enderman", true, 35, false, false),
    FAKE_WITHER(warp -> new WarpFakeSound("fakewither", warp, "warptheory:fakewither", 0, 1.5f, 0.9f), "FakeWither",
            "fake wither", true, 150, false, false),

    // Requires GregTech
    GREGTECH_FAKE_SOUND(WarpGregTechFakeSound::new, "GregTechFakeSound", "GregTech fake sound", true, 30, false,
            false) {

        @Override
        public void createWarpEvent(Consumer<IWarpEvent> consumer) {
            if (Loader.isModLoaded("gregtech") && Loader.isModLoaded("IC2NuclearControl")) {
                super.createWarpEvent(consumer);
            }
        }
    },
    FAKE_DISCORD_PING(warp -> new WarpFakeSoundRateLimited("fakediscordping", warp, "warptheory:discordping"),
            "FakeDiscordPing", "fake discord ping", true, 50, false, false),
    FAKE_DISCORD_VOICE_CHANNEL_JOIN(
            warp -> new WarpFakeSoundRateLimited(
                    "fakediscordvoicechanneljoin",
                    warp,
                    "warptheory:discordvoicechanneljoin"),
            "FakeDiscordVoiceChannelJoin", "fake discord voice channel join", true, 100, false, false);

    private static final int MAX_WARP_FOR_EFFECTS = 200;

    private static final String CONFIG_ENABLED_NAME_FORMAT_STRING = "allow%sEffect";
    private static final String CONFIG_ENABLED_DESCRIPTION_FORMAT_STRING = "Whether to allow %s warp effect.";
    private static final String CONFIG_ENABLED_DESCRIPTION_SERVER_KICK = " May cause server errors.";
    private static final String CONFIG_MIN_WARP_NAME_FORMAT_STRING = "minWarp%sEffect";
    private static final String CONFIG_MIN_WARP_DESCRIPTION_FORMAT_STRING = "Min warp required until %s can happen.";

    private final Function<Integer, IWarpEvent> constructor;
    private final String name;
    private final String description;
    private final boolean defaultEnabled;
    private final int defaultMinWarp;
    private final boolean isGlobal;
    private final boolean isServerKick;

    private boolean isEnabled;
    private int minWarp;

    WarpEventRegistry(Function<Integer, IWarpEvent> constructor, String name, String description,
            boolean defaultEnabled, int defaultMinWarp, boolean isGlobal, boolean isServerKick) {
        this.constructor = constructor;
        this.name = name;
        this.description = description;
        this.defaultEnabled = defaultEnabled;
        this.defaultMinWarp = defaultMinWarp;
        this.isGlobal = isGlobal;
        this.isServerKick = isServerKick;

        this.isEnabled = false;
        this.minWarp = 0;
    }

    public void loadConfig(Configuration config) {
        String enabledDescription = String.format(CONFIG_ENABLED_DESCRIPTION_FORMAT_STRING, description);
        if (isServerKick) {
            enabledDescription += CONFIG_ENABLED_DESCRIPTION_SERVER_KICK;
        }
        isEnabled = config.getBoolean(
                String.format(CONFIG_ENABLED_NAME_FORMAT_STRING, name),
                "warp_effects",
                defaultEnabled,
                enabledDescription);

        minWarp = config.getInt(
                String.format(CONFIG_MIN_WARP_NAME_FORMAT_STRING, name),
                "warp_levels",
                defaultMinWarp,
                1,
                MAX_WARP_FOR_EFFECTS,
                String.format(CONFIG_MIN_WARP_DESCRIPTION_FORMAT_STRING, description));
    }

    /**
     * This method should only be called after {@link #loadConfig(Configuration)} has been called.
     */
    public void createWarpEvent(Consumer<IWarpEvent> consumer) {
        if (isEnabled && (!isGlobal || ConfigHandler.allowGlobalWarpEffects)
                && (!isServerKick || ConfigHandler.allowServerKickWarpEffects)) {
            consumer.accept(constructor.apply(minWarp));
        }
    }
}
