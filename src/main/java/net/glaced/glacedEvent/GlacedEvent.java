package net.glaced.glacedEvent;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GlacedEvent extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("DesertJungleEvent Plugin by lowtime startet");
    }

    @Override
    public void onDisable() {
        getLogger().info("DesertJungleEvent Plugin by lowtime deaktiviert");
    }
}
