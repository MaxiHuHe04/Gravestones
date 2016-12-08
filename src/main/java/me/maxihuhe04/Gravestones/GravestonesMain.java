package me.maxihuhe04.Gravestones;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

//TODO: /graves (for OP: /graves all)

public final class GravestonesMain extends JavaPlugin {

    private static GravestonesMain instance;


    static GravestonesMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        this.getServer().getPluginManager().registerEvents(new GravestoneHandler(), this);

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
