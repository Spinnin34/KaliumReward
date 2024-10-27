package p.kaliumRewards.Manager;

import java.sql.*;

public class Database {

    private static Database instance;
    private Connection connection;

    private Database() { }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void connect(String path) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS player_cooldowns (" +
                        "uuid TEXT NOT NULL, " +
                        "reward_type TEXT NOT NULL, " +
                        "last_claim_time LONG NOT NULL, " +
                        "PRIMARY KEY (uuid, reward_type))")) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getCooldown(String uuid, String rewardType) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT last_claim_time FROM player_cooldowns WHERE uuid = ? AND reward_type = ?")) {
            statement.setString(1, uuid);
            statement.setString(2, rewardType);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("last_claim_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setCooldown(String uuid, String rewardType, long lastClaimTime) {
        try (PreparedStatement statement = connection.prepareStatement(
                "REPLACE INTO player_cooldowns (uuid, reward_type, last_claim_time) VALUES (?, ?, ?)")) {
            statement.setString(1, uuid);
            statement.setString(2, rewardType);
            statement.setLong(3, lastClaimTime);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
