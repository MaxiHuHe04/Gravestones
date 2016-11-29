package me.maxihuhe04.Gravestones;

import org.bukkit.plugin.java.JavaPlugin;

public final class GravestonesMain extends JavaPlugin {

    private GravestoneHandler gravestoneHandler = new GravestoneHandler();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(gravestoneHandler, this);
    }

    @Override
    public void onDisable() {
        gravestoneHandler.gravestones.forEach(Gravestone::remove);
    }
}
