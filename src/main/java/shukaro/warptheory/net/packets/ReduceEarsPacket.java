package shukaro.warptheory.net.packets;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import shukaro.warptheory.util.MiscHelper;

public class ReduceEarsPacket implements IMessage {

    public ReduceEarsPacket() {}

    @Override
    public void fromBytes(ByteBuf bytes) {}

    @Override
    public void toBytes(ByteBuf bytes) {}

    public static final class Handler implements IMessageHandler<ReduceEarsPacket, IMessage> {

        @Override
        public IMessage onMessage(ReduceEarsPacket message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            int ears = MiscHelper.getWarpTag(player).getInteger("ears");
            MiscHelper.getWarpTag(player).setInteger("ears", --ears);
            if (ears <= 0) MiscHelper.getWarpTag(player).removeTag("ears");
            return null;
        }
    }
}
