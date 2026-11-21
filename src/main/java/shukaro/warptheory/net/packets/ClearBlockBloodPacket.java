package shukaro.warptheory.net.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import shukaro.warptheory.handlers.warpevents.WarpBlood;

public class ClearBlockBloodPacket implements IMessage {

    @Override
    public void fromBytes(ByteBuf bytes) {}

    @Override
    public void toBytes(ByteBuf bytes) {}

    public static final class Handler implements IMessageHandler<ClearBlockBloodPacket, IMessage> {

        @Override
        public IMessage onMessage(ClearBlockBloodPacket message, MessageContext ctx) {
            WarpBlood.bloody.clear();
            WarpBlood.blackBloody.clear();

            return null;
        }
    }
}
