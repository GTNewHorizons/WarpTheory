package shukaro.warptheory.handlers.warpevents;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpSnow extends IWorldTickWarpEvent {

    public WarpSnow(int minWarp) {
        super("biomeSnow", minWarp, world -> 16 + world.rand.nextInt(16));
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
    @SuppressWarnings("unchecked")
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target = RandomBlockHelper
                .randomBlock(world, player, 16, block -> MiscHelper.hasNonSolidNeighbor(world, block));
        if (target == null) {
            return 0;
        }

        BlockCoord below = target.copy().offset(0);
        if (target.isAir(world) && below.isTopSolid(world)) {
            world.setBlock(target.x, target.y, target.z, Blocks.snow_layer);
            world.playSoundEffect(target.x, target.y, target.z, "dig.snow", 1.0F, 1.0F);
            return 1;
        }

        if (target.getBlock(world) == Blocks.snow_layer) {
            int metadata = target.getMeta(world);
            if (metadata < 7) {
                world.setBlockMetadataWithNotify(target.x, target.y, target.z, metadata + 1, 3);
            } else {
                world.setBlock(target.x, target.y, target.z, Blocks.snow);
            }
            world.playSoundEffect(target.x, target.y, target.z, "dig.snow", 1.0F, 1.0F);
            return 1;
        }

        BlockCoord above = target.copy().offset(1);
        if (target.getBlock(world) == Blocks.water && above.isAir(world)) {
            world.setBlock(target.x, target.y, target.z, Blocks.ice);
            world.playSoundEffect(target.x, target.y, target.z, "dig.glass", 1.0F, 1.0F);
            return 1;
        }

        Block belowBlock = below.getBlock(world);
        if ((belowBlock == Blocks.ice || belowBlock == Blocks.snow || belowBlock == Blocks.snow_layer)
                && target.isAir(world)
                && above.isAir(world)) {
            EntitySnowman snowGolem = new EntitySnowman(world);
            snowGolem.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 3600 * 20));
            RandomBlockHelper.setLocation(world, snowGolem, target);

            if (world.spawnEntityInWorld(snowGolem)) {
                world.playSoundEffect(target.x, target.y, target.z, "dig.snow", 1.0F, 1.0F);
                return 1;
            }
        }

        return 0;
    }
}
