package com.yaxzer.bwya.listeners;

import com.yaxzer.bwya.integrations.BedWars1058Integration;
import com.yaxzer.bwya.managers.ConfigManager;
import com.yaxzer.bwya.managers.ShopManager;
import com.yaxzer.bwya.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class ShopListener implements Listener {

    private final Map<String, String> itemMapping = new HashMap<>();

    public ShopListener() {
        // 初始化物品名称映射
        itemMapping.put("自救平台", "self_rescue_platform");
        itemMapping.put("降落伞", "parachute");
        itemMapping.put("魔法羊毛", "magic_wool");
        itemMapping.put("回城卷轴", "return_scroll");
        // 英文名称映射
        itemMapping.put("Self Rescue Platform", "self_rescue_platform");
        itemMapping.put("Parachute", "parachute");
        itemMapping.put("Magic Wool", "magic_wool");
        itemMapping.put("Return Scroll", "return_scroll");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        // 检查玩家是否在游戏中
        if (!BedWars1058Integration.getInstance().isPlayerInGame(player)) return;

        // 检查是否是 BedWars 商店
        String title = event.getView().getTitle();
        if (!title.contains("商店") && !title.contains("Shop") && !title.contains("Store")) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String itemName = meta.getDisplayName();

        // 检查是否是我们的自定义物品
        for (Map.Entry<String, String> entry : itemMapping.entrySet()) {
            if (itemName.contains(entry.getKey())) {
                event.setCancelled(true);
                handlePurchase(player, entry.getValue());
                return;
            }
        }
    }

    private void handlePurchase(Player player, String itemKey) {
        if (!ConfigManager.getInstance().getShopItemsConfig().contains(itemKey)) {
            player.sendMessage(Utils.colorize("&c物品配置不存在!"));
            return;
        }

        int priceAmount = ConfigManager.getInstance().getShopItemsConfig().getInt(itemKey + ".price.amount");

        // 检查玩家是否有足够的经验等级
        if (player.getLevel() < priceAmount) {
            player.sendMessage(Utils.colorize("&c你没有足够的经验等级! 需要: &e" + priceAmount + " &c级"));
            player.closeInventory();
            return;
        }

        // 扣除经验等级
        player.setLevel(player.getLevel() - priceAmount);

        // 给予物品
        ItemStack item = getItemStack(itemKey);
        if (item != null) {
            // 检查背包是否已满
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Utils.colorize("&c你的背包已满!"));
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }

            String itemName = ConfigManager.getInstance().getShopItemsConfig().getString(itemKey + ".name");
            player.sendMessage(Utils.colorize("&a购买成功! &7消耗了 &e" + priceAmount + " &7经验等级"));
            player.sendMessage(Utils.colorize("&a你获得了 " + itemName));
            player.closeInventory();
        } else {
            player.sendMessage(Utils.colorize("&c物品获取失败!"));
        }
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
}
