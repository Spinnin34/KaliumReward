package p.kaliumRewards.Utils.Cache;

import p.kaliumRewards.Enum.RewardType;

import java.util.HashMap;
import java.util.Map;

public class User {

    private final String uuid;
    private final Map<RewardType, Long> cooldowns = new HashMap<>();

    public User(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public long getCooldown(RewardType rewardType) {
        return cooldowns.getOrDefault(rewardType, 0L);
    }

    public void setCooldown(RewardType rewardType, long time) {
        cooldowns.put(rewardType, time);
    }

    public boolean canClaim(RewardType rewardType) {
        long lastClaimTime = getCooldown(rewardType);
        long currentTime = System.currentTimeMillis();
        return currentTime - lastClaimTime >= rewardType.getCooldown();
    }
}

