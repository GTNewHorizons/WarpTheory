package shukaro.warptheory.items;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.FormatCodes;
import thaumcraft.common.lib.potions.PotionWarpWard;

public class ItemWarpWardAmulet extends Item implements IBauble {

    private IIcon icon;

    public ItemWarpWardAmulet() {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_PORTABLE_SHOWER);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.icon = reg.registerIcon(Constants.modID.toLowerCase(Locale.ENGLISH) + ":" + "itemWarpWardAmulet");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
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
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advanced) {
        infoList.add(
                FormatCodes.DarkGrey.code + FormatCodes.Italic.code
                        + StatCollector.translateToLocal("tooltip.warptheory.portableshower"));
    }
}
