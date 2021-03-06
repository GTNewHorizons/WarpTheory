package shukaro.warptheory.handlers.warpevents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpBlink extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpBlink(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "blink";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + StatCollector.translateToLocal("chat.warptheory.blink"));
        MiscHelper.modEventInt(player, "blink", 10 + world.rand.nextInt(20));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("blink") && e.world.getTotalWorldTime() % 20 == 0)
            {
                int blink = MiscHelper.getWarpTag(player).getInteger("blink");
                for (int i = 0; i < 8; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetY = (int)player.posY + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                    if (target.isAir(e.world) && target.copy().offset(1).isAir(e.world) && target.copy().offset(0).isTopSolid(e.world))
                    {
                        player.rotationPitch = (e.world.rand.nextInt(45) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(45) + e.world.rand.nextFloat());
                        player.rotationYaw = (e.world.rand.nextInt(360) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(360) + e.world.rand.nextFloat());
                        double dX = target.x + 0.5;
                        double dY = target.y + 0.01;
                        double dZ = target.z + 0.5;
                        player.setPositionAndUpdate(dX, dY, dZ);
                        PacketDispatcher.sendBlinkEvent(e.world, dX, dY, dZ);
                        e.world.playSoundEffect(dX, dY, dZ, "mob.endermen.portal", 1.0F, 1.0F);
                        MiscHelper.getWarpTag(player).setInteger("blink", --blink);
                        if (blink <= 0)
                            MiscHelper.getWarpTag(player).removeTag("blink");
                        break;
                    }
                }
            }
        }
    }
}
