package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public class EnderParticlesPacket implements IWarpPacket {

    public double x, y, z;

    public EnderParticlesPacket() {}

    public EnderParticlesPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        this.x = bytes.readDouble();
        this.y = bytes.readDouble();
        this.z = bytes.readDouble();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeDouble(this.x);
        bytes.writeDouble(this.y);
        bytes.writeDouble(this.z);
    }
}
