package shukaro.warptheory.net;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import shukaro.warptheory.util.Constants;

public class CommonProxy {

    public static EnumMap<Side, FMLEmbeddedChannel> warpChannel;

    public void init() {
        warpChannel = NetworkRegistry.INSTANCE
                .newChannel(Constants.modID, new WarpMessageToMessageCodec(), new PacketHandler());
    }

    public EntityPlayer getPlayer() {
        return null;
    }
}
