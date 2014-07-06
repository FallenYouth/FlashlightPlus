package com.fallenyouth.listeners;

import com.fallenyouth.main.FlashlightPlus;
import com.fallenyouth.utils.ItemBuilder;
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
			sender.sendMessage("You must be a player to use this command.");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("flashlight") && (player.hasPermission("flashlight.use"))) {
            if (args.length != 1) {
                player.sendMessage(ChatColor.GREEN + "=====================================");
                player.sendMessage(ChatColor.GOLD + "Usage: /flashlight on/off/spawn/reload" + ChatColor.BLACK + "/ctb");
                player.sendMessage(ChatColor.GREEN + "=====================================");
            }
        }
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("on") && player.hasPermission("flashlight.use.on")) {
				FlashlightPlus.togglePlayerOn(player);
			} else if (args[0].equalsIgnoreCase("off") && player.hasPermission("flashlight.use.off")) {
				FlashlightPlus.togglePlayerOff(player);
			} else if (args[0].equalsIgnoreCase("spawn") && player.hasPermission("flashlight.use.spawn")) {

				ItemStack torch = new ItemBuilder(Material.TORCH)
						.withName(ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]" + ChatColor.RESET)
						.withLore(ChatColor.GREEN + "Use the torch to light your way!")
						.withAmount(1)
						.toItemStack();
                if (player.getInventory().contains(torch)) {
                    player.sendMessage(FlashlightPlus.getMessage(""));
                }
				player.getInventory().addItem(torch);
			} else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("flashlight.admin.reload")) {
				FlashlightPlus.getPlugin().reloadConfig();
				sender.sendMessage(FlashlightPlus.getMessage("&aConfiguration file reloaded."));
			} else if (args[0].equalsIgnoreCase("ctb")) {
				player.sendMessage(ChatColor.GOLD + "This plugin is using a ItemBuilder class by CraftThatBlock");
				player.sendMessage(ChatColor.GOLD + "~Craft~is~boss~ < vouch");
			}
		}
		return true;
	}
}
