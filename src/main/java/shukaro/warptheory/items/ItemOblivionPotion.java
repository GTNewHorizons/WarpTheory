package shukaro.warptheory.items;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.Constants;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects;

public class ItemOblivionPotion extends Item {

    public ItemOblivionPotion() {
        this.setHasSubtypes(false);
        this.setUnlocalizedName(Constants.ITEM_POTION);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        warnPlayer(player);
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return super.onItemRightClick(stack, world, player);
    }

    private void warnPlayer(EntityPlayer player) {
        if (player.worldObj.isRemote) return;
        player.addChatMessage(new ChatComponentTranslation("chat.warptheory.potionwarning.1"));
        player.addChatMessage(new ChatComponentTranslation("chat.warptheory.potionwarning.2"));
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        clearAllKnowledgeAndWarp(player);
        if (!player.capabilities.isCreativeMode) {
            if (stack.stackSize <= 1) {
                return new ItemStack(Items.glass_bottle);
            }
            stack.stackSize--;
            player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }
        return super.onEaten(stack, world, player);
    }

    private void clearAllKnowledgeAndWarp(EntityPlayer player) {
        if (player.worldObj.isRemote) return;
        String name = player.getCommandSenderName();
        WarpHandler.Knowledge.wipePlayerKnowledge(name);
        for (ResearchCategoryList category : ResearchCategories.researchCategories.values()) {
            for (ResearchItem research : category.research.values()) {
                if (research.isAutoUnlock()) {
                    Thaumcraft.proxy.getResearchManager().completeResearch(player, research.key);
                }
            }
        }
        // Add the initial 15-19 primal knowledge points given when a player joins for the first time
        for (Aspect aspect : Aspect.getPrimalAspects()) {
            WarpHandler.Knowledge.setAspectPool(name, aspect, (short) (15 + player.worldObj.rand.nextInt(5)));
        }
        // Research is synced when opening the Thaumonomicon, but knowledge points need to be synced with this packet
        PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(player), (EntityPlayerMP) player);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 200;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean advanced) {
        tooltip.add(StatCollector.translateToLocal("tooltip.warptheory.oblivionpotion"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(Constants.modID.toLowerCase(Locale.ENGLISH) + ":itemOblivionPotion");
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.drink;
    }
}
