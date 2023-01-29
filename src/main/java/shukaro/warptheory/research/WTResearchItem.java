package shukaro.warptheory.research;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import shukaro.warptheory.util.Constants;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;

/**
 * Created by Ark on 3/8/2015.
 */
public class WTResearchItem extends ResearchItem {

    public WTResearchItem(String key) {
        super(key, Constants.CATEGORY_WARPTHEORY);
    }

    public WTResearchItem(String key, AspectList tags, int col, int row, int complex, ResourceLocation icon) {
        super(key, Constants.CATEGORY_WARPTHEORY, tags, col, row, complex, icon);
    }

    public WTResearchItem(String key, AspectList tags, int[] pos, int complex, ResourceLocation icon) {
        super(key, Constants.CATEGORY_WARPTHEORY, tags, pos[0], pos[1], complex, icon);
    }

    public WTResearchItem(String key, AspectList tags, int col, int row, int complex, ItemStack icon) {
        super(key, Constants.CATEGORY_WARPTHEORY, tags, col, row, complex, icon);
    }
}
