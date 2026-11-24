package shukaro.warptheory.net.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import shukaro.warptheory.handlers.warpevents.WarpFakeRain;

public class FakeRainPacket implements IMessage {

    public int eventLevel;

    public FakeRainPacket() {}

    public FakeRainPacket(int eventLevel) {
        this.eventLevel = eventLevel;
    }

    @Override
    public void fromBytes(ByteBuf bytes) {
        this.eventLevel = bytes.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytes) {
        bytes.writeInt(this.eventLevel);
    }

    public static final class Handler implements IMessageHandler<FakeRainPacket, IMessage> {

        @Override
        public IMessage onMessage(FakeRainPacket message, MessageContext ctx) {
            WarpFakeRain.rainLevel = message.eventLevel;
            return null;
        }
    }
}
