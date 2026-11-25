package shukaro.warptheory.net.packets;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class EnderParticlesPacket implements IMessage {

    public double x, y, z;

    public EnderParticlesPacket() {}

    public EnderParticlesPacket(double x, double y, double z) {
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

    public static final class Handler implements IMessageHandler<EnderParticlesPacket, IMessage> {

        @Override
        public IMessage onMessage(EnderParticlesPacket message, MessageContext ctx) {
            var world = Minecraft.getMinecraft().theWorld;
            for (int l = 0; l < 128; ++l) {
                world.spawnParticle(
                        "portal",
                        message.x + world.rand.nextDouble() - world.rand.nextDouble(),
                        message.y + world.rand.nextDouble() - world.rand.nextDouble(),
                        message.z + world.rand.nextDouble() - world.rand.nextDouble(),
                        (double) (world.rand.nextFloat() - 0.5F) * 0.2F,
                        (double) (world.rand.nextFloat() - 0.5F) * 0.2F,
                        (double) (world.rand.nextFloat() - 0.5F) * 0.2F);
            }

            return null;
        }
    }
}
