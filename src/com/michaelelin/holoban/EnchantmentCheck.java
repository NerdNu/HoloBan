package com.michaelelin.holoban;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantmentCheck implements ItemCheck {

    @Override
    public boolean checkValid(ItemStack item) {
        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            if (!isEnchantmentValid(enchantment, level)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEnchantmentValid(Enchantment enchantment, int level) {
        int maxLevel = enchantment.getMaxLevel();
        return level <= maxLevel;
    }

}
