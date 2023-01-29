package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.entity.EntityFakeCreeper;
import shukaro.warptheory.entity.EntityPassiveCreeper;
import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpFriend extends IMultiWarpEvent {

    public WarpFriend(int minWarp) {
        super("friend", minWarp, 3, world -> 1);
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;

        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomDoubleAirBlock(world, player, 4);
            if (target == null) {
                continue;
            }

            EntityCreeper creeper;
            switch (eventLevel) {
                case 2:
                case 1:
                    creeper = new EntityFakeCreeper(world);
                    break;

                case 0:
                default:
                    creeper = new EntityPassiveCreeper(world);
                    break;
            }
            if (eventLevel == 2) {
                creeper.addPotionEffect(new PotionEffect(Potion.invisibility.id, EntityFakeCreeper.ARMING_TIME));
            }

            try {
                creeper.setCustomNameTag(WarpTheory.normalNames.compose(world.rand.nextInt(3) + 2));
            } catch (Exception x) {
                x.printStackTrace();
            }

            creeper.playLivingSound();
            RandomBlockHelper.setLocation(world, creeper, target);
            if (world.spawnEntityInWorld(creeper)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }
}
