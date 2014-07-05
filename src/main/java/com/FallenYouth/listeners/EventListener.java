package com.FallenYouth.listeners;

import com.FallenYouth.main.FlashlightPlus;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Made by FallenYouth
 */

@SuppressWarnings("deprecation")
public class EventListener implements Listener {
    private FlashlightPlus plugin;

    public EventListener(FlashlightPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = (Player) e.getPlayer();

        if (plugin.flashlighttoggle.contains(player.getName())) {
            plugin.flashlighttoggle.remove(player.getName());
            e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack torch = player.getItemInHand();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || (event.getAction() == Action.LEFT_CLICK_AIR))) {
            if (torch.getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]" + ChatColor.RESET)) {
                if (player.hasPermission("flashlight.torch.use")) {
                    if (!plugin.flashlighttoggle.contains(player.getName())) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.FlashlightOnMsg")));
                        plugin.flashlighttoggle.add(player.getName());
                        player.playEffect(player.getLocation(), Effect.GHAST_SHOOT, 5);
                    } else {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.FlashlightOffMsg")));
                        plugin.flashlighttoggle.remove(player.getName());
                        player.playEffect(player.getLocation(), Effect.EXTINGUISH, 5);
                    }
                }
            }
        }
    }
}