package com.fallenyouth.main;

import com.fallenyouth.listeners.CommandExecute;
import com.fallenyouth.listeners.EventListener;
import com.fallenyouth.listeners.SignListener;
import com.fallenyouth.utils.Metrics;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Made by FallenYouth
 * This plugin is using the ItemBuilder class made by CraftThatBlock.
 */
public class FlashlightPlus extends JavaPlugin {

	@Getter
	private static FlashlightPlus plugin;

	@Getter
	private static ArrayList<String> flashLightToggle = new ArrayList<String>();

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();

		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
		getCommand("flashlight").setExecutor(new CommandExecute(this));

		if (getConfig().getBoolean("Backend.Metrics", true)) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				getLogger().warning("FlashlightPlus's stats failed to be sent :(");
			}
		}
	}

	public static String getConfigMessage(String message){
		return getMessage(getPlugin().getConfig().getConfigurationSection("Messages").getString(message));
	}

	public static String getMessage(String message){
		return ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getConfigurationSection("Messages").getString("Prefix") + message);
	}

	public static void togglePlayer(Player player) {
		if (player.hasPermission("flashlight.torch.use")) {
			if (!getFlashLightToggle().contains(player.getName())) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("Messages.FlashlightOnMsg")));
				getFlashLightToggle().add(player.getName());
				player.playEffect(player.getLocation(), Effect.GHAST_SHOOT, 5);
			} else {
				player.removePotionEffect(PotionEffectType.NIGHT_VISION);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("Messages.FlashlightOffMsg")));
				getFlashLightToggle().remove(player.getName());
				player.playEffect(player.getLocation(), Effect.EXTINGUISH, 5);
			}
		} else {
			player.sendMessage(ChatColor.RED + "You do not have permission to toggle your flashlight.");
		}
	}
}

