package shukaro.warptheory.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.Constants;

public class ItemPaper extends Item {

    public ItemPaper() {
        this.setTextureName("warptheory:itemPaper");
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_LITMUS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            int totalWarp = WarpHandler.getTotalWarp(player);
            int[] individualWarps = WarpHandler.getIndividualWarps(player);
            String severity;
            if (totalWarp <= 10) severity = "chat.warptheory.minorwarp";
            else if (totalWarp <= 25) severity = "chat.warptheory.averagewarp";
            else if (totalWarp <= 50) severity = "chat.warptheory.majorwarp";
            else severity = "chat.warptheory.deadlywarp";
            ChatHelper.sendTranslatedToPlayer(player, severity, ChatHelper.warpStyle());
            player.addChatMessage(
                    new ChatComponentTranslation(
                            "chat.warptheory.warpcounts",
                            individualWarps[0],
                            individualWarps[1],
                            individualWarps[2]));
        }

        if (!player.capabilities.isCreativeMode && WarpHandler.getTotalWarp(player) > 10) stack.stackSize--;

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> infoList, boolean advanced) {
        infoList.add(
                EnumChatFormatting.DARK_GRAY + EnumChatFormatting.ITALIC.toString()
                        + StatCollector.translateToLocal("tooltip.warptheory.paper"));
    }
}
