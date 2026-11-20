package shukaro.warptheory.items;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.Constants;
import thaumcraft.common.lib.potions.PotionWarpWard;

public class ItemWarpWardAmulet extends Item implements IBauble {

    public ItemWarpWardAmulet() {
        this.setMaxStackSize(1);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setTextureName("warptheory:itemWarpWardAmulet");
        this.setUnlocalizedName(Constants.ITEM_PORTABLE_SHOWER);
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
            if (!player.isPotionActive(PotionWarpWard.instance.id))
                player.addPotionEffect(new PotionEffect(PotionWarpWard.instance.id, 12000, 0, true));
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
                        + StatCollector.translateToLocal("tooltip.warptheory.portableshower"));
    }
}
