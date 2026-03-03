package com.yaxzer.bwya.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class HealthManager {
    private static HealthManager instance;
    private JavaPlugin plugin;
    private boolean isFirstHealthAdded = false;
    private boolean isSecondHealthAdded = false;
    
    public static HealthManager getInstance() {
        if (instance == null) {
            instance = new HealthManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        startHealthAddTasks();
    }
    
    private void startHealthAddTasks() {
        if (!ConfigManager.getInstance().getConfig().getBoolean("enabled_features.health_add")) {
            return;
        }
        
        int healthAddCount = ConfigManager.getInstance().getHealthAddConfig().getInt("health_add_count");
        
        if (healthAddCount == 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    addHealthToAllPlayers();
                }
            }.runTaskLater(plugin, 20 * 60 * 20L);
        } else if (healthAddCount == 2) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    addHealthToAllPlayers();
                    isFirstHealthAdded = true;
                    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            addHealthToAllPlayers();
                            isSecondHealthAdded = true;
                        }
                    }.runTaskLater(plugin, 25 * 60 * 20L);
                }
            }.runTaskLater(plugin, 15 * 60 * 20L);
        }
    }
    
    private void addHealthToAllPlayers() {
        int healthAmount = ConfigManager.getInstance().getHealthAddConfig().getInt("health_amount");
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setHealth(Math.min(player.getHealth() + healthAmount, 20.0));
            
            String title = ConfigManager.getInstance().getHealthAddConfig().getString("title.text");
            String subtitle = ConfigManager.getInstance().getHealthAddConfig().getString("subtitle.text");
            player.sendTitle(title, subtitle, 10, 70, 20);
            
            if (ConfigManager.getInstance().getHealthAddConfig().getBoolean("message.enabled")) {
                String message = ConfigManager.getInstance().getHealthAddConfig().getString("message.text");
                player.sendMessage(message);
            }
        }
    }
    
    public boolean isFirstHealthAdded() {
        return isFirstHealthAdded;
    }
    
    public boolean isSecondHealthAdded() {
        return isSecondHealthAdded;
    }
}