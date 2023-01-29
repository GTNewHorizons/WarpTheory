package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;
import thaumcraft.common.config.ConfigItems;

public class WarpCoin extends IWarpEvent {

    public WarpCoin(int minWarp) {
        super("coin", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        player.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 18), 0.0f);
        world.playSoundAtEntity(player, "random.orb", 1.0F, 1.0F);
        sendChatMessage(player);
        return true;
    }
}
