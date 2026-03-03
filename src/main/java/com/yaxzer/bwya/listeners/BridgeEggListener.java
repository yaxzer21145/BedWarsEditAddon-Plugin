package com.yaxzer.bwya.listeners;


import com.yaxzer.bwya.managers.ConfigManager;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class BridgeEggListener implements Listener {
    
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.EGG) {
            return;
        }
        
        Egg egg = (Egg) event.getEntity();
        Vector direction = egg.getVelocity().normalize();
        
        if (!ConfigManager.getInstance().getConfig().getBoolean("enabled_features.bridge_egg_modify")) {
            return;
        }
        
        if (!(egg.getShooter() instanceof Player)) {
            return;
        }
        
        Player player = (Player) egg.getShooter();
        World world = player.getWorld();
        
        Location startLocation;
        boolean isVertical = Math.abs(direction.getY()) > 0.7;
        
        if (isVertical) {
            startLocation = player.getLocation().clone().add(0, -0.5, 0);
            generateVerticalBridge(world, startLocation, direction);
        } else {
            startLocation = player.getLocation().clone().add(direction.clone().multiply(0.25));
            generateHorizontalBridge(world, startLocation, direction);
        }
    }
    
    private void generateHorizontalBridge(World world, Location startLocation, Vector direction) {
        Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).normalize();
        
        for (int i = 0; i < 20; i++) {
            Location loc1 = startLocation.clone().add(direction.clone().multiply(i)).add(perpendicular.clone().multiply(0.5));
            Location loc2 = startLocation.clone().add(direction.clone().multiply(i)).subtract(perpendicular.clone().multiply(0.5));
            
            if (world.getBlockAt(loc1).getType() == Material.AIR) {
                world.getBlockAt(loc1).setType(Material.SANDSTONE);
            } else {
                break;
            }
            
            if (world.getBlockAt(loc2).getType() == Material.AIR) {
                world.getBlockAt(loc2).setType(Material.SANDSTONE);
            } else {
                break;
            }
        }
    }
    
    private void generateVerticalBridge(World world, Location startLocation, Vector direction) {
        Vector perpendicular1 = new Vector(1, 0, 0);
        Vector perpendicular2 = new Vector(0, 0, 1);
        
        for (int i = 0; i < 20; i++) {
            Location loc1 = startLocation.clone().add(direction.clone().multiply(i)).add(perpendicular1.clone().multiply(0.5)).add(perpendicular2.clone().multiply(0.5));
            Location loc2 = startLocation.clone().add(direction.clone().multiply(i)).add(perpendicular1.clone().multiply(0.5)).subtract(perpendicular2.clone().multiply(0.5));
            Location loc3 = startLocation.clone().add(direction.clone().multiply(i)).subtract(perpendicular1.clone().multiply(0.5)).add(perpendicular2.clone().multiply(0.5));
            Location loc4 = startLocation.clone().add(direction.clone().multiply(i)).subtract(perpendicular1.clone().multiply(0.5)).subtract(perpendicular2.clone().multiply(0.5));
            
            boolean canContinue = true;
            
            if (world.getBlockAt(loc1).getType() == Material.AIR) {
                world.getBlockAt(loc1).setType(Material.SANDSTONE);
            } else {
                canContinue = false;
            }
            
            if (world.getBlockAt(loc2).getType() == Material.AIR) {
                world.getBlockAt(loc2).setType(Material.SANDSTONE);
            } else {
                canContinue = false;
            }
            
            if (world.getBlockAt(loc3).getType() == Material.AIR) {
                world.getBlockAt(loc3).setType(Material.SANDSTONE);
            } else {
                canContinue = false;
            }
            
            if (world.getBlockAt(loc4).getType() == Material.AIR) {
                world.getBlockAt(loc4).setType(Material.SANDSTONE);
            } else {
                canContinue = false;
            }
            
            if (!canContinue) {
                break;
            }
        }
    }
}