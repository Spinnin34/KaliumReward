package p.kaliumRewards.Runnable;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import p.kaliumRewards.Utils.Cache.Handeler.UserHandler;

public class AutoSaveTask extends BukkitRunnable {

    private final UserHandler userHandler;
    private final Plugin plugin;

    public AutoSaveTask(UserHandler userHandler, Plugin plugin) {
        this.userHandler = userHandler;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        userHandler.saveAllUsers();
    }

    public void startTask() {
        long interval = 6000L;
        this.runTaskTimer(plugin, interval, interval);
    }
}

