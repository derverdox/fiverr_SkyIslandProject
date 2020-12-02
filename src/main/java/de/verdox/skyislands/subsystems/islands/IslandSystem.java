package de.verdox.skyislands.subsystems.islands;

import de.verdox.skyislands.Core;
import de.verdox.skyislands.subsystems.guis.IslandsGUI;
import de.verdox.skyislands.subsystems.guis.TestPrice;
import de.verdox.skyislands.subsystems.guis.TestReward;
import de.verdox.skyislands.subsystems.islands.commands.IslandCommand;
import de.verdox.skyislands.subsystems.islands.files.FileManager;
import de.verdox.skyislands.subsystems.islands.files.MainConfig;
import de.verdox.skyislands.subsystems.islands.listener.CustomListener;
import de.verdox.skyislands.subsystems.islands.listener.PlayerListener;
import de.verdox.skyislands.subsystems.islands.model.IslandManager;
import de.verdox.skyislands.subsystems.islands.playerdata.SessionListener;
import de.verdox.vcore.SubsystemManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class IslandSystem extends SubsystemManager {

    private MainConfig mainConfig;

    public IslandSystem(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),Core.getInstance());
        Bukkit.getPluginManager().registerEvents(new SessionListener(),Core.getInstance());
        Bukkit.getPluginManager().registerEvents(new CustomListener(),Core.getInstance());
    }

    @Override
    public void registerCommands() {
        Core.getInstance().getCommand("islands").setExecutor(new IslandCommand());
        Core.getInstance().getCommand("islands").setTabCompleter(new IslandCommand());
    }

    @Override
    public void onEnable() {
        FileManager.getInstance();
        IslandManager.getInstance();
        TestPrice testPrice = new TestPrice(Core.getInstance());
        TestReward testReward = new TestReward(Core.getInstance());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void initFiles() {
        this.mainConfig = new MainConfig(Core.getInstance(),"mainConfig.yml","\\settings");
        mainConfig.init();
        IslandsGUI islandsGUI = new IslandsGUI(Core.getInstance(), "islandsGUI.yml","\\guis");
        IslandManager.getInstance().getCache();
    }

    @Override
    public void onServerStart() {
        IslandManager.getInstance().createMap();
    }

    @Override
    public boolean isActivated() {
        return true;
    }

    @Override
    public String name() {
        return null;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }
}
