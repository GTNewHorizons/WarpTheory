package shukaro.warptheory.research;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.util.Constants;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

/**
 * Created by Ark on 3/11/2015. stolen from Nividica @ https://github.com/Nividica/ThaumicEnergistics/
 */
public class FakeResearch extends ResearchItem {

    private ResearchItem real;

    private FakeResearch(String key, int col, int row, ResourceLocation icon) {
        super(key, Constants.CATEGORY_WARPTHEORY, new AspectList(), col, row, 1, icon);
        this.setHidden().setStub();
    }

    private FakeResearch(String key, int col, int row, ItemStack icon) {
        super(key, Constants.CATEGORY_WARPTHEORY, new AspectList(), col, row, 1, icon);
        this.setHidden().setStub();
    }

    public static FakeResearch newFake(String key, String realKey, String realCat, int col, int row) {
        FakeResearch fake;

        ResearchItem realResearch = ResearchCategories.getResearch(realKey);

        if (realResearch.icon_item != null) fake = new FakeResearch(key, col, row, realResearch.icon_item);
        else fake = new FakeResearch(key, col, row, realResearch.icon_resource);

        fake.real = realResearch;

        fake.link();

        return fake;
    }

    private void link() {
        if (real.siblings == null) real.setSiblings(this.key);
        else {
            String[] sibs = new String[real.siblings.length + 1];

            System.arraycopy(real.siblings, 0, sibs, 0, real.siblings.length);

            sibs[real.siblings.length] = this.key;

            real.setSiblings(sibs);
        }
    }

    // redirect functions to get info from real research
    @SideOnly(Side.CLIENT)
    @Override
    public String getName() {
        return this.real.getName();
    }

    @Override
    public ResearchPage[] getPages() {
        return this.real.getPages();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getText() {
        return this.real.getText();
    }
}
