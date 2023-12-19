package shukaro.warptheory.items;

import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.Constants;

public class ItemCleanserMinor extends ItemCleanser {

    private IIcon icon;

    public ItemCleanserMinor() {
        super();
        this.setUnlocalizedName(Constants.ITEM_WARPCLEANSERMINOR);
    }

    @Override
    protected void purgeWarp(EntityPlayer player) {
        if (WarpHandler.getTotalWarp(player) == 0) {
            ChatHelper.sendToPlayer(player, StatCollector.translateToLocal("chat.warptheory.purgefail"));
        }

        WarpHandler.purgeWarpMinor(player);
    }

    @Override
    protected String getIcon() {
        return "itemCleanserMinor";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.icon = reg.registerIcon(Constants.modID.toLowerCase(Locale.ENGLISH) + ":itemCleanserMinor");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return icon;
    }

    @Override
    protected String getToolTip() {
        return "tooltip.warptheory.cleanserminor";
    }
}
