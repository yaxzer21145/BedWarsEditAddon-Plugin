package com.yaxzer.bwya.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {
    private static ConfigManager instance;
    private JavaPlugin plugin;
    
    private FileConfiguration config;
    private FileConfiguration xpBwConfig;
    private FileConfiguration healthAddConfig;
    private FileConfiguration shopConfig;
    private FileConfiguration messageConfig;
    private FileConfiguration shopItemsConfig;
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        loadAllConfigs();
    }
    
    private void loadAllConfigs() {
        loadConfig("Config.yml", "config");
        loadConfig("XpBw.yml", "xpBw");
        loadConfig("HealthAdd.yml", "healthAdd");
        loadConfig("Shop.yml", "shop");
        loadConfig("message.yml", "message");
        loadConfig("shop_items.yml", "shopItems");
    }
    
    private void loadConfig(String fileName, String configType) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        
        switch (configType) {
            case "config":
                this.config = config;
                break;
            case "xpBw":
                this.xpBwConfig = config;
                break;
            case "healthAdd":
                this.healthAddConfig = config;
                break;
            case "shop":
                this.shopConfig = config;
                break;
            case "message":
                this.messageConfig = config;
                break;
            case "shopItems":
                this.shopItemsConfig = config;
                break;
        }
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    public FileConfiguration getXpBwConfig() {
        return xpBwConfig;
    }
    
    public FileConfiguration getHealthAddConfig() {
        return healthAddConfig;
    }
    
    public FileConfiguration getShopConfig() {
        return shopConfig;
    }
    
    public FileConfiguration getMessageConfig() {
        return messageConfig;
    }
    
    public FileConfiguration getShopItemsConfig() {
        return shopItemsConfig;
    }
    
    public void reloadConfig() {
        loadAllConfigs();
    }
}