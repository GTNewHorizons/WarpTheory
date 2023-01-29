package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public class ClientEventPacket implements IWarpPacket {

    public int id;
    public int amount;

    public ClientEventPacket() {}

    public ClientEventPacket(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        this.id = bytes.readInt();
        this.amount = bytes.readInt();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(this.id);
        bytes.writeInt(this.amount);
    }
}
