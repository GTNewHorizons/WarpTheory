package shukaro.warptheory.recipe;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/** Helper class for registering Thaumcraft aspects. */
public class WarpAspects {

    public static void init() {
        // Items already inherit aspects based on their recipe, so we don't need to do much there.
        // Entities, however, do not appear to inherit, so we must add them ourselves.
        ThaumcraftApi.registerEntityTag(
                "WarpTheory.creeperPassive",
                (new AspectList()).add(Aspect.PLANT, 2),
                new ThaumcraftApi.EntityTagsNBT[0]);
        ThaumcraftApi.registerEntityTag(
                "WarpTheory.creeperPassive",
                (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.ENERGY, 2),
                new ThaumcraftApi.EntityTagsNBT[] {
                        new ThaumcraftApi.EntityTagsNBT("powered", Byte.valueOf((byte) 1)) });
        ThaumcraftApi.registerEntityTag(
                "WarpTheory.creeperFake",
                (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.FIRE, 1),
                new ThaumcraftApi.EntityTagsNBT[0]);
        ThaumcraftApi.registerEntityTag(
                "WarpTheory.creeperFake",
                (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.FIRE, 1).add(Aspect.ENERGY, 2),
                new ThaumcraftApi.EntityTagsNBT[] {
                        new ThaumcraftApi.EntityTagsNBT("powered", Byte.valueOf((byte) 1)) });
        ThaumcraftApi.registerEntityTag(
                "WarpTheory.doppelganger",
                (new AspectList()).add(Aspect.MAN, 2).add(Aspect.UNDEAD, 1).add(Aspect.TAINT, 1),
                new ThaumcraftApi.EntityTagsNBT[0]);
        // Not that I expect anyone to actually manage to scan this...
        ThaumcraftApi.registerEntityTag(
                "WarpTheory.phantom",
                (new AspectList()).add(Aspect.DARKNESS, 3).add(Aspect.MAGIC, 1),
                new ThaumcraftApi.EntityTagsNBT[0]);
        ThaumcraftApi.registerEntityTag(
                "WarpTheory.taintSheepSafe",
                (new AspectList()).add(Aspect.TAINT, 2).add(Aspect.EARTH, 2),
                new ThaumcraftApi.EntityTagsNBT[0]);
    }
}
