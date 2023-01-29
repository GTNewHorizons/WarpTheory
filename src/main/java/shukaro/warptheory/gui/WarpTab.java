package shukaro.warptheory.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import shukaro.warptheory.items.WarpItems;

public class WarpTab extends CreativeTabs {

    public WarpTab(String label) {
        super(label);
    }

    @Override
    public Item getTabIconItem() {
        return WarpItems.itemCleanser;
    }
}
