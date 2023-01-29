package shukaro.warptheory.util;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChatHelper {

    private static Random rand = new Random();

    public static void sendToPlayer(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    public static void sendToAll(String message) {
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(message));
    }

    public static void sendToWorld(World world, String message) {
        for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities)
            player.addChatMessage(new ChatComponentText(message));
    }

    public static String getUsername(IChatComponent message) {
        if (!message.getUnformattedText().contains("<") || !message.getUnformattedText().contains(">")) return "";
        return message.getUnformattedText().split(" ")[0].replace("<", "").replace(">", "");
    }

    @SideOnly(Side.CLIENT)
    public static String getFormattedUsername(IChatComponent message) {
        if (!message.getFormattedText().contains("<") || !message.getFormattedText().contains(">")) return "";
        return message.getFormattedText().split(" ")[0];
    }

    public static String getText(IChatComponent message) {
        return message.getUnformattedText().replaceFirst("<.*> ", "");
    }

    @SideOnly(Side.CLIENT)
    public static String getFormattedText(IChatComponent message) {
        return message.getFormattedText().replaceFirst("<.*> ", "");
    }

    public static String garbleMessage(IChatComponent message) {
        String text = getText(message);
        String newText = "";
        for (String word : text.split("\\s+")) {
            String newWord = "";
            for (int i = 0; i < word.length(); i++) {
                String c;
                if (word.charAt(i) == '\u00A7') {
                    c = String.valueOf(word.charAt(i) + String.valueOf(word.charAt(i + 1)));
                    i++;
                } else c = rand.nextInt(5) == 0
                        ? FormatCodes.RandomChar.code + String.valueOf(word.charAt(i)) + FormatCodes.Reset.code
                        : String.valueOf(word.charAt(i));
                if (rand.nextBoolean()) newWord = newWord + c;
                else newWord = c + newWord;
            }
            if (rand.nextBoolean())
                newText = newText + (newWord.length() > 0 && newText.length() > 0 ? " " : "") + newWord;
            else newText = newWord + (newWord.length() > 0 && newText.length() > 0 ? " " : "") + newText;
        }
        return FormatCodes.Reset.code + newText + FormatCodes.Reset.code;
    }
}
