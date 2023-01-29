package shukaro.warptheory.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPhantom extends RenderBiped {

    public RenderPhantom() {
        super(new ModelBiped(0.0F), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (!(entity instanceof EntityPhantom)) {
            return EntityPhantom.SKINS.get(0);
        } else {
            return EntityPhantom.SKINS.get(((EntityPhantom) entity).getSkinIndex());
        }
    }
}
