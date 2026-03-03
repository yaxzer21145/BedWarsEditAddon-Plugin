package com.yaxzer.bwya.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.yaxzer.bwya.integrations.BedWars1058Integration;

public class VariableManager {
    private static VariableManager instance;
    
    public static VariableManager getInstance() {
        if (instance == null) {
            instance = new VariableManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
    }
    
    public String parseVariables(Player player, String message) {
        if (message == null) return "";
        
        if (!ConfigManager.getInstance().getConfig().getBoolean("enabled_features.variable_system")) {
            return message;
        }
        
        message = message.replace("%bw1058_teampeople%", getTeamPeople(player));
        message = message.replace("%bw1058_totalplaytime%", getTotalPlayTime(player));
        message = message.replace("%bw1058_firstplaytime%", getFirstPlayTime(player));
        message = message.replace("%bw1058_fullprefix%", getFullPrefix(player));
        message = message.replace("%bw1058_ladderprefix%", getLadderPrefix(player));
        message = message.replace("%bw1058_levelprefix%", getLevelPrefix(player));
        message = message.replace("%bw1058_numbercombatpower%", getNumberCombatPower(player));
        message = message.replace("%bw1058_gridcombatpower%", getGridCombatPower(player));
        message = message.replace("%bw1058_nowcombatpower%", getNowCombatPower(player));
        message = message.replace("%bw1058_totalcombatpower%", getTotalCombatPower(player));
        message = message.replace("%bw1058_nowlargepowervalue%", getNowLargePowerValue(player));
        message = message.replace("%bw1058_ladder%", getLadder(player));
        
        return message;
    }
    
    private String getTeamPeople(Player player) {
        return String.valueOf(BedWars1058Integration.getInstance().getTeamSize(player));
    }
    
    private String getTotalPlayTime(Player player) {
        long totalPlayTime = CombatManager.getInstance().getTotalPlayTime(player);
        long hours = totalPlayTime / 3600;
        long minutes = (totalPlayTime % 3600) / 60;
        long seconds = totalPlayTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    private String getFirstPlayTime(Player player) {
        return CombatManager.getInstance().getFirstPlayTime(player);
    }
    
    private String getFullPrefix(Player player) {
        return CombatManager.getInstance().getFullPrefix(player);
    }
    
    private String getLadderPrefix(Player player) {
        return CombatManager.getInstance().getLadderPrefix(player);
    }
    
    private String getLevelPrefix(Player player) {
        return CombatManager.getInstance().getLevelPrefix(player);
    }
    
    private String getNumberCombatPower(Player player) {
        return CombatManager.getInstance().getNumberCombatPower(player);
    }
    
    private String getGridCombatPower(Player player) {
        return CombatManager.getInstance().getGridCombatPower(player);
    }
    
    private String getNowCombatPower(Player player) {
        return String.valueOf(CombatManager.getInstance().getCurrentCombatPower(player));
    }
    
    private String getTotalCombatPower(Player player) {
        return String.valueOf(CombatManager.getInstance().getTotalCombatPower(player));
    }
    
    private String getNowLargePowerValue(Player player) {
        return String.valueOf(CombatManager.getInstance().getCurrentLevelMaxPower(player));
    }
    
    private String getLadder(Player player) {
        return CombatManager.getInstance().getLadder(player);
    }
}