package com.yaxzer.bwya.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import com.yaxzer.bwya.integrations.BedWars1058Integration;
import com.yaxzer.bwya.utils.Utils;

public class ItemManager {
    private static ItemManager instance;
    private JavaPlugin plugin;
    private Map<Player, Long> selfRescueCooldowns = new HashMap<>();
    private Map<Player, Long> returnScrollCooldowns = new HashMap<>();
    private Map<Player, Boolean> isReturning = new HashMap<>();
    
    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void handleItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null) return;
        
        Material material = item.getType();
        
        if (!ConfigManager.getInstance().getConfig().getBoolean("enabled_features.new_items")) {
            return;
        }
        
        if (material == Material.BLAZE_ROD) {
            handleSelfRescuePlatform(player, item);
        }
        else if (material == Material.FEATHER) {
            handleParachute(player);
        }
        else if (material == Material.WHITE_WOOL) {
            handleMagicWool(player);
        }
        else if (material == Material.GUNPOWDER) {
            handleReturnScroll(player, item);
        }
        else if (material == Material.GLOWSTONE_DUST) {
            handleReturnScrollCancel(player);
        }
    }
    
    private void handleSelfRescuePlatform(Player player, ItemStack item) {
        long cooldown = ConfigManager.getInstance().getShopConfig().getInt("self_rescue_platform.cooldown") * 1000;
        if (selfRescueCooldowns.containsKey(player) && System.currentTimeMillis() - selfRescueCooldowns.get(player) < cooldown) {
            long remaining = (cooldown - (System.currentTimeMillis() - selfRescueCooldowns.get(player))) / 1000;
            String message = ConfigManager.getInstance().getShopConfig().getString("self_rescue_platform.cooldown_message");
            message = message.replace("%time%", String.valueOf(remaining));
            player.sendMessage(message);
            return;
        }
        
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }
        
        Location loc = player.getLocation();
        World world = player.getWorld();
        
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location platformLoc = new Location(world, loc.getX() + x, loc.getY() - 8, loc.getZ() + z);
                world.getBlockAt(platformLoc).setType(Material.SLIME_BLOCK);
            }
        }
        
        selfRescueCooldowns.put(player, System.currentTimeMillis());
        
        String message = ConfigManager.getInstance().getMessageConfig().getString("items.self_rescue_platform.used");
        player.sendMessage(message);
    }
    
    private void handleParachute(Player player) {
        if (!ConfigManager.getInstance().getShopConfig().getBoolean("parachute.enabled")) {
            return;
        }
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }
        
        final int flightHeight = ConfigManager.getInstance().getShopConfig().getInt("parachute.flight_height");
        final Location startLoc = player.getLocation();
        final double startY = startLoc.getY();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks >= 100) {
                    this.cancel();
                    return;
                }
                
                if (player.getLocation().getY() - startY >= flightHeight) {
                    this.cancel();
                    return;
                }
                
                player.setVelocity(new Vector(0, 1, 0));
                ticks++;
            }
        }.runTaskTimer(plugin, 0, 1);
        
        String message = ConfigManager.getInstance().getMessageConfig().getString("items.parachute.used");
        player.sendMessage(message);
    }
    
    private void handleMagicWool(Player player) {
        if (!ConfigManager.getInstance().getShopConfig().getBoolean("magic_wool.enabled")) {
            return;
        }
        
        int extendDistance = ConfigManager.getInstance().getShopConfig().getInt("magic_wool.extend_distance");
        
        Location loc = player.getLocation();
        World world = player.getWorld();
        Vector direction = loc.getDirection().normalize();
        
        for (int i = 1; i <= extendDistance; i++) {
            Location woolLoc = loc.clone().add(direction.clone().multiply(i));
            if (world.getBlockAt(woolLoc).getType() == Material.AIR) {
                world.getBlockAt(woolLoc).setType(Material.WHITE_WOOL);
            }
        }
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }
        
        String message = ConfigManager.getInstance().getMessageConfig().getString("items.magic_wool.used");
        player.sendMessage(message);
    }
    
    private void handleReturnScroll(Player player, ItemStack item) {
        if (!ConfigManager.getInstance().getShopConfig().getBoolean("return_scroll.enabled")) {
            return;
        }
        
        long cooldown = ConfigManager.getInstance().getShopConfig().getInt("return_scroll.cooldown") * 1000;
        if (returnScrollCooldowns.containsKey(player) && System.currentTimeMillis() - returnScrollCooldowns.get(player) < cooldown) {
            long remaining = (cooldown - (System.currentTimeMillis() - returnScrollCooldowns.get(player))) / 1000;
            String message = ConfigManager.getInstance().getShopConfig().getString("return_scroll.cooldown_message");
            message = message.replace("%time%", String.valueOf(remaining));
            player.sendMessage(message);
            return;
        }
        
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }
        
        ItemStack glowstone = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta meta = glowstone.getItemMeta();
        meta.setDisplayName(Utils.colorize("&e回城卷轴（取消）"));
        glowstone.setItemMeta(meta);
        player.getInventory().addItem(glowstone);
        
        isReturning.put(player, true);
        
        int countdown = ConfigManager.getInstance().getShopConfig().getInt("return_scroll.countdown");
        final int[] timeLeft = {countdown};
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isReturning.containsKey(player) || !isReturning.get(player)) {
                    this.cancel();
                    player.getInventory().remove(Material.GLOWSTONE_DUST);
                    return;
                }
                
                if (timeLeft[0] <= 0) {
                    teleportToSpawn(player);
                    this.cancel();
                    isReturning.remove(player);
                    player.getInventory().remove(Material.GLOWSTONE_DUST);
                    returnScrollCooldowns.put(player, System.currentTimeMillis());
                    return;
                }
                
                player.setExp((float) timeLeft[0] / countdown);
                timeLeft[0]--;
            }
        }.runTaskTimer(plugin, 0, 20);
        
        String message = ConfigManager.getInstance().getMessageConfig().getString("items.return_scroll.used");
        player.sendMessage(message);
    }
    
    private void handleReturnScrollCancel(Player player) {
        if (isReturning.containsKey(player) && isReturning.get(player)) {
            isReturning.put(player, false);
            player.sendMessage(ConfigManager.getInstance().getMessageConfig().getString("items.return_scroll.cancelled"));
        }
    }
    
    private void teleportToSpawn(Player player) {
        Location spawnLoc = BedWars1058Integration.getInstance().getTeamSpawn(player);
        player.teleport(spawnLoc);
        player.sendMessage(ConfigManager.getInstance().getMessageConfig().getString("items.return_scroll.success"));
    }
    
    public boolean isPlayerReturning(Player player) {
        return isReturning.containsKey(player) && isReturning.get(player);
    }
    
    public void cancelReturn(Player player) {
        if (isReturning.containsKey(player)) {
            isReturning.put(player, false);
        }
    }
}