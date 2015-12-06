package net.mattslab.FlashlightPlus.api;

import lombok.Getter;
import net.mattslab.FlashlightPlus.FlashlightPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Matt
 */
public class API {

    @Getter
    public static ArrayList<String> flashLightToggle = new ArrayList<String>();

    @Getter
    public static final HashMap<UUID, Integer> cooldown = new HashMap<UUID, Integer>();

    private  static FlashlightPlus flashlightPlus;

    public API(FlashlightPlus plugin) {
        flashlightPlus = plugin;
    }

    public static FlashlightPlus getInstance() {
        return flashlightPlus;
    }

    public static void say(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getPrefix() + msg));
    }

    public static void say(CommandSender player, String msg) {
        say((Player) player, msg);
    }

    private static String getPrefix() {
        return getInstance().getConfig().getString("Messages.Prefix");
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

    private static void togglePlayerOn(Player player) {
        if (addToCooldown(player)) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        say(player, ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("Messages.FlashlightOnMsg")));
        getFlashLightToggle().add(player.getName());
        player.getWorld().playEffect(player.getLocation(), Effect.CLICK1, 5);
    }

    private static void togglePlayerOff(Player player) {
        if (addToCooldown(player)) return;
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        say(player, ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("Messages.FlashlightOffMsg")));
        getFlashLightToggle().remove(player.getName());
        player.getWorld().playEffect(player.getLocation(), Effect.CLICK2, 5);
    }

    private static boolean addToCooldown(Player player) {
        if (isInCooldown(player)) {
            say(player, ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("Messages.CooldownMsg")));
            return true;
        } else {
            if (!player.hasPermission("flashlight.bypasscooldown"))
                cooldown.put(player.getUniqueId(), getInstance().getConfig().getInt("Backend.Cooldown", 30));
            return false;
        }
    }

    private static boolean isInCooldown(Player player) {
        return cooldown.containsKey(player.getUniqueId());
    }

    public static void checkConfig() {
        if (getInstance().getConfig().getInt("Version") != 2) {
            getInstance().getLogger().warning("Outdated Config");
            getInstance().getLogger().warning("Please reset your config!");
            Bukkit.getPluginManager().disablePlugin(getInstance());
        }
    }


}
