package me.maxihuhe04.Gravestones;

import org.bukkit.plugin.java.JavaPlugin;

public final class GravestonesMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new GravestoneHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
