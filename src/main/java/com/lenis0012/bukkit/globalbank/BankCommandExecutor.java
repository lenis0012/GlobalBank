package com.lenis0012.bukkit.globalbank;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lenny on 14-7-2014.
 */
public class BankCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        final Player player = (Player) sender;
        if(args.length == 0) {
            help(player);
        } else {
            switch(args[0].toLowerCase()) {
                case "create":
                    create(player, args);
                    break;
                case "delete":
                    delete(player);
                    break;
                case "face":
                    face(player);
                    break;
                case "save":
                    save(player);
                    break;
                case "info":
                    info(player);
                    break;
                case "help":
                    help(player);
                    break;
                default:
                    reply(player, "&cUnknown sub command, check /gb help.");
                    break;
            }
        }

        return true;
    }

    private void reply(Player player, String message, Object... args) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(message, args)));
    }

    private boolean check(Player player, String permission) {
        if(player.hasPermission(permission)) {
            return true;
        } else {
            reply(player, "&cYou don't have permission to execute this sub command.");
            return false;
        }
    }

    private boolean check(Player player, String[] args, int minArgs) {
        if(args.length >= minArgs + 1) {
            return true;
        } else {
            reply(player, "&cYou didn't specify enough arguments for this sub command.");
            return false;
        }
    }

    /**
     * Sub commands
     */

    public void create(Player player, String[] args) {
        if(check(player, "gb.create")) {
            if(check(player, args, 1)) {
                //TODO: Create banker.
                reply(player, "&aCreated banker with bank named '%s'!", args[1]);
            }
        }
    }

    public void delete(Player player) {
        if(check(player, "gb.delete")) {
            //TODO: Delete banker.
            reply(player, "&aRight click the banker you want to delete.");
        }
    }

    public void face(Player player) {
        if(check(player, "gb.face")) {
            //TODO: Face banker.
            reply(player, "&aRight click the banker that needs to look at you.");
        }
    }

    public void save(Player player) {
        if(check(player, "gb.save")) {
            //TODO: Save all bankers.
            reply(player, "&aAll bankers and banks have been saved.");
        }
    }

    public void info(Player player) {
        BankPlugin plugin = JavaPlugin.getPlugin(BankPlugin.class);
        reply(player, "&lAuthors: &a%s", plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""));
        reply(player, "&lVersion: &a%s", plugin.getDescription().getVersion());
    }

    public void help(Player player) {
        reply(player, "&6GlobalBank command help:");
        reply(player, "&a/gb create <name> &7- Create a banker");
        reply(player, "&a/gb delete &7- Delete a banker");
        reply(player, "&a/gb face &7- Make banker face at your location");
        reply(player, "&a/gb save &7- Force save all data");
        reply(player, "&a/gb info &7- Displays plugin version & authors");
    }
}
