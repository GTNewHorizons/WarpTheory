package shukaro.warptheory.block;

import shukaro.warptheory.tile.TileEntityVanish;
import cpw.mods.fml.common.registry.GameRegistry;

public class WarpBlocks {

    public static BlockVanish blockVanish;

    public static void initBlocks() {
        blockVanish = new BlockVanish();
        GameRegistry.registerBlock(blockVanish, "blockVanish");
        GameRegistry.registerTileEntity(TileEntityVanish.class, "tileEntityVanish");
    }
}
