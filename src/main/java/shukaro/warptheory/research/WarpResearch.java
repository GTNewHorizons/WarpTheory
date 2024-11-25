package shukaro.warptheory.research;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import shukaro.warptheory.items.WarpItems;
import shukaro.warptheory.recipe.WarpRecipes;
import shukaro.warptheory.util.Constants;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchPage;

public class WarpResearch {

    public static HashMap<String, Object> recipes = new HashMap();

    public static void init() {
        ResearchCategories.registerCategory(
                Constants.CATEGORY_WARPTHEORY,
                new ResourceLocation("warptheory", "textures/items/r_itemCleanser.png"),
                new ResourceLocation("thaumcraft", "textures/gui/gui_researchbackeldritch.png"));

        // create pseudo research for Sanity Soap
        FakeResearch.newFake("FAKESOAP", "SANESOAP", "ALCHEMY", 0, 0).registerResearchItem();

        FakeResearch.newFake("FAKEELDMAJOR", "ELDRITCHMAJOR", "ELDRITCH", 0, -3).registerResearchItem();

        // attempt to make lesser pure tear
        new WTResearchItem(
                Constants.ITEM_WARPCLEANSERMINOR,
                new AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.EXCHANGE, 1),
                -1,
                -2,
                2,
                new ItemStack(WarpItems.itemCleanserMinor)).setPages(
                        new ResearchPage[] {
                                new ResearchPage(
                                        StatCollector.translateToLocal("research.warptheory.warpcleanserminor")),
                                new ResearchPage((InfusionRecipe) recipes.get("PureTearMinor")) })
                        .setParents("ELDRITCHMAJOR", Constants.ITEM_LITMUS, "FAKEELDMAJOR").setHidden()
                        .setItemTriggers(ItemApi.getItem("itemSanitySoap", 0)).registerResearchItem();

        new WTResearchItem(
                Constants.ITEM_WARPCLEANSER,
                new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.EXCHANGE, 3),
                -3,
                -2,
                2,
                new ItemStack(WarpItems.itemCleanser)).setPages(
                        new ResearchPage[] {
                                new ResearchPage(StatCollector.translateToLocal("research.warptheory.warpcleanser")),
                                new ResearchPage((InfusionRecipe) recipes.get("PureTear")) })
                        .setParents("ELDRITCHMAJOR", Constants.ITEM_LITMUS, "FAKEELDMAJOR").setHidden()
                        .setItemTriggers(ItemApi.getItem("itemSanitySoap", 0)).registerResearchItem();

        new WTResearchItem(
                Constants.ITEM_PURE_TALISMAN,
                new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.EXCHANGE, 3).add(Aspect.MAGIC, 3),
                -4,
                -3,
                3,
                new ItemStack(WarpItems.itemAmulet))
                        .setPages(
                                new ResearchPage[] {
                                        new ResearchPage(StatCollector.translateToLocal("research.warptheory.amulet")),
                                        new ResearchPage((InfusionRecipe) recipes.get("PureAmulet")) })
                        .setParents(Constants.ITEM_WARPCLEANSER).setConcealed().registerResearchItem();

        new WTResearchItem(
                Constants.ITEM_PORTABLE_SHOWER,
                new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.EXCHANGE, 3).add(Aspect.MAGIC, 3),
                5,
                5,
                3,
                new ItemStack(WarpItems.itemWarpWardAmulet)).setPages(
                        new ResearchPage[] {
                                new ResearchPage(StatCollector.translateToLocal("research.warptheory.portableshower")),
                                new ResearchPage((InfusionRecipe) recipes.get("WarpWardAmulet")) })
                        .setParents(Constants.ITEM_PURE_TALISMAN).setConcealed().registerResearchItem();

        ResearchPage[] somethingPages = new ResearchPage[WarpRecipes.meats.size() + 1];
        somethingPages[0] = new ResearchPage(StatCollector.translateToLocal("research.warptheory.warpsomething"));
        int i = 1;
        for (ItemStack meat : WarpRecipes.meats) {
            somethingPages[i] = (new ResearchPage((CrucibleRecipe) recipes.get("WarpChunk" + meat)));
            i++;
        }

        new WTResearchItem(
                Constants.ITEM_SOMETHING,
                new AspectList().add(Aspect.ELDRITCH, 3),
                2,
                0,
                1,
                new ItemStack(WarpItems.itemSomething)).setPages(somethingPages).setParents("FAKESOAP").setSecondary()
                        .setAspectTriggers(Aspect.ELDRITCH, Aspect.FLESH, Aspect.EXCHANGE).registerResearchItem();
        ThaumcraftApi.addWarpToResearch(Constants.ITEM_SOMETHING, 2);

        new WTResearchItem(
                Constants.ITEM_LITMUS,
                new AspectList().add(Aspect.ELDRITCH, 3).add(Aspect.MAGIC, 3),
                -2,
                0,
                2,
                new ItemStack(WarpItems.itemPaper))
                        .setPages(
                                new ResearchPage[] {
                                        new ResearchPage(StatCollector.translateToLocal("research.warptheory.paper")),
                                        new ResearchPage((ShapelessArcaneRecipe) recipes.get("Litmus")) })
                        .setConcealed().setParents("FAKESOAP")
                        .setAspectTriggers(Aspect.ELDRITCH, Aspect.MAGIC, Aspect.SENSES).registerResearchItem();
    }
}
