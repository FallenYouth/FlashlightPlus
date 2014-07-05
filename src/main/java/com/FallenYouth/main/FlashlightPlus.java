package com.FallenYouth.main;

import com.FallenYouth.listeners.CommandExecute;
import com.FallenYouth.listeners.EventListener;
import com.FallenYouth.listeners.SignListener;
import com.FallenYouth.utils.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Made by FallenYouth
 * This plugin is using the ItemBuilder class made by CraftThatBlock.
 */
public class FlashlightPlus extends JavaPlugin{
    public ArrayList<String> flashlighttoggle = new ArrayList<String>();

    FileConfiguration config;
    File cfile;

    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults(true);
        cfile = new File(getDataFolder(), "config.yml");
        saveConfig();

        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getCommand("Flashlight").setExecutor(new CommandExecute(this));
        getCommand("FlashlightReload").setExecutor(new CommandExecute(this));
        getCommand("FlashlightSpawn").setExecutor(new CommandExecute(this));

        if (getConfig().getBoolean("Backend.Metrics", true)) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                System.out.println(ChatColor.RED + "[FlashlightPlus] Stats where not sent :(");
            }
        }
    }
}

