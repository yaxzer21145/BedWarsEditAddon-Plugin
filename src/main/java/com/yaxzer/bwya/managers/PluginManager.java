package com.yaxzer.bwya.managers;

import com.yaxzer.bwya.integrations.BedWarsShopIntegration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginManager {
    private static PluginManager instance;
    
    public static PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }
    
    public void initialize() {
        System.out.println("Warning: PluginManager.initialize() called without plugin instance. Managers may not be initialized correctly.");
    }
    
    public void initialize(JavaPlugin plugin) {
        ConfigManager.getInstance().initialize(plugin);
        LanguageManager.getInstance().initialize(plugin);
        HealthManager.getInstance().initialize(plugin);
        ItemManager.getInstance().initialize(plugin);
        CombatManager.getInstance().initialize(plugin);
        VariableManager.getInstance().initialize(plugin);
        ShopManager.getInstance().initialize(plugin);
        CommandManager.getInstance().initialize(plugin);
        
        // 初始化计分板管理器
        plugin.getLogger().info("[BedWarsEditAddon] 正在初始化计分板...");
        ScoreboardManager.getInstance().initialize(plugin);
        
        // 初始化 BedWars1058 商店集成
        plugin.getLogger().info("[BedWarsEditAddon] 正在初始化商店集成...");
        BedWarsShopIntegration.getInstance().initialize(plugin);
    }
}