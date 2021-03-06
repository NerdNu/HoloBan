package com.michaelelin.holoban;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullMetaCheck implements ItemCheck {
    @Override
    public boolean checkValid(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            @SuppressWarnings("deprecation")
            String ownerName = skullMeta.getOwner();
            if (ownerName != null && ownerName.length() > 16) {
                return false;
            }
        }
        return true;
    }
}
