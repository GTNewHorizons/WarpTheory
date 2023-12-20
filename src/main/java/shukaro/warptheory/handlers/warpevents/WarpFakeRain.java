package shukaro.warptheory.handlers.warpevents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.entity.EntityRainParticleFX;
import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpFakeRain extends IMultiWarpEvent {

    public static int rainLevel = -1;

    public WarpFakeRain(int minWarp) {
        super("fakerain", minWarp, 2, world -> 30 + world.rand.nextInt(30));
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // Fake effects should have no message, to confuse the player.
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        if (eventAmount > 1) {
            PacketDispatcher.sendFakeRainEvent(player, eventLevel);
        } else {
            PacketDispatcher.sendFakeRainEvent(player, -1);
        }

        world.playSoundAtEntity(player, "ambient.weather.rain", 0.5F, 1.0F);
        return 1;
    }

    private static boolean isValid(World world, BlockCoord block) {
        for (int i = 0; i <= 8; i++) {
            if (!world.isAirBlock(block.x, block.y + i, block.z)) {
                return false;
            }
        }

        return true;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        if (rainLevel >= 0) {
            World world = Minecraft.getMinecraft().theWorld;
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            if (world != null && world.getTotalWorldTime() % 5 == 0 && player != null) {
                int y = (int) player.posY + 8;
                for (int i = 0; i < 32; i++) {
                    BlockCoord target = RandomBlockHelper.randomBlock(world, player, 16, block -> true);
                    if (target == null) {
                        continue;
                    }

                    EntityFX fx;
                    switch (rainLevel) {
                        case 1:
                            fx = new EntityRainParticleFX(
                                    world,
                                    target.x,
                                    y,
                                    target.z,
                                    world.rand.nextFloat() + 0.2f,
                                    0.0f,
                                    0.0f);
                            break;

                        case 0:
                        default:
                            fx = new EntityRainParticleFX(world, target.x, y, target.z, 0.0f, 0.0f, 1.0f);
                            break;
                    }
                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
                }
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 20 != 0) {
            return;
        }

        super.onTick(e);
    }
}
