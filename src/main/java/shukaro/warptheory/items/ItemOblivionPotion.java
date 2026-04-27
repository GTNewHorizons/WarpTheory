package shukaro.warptheory.items;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
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

public class ItemOblivionPotion extends ItemPotion {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public ItemOblivionPotion() {
        this.setHasSubtypes(false);
        this.setUnlocalizedName(Constants.ITEM_POTION);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        warnPlayer(player);
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
        // Research is synced when opening the Thaumonomicon, but knowledge points need to be synced with a packet
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
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.icon = reg.registerIcon(Constants.modID.toLowerCase(Locale.ENGLISH) + ":itemOblivionPotion");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int meta) {
        return 0xFFFFFF;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> l) {
        l.add(new ItemStack(item, 1, 0));
    }
}
