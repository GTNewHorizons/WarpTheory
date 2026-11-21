package shukaro.warptheory.net;

import static shukaro.warptheory.net.CommonProxy.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry;
import shukaro.warptheory.net.packets.BloodPacket;
import shukaro.warptheory.net.packets.ClearBlockBloodPacket;
import shukaro.warptheory.net.packets.EnderParticlesPacket;
import shukaro.warptheory.net.packets.FakeRainPacket;
import shukaro.warptheory.net.packets.InflictEarsPacket;
import shukaro.warptheory.net.packets.ReduceEarsPacket;
import shukaro.warptheory.net.packets.VelocityPacket;

public class PacketDispatcher {

    public static void sendBlinkEvent(World world, double x, double y, double z) {
        int dim = world.provider.dimensionId;
        network.sendToAllAround(
                new EnderParticlesPacket(x, y, z),
                new NetworkRegistry.TargetPoint(dim, (int) x, (int) y, (int) z, 128));
    }

    public static void sendWindEvent(EntityPlayerMP player, double x, double y, double z) {
        network.sendTo(new VelocityPacket(x, y, z), player);
    }

    public static void sendBloodEvent(EntityPlayerMP player, int eventLevel, int x, int y, int z) {
        network.sendTo(new BloodPacket(player.worldObj.provider.dimensionId, eventLevel, x, y, z), player);
    }

    public static void sendBloodClearEvent(EntityPlayerMP player) {
        network.sendTo(new ClearBlockBloodPacket(), player);
    }

    public static void sendEarDecrementEvent() {
        network.sendToServer(new ReduceEarsPacket());
    }

    public static void sendEarStartEvent(EntityPlayerMP player, int amount) {
        network.sendTo(new InflictEarsPacket(amount), player);
    }

    public static void sendFakeRainEvent(EntityPlayerMP player, int eventLevel) {
        network.sendTo(new FakeRainPacket(eventLevel), player);
    }

}
