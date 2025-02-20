package com.hakan.core;

import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        HCore.initialize(this);
    }
}
