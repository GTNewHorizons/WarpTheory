package shukaro.warptheory.handlers.warpevents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpInventorySwap extends IWarpEvent {

    public WarpInventorySwap(int minWarp) {
        super("inventoryswap", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        ItemStack[] inventory = player.inventory.mainInventory;
        // Find empty and full slots in the player's main inventory (excluding hotbar).
        List<Integer> emptySlots = new ArrayList<>();
        List<Integer> fullSlots = new ArrayList<>();
        for (int i = 10; i < 36; i++) {
            if (inventory[i] == null) {
                emptySlots.add(i);
            } else {
                fullSlots.add(i);
            }
        }

        boolean canSwapHandItem = player.inventory.getCurrentItem() != null && !emptySlots.isEmpty()
                && player.inventory.currentItem < player.inventory.mainInventory.length;
        boolean canSwapInventory = !fullSlots.isEmpty();
        if (canSwapHandItem && canSwapInventory) {
            // Pick one of the two to do.
            if (world.rand.nextBoolean()) {
                canSwapHandItem = false;
            } else {
                canSwapInventory = false;
            }
        }

        if (canSwapHandItem) {
            int swapIndex = emptySlots.get(world.rand.nextInt(emptySlots.size()));

            inventory[swapIndex] = player.inventory.getCurrentItem();
            inventory[player.inventory.currentItem] = null;
        } else if (canSwapInventory) {
            int swapIndex1 = fullSlots.get(world.rand.nextInt(fullSlots.size()));
            List<Integer> otherIndices = IntStream.range(10, 36).boxed().collect(Collectors.toList());
            int swapIndex2 = otherIndices.get(world.rand.nextInt(otherIndices.size()));

            ItemStack temp = inventory[swapIndex1];
            inventory[swapIndex1] = inventory[swapIndex2];
            inventory[swapIndex2] = temp;
        }

        if (canSwapHandItem || canSwapInventory) {
            player.inventory.inventoryChanged = true;
            world.playSoundAtEntity(player, "random.pop", 1.0F, 1.0F);
            // No message for this one.
        }

        return true;
    }
}
