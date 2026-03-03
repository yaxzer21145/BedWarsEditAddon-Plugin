package com.yaxzer.bwya.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CombatManager {
    private static CombatManager instance;
    private Map<UUID, PlayerCombatData> playerCombatData = new HashMap<>();
    private File dataFolder;
    
    public static CombatManager getInstance() {
        if (instance == null) {
            instance = new CombatManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
        this.dataFolder = new File(plugin.getDataFolder(), "combat_data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        
        loadAllPlayerData();
    }
    
    private void loadAllPlayerData() {
        File[] files = dataFolder.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.getName().endsWith(".yml")) {
                UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                
                int combatPower = config.getInt("combat_power");
                int totalCombatPower = config.getInt("total_combat_power");
                String firstPlayTime = config.getString("first_play_time");
                long totalPlayTime = config.getLong("total_play_time");
                
                PlayerCombatData data = new PlayerCombatData(combatPower, totalCombatPower, firstPlayTime, totalPlayTime);
                playerCombatData.put(uuid, data);
            }
        }
    }
    
    private void savePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerCombatData.containsKey(uuid)) return;
        
        File file = new File(dataFolder, uuid.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        PlayerCombatData data = playerCombatData.get(uuid);
        config.set("combat_power", data.getCombatPower());
        config.set("total_combat_power", data.getTotalCombatPower());
        config.set("first_play_time", data.getFirstPlayTime());
        config.set("total_play_time", data.getTotalPlayTime());
        
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private PlayerCombatData getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerCombatData.containsKey(uuid)) {
            String firstPlayTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
            PlayerCombatData data = new PlayerCombatData(0, 0, firstPlayTime, 0);
            playerCombatData.put(uuid, data);
            savePlayerData(player);
        }
        return playerCombatData.get(uuid);
    }
    
    public void addCombatPower(Player player, int amount, String reason) {
        PlayerCombatData data = getPlayerData(player);
        int oldPower = data.getCombatPower();
        int oldTotalPower = data.getTotalCombatPower();
        
        data.setCombatPower(oldPower + amount);
        data.setTotalCombatPower(oldTotalPower + amount);
        
        checkForLevelUp(player, oldPower, data.getCombatPower());
        
        savePlayerData(player);
    }
    
    private void checkForLevelUp(Player player, int oldPower, int newPower) {
        String oldPrefix = getFullPrefix(oldPower);
        String newPrefix = getFullPrefix(newPower);
        
        if (!oldPrefix.equals(newPrefix)) {
            player.sendTitle(
                ConfigManager.getInstance().getMessageConfig().getString("combat_system.level_up"),
                ConfigManager.getInstance().getMessageConfig().getString("combat_system.new_level").replace("%level%", newPrefix),
                10, 70, 20
            );
        }
    }
    
    public String getFullPrefix(Player player) {
        PlayerCombatData data = getPlayerData(player);
        return getFullPrefix(data.getCombatPower());
    }
    
    public String getFullPrefix(int combatPower) {
        return getLadderPrefix(combatPower) + "-" + getLevelPrefix(combatPower);
    }
    
    public String getLadderPrefix(Player player) {
        PlayerCombatData data = getPlayerData(player);
        return getLadderPrefix(data.getCombatPower());
    }
    
    public String getLadderPrefix(int combatPower) {
        if (combatPower < 1500) {
            return "[坚韧黑铁]";
        } else if (combatPower < 3500) {
            return "[秩序白银]";
        } else if (combatPower < 6500) {
            return "[荣耀黄金]";
        } else if (combatPower < 10500) {
            return "[闪耀钻石]";
        } else {
            return "[至尊合金]";
        }
    }
    
    public String getLevelPrefix(Player player) {
        PlayerCombatData data = getPlayerData(player);
        return getLevelPrefix(data.getCombatPower());
    }
    
    public String getLevelPrefix(int combatPower) {
        if (combatPower < 1500) {
            int level = (combatPower / 200) + 1;
            if (level > 5) level = 5;
            return getRomanNumeral(6 - level);
        } else if (combatPower < 3500) {
            int level = ((combatPower - 1500) / 250) + 1;
            if (level > 5) level = 5;
            return getRomanNumeral(6 - level);
        } else if (combatPower < 6500) {
            int level = ((combatPower - 3500) / 300) + 1;
            if (level > 7) level = 7;
            return getRomanNumeral(8 - level);
        } else if (combatPower < 10500) {
            int level = ((combatPower - 6500) / 350) + 1;
            if (level > 7) level = 7;
            return getRomanNumeral(8 - level);
        } else {
            int level = ((combatPower - 10500) / 400) + 1;
            if (level > 20) level = 20;
            return getRomanNumeral(21 - level);
        }
    }
    
    private String getRomanNumeral(int number) {
        String[] romanNumerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"};
        return romanNumerals[Math.min(number, 20)];
    }
    
    public int getCurrentCombatPower(Player player) {
        PlayerCombatData data = getPlayerData(player);
        return data.getCombatPower();
    }
    
    public int getTotalCombatPower(Player player) {
        PlayerCombatData data = getPlayerData(player);
        return data.getTotalCombatPower();
    }
    
    public String getNumberCombatPower(Player player) {
        PlayerCombatData data = getPlayerData(player);
        int currentPower = data.getCombatPower();
        int nextLevelPower = getNextLevelPower(currentPower);
        return currentPower + "/" + nextLevelPower;
    }
    
    public String getGridCombatPower(Player player) {
        PlayerCombatData data = getPlayerData(player);
        int currentPower = data.getCombatPower();
        int nextLevelPower = getNextLevelPower(currentPower);
        int maxPower = nextLevelPower;
        int minPower = getCurrentLevelMinPower(currentPower);
        int range = maxPower - minPower;
        int currentInRange = currentPower - minPower;
        
        int filledBlocks = (currentInRange * 10) / range;
        if (filledBlocks > 10) filledBlocks = 10;
        
        StringBuilder grid = new StringBuilder();
        for (int i = 0; i < filledBlocks; i++) {
            grid.append("&7■");
        }
        for (int i = filledBlocks; i < 10; i++) {
            grid.append("&0■");
        }
        
        return grid.toString();
    }
    
    private int getCurrentLevelMinPower(int currentPower) {
        if (currentPower < 200) return 0;
        else if (currentPower < 450) return 200;
        else if (currentPower < 750) return 450;
        else if (currentPower < 1100) return 750;
        else if (currentPower < 1500) return 1100;
        else if (currentPower < 1750) return 1500;
        else if (currentPower < 2050) return 1750;
        else if (currentPower < 2400) return 2050;
        else if (currentPower < 2800) return 2400;
        else if (currentPower < 3500) return 2800;
        else if (currentPower < 3800) return 3500;
        else if (currentPower < 4150) return 3800;
        else if (currentPower < 4550) return 4150;
        else if (currentPower < 5000) return 4550;
        else if (currentPower < 5500) return 5000;
        else if (currentPower < 6050) return 5500;
        else if (currentPower < 6500) return 6050;
        else if (currentPower < 6850) return 6500;
        else if (currentPower < 7250) return 6850;
        else if (currentPower < 7700) return 7250;
        else if (currentPower < 8200) return 7700;
        else if (currentPower < 8750) return 8200;
        else if (currentPower < 9350) return 8750;
        else if (currentPower < 10000) return 9350;
        else if (currentPower < 10500) return 10000;
        else {
            int level = ((currentPower - 10500) / 400) + 1;
            return 10500 + (level - 1) * 400;
        }
    }
    
    private int getNextLevelPower(int currentPower) {
        if (currentPower < 200) return 200;
        else if (currentPower < 450) return 450;
        else if (currentPower < 750) return 750;
        else if (currentPower < 1100) return 1100;
        else if (currentPower < 1500) return 1500;
        else if (currentPower < 1750) return 1750;
        else if (currentPower < 2050) return 2050;
        else if (currentPower < 2400) return 2400;
        else if (currentPower < 2800) return 2800;
        else if (currentPower < 3500) return 3500;
        else if (currentPower < 3800) return 3800;
        else if (currentPower < 4150) return 4150;
        else if (currentPower < 4550) return 4550;
        else if (currentPower < 5000) return 5000;
        else if (currentPower < 5500) return 5500;
        else if (currentPower < 6050) return 6050;
        else if (currentPower < 6500) return 6500;
        else if (currentPower < 6850) return 6850;
        else if (currentPower < 7250) return 7250;
        else if (currentPower < 7700) return 7700;
        else if (currentPower < 8200) return 8200;
        else if (currentPower < 8750) return 8750;
        else if (currentPower < 9350) return 9350;
        else if (currentPower < 10000) return 10000;
        else if (currentPower < 10500) return 10500;
        else {
            int level = ((currentPower - 10500) / 400) + 1;
            return 10500 + level * 400;
        }
    }
    
    public String getLadder(Player player) {
        PlayerCombatData data = getPlayerData(player);
        int combatPower = data.getCombatPower();
        
        if (combatPower < 1500) return "坚韧黑铁";
        else if (combatPower < 3500) return "秩序白银";
        else if (combatPower < 6500) return "荣耀黄金";
        else if (combatPower < 10500) return "闪耀钻石";
        else return "至尊合金";
    }
    
    public int getCurrentLevelMaxPower(Player player) {
        PlayerCombatData data = getPlayerData(player);
        int currentPower = data.getCombatPower();
        return getNextLevelPower(currentPower);
    }
    
    public void addKillCombatPower(Player player) {
        addCombatPower(player, 2, "kill");
    }
    
    public void addBedDestroyCombatPower(Player player) {
        addCombatPower(player, 6, "bed_destroy");
    }
    
    public void addFinalKillCombatPower(Player player) {
        addCombatPower(player, 5, "final_kill");
    }
    
    public void saveAllPlayerData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player);
        }
    }
    
    public void updatePlayTime(Player player, long additionalTime) {
        PlayerCombatData data = getPlayerData(player);
        data.setTotalPlayTime(data.getTotalPlayTime() + additionalTime);
        savePlayerData(player);
    }
    
    public String getFirstPlayTime(Player player) {
        PlayerCombatData data = getPlayerData(player);
        return data.getFirstPlayTime();
    }
    
    public long getTotalPlayTime(Player player) {
        PlayerCombatData data = getPlayerData(player);
        return data.getTotalPlayTime();
    }
    
    private class PlayerCombatData {
        private int combatPower;
        private int totalCombatPower;
        private String firstPlayTime;
        private long totalPlayTime;
        
        public PlayerCombatData(int combatPower, int totalCombatPower, String firstPlayTime, long totalPlayTime) {
            this.combatPower = combatPower;
            this.totalCombatPower = totalCombatPower;
            this.firstPlayTime = firstPlayTime;
            this.totalPlayTime = totalPlayTime;
        }
        
        public int getCombatPower() {
            return combatPower;
        }
        
        public void setCombatPower(int combatPower) {
            this.combatPower = combatPower;
        }
        
        public int getTotalCombatPower() {
            return totalCombatPower;
        }
        
        public void setTotalCombatPower(int totalCombatPower) {
            this.totalCombatPower = totalCombatPower;
        }
        
        public String getFirstPlayTime() {
            return firstPlayTime;
        }
        
        public long getTotalPlayTime() {
            return totalPlayTime;
        }
        
        public void setTotalPlayTime(long totalPlayTime) {
            this.totalPlayTime = totalPlayTime;
        }
    }
}