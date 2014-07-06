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

	@EventHandler
	public void onSignCreate(SignChangeEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPermission("flashlight.admin.sign")) {
			player.sendMessage(FlashlightPlus.getMessage("&6You don't have permission to make this sign."));
			event.setLine(0, "Error");
			return;
		}
		if (event.getLine(0).equals("[Flashlight]")) {
			player.sendMessage(FlashlightPlus.getMessage("&asign created!"));

			event.setLine(0, ChatColor.GREEN + "[Flashlight]");
			event.setLine(1, ChatColor.WHITE + "Click here");
			event.setLine(2, ChatColor.WHITE + "to use");
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				Player player = event.getPlayer();

				if (sign.getLine(0).equals(ChatColor.GREEN + "[Flashlight]")) {
					FlashlightPlus.togglePlayer(player);
				}
			}
		}
	}
}