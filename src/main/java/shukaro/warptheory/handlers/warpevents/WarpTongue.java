package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.MiscHelper;

public class WarpTongue extends IWarpEvent {

    public WarpTongue(int minWarp) {
        super("tongue", minWarp);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        sendChatMessage(player);
        MiscHelper.modEventInt(player, name, 10 + world.rand.nextInt(15));
        return true;
    }

    @SubscribeEvent
    public void onMessageReceived(ServerChatEvent e) {
        // Warp tongue
        if (MiscHelper.getWarpTag(e.player).hasKey(name)) {
            e.component = new ChatComponentTranslation(
                    "<" + ChatHelper.getUsername(e.component) + "> " + ChatHelper.garbleMessage(e.component));
            decreaseTag(e.player, name, 1);
        }
    }
}
