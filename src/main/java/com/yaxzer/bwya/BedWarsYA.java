package com.yaxzer.bwya;

import org.bukkit.plugin.java.JavaPlugin;
import com.yaxzer.bwya.managers.PluginManager;
import com.yaxzer.bwya.listeners.PlayerListener;
import com.yaxzer.bwya.integrations.BedWars1058Integration;

public class BedWarsYA extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // 使用警告日志级别让启动信息更加明显
        getLogger().warning("╔══════════════════════════════════════════════════════════╗");
        getLogger().warning("║                                                          ║");
        getLogger().warning("║         BedWarsEditAddon v1.2.5 正在启动...              ║");
        getLogger().warning("║                                                          ║");
        getLogger().warning("╚══════════════════════════════════════════════════════════╝");
        
        getLogger().info("[BedWarsEditAddon] 作者: ya_xzer21145");
        getLogger().info("[BedWarsEditAddon] 适配版本: Minecraft 1.20.1");
        getLogger().info("[BedWarsEditAddon] 正在初始化 BedWars1058 集成...");
        BedWars1058Integration.getInstance().initialize();
        getLogger().info("[BedWarsEditAddon] ✓ BedWars1058 集成初始化完成!");
        
        getLogger().info("[BedWarsEditAddon] 正在初始化管理器...");
        PluginManager.getInstance().initialize(this);
        getLogger().info("[BedWarsEditAddon] ✓ 管理器初始化完成!");
        
        getLogger().info("[BedWarsEditAddon] 正在注册监听器...");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new com.yaxzer.bwya.listeners.ResourceListener(), this);
        getServer().getPluginManager().registerEvents(new com.yaxzer.bwya.listeners.BridgeEggListener(), this);
        getServer().getPluginManager().registerEvents(new com.yaxzer.bwya.listeners.ShopListener(), this);
        getLogger().info("[BedWarsEditAddon] ✓ 监听器注册完成!");
        
        getLogger().warning("╔══════════════════════════════════════════════════════════╗");
        getLogger().warning("║                                                          ║");
        getLogger().warning("║      ✓ BedWarsEditAddon v1.2.5 已成功启用!               ║");
        getLogger().warning("║                                                          ║");
        getLogger().warning("╠══════════════════════════════════════════════════════════╣");
        getLogger().warning("║  功能特性:                                               ║");
        getLogger().warning("║    • 资源转经验系统                                      ║");
        getLogger().warning("║    • 生命恢复事件系统                                    ║");
        getLogger().warning("║    • 新物品系统 (自救平台/降落伞/魔法羊毛/回城卷轴)      ║");
        getLogger().warning("║    • 搭桥蛋修改                                          ║");
        getLogger().warning("║    • 战斗系统                                            ║");
        getLogger().warning("║    • 变量系统                                            ║");
        getLogger().warning("║    • 中英双语支持                                        ║");
        getLogger().warning("║    • BedWars1058 集成                                    ║");
        getLogger().warning("╚══════════════════════════════════════════════════════════╝");
    }

    @Override
    public void onDisable() {
        getLogger().warning("[BedWarsEditAddon] ✗ BedWarsEditAddon v1.2.5 已禁用!");
    }
    
}