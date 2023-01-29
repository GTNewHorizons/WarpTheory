package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public class BloodPacket implements IWarpPacket {

    public int dim, eventLevel, x, y, z;

    public BloodPacket() {}

    public BloodPacket(int dim, int eventLevel, int x, int y, int z) {
        this.dim = dim;
        this.eventLevel = eventLevel;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        this.eventLevel = bytes.readInt();
        this.x = bytes.readInt();
        this.y = bytes.readInt();
        this.z = bytes.readInt();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(this.eventLevel);
        bytes.writeInt(this.x);
        bytes.writeInt(this.y);
        bytes.writeInt(this.z);
    }
}
