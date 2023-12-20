package shukaro.warptheory.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import shukaro.warptheory.net.packets.BloodPacket;
import shukaro.warptheory.net.packets.ClearPacket;
import shukaro.warptheory.net.packets.ClientEventPacket;
import shukaro.warptheory.net.packets.DecrementPacket;
import shukaro.warptheory.net.packets.EnderParticlesPacket;
import shukaro.warptheory.net.packets.FakeRainPacket;
import shukaro.warptheory.net.packets.IWarpPacket;
import shukaro.warptheory.net.packets.VelocityPacket;

public class PacketDispatcher {

    public static void sendBlinkEvent(World world, double x, double y, double z) {
        try {
            int dim = world.provider.dimensionId;
            sendToAllAround(new EnderParticlesPacket(x, y, z), dim, (int) x, (int) y, (int) z, 128);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendWindEvent(EntityPlayer player, double x, double y, double z) {
        try {
            sendToPlayer(new VelocityPacket(x, y, z), player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBloodEvent(EntityPlayer player, int eventLevel, int x, int y, int z) {
        try {
            sendToPlayer(new BloodPacket(player.worldObj.provider.dimensionId, eventLevel, x, y, z), player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBloodClearEvent(EntityPlayer player) {
        try {
            sendToPlayer(new ClearPacket(0), player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEarDecrementEvent(EntityPlayer player) {
        try {
            sendToServer(new DecrementPacket(0, player.getEntityId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEarStartEvent(EntityPlayer player, int amount) {
        try {
            sendToPlayer(new ClientEventPacket(0, amount), player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendFakeRainEvent(EntityPlayer player, int eventLevel) {
        try {
            sendToPlayer(new FakeRainPacket(eventLevel), player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FMLEmbeddedChannel getClientOutboundChannel() {
        return ClientProxy.warpChannel.get(Side.CLIENT);
    }

    private static FMLEmbeddedChannel getServerOutboundChannel() {
        return CommonProxy.warpChannel.get(Side.SERVER);
    }

    private static void sendToServer(IWarpPacket packet) {
        getClientOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        getClientOutboundChannel().writeOutbound(packet);
    }

    private static void sendToPlayer(IWarpPacket packet, EntityPlayer player) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET)
                    .set(FMLOutboundHandler.OutboundTarget.PLAYER);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }

    private static void sendToAll(IWarpPacket packet) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET)
                    .set(FMLOutboundHandler.OutboundTarget.ALL);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }

    private static void sendToAllAround(IWarpPacket packet, int dim, int x, int y, int z, int range) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(dim, x, y, z, range);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET)
                    .set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(tp);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }

    private static void sendToAllInDim(IWarpPacket packet, int dim) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET)
                    .set(FMLOutboundHandler.OutboundTarget.DIMENSION);
            getServerOutboundChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dim);
            getServerOutboundChannel().writeOutbound(packet);
        }
    }
}
