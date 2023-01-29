package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public class DecrementPacket implements IWarpPacket {

    public int id;
    public int player;

    public DecrementPacket() {}

    public DecrementPacket(int id, int player) {
        this.id = id;
        this.player = player;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        this.id = bytes.readInt();
        this.player = bytes.readInt();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(this.id);
        bytes.writeInt(this.player);
    }
}
