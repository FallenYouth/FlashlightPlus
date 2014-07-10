package com.fallenyouth;

import com.fallenyouth.listeners.CommandExecute;
import com.fallenyouth.listeners.EventListener;
import com.fallenyouth.listeners.SignListener;
import com.fallenyouth.utils.Metrics;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
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

    int version = 1;

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
    public void loadConfig() {
        File file = new File(this.getDataFolder() + File.separator + "config.yml");

        if (!file.exists()) {
            this.saveDefaultConfig();
        } else {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (config.contains("Backend.Version") || config.getInt("Backend.Version", version) == version) {
                file.delete();
                File tempfile = new File(this.getDataFolder() + File.separator + "oldconfig.yml");
                try {
                    config.save(tempfile);
                } catch (IOException e) {
                    getLogger().warning("FlashlightPlus has an Error, please report to FallenYouth");
                }
                updateConfig();
            }
        }
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
        }
    }

    public static void togglePlayerOn(Player player) {
        if (addToCooldown(player)) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true));
        player.sendMessage(getMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("Messages.FlashlightOnMsg"))));
        getFlashLightToggle().add(player.getName());
        player.playEffect(player.getLocation(), Effect.GHAST_SHOOT, 5);
    }

    public static void togglePlayerOff(Player player) {
        if (addToCooldown(player)) return;
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.sendMessage(getMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("Messages.FlashlightOffMsg"))));
        getFlashLightToggle().remove(player.getName());
        player.playEffect(player.getLocation(), Effect.EXTINGUISH, 5);
    }

    public static boolean addToCooldown(Player player) {
        if (isInCooldown(player)) {
            player.sendMessage(getMessage(ChatColor.translateAlternateColorCodes('&', FlashlightPlus.getPlugin().getConfig().getString("Messages.CooldownMsg"))));
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

    public static boolean invCheck(Player player, String name) {
        for (ItemStack item : player.getInventory().getContents())
            if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(name)) return true;
        return false;
    }

    private void updateConfig() {
        File tempfile = new File(this.getDataFolder() + File.separator + "oldconfig.yml");

        FileConfiguration oldC = YamlConfiguration.loadConfiguration(tempfile);
        this.saveDefaultConfig();
        this.getConfig().set("Messages.Prefix", oldC.getStringList("Messages.Prefix"));
        this.getConfig().set("Messages.FlashlightOnMsg", oldC.getStringList("Messages.FlashlightOnMsg"));
        this.getConfig().set("Messages.FlashlightOffMsg", oldC.getStringList("Messages.FlashlightOffMsg"));
        this.getConfig().set("Messages.NoPermMsg", oldC.getStringList("Messages.NoPermMsg"));
        this.getConfig().set("Messages.CooldownMsg", oldC.getStringList("Messages.CooldownMsg"));
        this.getConfig().set("Sign.Line1", oldC.getStringList("Sign.Line1"));
        this.getConfig().set("Sign.Line2", oldC.getStringList("Sign.Line2"));
        this.getConfig().set("Sign.Line3", oldC.getStringList("Sign.Line3"));
        this.getConfig().set("Sign.Line4", oldC.getStringList("Sign.Line4"));
        this.getConfig().set("Backend.Metrics", oldC.getBoolean("Backend.Metrics"));
        this.getConfig().set("Backend.Cooldown", oldC.getInt("Backend.Cooldown"));
        this.saveConfig();
    }
}