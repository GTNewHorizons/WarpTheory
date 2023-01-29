package shukaro.warptheory.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import shukaro.warptheory.entity.EntityDoppelganger;
import shukaro.warptheory.entity.EntityPhantom;
import shukaro.warptheory.entity.RenderDoppelganger;
import shukaro.warptheory.entity.RenderPhantom;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    public void init() {
        super.init();

        RenderingRegistry.registerEntityRenderingHandler(EntityDoppelganger.class, new RenderDoppelganger());
        RenderingRegistry.registerEntityRenderingHandler(EntityPhantom.class, new RenderPhantom());
    }

    @Override
    public EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
