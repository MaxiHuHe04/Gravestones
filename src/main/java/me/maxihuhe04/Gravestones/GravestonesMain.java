package me.maxihuhe04.Gravestones;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class GravestonesMain extends JavaPlugin {

    private static GravestonesMain instance;

    static GravestonesMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        GravestoneHandler handler = new GravestoneHandler();

        this.getServer().getPluginManager().registerEvents(handler, this);
        this.getCommand("graves").setExecutor(handler);

    }

    /**
     * Destroy every Grave on Server shutdown
     */
    @Override
    public void onDisable() {

        try {
            if(!Grave.gravestones.isEmpty()) {
                //noinspection unchecked
                ((ArrayList<Grave>) Grave.gravestones.clone()).forEach(grave -> {
                    try {
                        grave.destroy();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
