package net.mattslab.FlashlightPlus.listeners;

import net.mattslab.FlashlightPlus.api.API;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.mattslab.FlashlightPlus.api.API.togglePlayer;

/**
 * Made by Matt
 */

public class EventListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (API.getFlashLightToggle().contains(player.getName())) {
            API.getFlashLightToggle().remove(player.getName());
            e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInventory playerInv = event.getPlayer().getInventory();
        ItemStack torchMain = playerInv.getItemInMainHand();
        if ((event.getAction() == Action.LEFT_CLICK_AIR)) {
            if (torchMain.getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]")) {
                togglePlayer(player);
            }
        }
    }


    @EventHandler
    public void onPlayerPlace(PlayerInteractEvent event) {
        PlayerInventory playerInv = event.getPlayer().getInventory();
        ItemStack torchMain = playerInv.getItemInMainHand();
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                (torchMain.hasItemMeta()) &&
                (torchMain.getItemMeta().getDisplayName() != null) &&
                (torchMain.getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Flashlight" + ChatColor.DARK_AQUA + "]"))) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onConsumeMilk(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack milk = new ItemStack(event.getItem());
        if (milk.getType().equals(Material.MILK_BUCKET) && API.getFlashLightToggle().contains(player.getName())) {
            event.setCancelled(true);
            player.getActivePotionEffects().stream().filter(effect -> !(player.getActivePotionEffects() == PotionEffectType.NIGHT_VISION)).forEach(effect -> player.removePotionEffect(effect.getType()));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
    }
}