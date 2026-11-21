package shukaro.warptheory.items;

import cpw.mods.fml.common.registry.GameRegistry;

public class WarpItems {

    public static final ItemCleanserMinor itemCleanserMinor = new ItemCleanserMinor();
    public static final ItemCleanser itemCleanser = new ItemCleanser();
    public static final ItemAmulet itemAmulet = new ItemAmulet();
    public static final ItemSomething itemSomething = new ItemSomething();
    public static final ItemPaper itemPaper = new ItemPaper();
    public static final ItemWarpWardAmulet itemWarpWardAmulet = new ItemWarpWardAmulet();
    public static final ItemOblivionPotion itemOblivionPotion = new ItemOblivionPotion();

    public static void initItems() {
        GameRegistry.registerItem(itemCleanserMinor, itemCleanserMinor.getUnlocalizedName());
        GameRegistry.registerItem(itemCleanser, itemCleanser.getUnlocalizedName());
        GameRegistry.registerItem(itemAmulet, itemAmulet.getUnlocalizedName());
        GameRegistry.registerItem(itemWarpWardAmulet, itemWarpWardAmulet.getUnlocalizedName());

        GameRegistry.registerItem(itemSomething, itemSomething.getUnlocalizedName());
        GameRegistry.registerItem(itemPaper, itemPaper.getUnlocalizedName());
        GameRegistry.registerItem(itemOblivionPotion, itemOblivionPotion.getUnlocalizedName());
    }
}
