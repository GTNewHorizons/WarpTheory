package shukaro.warptheory.items;

import cpw.mods.fml.common.registry.GameRegistry;

public class WarpItems {

    public static ItemCleanserMinor itemCleanserMinor;
    public static ItemCleanser itemCleanser;
    public static ItemAmulet itemAmulet;
    public static ItemSomething itemSomething;
    public static ItemPaper itemPaper;

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
    }
}
