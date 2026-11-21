package shukaro.warptheory.net.packets;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import shukaro.warptheory.WarpTheory;

public class VelocityPacket implements IMessage {

    public double x, y, z;

    public VelocityPacket() {}

    public VelocityPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf bytes) {
        this.x = bytes.readDouble();
        this.y = bytes.readDouble();
        this.z = bytes.readDouble();
    }

    @Override
    public void toBytes(ByteBuf bytes) {
        bytes.writeDouble(this.x);
        bytes.writeDouble(this.y);
        bytes.writeDouble(this.z);
    }

    public static final class Handler implements IMessageHandler<VelocityPacket, IMessage> {

        @Override
        public IMessage onMessage(VelocityPacket message, MessageContext ctx) {
            EntityPlayer player = WarpTheory.proxy.getPlayer();
            if (player != null) {
                player.addVelocity(message.x, message.y, message.z);
            }
            return null;
        }
    }
}
