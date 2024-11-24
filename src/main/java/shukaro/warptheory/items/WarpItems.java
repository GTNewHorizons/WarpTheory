package shukaro.warptheory.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class WarpItems {

    public static ItemCleanserMinor itemCleanserMinor;
    public static ItemCleanser itemCleanser;
    public static ItemAmulet itemAmulet;
    public static ItemSomething itemSomething;
    public static ItemPaper itemPaper;
    public static ItemWarpWardAmulet itemWarpWardAmulet;

    public static void initItems() {
        itemCleanserMinor = new ItemCleanserMinor();
        itemCleanser = new ItemCleanser();
        itemAmulet = new ItemAmulet();

        itemSomething = new ItemSomething();
        itemPaper = new ItemPaper();
        GameRegistry.registerItem(itemCleanserMinor, itemCleanserMinor.getUnlocalizedName());
        GameRegistry.registerItem(itemCleanser, itemCleanser.getUnlocalizedName());
        GameRegistry.registerItem(itemAmulet, itemAmulet.getUnlocalizedName());

        GameRegistry.registerItem(itemSomething, itemSomething.getUnlocalizedName());
        GameRegistry.registerItem(itemPaper, itemPaper.getUnlocalizedName());
        if (Loader.isModLoaded("dreamcraft")) { // the item will not be loaded without nhcoremod, however it is
                                                // available to be functional for users outside GTNH being able
                                                // to add their own recipe for it.
            itemWarpWardAmulet = new ItemWarpWardAmulet();
            GameRegistry.registerItem(itemWarpWardAmulet, itemWarpWardAmulet.getUnlocalizedName());
        }
    }
}
