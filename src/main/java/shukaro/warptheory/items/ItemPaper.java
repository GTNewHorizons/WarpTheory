package shukaro.warptheory.items;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.FormatCodes;

public class ItemPaper extends Item {

    private IIcon icon;

    public ItemPaper() {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_LITMUS);
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
        this.icon = reg.registerIcon(Constants.modID.toLowerCase(Locale.ENGLISH) + ":" + "itemPaper");
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
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return this.icon;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            int totalWarp = WarpHandler.getTotalWarp(player);
            int[] individualWarps = WarpHandler.getIndividualWarps(player);
            String severity;
            if (totalWarp <= 10) severity = StatCollector.translateToLocal("chat.warptheory.minorwarp");
            else if (totalWarp <= 25) severity = StatCollector.translateToLocal("chat.warptheory.averagewarp");
            else if (totalWarp <= 50) severity = StatCollector.translateToLocal("chat.warptheory.majorwarp");
            else severity = StatCollector.translateToLocal("chat.warptheory.deadlywarp");
            player.addChatMessage(new ChatComponentText(FormatCodes.Purple.code + FormatCodes.Italic.code + severity));
            player.addChatMessage(
                    new ChatComponentText(
                            " (" + individualWarps[0]
                                    + " "
                                    + StatCollector.translateToLocal("chat.warptheory.permanentwarp")
                                    + ", "
                                    + individualWarps[1]
                                    + " "
                                    + StatCollector.translateToLocal("chat.warptheory.normalwarp")
                                    + ", "
                                    + individualWarps[2]
                                    + " "
                                    + StatCollector.translateToLocal("chat.warptheory.tempwarp")
                                    + ")"));
        }

        if (!player.capabilities.isCreativeMode && WarpHandler.getTotalWarp(player) > 10) stack.stackSize--;

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advanced) {
        infoList.add(
                FormatCodes.DarkGrey.code + FormatCodes.Italic.code
                        + StatCollector.translateToLocal("tooltip.warptheory.paper"));
    }
}
