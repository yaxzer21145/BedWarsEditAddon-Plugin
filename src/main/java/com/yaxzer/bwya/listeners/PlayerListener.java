package com.yaxzer.bwya.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.yaxzer.bwya.managers.ItemManager;
import com.yaxzer.bwya.managers.ScoreboardManager;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 清理玩家计分板
        ScoreboardManager.getInstance().removeScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemManager.getInstance().handleItemInteract(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (ItemManager.getInstance().isPlayerReturning(event.getPlayer())) {
            ItemManager.getInstance().cancelReturn(event.getPlayer());
        }
    }
}