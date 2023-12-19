package shukaro.warptheory.handlers.warpevents;

import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;

import shukaro.warptheory.handlers.IWarpEvent;

public class WarpJunk extends IWarpEvent {

    private static final ImmutableList<Supplier<ItemStack>> JUNK = ImmutableList.of(
            () -> new ItemStack(Items.wheat_seeds),
            () -> new ItemStack(Items.rotten_flesh),
            () -> new ItemStack(Items.bone),
            () -> new ItemStack(Items.snowball),
            () -> new ItemStack(Items.slime_ball),
            () -> new ItemStack(Items.feather),
            () -> new ItemStack(Items.dye, 1, 0),
            () -> new ItemStack(Items.stick),
            () -> new ItemStack(Items.apple),
            () -> new ItemStack(Items.brick),
            () -> new ItemStack(Blocks.red_flower),
            () -> new ItemStack(Blocks.yellow_flower),
            () -> new ItemStack(Blocks.brown_mushroom),
            () -> new ItemStack(Blocks.red_mushroom),
            () -> new ItemStack(Blocks.cobblestone),
            () -> new ItemStack(Blocks.gravel),
            () -> new ItemStack(Blocks.sand),
            () -> new ItemStack(Blocks.dirt));

    public WarpJunk(int minWarp) {
        super("junk", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        boolean successful = false;
        ItemStack[] inventory = player.inventory.mainInventory;
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = getJunk(world);
                successful = true;
            }
        }

        if (successful) {
            player.inventory.inventoryChanged = true;
            world.playSoundAtEntity(player, "random.pop", 1.0F, 1.0F);
            sendChatMessage(player);
        }

        return true;
    }

    private static ItemStack getJunk(World world) {
        ItemStack itemStack = JUNK.get(world.rand.nextInt(JUNK.size())).get();
        for (int i = 1; i < 4; i++) {
            if (world.rand.nextInt(1 << i) == 0) {
                itemStack.stackSize++;
            }
        }

        return itemStack;
    }
}
