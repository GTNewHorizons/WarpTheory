package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public class VelocityPacket implements IWarpPacket {

    public double x, y, z;

    public VelocityPacket() {}

    public VelocityPacket(double x, double y, double z) {
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
