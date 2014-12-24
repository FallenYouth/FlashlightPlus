package com.fallenyouth.FlashlightPlus.listeners;

import com.fallenyouth.FlashlightPlus.FlashlightPlus;
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

public class SignListener implements Listener {

	@EventHandler
	public void onSignCreate(SignChangeEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPermission("flashlight.admin.sign")) {
			player.sendMessage(FlashlightPlus.getMessage(ChatColor.RED + "You don't have permission to make this sign."));
			event.setLine(0, ChatColor.RED + "Error");
			return;
		}
		if (event.getLine(0).equals("[Flashlight]")) {
			player.sendMessage(FlashlightPlus.getMessage(ChatColor.GREEN + "sign created!"));

			event.setLine(0, ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Sign.Line1")));
			event.setLine(1, ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Sign.Line2")));
			event.setLine(2, ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Sign.Line3")));
            event.setLine(3, ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Sign.Line4")));
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				Player player = event.getPlayer();

				if (sign.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Sign.Line1")))) {
					FlashlightPlus.togglePlayer(player);
				} else {
                    player.sendMessage(FlashlightPlus.getMessage(ChatColor.RED + "You do not have permission to use this sign"));
                }
			}
		}
	}
}