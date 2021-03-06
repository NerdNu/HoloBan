package com.michaelelin.holoban;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HoloBan extends JavaPlugin {
    static HoloBan instance;

    private Collection<ItemCheck> checks;

    /**
     * Permission to bypass restrictions.
     */
    public static String BYPASS_PERMISSION = "holoban.bypass";

    /**
     * Permission to receive notifications about blocked actions.
     */
    public static String NOTIFY_PERMISSION = "holoban.notify";

    @Override
    public void onEnable() {
        instance = this;
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
            if (!tagCheck.isOk()) {
                getLogger().severe("HoloBan failed to initialise and is being disabled. Contact a tech!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
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
        checks.add(new SkullMetaCheck());
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
        final String colorMessage = ChatColor.LIGHT_PURPLE + message;
        // Includes console.
        Bukkit.broadcast(colorMessage, NOTIFY_PERMISSION);
    }

}
