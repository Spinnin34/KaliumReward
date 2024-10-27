package p.kaliumRewards.Utils.Messages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import p.kaliumRewards.KaliumRewards;
import p.kaliumRewards.Messages.MessageUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LangUtil {


    private static FileConfiguration langConfig = null;
    private static File langFile = null;

    public LangUtil(KaliumRewards plugin) {

        saveDefaultLang();
    }

    public static void reloadLang() {
        if (langFile == null) {
            langFile = new File(KaliumRewards.getInstance().getDataFolder(), "lang.yml");
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        InputStream defaultLangStream = KaliumRewards.getInstance().getResource("lang.yml");
        if (defaultLangStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultLangStream));
            langConfig.setDefaults(defaultConfig);
        }
    }

    public static FileConfiguration getLang() {
        if (langConfig == null) {
            reloadLang();
        }
        return langConfig;
    }

    public void saveDefaultLang() {
        if (langFile == null) {
            langFile = new File(KaliumRewards.getInstance().getDataFolder(), "lang.yml");
        }
        if (!langFile.exists()) {
           KaliumRewards.getInstance().saveResource("lang.yml", false);
        }
    }

    public static String getString(String path, boolean usePrefix) {
        String rawString = getLang().getString(path);

        if (rawString == null) {
            return "Texto por defecto para " + path;
        }

        if (usePrefix) {
            String prefix = getLang().getString("messages.prefix", "");
            rawString = prefix + rawString;
        }

        return ColoredStringUtil.colorHex(rawString);
    }

    public static @NotNull String getMessageList(String path, boolean centered) {
        List<String> messages = getLang().getStringList(path);
        StringBuilder formattedMessages = new StringBuilder();

        for (String message : messages) {
            String coloredMessage = ColoredStringUtil.colorHex(message);

            if (centered) {
                formattedMessages.append(MessageUtils.sendCenteredMessage(coloredMessage)).append("\n");
            } else {
                formattedMessages.append(coloredMessage).append("\n");
            }
        }

        return formattedMessages.toString();
    }


    public static void sendMessageList(Player player, String path, boolean centered) {
        String messages = getMessageList(path, centered);
        player.sendMessage(messages);
    }
}
