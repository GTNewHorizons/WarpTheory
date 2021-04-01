package shukaro.warptheory.handlers;

import baubles.api.BaublesApi;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import shukaro.warptheory.handlers.warpevents.*;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import shukaro.warptheory.util.ChatHelper;
import thaumcraft.api.IWarpingGear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.PlayerKnowledge;

public class WarpHandler
{
    public static final PlayerKnowledge Knowledge = Thaumcraft.proxy.getPlayerKnowledge();
    private static HashMap<UUID, Integer> Unavoidable = new HashMap<UUID, Integer>();
    public static ArrayList<IWarpEvent> warpEvents = new ArrayList<IWarpEvent>();

    public static Map<NameMetaPair, NameMetaPair> decayMappings = new THashMap<NameMetaPair, NameMetaPair>();

    public static void addDecayMapping(Block start, Block end)
    {
        addDecayMapping(start, 0, end, 0);
    }

    public static void addDecayMapping(Block start, int startMeta, Block end, int endMeta)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, endMeta));
    }

    public static void addDecayMapping(Block start, int startMeta, Block end)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, 0));
    }

    public static void initEvents()
    {
    	if(ConfigHandler.allowWarpEffect1)
    		warpEvents.add(new WarpBats(ConfigHandler.minimumWarpForEffect1));
    	if(ConfigHandler.allowWarpEffect2)
    		warpEvents.add(new WarpBlink(ConfigHandler.minimumWarpForEffect2));
    	if(ConfigHandler.allowWarpEffect3)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect3, "poison", new PotionEffect(Potion.poison.id, 20 * 20)));
    	if(ConfigHandler.allowWarpEffect4)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect4, "nausea", new PotionEffect(Potion.confusion.id, 20 * 20)));
    	if(ConfigHandler.allowWarpEffect5)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect5, "jump", new PotionEffect(Potion.jump.id, 20 * 20, 20)));
    	if(ConfigHandler.allowWarpEffect6)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect6, "blind", new PotionEffect(Potion.blindness.id, 20 * 20)));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect7)
    		warpEvents.add(new WarpDecay(ConfigHandler.minimumWarpForEffect7));
    	if(ConfigHandler.allowWarpEffect8)
    		warpEvents.add(new WarpEars(ConfigHandler.minimumWarpForEffect8));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect9)
    		warpEvents.add(new WarpSwamp(ConfigHandler.minimumWarpForEffect9));
    	if(ConfigHandler.allowWarpEffect10)
    		warpEvents.add(new WarpTongue(ConfigHandler.minimumWarpForEffect10));
    	if(ConfigHandler.allowWarpEffect11)
    		warpEvents.add(new WarpFriend(ConfigHandler.minimumWarpForEffect11));
    	if(ConfigHandler.allowWarpEffect12)
    		warpEvents.add(new WarpLivestockRain(ConfigHandler.minimumWarpForEffect12));
    	if(ConfigHandler.allowWarpEffect13)
    		warpEvents.add(new WarpWind(ConfigHandler.minimumWarpForEffect13));
    	if(ConfigHandler.allowWarpEffect14)
    		warpEvents.add(new WarpChests(ConfigHandler.minimumWarpForEffect14));
    	if(ConfigHandler.allowWarpEffect15)
    		warpEvents.add(new WarpBlood(ConfigHandler.minimumWarpForEffect15));
    	if(ConfigHandler.allowWarpEffect16)
    		warpEvents.add(new WarpAcceleration(ConfigHandler.minimumWarpForEffect16));
    	if(ConfigHandler.allowWarpEffect17)
    		warpEvents.add(new WarpLightning(ConfigHandler.minimumWarpForEffect17));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect18 && ConfigHandler.allowServerKickWarpEffects)
    		warpEvents.add(new WarpFall(ConfigHandler.minimumWarpForEffect18));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect19)
    		warpEvents.add(new WarpRain(ConfigHandler.minimumWarpForEffect19));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect20)
    		warpEvents.add(new WarpWither(ConfigHandler.minimumWarpForEffect20));
    	if(ConfigHandler.allowWarpEffect21)
    		warpEvents.add(new WarpFakeSound(ConfigHandler.minimumWarpForEffect21, "fakeexplosion", "random.explode", 8));
    	if(ConfigHandler.allowWarpEffect22)
    		warpEvents.add(new WarpFakeSoundBehind(ConfigHandler.minimumWarpForEffect22, "fakecreeper", "creeper.primed", 2));

        addDecayMapping(Blocks.grass, Blocks.dirt);
        addDecayMapping(Blocks.dirt, 0, Blocks.sand);
        addDecayMapping(Blocks.dirt, 1, Blocks.sand);
        addDecayMapping(Blocks.dirt, 2, Blocks.dirt);
        addDecayMapping(Blocks.stone, Blocks.cobblestone);
        addDecayMapping(Blocks.cobblestone, Blocks.gravel);
        addDecayMapping(Blocks.sandstone, Blocks.sand);
        addDecayMapping(Blocks.gravel, Blocks.sand);
        addDecayMapping(Blocks.sand, Blocks.air);
        addDecayMapping(Blocks.lava, Blocks.cobblestone);
        addDecayMapping(Blocks.flowing_lava, Blocks.cobblestone);
        addDecayMapping(Blocks.water, Blocks.air);
        addDecayMapping(Blocks.snow, Blocks.water);
        addDecayMapping(Blocks.snow_layer, Blocks.air);
        addDecayMapping(Blocks.ice, Blocks.water);
        addDecayMapping(Blocks.clay, Blocks.sand);
        addDecayMapping(Blocks.mycelium, Blocks.grass);
        addDecayMapping(Blocks.stained_hardened_clay, Blocks.hardened_clay);
        addDecayMapping(Blocks.hardened_clay, Blocks.clay);
        addDecayMapping(Blocks.coal_ore, Blocks.stone);
        addDecayMapping(Blocks.diamond_ore, Blocks.stone);
        addDecayMapping(Blocks.emerald_ore, Blocks.stone);
        addDecayMapping(Blocks.gold_ore, Blocks.stone);
        addDecayMapping(Blocks.iron_ore, Blocks.stone);
        addDecayMapping(Blocks.lapis_ore, Blocks.stone);
        addDecayMapping(Blocks.lit_redstone_ore, Blocks.stone);
        addDecayMapping(Blocks.redstone_ore, Blocks.stone);
        addDecayMapping(Blocks.quartz_ore, Blocks.netherrack);
        addDecayMapping(Blocks.netherrack, Blocks.cobblestone);
        addDecayMapping(Blocks.soul_sand, Blocks.sand);
        addDecayMapping(Blocks.glowstone, Blocks.cobblestone);
        addDecayMapping(Blocks.log, Blocks.dirt);
        addDecayMapping(Blocks.log2, Blocks.dirt);
        addDecayMapping(Blocks.brown_mushroom_block, Blocks.dirt);
        addDecayMapping(Blocks.red_mushroom_block, Blocks.dirt);
        addDecayMapping(Blocks.end_stone, Blocks.cobblestone);
        addDecayMapping(Blocks.obsidian, Blocks.cobblestone);
    }

    public static void purgeWarp(EntityPlayer player)
    {
        int count = queueMultipleEvents(player, getTotalWarp(player));
        addUnavoidableCount(player, count);
        removeWarp(player, getTotalWarp(player));
    }

	//add function to remove 5 warp, only at 50+
    public static void purgeWarpMinor(EntityPlayer player)
    {
        int[] warp = getIndividualWarps(player);
        if (warp[0] + warp[1] + warp[2] >= 50)
		{
			removeWarp(player, 5);
			ChatHelper.sendToPlayer(player, StatCollector.translateToLocal("chat.warptheory.purgeminor"));
		}
		else
			ChatHelper.sendToPlayer(player, StatCollector.translateToLocal("chat.warptheory.purgefailed"));
    }
	
    public static void removeWarp(EntityPlayer player, int amount)
    {
        if (amount <= 0)
            return;
        String name = player.getDisplayName();
        int wp = Knowledge.getWarpPerm(name);
        int wn = Knowledge.getWarpSticky(name);
        int wt = Knowledge.getWarpTemp(name);
        // reset the warp counter so
        // 1) if partial warp reduction, reset the counter so vanilla TC warp events would fire
        //    the same behavior can be observed on TC sanitizing soap
        // 2) if total warp reduction, the counter would be reduced to 0, so vanilla TC warp events would
        //    no longer fire
        Knowledge.setWarpCounter(name, wp + wn + wt - amount);

        Knowledge.addWarpTemp(name, -amount);
        amount -= wt;
        if (amount <= 0)
            return;

        Knowledge.addWarpSticky(name, -amount);
        amount -= wn;
        if (amount <= 0)
            return;

    	if (ConfigHandler.allowPermWarpRemoval)
    	{
            amount = (int) Math.ceil(amount / ConfigHandler.permWarpMult);
            Knowledge.addWarpPerm(name, -amount);
    	}
    }

    public static int getTotalWarp(EntityPlayer player)
    {
        String name = player.getDisplayName();
        int innerWarp = Knowledge.getWarpTotal(name);
        int extraPerm = Knowledge.getWarpPerm(name) * (int) Math.max(0, ConfigHandler.permWarpMult - 1);
        int outerWarp = getWarpFromGear(player);
        return innerWarp + extraPerm + outerWarp;
    }

    public static int[] getIndividualWarps(EntityPlayer player)
    {
        String userName = player.getDisplayName();
        int[] totals = new int[] { Knowledge.getWarpPerm(userName), Knowledge.getWarpSticky(userName), Knowledge.getWarpTemp(userName) };
        return totals;
    }

    public static int queueMultipleEvents(EntityPlayer player, int amount)
    {
        int w = amount;
        int count = 0;
        while (w > 0)
        {
            IWarpEvent event = queueOneEvent(player, w);
            if (event == null)
                return w;
            w -= event.getCost();
            count += 1;
        }
        return count;
    }

    public static IWarpEvent queueOneEvent(EntityPlayer player, int maxSeverity)
    {
        IWarpEvent event = getAppropriateEvent(player, maxSeverity);
        if (event != null)
            queueEvent(player, event);
        return event;
    }

    public static IWarpEvent getAppropriateEvent(EntityPlayer player, int maxSeverity)
    {
        ArrayList<IWarpEvent> shuffled = (ArrayList<IWarpEvent>)warpEvents.clone();
        Collections.shuffle(shuffled);
        for (IWarpEvent e : shuffled)
        {
            if (e.getSeverity() <= maxSeverity)
                return e;
        }
        return null;
    }

    public static int getWarpFromGear(EntityPlayer player)
    {
        int w = getFinalWarp(player.getCurrentEquippedItem(), player);
        for (int a = 0; a < 4; a++)
            w += getFinalWarp(player.inventory.armorItemInSlot(a), player);
        IInventory baubles = BaublesApi.getBaubles(player);
        for (int i = 0; i < 4; i++)
            w += getFinalWarp(baubles.getStackInSlot(i), player); 
        return w;
    }

    public static int getFinalWarp(ItemStack stack, EntityPlayer player)
    {
        if (stack == null || !(stack.getItem() instanceof IWarpingGear))
            return 0;
        IWarpingGear armor = (IWarpingGear) stack.getItem();
        return armor.getWarp(stack, player);
    }
	
    public static IWarpEvent getEventFromName(String name)
    {
        for (IWarpEvent event : warpEvents)
        {
            if (event.getName().equals(name))
                return event;
        }
        return null;
    }

    public static void queueEvent(EntityPlayer player, IWarpEvent event)
    {
        String queue;
        if (!MiscHelper.getWarpTag(player).hasKey("queuedEvents"))
            queue = "";
        else
            queue = MiscHelper.getWarpTag(player).getString("queuedEvents");
        queue += event.getName() + " ";
        MiscHelper.getWarpTag(player).setString("queuedEvents", queue);
    }

    public static IWarpEvent dequeueEvent(EntityPlayer player)
    {
        String queue;
        if (!MiscHelper.getWarpTag(player).hasKey("queuedEvents"))
            queue = "";
        else
            queue = MiscHelper.getWarpTag(player).getString("queuedEvents");
        if (queue.length() > 0)
        {
            ArrayList<String> names = new ArrayList<String>();
            for (String n : queue.split(" "))
                names.add(n);
            Collections.shuffle(names);
            String todo = names.remove(player.worldObj.rand.nextInt(names.size()));
            queue = "";
            for (String n : names)
                queue += n + " ";
            MiscHelper.getWarpTag(player).setString("queuedEvents", queue);
            return getEventFromName(todo);
        }
        return null;
    }
    
    public static void setUnavoidableCount(EntityPlayer player, int count)
    {
        if(ConfigHandler.disableRebound)
            return;
        UUID uuid = player.getUniqueID();
        Unavoidable.put(uuid, Math.max(0,count));
    }
    
    public static void addUnavoidableCount(EntityPlayer player, int count)
    {
        if(ConfigHandler.disableRebound)
            return;
        UUID uuid = player.getUniqueID();
        if(!Unavoidable.containsKey(uuid))
            Unavoidable.put(uuid, 0);
        count = Math.max(0,count + Unavoidable.get(uuid));
        Unavoidable.put(uuid, count);
    }
    
    public static int getUnavoidableCount(EntityPlayer player)
    {
        if(ConfigHandler.disableRebound)
            return 0;
        UUID uuid = player.getUniqueID();
        if(!Unavoidable.containsKey(uuid))
            Unavoidable.put(uuid, 0);
        return Unavoidable.get(uuid);
    }
}
