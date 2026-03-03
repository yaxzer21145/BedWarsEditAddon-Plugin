package com.yaxzer.bwya.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LanguageManager {
    private static LanguageManager instance;
    private JavaPlugin plugin;
    private Map<UUID, String> playerLanguages = new HashMap<>();
    private FileConfiguration defaultLangConfig;
    private FileConfiguration enLangConfig;
    
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        loadLanguageConfigs();
    }
    
    private void loadLanguageConfigs() {
        File defaultLangFile = new File(plugin.getDataFolder(), "message.yml");
        if (!defaultLangFile.exists()) {
            plugin.saveResource("message.yml", false);
        }
        defaultLangConfig = YamlConfiguration.loadConfiguration(defaultLangFile);
        
        File enLangFile = new File(plugin.getDataFolder(), "message_en.yml");
        if (!enLangFile.exists()) {
            plugin.saveResource("message_en.yml", false);
        }
        enLangConfig = YamlConfiguration.loadConfiguration(enLangFile);
    }
    
    public String getPlayerLanguage(Player player) {
        return playerLanguages.getOrDefault(player.getUniqueId(), getDefaultLanguage());
    }
    
    public void setPlayerLanguage(Player player, String language) {
        if (isLanguageSupported(language)) {
            playerLanguages.put(player.getUniqueId(), language);
        }
    }
    
    public String getDefaultLanguage() {
        return ConfigManager.getInstance().getConfig().getString("language.default", "zh_CN");
    }
    
    public boolean isLanguageSupported(String language) {
        return ConfigManager.getInstance().getConfig().getStringList("language.supported").contains(language);
    }
    
    public String getMessage(Player player, String path) {
        return getMessage(player, path, "");
    }
    
    public String getMessage(Player player, String path, String... replacements) {
        FileConfiguration langConfig = getLanguageConfig(player);
        String message = langConfig.getString(path, "");
        
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }
        
        return message;
    }
    
    private FileConfiguration getLanguageConfig(Player player) {
        String language = getPlayerLanguage(player);
        if (language.equals("en_US")) {
            return enLangConfig;
        } else {
            return defaultLangConfig;
        }
    }
    
    public boolean switchLanguage(Player player, String language) {
        if (!isLanguageSupported(language)) {
            return false;
        }
        
        setPlayerLanguage(player, language);
        return true;
    }
    
    public void savePlayerLanguages() {
        File langDataFile = new File(plugin.getDataFolder(), "player_languages.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(langDataFile);
        
        for (Map.Entry<UUID, String> entry : playerLanguages.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        
        try {
            config.save(langDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadPlayerLanguages() {
        File langDataFile = new File(plugin.getDataFolder(), "player_languages.yml");
        if (!langDataFile.exists()) {
            return;
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(langDataFile);
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String language = config.getString(key);
                if (isLanguageSupported(language)) {
                    playerLanguages.put(uuid, language);
                }
            } catch (IllegalArgumentException e) {
            }
        }
    }
}