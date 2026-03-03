package com.yaxzer.bwya.integrations;

import org.bukkit.entity.Player;
import org.bukkit.Location;

public class BedWars1058Integration {
    private static BedWars1058Integration instance;
    private Object bedWars1058;
    
    public static BedWars1058Integration getInstance() {
        if (instance == null) {
            instance = new BedWars1058Integration();
        }
        return instance;
    }
    
    public void initialize() {
        try {
            Class<?> mainClass = Class.forName("com.andrei1058.bedwars.BedWars");
            bedWars1058 = mainClass.getMethod("getInstance").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean isBedWars1058Available() {
        return bedWars1058 != null;
    }
    
    public Location getTeamSpawn(Player player) {
        if (!isBedWars1058Available()) {
            return player.getWorld().getSpawnLocation();
        }
        
        try {
            Object arena = getArena(player);
            if (arena != null) {
                Object team = getTeam(player, arena);
                if (team != null) {
                    return (Location) team.getClass().getMethod("getSpawn").invoke(team);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return player.getWorld().getSpawnLocation();
    }
    
    public int getTeamSize(Player player) {
        if (!isBedWars1058Available()) {
            return 4;
        }
        
        try {
            Object arena = getArena(player);
            if (arena != null) {
                Object team = getTeam(player, arena);
                if (team != null) {
                    Object players = team.getClass().getMethod("getMembers").invoke(team);
                    return ((java.util.Collection<?>) players).size();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 4;
    }
    
    public boolean isPlayerInGame(Player player) {
        if (!isBedWars1058Available()) {
            return false;
        }
        
        try {
            Object arena = getArena(player);
            return arena != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Object getArena(Player player) throws Exception {
        Object api = bedWars1058.getClass().getMethod("getAPI").invoke(bedWars1058);
        return api.getClass().getMethod("getArenaByPlayer", Player.class).invoke(api, player);
    }
    
    private Object getTeam(Player player, Object arena) throws Exception {
        return arena.getClass().getMethod("getTeam", Player.class).invoke(arena, player);
    }
    
    public void onPlayerKill(Player killer, Player victim) {
        if (isPlayerInGame(killer)) {
            com.yaxzer.bwya.managers.CombatManager.getInstance().addKillCombatPower(killer);
        }
    }
    
    public void onBedDestroy(Player player) {
        if (isPlayerInGame(player)) {
            com.yaxzer.bwya.managers.CombatManager.getInstance().addBedDestroyCombatPower(player);
        }
    }
    
    public void onFinalKill(Player killer, Player victim) {
        if (isPlayerInGame(killer)) {
            com.yaxzer.bwya.managers.CombatManager.getInstance().addFinalKillCombatPower(killer);
        }
    }
}