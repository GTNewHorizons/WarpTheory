package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public class FakeRainPacket implements IWarpPacket {

    public int eventLevel;

    public FakeRainPacket() {}

    public FakeRainPacket(int eventLevel) {
        this.eventLevel = eventLevel;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        this.eventLevel = bytes.readInt();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(this.eventLevel);
    }
}
