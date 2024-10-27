package p.kaliumRewards.Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import p.kaliumRewards.Enum.RewardType;
import p.kaliumRewards.Utils.Messages.ColoredStringUtil;
import p.kaliumRewards.Utils.Messages.LangUtil;

import java.util.List;

public class RewardService {

    private final FileConfiguration config;

    public RewardService(FileConfiguration config) {
        this.config = config;
    }

    public void giveReward(Player player, RewardType type) {
        String rewardPath = "rewards." + type.getName();
        List<String> commands = config.getStringList(rewardPath + ".commands");
        List<String> messages = config.getStringList(rewardPath + ".messages");

        if (commands != null && !commands.isEmpty()) {
            for (String command : commands) {
                String formattedCommand = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
            }

            if (messages != null && !messages.isEmpty()) {
                for (String msg : messages) {
                    player.sendMessage(ColoredStringUtil.colorHex(msg));
                }
            } else {
                player.sendMessage(LangUtil.getString("messages.not-config", true));
            }
        } else {
            player.sendMessage(LangUtil.getString("messages.not-config-type", true));
        }
    }

    public String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + " días";
        } else if (hours > 0) {
            return hours + " horas";
        } else if (minutes > 0) {
            return minutes + " minutos";
        } else {
            return seconds + " segundos";
        }
    }
}
