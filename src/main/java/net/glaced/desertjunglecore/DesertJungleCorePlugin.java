package net.glaced.desertjunglecore;

import net.glaced.desertjunglecore.command.EventCommand;
import net.glaced.desertjunglecore.command.GroupCommand;
import net.glaced.desertjunglecore.command.TeamCommand;
import net.glaced.desertjunglecore.config.PluginConfiguration;
import net.glaced.desertjunglecore.data.SqliteRepository;
import net.glaced.desertjunglecore.event.EventService;
import net.glaced.desertjunglecore.listener.GameListener;
import net.glaced.desertjunglecore.service.GroupService;
import net.glaced.desertjunglecore.service.IconService;
import net.glaced.desertjunglecore.service.SpawnService;
import net.glaced.desertjunglecore.service.TeamService;
import net.glaced.desertjunglecore.ui.HudService;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/** Bootstrap and composition root for DesertJungleCore. */
public final class DesertJungleCorePlugin extends JavaPlugin {
    private SqliteRepository repository;
    private TeamService teams;
    private SpawnService spawns;
    private GroupService groups;
    private EventService event;
    private HudService hud;

    @Override public void onEnable() {
        saveDefaultConfig(); saveResource("groups.yml", false);
        var config = new PluginConfiguration(this);
        repository = new SqliteRepository(this, config.databaseFile());
        repository.initialize();
        spawns = new SpawnService(this);
        teams = new TeamService(this, repository, config, spawns);
        groups = new GroupService(this, repository);
        event = new EventService(this, teams, config);
        hud = new HudService(this, teams, event, groups, config);
        teams.loadOnlinePlayers(); groups.loadDefinitions(); hud.start();
        getServer().getOnlinePlayers().forEach(player -> player.getScheduler().run(this, ignored -> hud.initialize(player), null));
        getServer().getPluginManager().registerEvents(new GameListener(this, teams, event, hud), this);
        register("team", new TeamCommand(teams, spawns)); register("event", new EventCommand(event)); register("group", new GroupCommand(groups));
        getLogger().info("DesertJungleCore enabled.");
    }
    private void register(String name, org.bukkit.command.CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) throw new IllegalStateException("Missing command in plugin.yml: " + name);
        command.setExecutor(executor); command.setTabCompleter((org.bukkit.command.TabCompleter) executor);
    }
    @Override public void onDisable() { if (hud != null) hud.stop(); if (repository != null) repository.close(); }
}
