package shukaro.warptheory.handlers.warpevents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import shukaro.warptheory.block.WarpBlocks;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.tile.TileEntityVanish;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;

public class WarpFall extends IWarpEvent {

    private static Map<String, BlockCoord> originalPositions = new HashMap<String, BlockCoord>();
    private static Map<String, Long> returnTimes = new HashMap<String, Long>();

    public WarpFall(int minWarp) {
        super("fall", minWarp);
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public boolean canDo(World world, EntityPlayer player) {
        if (originalPositions.get(player.getCommandSenderName()) != null) return false;
        return true;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        sendChatMessage(player);
        MiscHelper.modEventInt(player, name, 4);
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER) return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>) e.world.playerEntities) {
            if (MiscHelper.getWarpTag(player).hasKey(name)) {
                if (!originalPositions.containsKey(player.getCommandSenderName())) {
                    int fall = MiscHelper.getWarpTag(player).getInteger(name);
                    originalPositions.put(
                            player.getCommandSenderName(),
                            new BlockCoord((int) player.posX, (int) player.posY, (int) player.posZ));
                    returnTimes.put(player.getCommandSenderName(), e.world.getTotalWorldTime() + fall * 20);
                    e.world.playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                    for (int i = (int) player.posX - 5; i < player.posX + 5; i++) {
                        for (int j = 0; j < e.world.getHeight(); j++) {
                            for (int k = (int) player.posZ - 5; k < player.posZ + 5; k++) {
                                if (!e.world.isAirBlock(i, j, k)) {
                                    TileEntityVanish vanish = new TileEntityVanish(
                                            e.world,
                                            i,
                                            j,
                                            k,
                                            returnTimes.get(player.getCommandSenderName()));
                                    if (e.world.setBlock(i, j, k, WarpBlocks.blockVanish, 0, 0))
                                        e.world.setTileEntity(i, j, k, vanish);
                                    e.world.markBlockForUpdate(i, j, k);
                                }
                            }
                        }
                    }
                } else if (e.world.getTotalWorldTime() >= returnTimes.get(player.getCommandSenderName())) {
                    BlockCoord o = originalPositions.get(player.getCommandSenderName());
                    double dx = o.x + e.world.rand.nextDouble();
                    double dz = o.z + e.world.rand.nextDouble();
                    player.setPositionAndUpdate(dx, o.y, dz);
                    PacketDispatcher.sendBlinkEvent(e.world, dx, o.y, dz);
                    e.world.playSoundEffect(dx, o.y, dz, "mob.endermen.portal", 1.0F, 1.0F);
                    MiscHelper.getWarpTag(player).removeTag(name);
                    originalPositions.remove(player.getCommandSenderName());
                    returnTimes.remove(player.getCommandSenderName());
                }
            }
        }
    }
}
