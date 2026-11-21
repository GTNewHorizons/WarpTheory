package shukaro.warptheory.items;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.Constants;

public class ItemCleanser extends Item {

    private IIcon icon1;
    private IIcon icon2;

    public ItemCleanser() {
        this.setMaxStackSize(16);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_WARPCLEANSER);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.icon1 = reg.registerIcon(Constants.modID.toLowerCase(Locale.ENGLISH) + ":itemCleanser"); // that's what
                                                                                                      // renders inHand
                                                                                                      // apparently
        this.icon2 = reg.registerIcon(Constants.modID.toLowerCase(Locale.ENGLISH) + ":itemCleanserOpaque");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return icon2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    protected void purgeWarp(EntityPlayer player) {
        if (WarpHandler.getTotalWarp(player) > 0) ChatHelper.sendTranslatedToPlayer(player, "chat.warptheory.purge");
        else ChatHelper.sendTranslatedToPlayer(player, "chat.warptheory.purgefail");

        WarpHandler.purgeWarp(player);
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            world.playSoundAtEntity(player, "game.potion.smash", 1.0f, 1.0f);
            purgeWarp(player);
        }

        if (!player.capabilities.isCreativeMode) stack.stackSize--;

        return stack.stackSize <= 0 ? null : stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 24;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.eat;
    }

    protected String getToolTip() {
        return "tooltip.warptheory.cleanser";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> infoList, boolean advanced) {
        infoList.add(
                EnumChatFormatting.DARK_GRAY + EnumChatFormatting.ITALIC.toString()
                        + StatCollector.translateToLocal(getToolTip()));
    }
}
