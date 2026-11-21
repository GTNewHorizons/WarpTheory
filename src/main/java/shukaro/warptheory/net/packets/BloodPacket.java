package shukaro.warptheory.net.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import shukaro.warptheory.handlers.warpevents.WarpBlood;
import shukaro.warptheory.util.BlockCoord;

public class BloodPacket implements IMessage {

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
    public void fromBytes(ByteBuf bytes) {
        this.eventLevel = bytes.readInt();
        this.x = bytes.readInt();
        this.y = bytes.readInt();
        this.z = bytes.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytes) {
        bytes.writeInt(this.eventLevel);
        bytes.writeInt(this.x);
        bytes.writeInt(this.y);
        bytes.writeInt(this.z);
    }

    public static final class Handler implements IMessageHandler<BloodPacket, IMessage> {

        @Override
        public IMessage onMessage(BloodPacket message, MessageContext ctx) {
            BlockCoord block = new BlockCoord(message.x, message.y, message.z);
            switch (message.eventLevel) {
                case 0:
                    WarpBlood.bloody.put(message.dim, block);
                    break;

                case 1:
                    WarpBlood.blackBloody.put(message.dim, block);
                    break;
            }

            return null;
        }
    }
}
