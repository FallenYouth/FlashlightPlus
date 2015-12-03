package net.mattslab.FlashlightPlus.listeners;

import net.mattslab.FlashlightPlus.FlashlightPlus;
import net.mattslab.FlashlightPlus.api.API;
import net.mattslab.FlashlightPlus.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Made by Matt
 */

public class Commander implements CommandExecutor {

    FlashlightPlus plugin = API.getInstance();

    public Commander() {

        plugin.getCommand("fl").setExecutor(this);
        plugin.getCommand("flashlight").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            API.say(sender, "You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("flashlight") || cmd.getName().equalsIgnoreCase("fl") && (player.hasPermission("flashlight.toggle"))) {
            if (args[0].equals("")) {
                API.togglePlayer(player);
            } else {
                API.say(player, ChatColor.RED + "You do not have permission to use this command!");
            }
        }
        if (args[0].equalsIgnoreCase("help") && player.hasPermission("flashlight.help")) {
                player.sendMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-------------" + ChatColor.GREEN + "o0o" + ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-------------");
                player.sendMessage(ChatColor.DARK_RED + "Usage:");
                player.sendMessage(ChatColor.GREEN + "/flashlight - Toggles flashlight on/off");
                player.sendMessage(ChatColor.DARK_RED + "Sub-Commands:");
                player.sendMessage(ChatColor.DARK_PURPLE + "/flashlight <sub-command>");
                player.sendMessage(ChatColor.GREEN + "help - Displays the flashlight menu");
                player.sendMessage(ChatColor.GREEN + "spawn - Spawns Flashlight Torch");
                player.sendMessage(ChatColor.GREEN + "spawn <player> - spawns a flashlight to another player");
                player.sendMessage(ChatColor.DARK_RED + "Admin Commands:");
                player.sendMessage(ChatColor.GREEN + "reload - Reloads the config");
                player.sendMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-----------------------------");
        } else if (args[0].equalsIgnoreCase("spawn") && player.hasPermission("flashlight.spawn")) {
            ItemStack flashlight = new ItemBuilder(Material.TORCH)
                    .withName(ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]")
                    .withLores(ChatColor.RED + "Usage:", ChatColor.GREEN + "Right click for a night vision effect")
                    .withAmount(1)
                    .toItemStack();

            if (args.length == 1) {
                if (!player.getInventory().containsAtLeast(flashlight, 1)) {
                    player.getInventory().addItem(flashlight);
                } else {
                    API.say(player, ChatColor.RED + "You already have a flashlight in your inventory!");
                }
            } else if (args.length == 2 && sender.hasPermission("flashlight.spawn.others")) {
                Player target = sender.getServer().getPlayer(args[1]);
                if (sender.getServer().getPlayer(args[1]) != null) {
                    if (!target.getInventory().containsAtLeast(flashlight, 1)) {
                        target.getInventory().addItem(flashlight);
                        API.say(target, ChatColor.GREEN + "You have relieved a flashlight!");
                    } else {
                        API.say(player, ChatColor.RED + "Specified player already has a flashlight in their inventory!");
                    }
                }
            }
            /** end of questioned code **/
        } else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("flashlight.admin.reload")) {
            API.getInstance().reloadConfig();
            API.say(sender, "&aConfiguration file reloaded.");
        } else {
            API.say(player, ChatColor.translateAlternateColorCodes('&', API.getInstance().getConfig().getString("Messages.NoPermMsg")));
        }
        return true;
    }
}