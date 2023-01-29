package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public class ClearPacket implements IWarpPacket {

    public int id;

    public ClearPacket() {}

    public ClearPacket(int id) {
        this.id = id;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        this.id = bytes.readInt();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(this.id);
    }
}
