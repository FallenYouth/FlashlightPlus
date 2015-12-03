package net.mattslab.FlashlightPlus.api;

import lombok.Getter;
import net.mattslab.FlashlightPlus.FlashlightPlus;
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

    public static void loadConfig() {
        int version = 2;

        File file = new File(getInstance().getDataFolder() + File.separator + "config.yml");

        if (!file.exists()) {
            getInstance().saveDefaultConfig();
        } else {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (config.contains("Version") || config.getInt("Version", version) == version) {
                file.delete();
                File tempfile = new File(getInstance().getDataFolder() + File.separator + "oldconfig.yml");
                try {
                    config.save(tempfile);
                } catch (IOException e) {
                    e.printStackTrace();
                    getInstance().getLogger().warning("FlashlightPlus has encountered a problem, report the issue @");
                    getInstance().getLogger().warning("https://github.com/MattsLab/FlashlightPlus/issues/new");
                }
                updateConfig();
                getInstance().getLogger().info("Configuration File updated!");
            }
        }
    }

    private static void updateConfig() {
        File tempfile = new File(getInstance().getDataFolder() + File.separator + "oldconfig.yml");

        FileConfiguration oldC = YamlConfiguration.loadConfiguration(tempfile);
        getInstance().saveDefaultConfig();
        getInstance().getConfig().set("Messages.Prefix", oldC.getString("Messages.Prefix"));
        getInstance().getConfig().set("Messages.FlashlightOnMsg", oldC.getString("Messages.FlashlightOnMsg"));
        getInstance().getConfig().set("Messages.FlashlightOffMsg", oldC.getString("Messages.FlashlightOffMsg"));
        getInstance().getConfig().set("Messages.NoPermMsg", oldC.getStringList("Messages.NoPermMsg"));
        getInstance().getConfig().set("Messages.CooldownMsg", oldC.getString("Messages.CooldownMsg"));
        getInstance().getConfig().set("Sign.Line1", oldC.getString("Sign.Line1"));
        getInstance().getConfig().set("Sign.Line2", oldC.getString("Sign.Line2"));
        getInstance().getConfig().set("Sign.Line3", oldC.getString("Sign.Line3"));
        getInstance().getConfig().set("Sign.Line4", oldC.getString("Sign.Line4"));
        getInstance().getConfig().set("Backend.Cooldown", oldC.getInt("Backend.Cooldown"));
        getInstance().getConfig().set("Backend.Metrics", oldC.getBoolean("Backend.Metrics"));
        getInstance().saveConfig();
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


}
