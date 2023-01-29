package shukaro.warptheory.handlers.warpevents;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpDecay extends IWorldTickWarpEvent {

    public WarpDecay(int minWarp) {
        super("biomeDecay", minWarp, world -> 512 + world.rand.nextInt(256));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canDo(World world, EntityPlayer player) {
        for (String n : (Set<String>) MiscHelper.getWarpTag(player).func_150296_c()) {
            if (n.startsWith("biome") && !n.equals(getName())) return false;
        }
        return true;
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target = RandomBlockHelper
                .randomBlock(world, player, 8, block -> MiscHelper.hasNonSolidNeighbor(world, block));
        if (target == null) {
            return 0;
        }

        NameMetaPair pair = new NameMetaPair(target.getBlock(world), target.getMeta(world));
        if (WarpHandler.decayMappings.containsKey(pair) || pair.getBlock() instanceof IPlantable
                || pair.getBlock().getMaterial() == Material.leaves) {
            NameMetaPair decayed = WarpHandler.decayMappings.get(pair);
            if (decayed == null) decayed = new NameMetaPair(Blocks.air, 0);
            if (world.setBlock(target.x, target.y, target.z, decayed.getBlock(), decayed.getMetadata(), 3)) {
                if (target.isAir(world)) world.playAuxSFXAtEntity(
                        null,
                        2001,
                        target.x,
                        target.y,
                        target.z,
                        Block.getIdFromBlock(pair.getBlock()) + (pair.getMetadata() << 12));
                return 1;
            }
        }

        return 0;
    }
}
