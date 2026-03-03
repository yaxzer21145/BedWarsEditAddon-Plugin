package com.yaxzer.bwya.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.yaxzer.bwya.managers.ConfigManager;
import com.yaxzer.bwya.utils.Utils;

public class ResourceListener implements Listener {

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack item = event.getItem().getItemStack();
        Material material = item.getType();

        if (!ConfigManager.getInstance().getConfig().getBoolean("enabled_features.resource_to_exp")) {
            return;
        }

        // 检查是否是资源物品
        if (material != Material.IRON_INGOT && material != Material.GOLD_INGOT && material != Material.EMERALD) {
            return;
        }

        // 取消事件，防止玩家获得物品
        event.setCancelled(true);
        // 移除地上的物品实体
        event.getItem().remove();

        int ironLevel = ConfigManager.getInstance().getXpBwConfig().getInt("xp_values.iron");
        int goldLevel = ConfigManager.getInstance().getXpBwConfig().getInt("xp_values.gold");
        int emeraldLevel = ConfigManager.getInstance().getXpBwConfig().getInt("xp_values.emerald");

        int levels = 0;
        if (material == Material.IRON_INGOT) {
            levels = ironLevel * item.getAmount();
        } else if (material == Material.GOLD_INGOT) {
            levels = goldLevel * item.getAmount();
        } else if (material == Material.EMERALD) {
            levels = emeraldLevel * item.getAmount();
        }

        // 给予等级而不是经验值
        player.giveExpLevels(levels);
        sendXpActionBar(player, levels);
    }

    private void sendXpActionBar(Player player, int levels) {
        if (ConfigManager.getInstance().getMessageConfig().getBoolean("resource_to_exp.enabled")) {
            String message = ConfigManager.getInstance().getMessageConfig().getString("resource_to_exp.message");
            message = message.replace("%xp%", String.valueOf(levels));
            // 使用 ActionBar 发送消息
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.colorize(message)));
        }
    }
}