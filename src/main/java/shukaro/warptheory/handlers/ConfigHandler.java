package shukaro.warptheory.handlers;

import java.io.File;
import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import shukaro.warptheory.util.Constants;

/**
 * Created by Ark on 3/7/2015. code in part provided by pahimar and TheOldOne822
 */
public class ConfigHandler {

    public static Configuration config;
    public static boolean wussMode = false;
    public static boolean disableRebound = false;
    public static int permWarpMult = 4;
    public static boolean allowPermWarpRemoval = true;
    public static boolean allowGlobalWarpEffects = false;
    public static boolean allowMultiWarpEffects = false;
    public static boolean allowWarpEffects = false;
    public static boolean allowServerKickWarpEffects = false;

    public static void init(File configFile) {
        if (config == null) {
            // Create the config object from the given config file
            config = new Configuration(configFile);

            config.addCustomCategoryComment("general", "Change the inner workings of the mod requires restart");
            config.addCustomCategoryComment(
                    "warp_effects",
                    "Toggle specific warp effect, If all disabled Pure Tear will not work");
            config.setCategoryRequiresMcRestart("general", true);

            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        wussMode = config.getBoolean("wussMode", "general", false, "enables less expensive recipes");
        disableRebound = config.getBoolean(
                "disableRebound",
                "general",
                false,
                "disable warp events ignoring warpwarding which occur by Pure Tear");
        permWarpMult = config.getInt(
                "permWarpMult",
                "general",
                1,
                0,
                Integer.MAX_VALUE,
                "how much more 'expensive' permanent warp is compared to normal warp");
        allowPermWarpRemoval = config
                .getBoolean("allowPermWarpRemoval", "general", true, "whether items can remove permanent warp or not");
        allowGlobalWarpEffects = config.getBoolean(
                "allowGlobalWarpEffects",
                "general",
                true,
                "whether warp effects that involve the environment are triggered");
        allowMultiWarpEffects = config.getBoolean(
                "allowMultiWarpEffects",
                "general",
                true,
                "whether multi-level warp effects are allowed. If false, only the first level will trigger");
        allowWarpEffects = config.getBoolean(
                "addWarpEffects",
                "general",
                true,
                "whether to add general warp effects. If false extra effects will only be seen when using Pure Tear");
        allowServerKickWarpEffects = config.getBoolean(
                "allowServerErrorWarpEffects",
                "general",
                false,
                "whether to allow warp effects known to cause errors on servers");

        Arrays.stream(WarpEventRegistry.values()).forEach(warpEvent -> warpEvent.loadConfig(config));

        if (config.hasChanged()) config.save();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Constants.modID)) {
            loadConfiguration();
        }
    }
}
