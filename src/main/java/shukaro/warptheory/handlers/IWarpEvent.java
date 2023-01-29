package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

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

    public boolean canDo(World world, EntityPlayer player) {
        return true;
    }

    public abstract boolean doEvent(World world, EntityPlayer player);

    public void sendChatMessage(EntityPlayer player) {
        sendChatMessage(player, name);
    }

    public void sendChatMessage(EntityPlayer player, String messageName) {
        ChatHelper.sendToPlayer(
                player,
                FormatCodes.Purple.code + FormatCodes.Italic.code
                        + StatCollector.translateToLocal("chat.warptheory." + messageName));
    }

    /**
     * Returns the new value of the tag.
     */
    public int decreaseTag(EntityPlayer player, String tag, int decrement) {
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
