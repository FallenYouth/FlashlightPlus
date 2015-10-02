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

/**
 * Made by FallenYouth
 */

public class CommandExecute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(FlashlightPlus.getMessage("You must be a player to use this command."));
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("flashlight") || cmd.getName().equals("fl") && (player.hasPermission("flashlight.use"))) {
            if (args.length != 1) {
                player.sendMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-------------" + ChatColor.GREEN + "o0o" + ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-------------");
                player.sendMessage(ChatColor.DARK_RED + "Usage:");
                player.sendMessage(ChatColor.GREEN + "/flashlight <sub-command>");
                player.sendMessage(ChatColor.DARK_RED + "Sub-Commands:");
                player.sendMessage(ChatColor.GREEN + "on - Turns Flashlight on");
                player.sendMessage(ChatColor.GREEN + "off - Turns Flashlight off");
                player.sendMessage(ChatColor.GREEN + "spawn - Spawns Flashlight Torch");
                player.sendMessage(ChatColor.DARK_RED + "Admin Commands:");
                player.sendMessage(ChatColor.GREEN + "reload - Reloads the config");
                player.sendMessage(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "-----------------------------");
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("on") && player.hasPermission("flashlight.use.on")) {
                    FlashlightPlus.togglePlayerOn(player);
                } else if (args[0].equalsIgnoreCase("off") && player.hasPermission("flashlight.use.off")) {
                    FlashlightPlus.togglePlayerOff(player);
                } else if (args[0].equalsIgnoreCase("spawn") && player.hasPermission("flashlight.use.spawn")) {
                    ItemStack flashlight = new ItemBuilder(Material.TORCH)
                            .withName(ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]")
                            .withLores(ChatColor.RED + "Usage:", ChatColor.GREEN + "Right click for a night vision effect")
                            .withAmount(1)
                            .toItemStack();
                    player.getInventory().addItem(flashlight);
                } else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("flashlight.admin.reload")) {
                    FlashlightPlus.getPlugin().reloadConfig();
                    sender.sendMessage(FlashlightPlus.getMessage("&aConfiguration file reloaded."));
                } else {
                    player.sendMessage(FlashlightPlus.getMessage(ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Messages.NoPermMsg"))));
                }
            }
        }
        return true;
    }
}