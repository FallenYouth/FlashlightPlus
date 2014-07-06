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
            sender.sendMessage(FlashlightPlus.getMessage("You must be a player to use this command."));
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
                String torch = ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]" + ChatColor.RESET;
                ItemStack flashlight = new ItemBuilder(Material.TORCH)
                        .withName(ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Messages.Torch.Name")))
                        .withLore(ChatColor.RED + "Usage:").withLores(new String[]{ChatColor.GREEN + "Right/Left click in air"})
                        .withAmount(1)
                        .toItemStack();
                if (FlashlightPlus.invCheck(player, torch)) {
                    player.sendMessage(FlashlightPlus.getMessage(ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Messages.InvCheckMsg"))));
                } else {
                    player.getInventory().addItem(flashlight);
                }
            } else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("flashlight.admin.reload")) {
                FlashlightPlus.getPlugin().reloadConfig();
                sender.sendMessage(FlashlightPlus.getMessage("&aConfiguration file reloaded."));
            } else if (args[0].equalsIgnoreCase("ctb")) {
                player.sendMessage(ChatColor.GOLD + "This plugin is using a ItemBuilder class by CraftThatBlock");
                player.sendMessage(ChatColor.GOLD + "~Craft~is~boss~ < vouch");
            } else {
                player.sendMessage(FlashlightPlus.getMessage(ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Messages.NoPermMsg"))));
            }
        }
        return false;
    }
}
