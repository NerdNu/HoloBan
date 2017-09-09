package com.michaelelin.holoban;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoloBan extends JavaPlugin {

    private Collection<ItemCheck> checks;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new HoloBanListener(this), this);
        saveDefaultConfig();
        reloadConfiguration();
    }

    public void reloadConfiguration() {
        reloadConfig();
        checks = new ArrayList<>();
        ConfigurationSection baseConfig = getConfig();
        ConfigurationSection blockedItems = baseConfig.getConfigurationSection("blocked_items");
        if (blockedItems != null) {
            TagCheck tagCheck = new TagCheck();
            for (String mat : blockedItems.getKeys(false)) {
                try {
                    Material material = Material.valueOf(mat.toUpperCase());
                    List<String> tags = blockedItems.getStringList(mat);
                    if (tags != null) {
                        tagCheck.addBannedTags(material, tags);
                    } else {
                        getLogger().warning("Invalid tag list for " + mat);
                    }
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Invalid material type: " + mat);
                }
            }
            checks.add(tagCheck);
        }
        if (baseConfig.getBoolean("block_illegal_enchants", true)) {
            checks.add(new EnchantmentCheck());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (command.getName().equalsIgnoreCase("holoban-reload")) {
            reloadConfiguration();
            sender.sendMessage("HoloBan configuration reloaded.");
        }
        return true;
    }

    public boolean isInvalid(ItemStack item) {
        for (ItemCheck check : checks) {
            if (!check.checkValid(item)) {
                return true;
            }
        }
        return false;
    }

    public void notify(String message) {
        getLogger().info(message);
        String coloredMessage = ChatColor.LIGHT_PURPLE + message;
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.hasPermission("holoban.notify")) {
                player.sendMessage(coloredMessage);
            }
        }
    }

}
