package p.kaliumRewards.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p.kaliumRewards.Enum.RewardType;
import p.kaliumRewards.Utils.Cache.Handeler.UserHandler;
import p.kaliumRewards.Utils.Cache.User;
import p.kaliumRewards.Utils.Messages.LangUtil;
import p.kaliumRewards.Utils.RewardService;

public class RewardCommand implements CommandExecutor {

    private final UserHandler userHandler;
    private final RewardService rewardService;

    public RewardCommand(UserHandler userHandler, RewardService rewardService) {
        this.userHandler = userHandler;
        this.rewardService = rewardService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LangUtil.getString("messages.not-player", true));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("kalium.reward.use")) {
            player.sendMessage(LangUtil.getString("messages.not-perms", true));
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(LangUtil.getString("messages.use", true));
            return false;
        }

        User user = userHandler.getUser(player.getUniqueId().toString());
        if (user == null) {
            player.sendMessage("Error: No se pudo encontrar tu perfil de usuario.");
            return false;
        }

        RewardType rewardType = RewardType.fromString(args[0]);
        if (rewardType == null) {
            player.sendMessage(LangUtil.getString("messages.type-invalid", true));
            return false;
        }

        if (user.canClaim(rewardType)) {
            rewardService.giveReward(player, rewardType);
            userHandler.updateCooldown(user, rewardType);
        } else {
            long cooldown = rewardType.getCooldown();
            long timeSinceLastClaim = System.currentTimeMillis() - user.getCooldown(rewardType);
            long timeRemaining = cooldown - timeSinceLastClaim;

            if (timeRemaining > 0) {
                player.sendMessage(LangUtil.getMessageList("messages.remaining-time", false)
                        .replace("%time%", rewardService.formatTime(timeRemaining))
                        .replace("%typereward%", rewardType.getName()));
            } else {
                player.sendMessage(LangUtil.getString("messages.error-cooldown", true));
            }
        }

        return true;
    }
}

