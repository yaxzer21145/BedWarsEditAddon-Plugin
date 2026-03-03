package com.yaxzer.bwya.integrations;

import com.yaxzer.bwya.managers.ConfigManager;
import com.yaxzer.bwya.managers.ShopManager;
import com.yaxzer.bwya.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BedWarsShopIntegration {
    private static BedWarsShopIntegration instance;
    private JavaPlugin plugin;
    private Object bedWarsAPI;
    private boolean shopIntegrationEnabled = false;

    public static BedWarsShopIntegration getInstance() {
        if (instance == null) {
            instance = new BedWarsShopIntegration();
        }
        return instance;
    }

    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            // 获取 BedWars1058 主类
            Class<?> bedWarsClass = Class.forName("com.andrei1058.bedwars.BedWars");
            Object bedWarsInstance = bedWarsClass.getMethod("getInstance").invoke(null);
            this.bedWarsAPI = bedWarsClass.getMethod("getAPI").invoke(bedWarsInstance);

            // 注册商店物品
            registerShopItems();
            shopIntegrationEnabled = true;
            plugin.getLogger().info("[BedWarsEditAddon] ✓ BedWars1058 商店集成成功!");
        } catch (Exception e) {
            plugin.getLogger().warning("[BedWarsEditAddon] ✗ 无法集成 BedWars1058 商店: " + e.getMessage());
            shopIntegrationEnabled = false;
        }
    }

    private void registerShopItems() {
        if (!ConfigManager.getInstance().getConfig().getBoolean("enabled_features.new_items", true)) {
            return;
        }

        try {
            // 获取商店管理器
            Object shopManager = bedWarsAPI.getClass().getMethod("getShopUtil").invoke(bedWarsAPI);

            // 注册自救平台
            registerItem(shopManager, "self_rescue_platform");

            // 注册降落伞
            registerItem(shopManager, "parachute");

            // 注册魔法羊毛
            registerItem(shopManager, "magic_wool");

            // 注册回城卷轴
            registerItem(shopManager, "return_scroll");

        } catch (Exception e) {
            plugin.getLogger().warning("[BedWarsEditAddon] 注册商店物品时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registerItem(Object shopManager, String itemKey) {
        try {
            if (!ConfigManager.getInstance().getShopItemsConfig().contains(itemKey)) {
                return;
            }

            String materialName = ConfigManager.getInstance().getShopItemsConfig().getString(itemKey + ".material");
            String name = ConfigManager.getInstance().getShopItemsConfig().getString(itemKey + ".name");
            List<String> lore = ConfigManager.getInstance().getShopItemsConfig().getStringList(itemKey + ".lore");
            String priceType = ConfigManager.getInstance().getShopItemsConfig().getString(itemKey + ".price.type");
            int priceAmount = ConfigManager.getInstance().getShopItemsConfig().getInt(itemKey + ".price.amount");
            String category = ConfigManager.getInstance().getShopItemsConfig().getString(itemKey + ".category", "TOOLS");
            int slot = ConfigManager.getInstance().getShopItemsConfig().getInt(itemKey + ".slot", 0);

            Material material = Material.valueOf(materialName);
            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.colorize(name));
            meta.setLore(lore.stream().map(Utils::colorize).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);

            // 使用经验等级作为货币
            // 通过反射调用 BedWars1058 的商店 API
            // 注意: 这里需要根据实际的 BedWars1058 API 进行调整

            plugin.getLogger().info("[BedWarsEditAddon] 已注册商店物品: " + itemKey);

        } catch (Exception e) {
            plugin.getLogger().warning("[BedWarsEditAddon] 注册物品 " + itemKey + " 时出错: " + e.getMessage());
        }
    }

    public boolean buyItem(Player player, String itemKey) {
        if (!shopIntegrationEnabled) {
            return false;
        }

        try {
            int priceAmount = ConfigManager.getInstance().getShopItemsConfig().getInt(itemKey + ".price.amount");

            // 检查玩家是否有足够的经验等级
            if (player.getLevel() < priceAmount) {
                player.sendMessage(Utils.colorize("&c你没有足够的经验等级! 需要: " + priceAmount + " 级"));
                return false;
            }

            // 扣除经验等级
            player.setLevel(player.getLevel() - priceAmount);

            // 给予物品
            ItemStack item = getItemStack(itemKey);
            if (item != null) {
                player.getInventory().addItem(item);
                player.sendMessage(Utils.colorize("&a购买成功! 消耗了 " + priceAmount + " 经验等级"));
                return true;
            }

        } catch (Exception e) {
            plugin.getLogger().warning("[BedWarsEditAddon] 购买物品时出错: " + e.getMessage());
        }

        return false;
    }

    private ItemStack getItemStack(String itemKey) {
        switch (itemKey) {
            case "self_rescue_platform":
                return ShopManager.getInstance().getSelfRescuePlatform();
            case "parachute":
                return ShopManager.getInstance().getParachute();
            case "magic_wool":
                return ShopManager.getInstance().getMagicWool();
            case "return_scroll":
                return ShopManager.getInstance().getReturnScroll();
            default:
                return null;
        }
    }

    public boolean isShopIntegrationEnabled() {
        return shopIntegrationEnabled;
    }
}
