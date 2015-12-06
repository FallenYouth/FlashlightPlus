package net.mattslab.FlashlightPlus.listeners;

import net.mattslab.FlashlightPlus.FlashlightPlus;
import net.mattslab.FlashlightPlus.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.mattslab.FlashlightPlus.api.API.*;

/**
 * Made by Matt
 */

public class Commander implements CommandExecutor {

    FlashlightPlus plugin = getInstance();

    public Commander() {

        plugin.getCommand("fl").setExecutor(this);
        plugin.getCommand("flashlight").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            say(sender, "You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("flashlight") || cmd.getName().equalsIgnoreCase("fl") && (player.hasPermission("flashlight.toggle"))) {
            if (args.length == 0) {
                togglePlayer(player);
            }
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("help") && player.hasPermission("flashlight.help")) {
                    player.sendMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-------------" + ChatColor.GREEN + "o0o" + ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-------------");
                    player.sendMessage(ChatColor.DARK_RED + "Usage:");
                    player.sendMessage(ChatColor.GREEN + "/flashlight - Toggles flashlight on/off");
                    player.sendMessage(ChatColor.DARK_RED + "Sub-Commands:");
                    player.sendMessage(ChatColor.DARK_PURPLE + "/flashlight <sub-command>");
                    player.sendMessage(ChatColor.GREEN + "help - Displays the flashlight menu");
                    player.sendMessage(ChatColor.GREEN + "spawn <player> - Spawns Flashlight Torch");
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
                        } else if (player.getInventory().containsAtLeast(flashlight, 1)) {
                            say(player, ChatColor.RED + "You already have a flashlight in your inventory!");
                        }
                    } else if (args.length == 2 && player.hasPermission("flashlight.spawn.others")) {
                        Player target = player.getServer().getPlayer(args[1]);
                        if (player.getServer().getPlayer(args[1]) != null) {
                            if (!target.getInventory().containsAtLeast(flashlight, 1)) {
                                target.getInventory().addItem(flashlight);
                                say(player, ChatColor.GREEN + "Gave Flashlight to " + ChatColor.AQUA + target.getName());
                                say(target, ChatColor.GREEN + "You just received a flashlight!");
                            } else if (player.getInventory().containsAtLeast(flashlight, 1)) {
                                say(player, ChatColor.RED + "Target already has a flashlight in their inventory!");
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    getInstance().reloadConfig();
                    say(sender, "&aConfiguration file reloaded.");
                }
            }
        } else {
            say(player, ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("Messages.NoPermMsg")));
        }
        return true;
    }
}