package shukaro.warptheory.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import shukaro.warptheory.handlers.ConfigHandler;
import shukaro.warptheory.util.Constants;

/**
 * Created by Ark on 3/8/2015.
 */
public class WarpTheoryGuiConfig extends GuiConfig {

    public WarpTheoryGuiConfig(GuiScreen parent) {
        super(
                parent,
                getConfigObjects(),
                Constants.modID,
                false,
                false,
                cpw.mods.fml.client.config.GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }

    private static List<IConfigElement> getConfigObjects() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.addAll(new ConfigElement(ConfigHandler.config.getCategory("general")).getChildElements());

        list.addAll(new ConfigElement(ConfigHandler.config.getCategory("warp_effects")).getChildElements());

        return list;
    }
}
