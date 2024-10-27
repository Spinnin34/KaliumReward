package p.kaliumRewards.Enum;

public enum RewardType {
    DAILY("diaria", 24 * 60 * 60 * 1000L),
    WEEKLY("semanal", 7 * 24 * 60 * 60 * 1000L),
    MONTHLY("mensual", 30L * 24 * 60 * 60 * 1000L);

    private final String name;
    private final long cooldown;

    RewardType(String name, long cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public long getCooldown() {
        return cooldown;
    }

    public static RewardType fromString(String name) {
        for (RewardType type : RewardType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}

