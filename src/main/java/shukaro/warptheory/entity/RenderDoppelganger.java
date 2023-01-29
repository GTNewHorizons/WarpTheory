package shukaro.warptheory.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDoppelganger extends RenderBiped {

    private static final ResourceLocation FALLBACK_TEXTURE = new ResourceLocation("textures/entity/steve.png");

    public RenderDoppelganger() {
        super(new ModelBiped(0.0F), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (!(entity instanceof EntityDoppelganger)) {
            // This should never happen, but just in case.
            return FALLBACK_TEXTURE;
        }

        ResourceLocation playerSkin = ((EntityDoppelganger) entity).getPlayerSkin();
        return playerSkin != null ? playerSkin : FALLBACK_TEXTURE;
    }
}
