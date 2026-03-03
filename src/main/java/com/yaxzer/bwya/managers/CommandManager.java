package com.yaxzer.bwya.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.yaxzer.bwya.utils.Utils;

public class CommandManager implements CommandExecutor {
    private static CommandManager instance;
    
    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }
    
    public void initialize(JavaPlugin plugin) {
        plugin.getCommand("bedwarseditaddon").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("lang")) {
            handleLanguageCommand(player, args);
            return true;
        }
        
        return false;
    }
    
    private void sendHelpMessage(Player player) {
        String lang = LanguageManager.getInstance().getPlayerLanguage(player);
        if (lang.equals("zh_CN")) {
            player.sendMessage(Utils.colorize("&e[BedWarsEditAddon] &a命令帮助："));
            player.sendMessage(Utils.colorize("&e/bedwarseditaddon lang <语言> &7- 切换语言 (zh_CN 或 en_US)"));
            player.sendMessage(Utils.colorize("&e/bedwarseditaddon lang &7- 查看当前语言"));
        } else {
            player.sendMessage(Utils.colorize("&e[BedWarsEditAddon] &aCommand Help："));
            player.sendMessage(Utils.colorize("&e/bedwarseditaddon lang <language> &7- Switch language (zh_CN or en_US)"));
            player.sendMessage(Utils.colorize("&e/bedwarseditaddon lang &7- Check current language"));
        }
    }
    
    private void handleLanguageCommand(Player player, String[] args) {
        if (args.length == 1) {
            String currentLang = LanguageManager.getInstance().getPlayerLanguage(player);
            String langName = currentLang.equals("zh_CN") ? "中文" : "English";
            player.sendMessage(LanguageManager.getInstance().getMessage(player, "language.current", "%lang%", langName));
        } else if (args.length == 2) {
            String language = args[1];
            if (LanguageManager.getInstance().isLanguageSupported(language)) {
                LanguageManager.getInstance().setPlayerLanguage(player, language);
                String langName = language.equals("zh_CN") ? "中文" : "English";
                player.sendMessage(LanguageManager.getInstance().getMessage(player, "language.changed", "%lang%", langName));
            } else {
                player.sendMessage(LanguageManager.getInstance().getMessage(player, "language.not_supported", "%lang%", language));
            }
        }
    }
}