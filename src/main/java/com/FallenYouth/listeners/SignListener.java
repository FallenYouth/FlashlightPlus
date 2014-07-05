package com.FallenYouth.listeners;

import com.FallenYouth.main.FlashlightPlus;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Made by FallenYouth
 */

@SuppressWarnings("deprecation")
public class SignListener implements Listener {
    private FlashlightPlus plugin;

    public SignListener(FlashlightPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent sign) {
        Player player = sign.getPlayer();

        if (!player.hasPermission("flashlight.sign")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to make this sign!");
            sign.setLine(0, "Error");
            return;
        }
        if (sign.getLine(0).equals("[Flashlight]")) {
            player.sendMessage(ChatColor.GREEN + "FlashlightPlus sign created!");
            sign.setLine(0, ChatColor.GREEN + "[Flashlight]");
            sign.setLine(1, ChatColor.WHITE + "Click here");
            sign.setLine(2, ChatColor.WHITE + "to use");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                Player player = e.getPlayer();

                if (!player.hasPermission("flashlight.sign.use")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this sign!");
                    return;
                }
                if (sign.getLine(0).equals(ChatColor.GREEN + "[Flashlight]")) {

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