package shukaro.warptheory.handlers;

import cpw.mods.fml.common.Loader;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.Configuration;
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

import java.util.function.Consumer;
import java.util.function.Function;

public enum WarpEventRegistry {
    // Default Warp Theory warp events
    BATS(WarpBats::new, "Bats", "spawn bats", true, 15, false, false),
    BLINK(WarpBlink::new, "Blink", "random teleport", true, 30, false, false),
    POISON(
            warp -> new WarpBuff("poison", warp, new PotionEffect(Potion.poison.id, 20 * 20)),
            "Poison", "poison", true, 16, false, false),
    NAUSEA(
            warp -> new WarpBuff("nausea", warp, new PotionEffect(Potion.confusion.id, 20 * 20)),
            "Nausea", "nausea", true, 25, false, false),
    JUMP(
            warp -> new WarpBuff("jump", warp, new PotionEffect(Potion.jump.id, 20 * 20, 20)),
            "Jump", "jump boost", true, 18, false, false),
    BLIND(
            warp -> new WarpBuff("blind", warp, new PotionEffect(Potion.blindness.id, 20 * 20)),
            "Blind", "blindness", true, 43, false, false),
    DECAY(WarpDecay::new, "Decay", "decay", true, 50, true, false),
    EARS(WarpEars::new, "Deaf", "ears (unable to read messages)", true, 12, false, false),
    SWAMP(WarpSwamp::new, "Swamp", "swamp (random trees)", true, 50, true, false),
    TONGUE(WarpTongue::new, "Mute", "tongue (unable to send messages)", true, 11, false, false),
    FRIEND(WarpFriend::new, "Friend", "friendly creeper", true, 26, false, false),
    LIVESTOCK_RAIN(WarpLivestockRain::new, "LiveStockRain", "livestock rain", true, 32, false, false),
    WIND(WarpWind::new, "Wind", "wind", true, 35, false, false),
    CHESTS(WarpChests::new, "Chest", "chest scramble", true, 35, true, false),
    BLOOD(WarpBlood::new, "Blood", "blood", true, 25, false, false),
    ACCELERATION(WarpAcceleration::new, "Acceleration", "acceleration", true, 27, true, false),
    LIGHTNING(WarpLightning::new, "Lightning", "lightning", true, 60, false, false),
    FALL(WarpFall::new, "WorldHole", "world hole", false, 36, true, true),
    RAIN(WarpRain::new, "Rain", "rain", true, 12, true, false),
    WITHER(WarpWither::new, "WitherSpawn", "spawn wither", true, 70, true, false),
    FAKE_EXPLOSION(
            warp -> new WarpFakeSound("fakeexplosion", warp, "random.explode", 8),
            "FakeBoom", "fake explosion", true, 10, false, false),
    FAKE_CREEPER(
            warp -> new WarpFakeSoundBehind("fakecreeper", warp, "creeper.primed", 2),
            "FakeBoomer", "fake creeper", true, 10, false, false),

