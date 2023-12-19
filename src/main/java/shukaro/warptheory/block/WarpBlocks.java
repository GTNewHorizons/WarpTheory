package shukaro.warptheory.block;

import cpw.mods.fml.common.registry.GameRegistry;
import shukaro.warptheory.tile.TileEntityVanish;

public class WarpBlocks {

    public static BlockVanish blockVanish;

    public static void initBlocks() {
        blockVanish = new BlockVanish();
        GameRegistry.registerBlock(blockVanish, "blockVanish");
        GameRegistry.registerTileEntity(TileEntityVanish.class, "tileEntityVanish");
    }
}
