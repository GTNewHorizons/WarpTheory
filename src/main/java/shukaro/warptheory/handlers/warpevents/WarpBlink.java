package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpBlink extends IWorldTickWarpEvent {

    public WarpBlink(int minWarp) {
        super("blink", minWarp, world -> 10 + world.rand.nextInt(20));
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        for (int i = 0; i < 8; i++) {
            BlockCoord target = RandomBlockHelper.randomBlock(world, player, 16, block -> isBlockValid(world, block));
            if (target == null) {
                continue;
            }

            player.rotationPitch = (world.rand.nextInt(45) + world.rand.nextFloat())
                    - (world.rand.nextInt(45) + world.rand.nextFloat());
            player.rotationYaw = (world.rand.nextInt(360) + world.rand.nextFloat())
                    - (world.rand.nextInt(360) + world.rand.nextFloat());
            double dX = target.x + 0.5;
            double dY = target.y + 0.01;
            double dZ = target.z + 0.5;
            player.setPositionAndUpdate(dX, dY, dZ);
            PacketDispatcher.sendBlinkEvent(world, dX, dY, dZ);
            world.playSoundEffect(dX, dY, dZ, "mob.endermen.portal", 1.0F, 1.0F);

            return 1 + eventAmount / 20;
        }

        return 0;
    }

    private static boolean isBlockValid(World world, BlockCoord block) {
        return block.isAir(world) && block.copy().offset(1).isAir(world) && block.copy().offset(0).isTopSolid(world);
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 20 != 0) {
            return;
        }

        super.onTick(e);
    }
}
