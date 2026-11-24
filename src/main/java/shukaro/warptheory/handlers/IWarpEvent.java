package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.MiscHelper;

/**
 * A custom warp event. Must be registered in {@link WarpEventRegistry} to function.
 */
public abstract class IWarpEvent {

    protected final String name;
    protected final int minWarp;

    protected IWarpEvent(String name, int minWarp) {
        this.name = name;
        this.minWarp = minWarp;
    }

    public String getName() {
        return name;
    }

    public int getSeverity() {
        return minWarp;
    }

    public final int getCost() {
        return (int) Math.ceil(getSeverity() / (double) 10);
    }

    /**
     * Check whether this event can apply to the player. Runs only on the server thread. (Skipped events still reduce
     * the warp counter and delay the next warp event.)
     *
     * @param world  The world the player is currently occupying.
     * @param player The player whose warp is being triggered.
     * @return Whether the event can occur.
     */
    public boolean canDo(World world, EntityPlayerMP player) {
        return true;
    }

    /**
     * Inflict the event on the player. Runs only on the server thread.
     *
     * @param world  The world the player is currently occupying.
     * @param player The player whose warp is being triggered.
     * @return Whether the event occurred? All usages of this seems to return true, and the return value is unused.
     */
    public abstract boolean doEvent(World world, EntityPlayerMP player);

    public void sendChatMessage(EntityPlayer player) {
        sendChatMessage(player, name);
    }

    public void sendChatMessage(EntityPlayer player, String messageName) {
        ChatHelper.sendTranslatedToPlayer(player, "chat.warptheory." + messageName, ChatHelper.warpStyle());
    }

    /**
     * Returns the new value of the tag.
     */
    public static int decreaseTag(EntityPlayer player, String tag, int decrement) {
        int amount = MiscHelper.getWarpTag(player).getInteger(tag);

        if (decrement > 0) {
            if (amount <= decrement) {
                MiscHelper.getWarpTag(player).removeTag(tag);
                return 0;
            } else {
                int newAmount = amount - decrement;
                MiscHelper.getWarpTag(player).setInteger(tag, newAmount);
                return newAmount;
            }
        } else {
            return amount;
        }
    }
}
