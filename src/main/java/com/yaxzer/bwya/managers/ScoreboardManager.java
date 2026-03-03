package com.yaxzer.bwya.managers;

import com.yaxzer.bwya.integrations.BedWars1058Integration;
import com.yaxzer.bwya.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    private static ScoreboardManager instance;
    private JavaPlugin plugin;
    private Map<Player, Scoreboard> playerScoreboards = new HashMap<>();
    private boolean enabled = true;

    public static ScoreboardManager getInstance() {
        if (instance == null) {
            instance = new ScoreboardManager();
        }
        return instance;
    }

    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        
        // 检查配置是否启用计分板覆盖
        this.enabled = ConfigManager.getInstance().getConfig().getBoolean("enabled_features.custom_scoreboard", true);
        
        if (enabled) {
            startScoreboardUpdater();
            plugin.getLogger().info("[BedWarsEditAddon] ✓ 自定义计分板已启用");
        }
    }

    private void startScoreboardUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }
            }
        }.runTaskTimer(plugin, 0, 20); // 每秒更新一次
    }

    public void updateScoreboard(Player player) {
        if (!enabled) return;
        
        // 检查玩家是否在游戏中
        if (!BedWars1058Integration.getInstance().isPlayerInGame(player)) {
            // 如果不在游戏中，移除自定义计分板
            if (playerScoreboards.containsKey(player)) {
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                playerScoreboards.remove(player);
            }
            return;
        }

        Scoreboard scoreboard = playerScoreboards.computeIfAbsent(player, p -> {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            p.setScoreboard(sb);
            return sb;
        });

        // 获取或创建 objective
        Objective objective = scoreboard.getObjective("bedwarseditaddon");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("bedwarseditaddon", "dummy");
            objective.setDisplayName(Utils.colorize("&e&lBEDWARS"));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        // 清除旧的分数
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // 设置计分板内容
        int score = 15;
        
        // 空行
        objective.getScore(Utils.colorize("&7")).setScore(score--);
        
        // 当前经验等级
        objective.getScore(Utils.colorize("&f经验等级: &a" + player.getLevel())).setScore(score--);
        
        // 空行
        objective.getScore(Utils.colorize("&7 ")).setScore(score--);
        
        // 战斗力量 (如果启用了战斗系统)
        if (ConfigManager.getInstance().getConfig().getBoolean("enabled_features.combat_system", true)) {
            int combatPower = CombatManager.getInstance().getCurrentCombatPower(player);
            objective.getScore(Utils.colorize("&f战斗力量: &c" + combatPower)).setScore(score--);
        }
        
        // 空行
        objective.getScore(Utils.colorize("&7  ")).setScore(score--);
        
        // 队伍信息
        try {
            int teamSize = BedWars1058Integration.getInstance().getTeamSize(player);
            objective.getScore(Utils.colorize("&f队伍人数: &e" + teamSize)).setScore(score--);
        } catch (Exception e) {
            // 忽略错误
        }
        
        // 空行
        objective.getScore(Utils.colorize("&7   ")).setScore(score--);
        
        // 服务器信息
        objective.getScore(Utils.colorize("&f在线玩家: &a" + Bukkit.getOnlinePlayers().size())).setScore(score--);
        
        // 空行
        objective.getScore(Utils.colorize("&7    ")).setScore(score--);
        
        // 底部信息
        objective.getScore(Utils.colorize("&e&lBedWarsEditAddon")).setScore(score--);
    }

    public void removeScoreboard(Player player) {
        if (playerScoreboards.containsKey(player)) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            playerScoreboards.remove(player);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            // 禁用计分板时，恢复所有玩家的默认计分板
            for (Player player : playerScoreboards.keySet()) {
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
            playerScoreboards.clear();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
