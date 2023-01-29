package shukaro.warptheory.net.packets;

import io.netty.buffer.ByteBuf;

public interface IWarpPacket {

    public void readBytes(ByteBuf bytes);

    public void writeBytes(ByteBuf bytes);
}
