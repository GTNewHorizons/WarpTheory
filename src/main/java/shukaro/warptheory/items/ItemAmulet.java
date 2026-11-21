package shukaro.warptheory.items;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.Constants;

public class ItemAmulet extends Item implements IBauble {

    public ItemAmulet() {
        this.setMaxStackSize(1);
        this.setTextureName("warptheory:itemAmulet");
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_PURE_TALISMAN);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer player) {
            if (player.ticksExisted % 500 != 0 || WarpHandler.getTotalWarp(player) <= 0 || player.worldObj.isRemote)
                return;
            if (player.worldObj.rand.nextInt(100) <= Math.sqrt(WarpHandler.getTotalWarp(player))) {
                IWarpEvent event = WarpHandler.queueOneEvent(player, WarpHandler.getTotalWarp(player));
                WarpHandler.removeWarp(player, (event != null) ? event.getCost() : 1);
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> infoList, boolean advanced) {
        infoList.add(
                EnumChatFormatting.DARK_GRAY + EnumChatFormatting.ITALIC.toString()
                        + StatCollector.translateToLocal("tooltip.warptheory.amulet"));
    }
}
