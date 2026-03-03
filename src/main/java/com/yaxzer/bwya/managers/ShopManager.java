package com.yaxzer.bwya.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import com.yaxzer.bwya.utils.Utils;

public class ShopManager {
    private static ShopManager instance;
    private JavaPlugin plugin;
    
    public static ShopManager getInstance() {
        if (instance == null) {
            instance = new ShopManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        loadShopItems();
    }
    
    private void loadShopItems() {
        if (!ConfigManager.getInstance().getConfig().getBoolean("enabled_features.new_items")) {
            return;
        }
        
        loadSelfRescuePlatform();
        loadParachute();
        loadMagicWool();
        loadReturnScroll();
    }
    
    private void loadSelfRescuePlatform() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("self_rescue_platform")) {
            return;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("self_rescue_platform.material");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("self_rescue_platform.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("self_rescue_platform.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        
        plugin.getLogger().info("Loaded Self Rescue Platform item");
    }
    
    private void loadParachute() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("parachute")) {
            return;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("parachute.material");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("parachute.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("parachute.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        
        plugin.getLogger().info("Loaded Parachute item");
    }
    
    private void loadMagicWool() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("magic_wool")) {
            return;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("magic_wool.material");
        int data = ConfigManager.getInstance().getShopItemsConfig().getInt("magic_wool.data");
        boolean enchanted = ConfigManager.getInstance().getShopItemsConfig().getBoolean("magic_wool.enchanted");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("magic_wool.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("magic_wool.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        
        if (enchanted) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        }
        
        item.setItemMeta(meta);
        
        plugin.getLogger().info("Loaded Magic Wool item");
    }
    
    private void loadReturnScroll() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("return_scroll")) {
            return;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("return_scroll.material");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("return_scroll.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("return_scroll.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        
        plugin.getLogger().info("Loaded Return Scroll item");
    }
    
    public ItemStack getSelfRescuePlatform() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("self_rescue_platform")) {
            return null;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("self_rescue_platform.material");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("self_rescue_platform.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("self_rescue_platform.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        
        return item;
    }
    
    public ItemStack getParachute() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("parachute")) {
            return null;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("parachute.material");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("parachute.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("parachute.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        
        return item;
    }
    
    public ItemStack getMagicWool() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("magic_wool")) {
            return null;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("magic_wool.material");
        int data = ConfigManager.getInstance().getShopItemsConfig().getInt("magic_wool.data");
        boolean enchanted = ConfigManager.getInstance().getShopItemsConfig().getBoolean("magic_wool.enchanted");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("magic_wool.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("magic_wool.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        
        if (enchanted) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        }
        
        item.setItemMeta(meta);
        
        return item;
    }
    
    public ItemStack getReturnScroll() {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains("return_scroll")) {
            return null;
        }
        
        String materialName = ConfigManager.getInstance().getShopItemsConfig().getString("return_scroll.material");
        String name = ConfigManager.getInstance().getShopItemsConfig().getString("return_scroll.name");
        java.util.List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList("return_scroll.lore");
        
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        
        return item;
    }
}