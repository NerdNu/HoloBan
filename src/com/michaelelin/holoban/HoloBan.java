package com.michaelelin.holoban;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoloBan extends JavaPlugin {

    private Map<Material, List<String>> blockedMaterials;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new HoloBanListener(this), this);
        saveDefaultConfig();
        reloadConfiguration();
    }

    public void reloadConfiguration() {
        reloadConfig();
        blockedMaterials = new HashMap<Material, List<String>>();
        ConfigurationSection blockedItems = getConfig().getConfigurationSection("blocked_items");
        if (blockedItems != null) {
            for (String mat : blockedItems.getKeys(false)) {
                try {
                    Material material = Material.valueOf(mat.toUpperCase());
                    List<String> tags = blockedItems.getStringList(mat);
                    if (tags != null) {
                        blockedMaterials.put(material, tags);
                    } else {
                        getLogger().warning("Invalid tag list for " + mat);
                    }
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Invalid material type: " + mat);
                }
            }
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

    public boolean isInvalid(ItemStack stack) {
        if (stack != null) {
            List<String> tags = blockedMaterials.get(stack.getType());
            if (tags != null) {
                net.minecraft.server.v1_12_R1.ItemStack handle = CraftItemStack.asNMSCopy(stack);
                if (handle != null) {
                    NBTTagCompound tag = handle.getTag();
                    if (tag != null) {
                        for (String bannedTag : tags) {
                            if (tag.hasKey(bannedTag)) {
                                return true;
                            }
                        }
                    }
                }
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
