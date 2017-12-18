package com.michaelelin.holoban;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagCheck implements ItemCheck {

    private Map<Material, List<String>> bannedTags = new HashMap<>();

    public void addBannedTags(Material mat, List<String> tags) {
        bannedTags.put(mat, tags);
    }

    @Override
    public boolean checkValid(ItemStack item) {
        Material mat = item.getType();
        List<String> tagsForMaterial = bannedTags.get(mat);
        return tagsForMaterial == null || !hasBannedTag(item, tagsForMaterial);
    }

    private boolean hasBannedTag(ItemStack item, List<String> tagsToCheck) {
        NBTTagCompound baseTag = getTag(item);
        if (baseTag != null) {
            for (String tagToCheck : tagsToCheck) {
                if (baseTag.hasKey(tagToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    private NBTTagCompound getTag(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack handle = CraftItemStack.asNMSCopy(item);
        return handle.getTag();
    }

}
