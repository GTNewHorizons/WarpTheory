package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpEnderPearl extends IWarpEvent {

    public WarpEnderPearl(int minWarp) {
        super("enderpearl", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        if (world.spawnEntityInWorld(new EntityEnderPearl(world, player))) {
            world.playSoundAtEntity(player, "mob.endermen.scream", 1.0F, 1.0F);
            sendChatMessage(player);
        }
        return true;
    }
}
