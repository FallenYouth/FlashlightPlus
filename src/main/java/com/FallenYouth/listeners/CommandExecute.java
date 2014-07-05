package com.FallenYouth.listeners;

import com.FallenYouth.main.FlashlightPlus;
import com.FallenYouth.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Made by FallenYouth
 */

public class CommandExecute implements CommandExecutor {
    public FlashlightPlus plugin;

    public String prefix = ChatColor.RED + "[FlashlightPlus]";

    public CommandExecute(FlashlightPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("flashlight") && (player.hasPermission("flashlight.use"))) {
            player.sendMessage(ChatColor.GREEN + "=====================================");
            player.sendMessage(ChatColor.GOLD + "Usage: /flashlight on/off/spawn/reload");
            player.sendMessage(ChatColor.GREEN + "=====================================");
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on") && player.hasPermission("flashlight.use.on")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.FlashlightOnMsg")));
                player.playEffect(player.getLocation(), Effect.GHAST_SHOOT, 5);
            } else if (args[0].equalsIgnoreCase("off") && player.hasPermission("flashlight.use.off")) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.FlashlightOffMsg")));
                player.playEffect(player.getLocation(), Effect.EXTINGUISH, 5);
            } else if (args[0].equalsIgnoreCase("spawn") && player.hasPermission("flashlight.use.spawn")) {
                ItemStack torch = new ItemBuilder(Material.TORCH)
                        .withName(ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]" + ChatColor.RESET)
                        .withLore(ChatColor.GREEN + "Use the torch to light your way!")
                        .withAmount(1)
                        .toItemStack();
                player.getInventory().addItem(torch);
            } else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("flashlight.admin.reload")) {
                plugin.reloadConfig();
                sender.sendMessage(prefix + ChatColor.GREEN + "Configuration file reloaded.");
            } else if (args[0].equalsIgnoreCase("ctb")) {
                player.sendMessage(ChatColor.GOLD + "This plugin is using a ItemBuilder class by CraftThatBlock");
                player.sendMessage(ChatColor.GOLD + "~Craft~is~boss~");
            }
        }
        return true;
    }
}
