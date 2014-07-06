package com.fallenyouth.main;

import com.fallenyouth.listeners.CommandExecute;
import com.fallenyouth.listeners.EventListener;
import com.fallenyouth.listeners.SignListener;
import com.fallenyouth.utils.Metrics;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Made by FallenYouth
 * This plugin is using the ItemBuilder class made by CraftThatBlock.
 */
public class FlashlightPlus extends JavaPlugin {

	@Getter
    public static FlashlightPlus plugin;

	@Getter
	private static ArrayList<String> flashLightToggle = new ArrayList<String>();

	@Getter
	private static HashMap<UUID, Integer> cooldown = new HashMap<UUID, Integer>();

	public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getCommand("flashlight").setExecutor(new CommandExecute());

        if (getConfig().getBoolean("Backend.Metrics", true)) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                getLogger().warning("FlashlightPlus's stats failed to be sent :(");
            }
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                for (Object o : ((HashMap) cooldown.clone()).entrySet()) {
                    Map.Entry pairs = (Map.Entry) o;
                    cooldown.remove(pairs.getKey());
                    if (((Integer) pairs.getValue()) > 0) {
                        cooldown.put((UUID) pairs.getKey(), ((Integer) pairs.getValue()) - 1);
                    }
                }
            }
        }, 20, 20);
    }

	public static String getConfigMessage(String message) {
		return getMessage(getPlugin().getConfig().getConfigurationSection("Messages").getString(message));
	}

	public static String getMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getConfigurationSection("Messages").getString("Prefix") + message);
	}

	public static void togglePlayer(Player player) {
		if (player.hasPermission("flashlight.use.torch")) {
			if (!getFlashLightToggle().contains(player.getName())) {
				togglePlayerOn(player);
			} else {
				togglePlayerOff(player);
			}
		} else {
			player.sendMessage(ChatColor.RED + "You do not have permission to toggle your flashlight.");
		}
	}

	public static void togglePlayerOn(Player player) {
        if (player.hasPermission("flashlight.use.on")) {
            if (addToCooldown(player)) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true));
            player.sendMessage(FlashlightPlus.getMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("Messages.FlashlightOnMsg"))));
            getFlashLightToggle().add(player.getName());
            player.playEffect(player.getLocation(), Effect.GHAST_SHOOT, 5);
        } else {
            player.sendMessage(FlashlightPlus.getMessage("You do not have permission to use this command"));
        }
    }

	public static void togglePlayerOff(Player player) {
        if (player.hasPermission("flashlight.use.off")) {
            if (addToCooldown(player)) return;
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(FlashlightPlus.getMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("Messages.FlashlightOffMsg"))));
            getFlashLightToggle().remove(player.getName());
            player.playEffect(player.getLocation(), Effect.EXTINGUISH, 5);
        } else {
            player.sendMessage(FlashlightPlus.getMessage("You do not have permission to use this command"));
        }
    }

	public static boolean addToCooldown(Player player) {
		if (isInCooldown(player)) {
			player.sendMessage(getMessage(ChatColor.RED + "Please wait for the cooldown to expire!"));
			return true;
		} else {
			if (!player.hasPermission("flashlight.bypasscooldown"))
				cooldown.put(player.getUniqueId(), getPlugin().getConfig().getInt("Backend.Cooldown", 30));
			return false;
		}
	}

	public static boolean isInCooldown(Player player) {
		return cooldown.containsKey(player.getUniqueId());

	}
}

