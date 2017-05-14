package com.michaelelin.holoban;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HoloBanListener implements Listener {

    private HoloBan plugin;

    public HoloBanListener(HoloBan plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryCreative(InventoryCreativeEvent e) {
        ItemStack stack = e.getCursor();
        if (plugin.isInvalid(stack)) {
            plugin.notify("Blocked player from spawning " + stack.getType() + " with extra data: "
                    + e.getWhoClicked().getName()
                    + " (" + e.getWhoClicked().getUniqueId() + ")");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack stack = e.getItem();
        if (plugin.isInvalid(stack)) {
            plugin.notify("Blocked player from using " + stack.getType() + " with extra data: "
                    + e.getPlayer().getName()
                    + " (" + e.getPlayer().getUniqueId() + ")");
            e.getPlayer().getInventory().remove(stack);
            e.setCancelled(true);
        }
    }

}
