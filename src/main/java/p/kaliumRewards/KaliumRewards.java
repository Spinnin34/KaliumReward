package p.kaliumRewards;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import p.kaliumRewards.Commands.RewardCommand;
import p.kaliumRewards.Commands.TabCompleter.RewardTabCompleter;
import p.kaliumRewards.Manager.Database;
import p.kaliumRewards.Runnable.AutoSaveTask;
import p.kaliumRewards.Utils.Cache.Handeler.UserHandler;
import p.kaliumRewards.Utils.Messages.LangUtil;
import p.kaliumRewards.Utils.RewardService;

public final class KaliumRewards extends JavaPlugin {
    private static KaliumRewards instance;
    private UserHandler userHandler;
    private RewardService rewardService;
    private Database database;
    private LangUtil langUtil;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        setupConfig();
        database = Database.getInstance();
        database.connect(getDataFolder() + "/database.db");

        userHandler = new UserHandler(database);
        rewardService = new RewardService(getConfig());

        getCommand("recompensa").setExecutor(new RewardCommand(userHandler, rewardService));
        getCommand("recompensa").setTabCompleter(new RewardTabCompleter());

        AutoSaveTask autoSaveTask = new AutoSaveTask(userHandler, this);
        autoSaveTask.startTask();

        Bukkit.getLogger().info("KaliumRewards ha sido activado.");
    }

    public void setupConfig() {
        langUtil = new LangUtil(this);
        langUtil.saveDefaultLang();
    }

    @Override
    public void onDisable() {
        database.close();
        Bukkit.getLogger().info("KaliumRewards ha sido desactivado.");
    }

    public static KaliumRewards getInstance() {
        return instance;
    }
}
