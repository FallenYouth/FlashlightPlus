package com.fallenyouth.listeners;

import com.fallenyouth.main.FlashlightPlus;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
            player.sendMessage(FlashlightPlus.getMessage("&6You don't have permission to make this sign."));
            sign.setLine(0, "Error");
            return;
        }
        if (sign.getLine(0).equals("[Flashlight]")) {
	        player.sendMessage(FlashlightPlus.getMessage("&aFlashlightPlus sign created!"));

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

                if (sign.getLine(0).equals(ChatColor.GREEN + "[Flashlight]")) {
                   FlashlightPlus.togglePlayer(player);
                }
            }
        }
    }
}