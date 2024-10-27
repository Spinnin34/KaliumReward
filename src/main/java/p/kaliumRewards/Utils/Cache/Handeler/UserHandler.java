package p.kaliumRewards.Utils.Cache.Handeler;


import p.kaliumRewards.Enum.RewardType;
import p.kaliumRewards.Manager.Database;
import p.kaliumRewards.Utils.Cache.User;

import java.util.HashMap;
import java.util.Map;

public class UserHandler {

    private final Database database;
    private final Map<String, User> userCache = new HashMap<>();

    public UserHandler(Database database) {
        this.database = database;
    }

    public User getUser(String uuid) {
        if (userCache.containsKey(uuid)) {
            return userCache.get(uuid);
        }

        User user = new User(uuid);
        for (RewardType type : RewardType.values()) {
            long lastClaimTime = database.getCooldown(uuid, type.getName());
            user.setCooldown(type, lastClaimTime);
        }

        userCache.put(uuid, user);
        return user;
    }

    public void saveUser(User user) {
        for (RewardType type : RewardType.values()) {
            long lastClaimTime = user.getCooldown(type);
            database.setCooldown(user.getUuid(), type.getName(), lastClaimTime);
        }
    }

    public void saveAllUsers() {
        for (User user : userCache.values()) {
            saveUser(user);
        }
    }

    public void updateCooldown(User user, RewardType rewardType) {
        user.setCooldown(rewardType, System.currentTimeMillis());
        saveUser(user);
    }
}
