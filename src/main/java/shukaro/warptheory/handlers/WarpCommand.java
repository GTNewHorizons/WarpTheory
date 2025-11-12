package shukaro.warptheory.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import shukaro.warptheory.util.MiscHelper;

public class WarpCommand implements ICommand {

    private Supplier<ImmutableMap<String, IWarpEvent>> nameMap = Suppliers.memoize(this::getNameMap)::get;
    private Supplier<ImmutableList<String>> timers = Suppliers.memoize(this::getTimers)::get;

    @Override
    public String getCommandName() {
        return "warptheory";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/warptheory <event> [player]";
    }

    @Override
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0 || args.length > 2) {
            String events = "";
            for (String name : nameMap.get().keySet()) events += name + " ";
            sender.addChatMessage(new ChatComponentText("Invalid Syntax, available events are: " + events));
            sender.addChatMessage(new ChatComponentText("Additional commands: print purge"));
        } else {
            EntityPlayer player;
            if (args.length == 1) {
                player = MiscHelper.getPlayerByName(sender.getCommandSenderName());
            } else {
                player = MiscHelper.getPlayerByName(args[1]);
            }

            if ("print".equals(args[0])) {
                boolean hasEffect = false;
                for (String name : nameMap.get().keySet()) {
                    int amount = MiscHelper.getWarpTag(player).getInteger(name);
                    if (amount > 0) {
                        sender.addChatMessage(new ChatComponentText(name + ": " + amount));
                        hasEffect = true;
                    }
                }
                for (String timer : timers.get()) {
                    int count = MiscHelper.getWarpTag(player).getInteger(timer);
                    if (count > 0) {
                        sender.addChatMessage(new ChatComponentText(timer + ": " + count));
                        hasEffect = true;
                    }
                }
                if (!hasEffect) {
                    sender.addChatMessage(new ChatComponentText("No active warp effects!"));
                }
            } else if ("purge".equals(args[0])) {
                boolean hasEffect = false;
                for (String name : nameMap.get().keySet()) {
                    if (MiscHelper.getWarpTag(player).getInteger(name) > 0) {
                        MiscHelper.getWarpTag(player).removeTag(name);
                        sender.addChatMessage(new ChatComponentText("Purged " + name + "!"));
                        hasEffect = true;
                    }
                }
                for (String timer : timers.get()) {
                    if (MiscHelper.getWarpTag(player).getInteger(timer) > 0) {
                        MiscHelper.getWarpTag(player).removeTag(timer);
                        sender.addChatMessage(new ChatComponentText("Purged " + timer + "!"));
                        hasEffect = true;
                    }
                }
                if (!hasEffect) {
                    sender.addChatMessage(new ChatComponentText("No active warp effects!"));
                }
            } else {
                IWarpEvent event = nameMap.get().get(args[0]);
                if (event == null) {
                    sender.addChatMessage(new ChatComponentText("Could not find warp event: " + args[0]));
                    return;
                }

                if (event instanceof IMultiWarpEvent) {
                    ((IMultiWarpEvent) event).doEvent(args[0], sender.getEntityWorld(), player);
                } else {
                    event.doEvent(sender.getEntityWorld(), player);
                }
                sender.addChatMessage(new ChatComponentText("Triggered warp event: " + args[0]));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayer player = MiscHelper.getPlayerByName(sender.getCommandSenderName());
            return player.capabilities.isCreativeMode || MiscHelper.isOp(sender.getCommandSenderName());
        } else {
            return sender instanceof MinecraftServer;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            ArrayList<String> completions = new ArrayList<String>();
            for (String name : nameMap.get().keySet()) {
                if (name.startsWith(args[0])) completions.add(name);
            }
            return completions;
        } else if (args.length == 2) {
            ArrayList<String> completions = new ArrayList<String>();
            for (EntityPlayer serverPlayer : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                if (serverPlayer.getCommandSenderName().startsWith(args[1]))
                    completions.add(serverPlayer.getCommandSenderName());
            }
            return completions;
        } else return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i) {
        return args.length == 2 && i == 1;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof ICommand) {
            ICommand other = (ICommand) o;
            return this.getCommandName().compareTo(other.getCommandName());
        }
        return 0;
    }

    /**
     * Don't call this directly! Call {@code nameMap.get()} instead.
     */
    private synchronized ImmutableMap<String, IWarpEvent> getNameMap() {
        ImmutableMap.Builder<String, IWarpEvent> builder = ImmutableMap.builder();
        for (IWarpEvent e : WarpHandler.warpEvents) {
            if (e instanceof IMultiWarpEvent) {
                ((IMultiWarpEvent) e).getEventLevels().values().forEach(name -> builder.put(name, e));
            } else {
                builder.put(e.getName(), e);
            }
        }
        return builder.build();
    }

    /**
     * Don't call this directly! Call {@code timers.get()} instead.
     */
    private synchronized ImmutableList<String> getTimers() {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (IWarpEvent e : WarpHandler.warpEvents) {
            if (e instanceof ITimerWarpEvent) {
                ((ITimerWarpEvent) e).getTimers().values().forEach(builder::add);
            }
        }
        return builder.build();
    }
}
