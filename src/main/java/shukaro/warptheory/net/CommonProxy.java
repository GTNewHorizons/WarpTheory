package shukaro.warptheory.net;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import shukaro.warptheory.net.packets.BloodPacket;
import shukaro.warptheory.net.packets.ClearBlockBloodPacket;
import shukaro.warptheory.net.packets.EnderParticlesPacket;
import shukaro.warptheory.net.packets.FakeRainPacket;
import shukaro.warptheory.net.packets.InflictEarsPacket;
import shukaro.warptheory.net.packets.ReduceEarsPacket;
import shukaro.warptheory.net.packets.VelocityPacket;
import shukaro.warptheory.util.Constants;

public class CommonProxy {

    public static SimpleNetworkWrapper network;

    public void init() {
        network = new SimpleNetworkWrapper(Constants.modID);

        network.registerMessage(EnderParticlesPacket.Handler.class, EnderParticlesPacket.class, 1, Side.CLIENT);
        network.registerMessage(VelocityPacket.Handler.class, VelocityPacket.class, 2, Side.CLIENT);
        network.registerMessage(BloodPacket.Handler.class, BloodPacket.class, 3, Side.CLIENT);
        network.registerMessage(ClearBlockBloodPacket.Handler.class, ClearBlockBloodPacket.class, 4, Side.CLIENT);
        network.registerMessage(ReduceEarsPacket.Handler.class, ReduceEarsPacket.class, 5, Side.SERVER);
        network.registerMessage(InflictEarsPacket.Handler.class, InflictEarsPacket.class, 6, Side.CLIENT);
        network.registerMessage(FakeRainPacket.Handler.class, FakeRainPacket.class, 7, Side.CLIENT);
    }

    public EntityPlayer getPlayer() {
        return null;
    }
}
