package com.michaelelin.holoban;

import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HoloBanListener implements Listener {

    private HoloBan plugin;

    private String formatBlockedMessage(HumanEntity player, String blockedAction, ItemStack stack) {
        StringBuilder message = new StringBuilder();
        message.append("HoloBan blocked ").append(player.getName());
        message.append(" from ").append(blockedAction).append(" ").append(stack.getType()).append(" with banned NBT data at ");

        Location loc = player.getLocation();
        message.append(loc.getWorld().getName());
        message.append(", ").append(loc.getBlockX());
        message.append(", ").append(loc.getBlockY());
        message.append(", ").append(loc.getBlockZ());
        message.append(".");
        return message.toString();
    }

    public HoloBanListener(HoloBan plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryCreative(InventoryCreativeEvent e) {
        if (e.getWhoClicked().hasPermission(HoloBan.BYPASS_PERMISSION)) {
            return;
        }

        ItemStack stack = e.getCursor();
        if (stack != null && plugin.isInvalid(stack)) {
            plugin.notify(formatBlockedMessage(e.getWhoClicked(), "spawning", stack));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().hasPermission(HoloBan.BYPASS_PERMISSION)) {
            return;
        }

        ItemStack stack = e.getItem();
        if (stack != null && plugin.isInvalid(stack)) {
            plugin.notify(formatBlockedMessage(e.getPlayer(), "using", stack));
            e.getPlayer().getInventory().remove(stack);
            e.setCancelled(true);
        }
    }

}
