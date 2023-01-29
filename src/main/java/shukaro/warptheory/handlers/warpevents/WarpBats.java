package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpBats extends IMultiWarpEvent {

    public WarpBats(int minWarp) {
        super("bats", minWarp, 3, world -> 15 + world.rand.nextInt(30));
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;
        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomAirBlock(world, player, 8);
            if (target == null) {
                continue;
            }

            EntityBat bat = new EntityBat(world);
            bat.playLivingSound();
            RandomBlockHelper.setLocation(world, bat, target);

            switch (eventLevel) {
                case 2:
                    bat.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 3600 * 20));
                    // Fall-through
                case 1:
                    bat.setFire(3600);
                    break;
            }

            if (world.spawnEntityInWorld(bat)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }
}
