package com.michaelelin.holoban;

import org.bukkit.plugin.java.JavaPlugin;

public class HoloBan extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new HoloBanListener(this), this);
    }

}
