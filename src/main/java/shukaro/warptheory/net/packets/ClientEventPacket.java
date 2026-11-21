package shukaro.warptheory.net.packets;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.MiscHelper;

public class ClientEventPacket implements IMessage {

    public int amount;

    public ClientEventPacket() {}

    public ClientEventPacket(int amount) {
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf bytes) {
        this.amount = bytes.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytes) {
        bytes.writeInt(this.amount);
    }

    public static final class Handler implements IMessageHandler<ClientEventPacket, IMessage> {

        @Override
        public IMessage onMessage(ClientEventPacket message, MessageContext ctx) {
            EntityPlayer player = WarpTheory.proxy.getPlayer();
            if (player != null) MiscHelper.modEventInt(player, "ears", message.amount);
            return null;
        }
    }
}