    // GTNH warp effects
    BLAZE_FIREBALL(WarpBlazeFireball::new, "BlazeFireball", "blaze fireball", true, 100, false, false),
    COIN(WarpCoin::new, "Coin", "coin", true, 100, false, false),
    COUNTDOWN_BOMB(WarpCountdownBomb::new, "CountdownBomb", "countdown bomb", true, 100, true, false),
    DOPPELGANGER(WarpDoppelganger::new, "Doppelganger", "doppelganger", true, 100, false, false),
    ENDER_PEARL(WarpEnderPearl::new, "EnderPearl", "ender pearl", true, 100, false, false),
    ENDERMEN(WarpEndermen::new, "Endermen", "endermen", true, 100, false, false),
    EYE_BLINK(WarpEyeBlink::new, "EyeBlink", "eye blink", true, 100, false, false),
    FAKE_RAIN(WarpFakeRain::new, "FakeRain", "fake rain", true, 100, false, false),
    FIRE_BATS(WarpFireBats::new, "FireBats", "fire bats", true, 100, false, false),
    INSOMNIA(WarpInsomnia::new, "Insomnia", "insomnia", true, 100, false, false),
    INVENTORY_SCRAMBLE(WarpInventoryScramble::new, "InventoryScramble", "inventory scramble", true, 100, false, false),
    INVENTORY_SWAP(WarpInventorySwap::new, "InventorySwap", "inventory swap", true, 100, false, false),
    JUNK(WarpJunk::new, "Junk", "junk", true, 100, false, false),
    LAY_EGGS(WarpLayEggs::new, "LayEggs", "lay eggs", true, 100, false, false),
    LITMUS_PAPER(WarpLitmusPaper::new, "LitmusPaper", "litmus paper", true, 100, false, false),
    MUSHROOMS(WarpMushrooms::new, "Mushrooms", "mushrooms", true, 100, true, false),
    OBSIDIAN(WarpObsidian::new, "Obsidian", "obsidian", true, 100, true, false),
    PHANTOMS(WarpPhantoms::new, "Phantoms", "phantoms", true, 100, false, false),
    PUMPKIN(WarpPumpkin::new, "Pumpkin", "pumpkin", true, 100, false, false),
    SNOW(WarpSnow::new, "Snow", "snow", true, 100, true, false),
    WAND_DRAIN(WarpWandDrain::new, "WandDrain", "wand drain", true, 100, false, false),
    WITHER_POTION(
            warp -> new WarpBuff(
                    "witherpotion", warp,
                    new PotionEffect(Potion.wither.id, 30 * 20, 2),
                    new PotionEffect(Potion.hunger.id, 30 * 20, 2),
                    new PotionEffect(Potion.moveSlowdown.id, 30 * 20, 2)),
            "WitherPotion", "wither potion", true, 100, false, false),
    FAKE_ENDERMAN(
            warp -> new WarpFakeSoundBehind("fakeenderman", warp, "mob.endermen.stare", 2, 1.5f, 0.1f),
            "FakeEnderman", "fake enderman", true, 100, false, false),
    FAKE_WITHER(
            warp -> new WarpFakeSoundBehind("fakewither", warp, "mob.wither.spawn", 2),
            "FakeWither", "fake wither", true, 100, false, false),

    // Requires GregTech
    GREGTECH_FAKE_SOUND(WarpGregTechFakeSound::new, "GregTechFakeSound", "GregTech fake sound", true, 100, false, false) {
        @Override
        public void createWarpEvent(Consumer<IWarpEvent> consumer) {
            if (Loader.isModLoaded("gregtech") && Loader.isModLoaded("IC2NuclearControl")) {
                super.createWarpEvent(consumer);
            }
        }
    };

    private static final int MAX_WARP_FOR_EFFECTS = 200;

    private static final String CONFIG_ENABLED_NAME_FORMAT_STRING = "allow%sEffect";
    private static final String CONFIG_ENABLED_DESCRIPTION_FORMAT_STRING =
            "Whether to allow %s warp effect.";
    private static final String CONFIG_ENABLED_DESCRIPTION_SERVER_KICK =
            " May cause server errors.";
    private static final String CONFIG_MIN_WARP_NAME_FORMAT_STRING = "minWarp%sEffect";
    private static final String CONFIG_MIN_WARP_DESCRIPTION_FORMAT_STRING =
            "Min warp required until %s can happen.";


    private final Function<Integer, IWarpEvent> constructor;
    private final String name;
    private final String description;
    private final boolean defaultEnabled;
    private final int defaultMinWarp;
    private final boolean isGlobal;
    private final boolean isServerKick;

    private boolean isEnabled;
    private int minWarp;

    WarpEventRegistry(
            Function<Integer, IWarpEvent> constructor,
            String name, String description,
            boolean defaultEnabled, int defaultMinWarp,
            boolean isGlobal, boolean isServerKick) {
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
        String enabledDescription =
                String.format(CONFIG_ENABLED_DESCRIPTION_FORMAT_STRING, description);
        if (isServerKick) {
            enabledDescription += CONFIG_ENABLED_DESCRIPTION_SERVER_KICK;
        }
        isEnabled =
                config.getBoolean(
                        String.format(CONFIG_ENABLED_NAME_FORMAT_STRING, name),
                        "warp_effects", defaultEnabled, enabledDescription);

        minWarp =
                config.getInt(
                        String.format(CONFIG_MIN_WARP_NAME_FORMAT_STRING, name),
                        "warp_levels", defaultMinWarp, 1, MAX_WARP_FOR_EFFECTS,
                        String.format(CONFIG_MIN_WARP_DESCRIPTION_FORMAT_STRING, description));
    }

    /**
     * This method should only be called after {@link #loadConfig(Configuration)} has been called.
     */
    public void createWarpEvent(Consumer<IWarpEvent> consumer) {
        if (isEnabled
                && (!isGlobal || ConfigHandler.allowGlobalWarpEffects)
                && (!isServerKick || ConfigHandler.allowServerKickWarpEffects)) {
            consumer.accept(constructor.apply(minWarp));
        }
    }
}