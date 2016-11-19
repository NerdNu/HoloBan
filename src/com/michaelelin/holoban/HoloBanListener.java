package com.michaelelin.holoban;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
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
        if (isInvalid(stack)) {
            plugin.getLogger().info("Blocked player from spawning armor stand with extra data: "
                    + e.getWhoClicked().getName()
                    + " (" + e.getWhoClicked().getUniqueId() + ")");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack stack = e.getItem();
        if (isInvalid(stack)) {
            plugin.getLogger().info("Blocked player from placing armor stand with extra data: "
                    + e.getPlayer().getName()
                    + " (" + e.getPlayer().getUniqueId() + ")");
            e.getPlayer().getInventory().remove(stack);
            e.setCancelled(true);
        }
    }

    private boolean isInvalid(ItemStack stack) {
        if (stack != null && stack.getType() == Material.ARMOR_STAND) {
            net.minecraft.server.v1_11_R1.ItemStack handle = CraftItemStack.asNMSCopy(stack);
            if (handle != null) {
                NBTTagCompound tag = handle.getTag();
                if (tag != null && tag.hasKey("EntityTag")) {
                    return true;
                }
            }
        }
        return false;
    }

}
