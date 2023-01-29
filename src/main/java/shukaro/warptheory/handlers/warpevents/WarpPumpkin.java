package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpPumpkin extends IWarpEvent {

    public WarpPumpkin(int minWarp) {
        super("pumpkin", minWarp);
    }

    @Override
    public boolean canDo(World world, EntityPlayer player) {
        // Only trigger if the player isn't wearing a helmet.
        return player.inventory.armorInventory[3] == null;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        // This should be guaranteed to be true, since we check in canDo().
        // But check again just to be safe (in case the code changes).
        if (player.inventory.armorInventory[3] == null) {
            player.inventory.armorInventory[3] = new ItemStack(Blocks.pumpkin);
            player.inventory.inventoryChanged = true;
            world.playSoundAtEntity(player, "dig.wood", 1.0F, 1.0F);
            sendChatMessage(player);
        }

        return true;
    }
}
