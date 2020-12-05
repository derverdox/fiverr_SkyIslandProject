package de.verdox.skyislands.subsystems.islands;

import de.verdox.skyislands.Core;
import de.verdox.skyislands.subsystems.islands.guis.IslandsGUI;
import de.verdox.skyislands.subsystems.islands.commands.IslandCommand;
import de.verdox.skyislands.subsystems.islands.files.FileManager;
import de.verdox.skyislands.subsystems.islands.files.MainConfig;
import de.verdox.skyislands.subsystems.islands.guis.RewardCreateIsland;
import de.verdox.skyislands.subsystems.islands.guis.RewardIslandHome;
import de.verdox.skyislands.subsystems.islands.listener.CustomListener;
import de.verdox.skyislands.subsystems.islands.listener.PlayerListener;
import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.model.IslandManager;
import de.verdox.skyislands.subsystems.islands.playerdata.IslandPlayerData;
import de.verdox.skyislands.subsystems.islands.playerdata.SessionListener;
import de.verdox.vcore.SubsystemManager;
import de.verdox.vcore.placeholder.PlaceholderManager;
import de.verdox.vcore.placeholder.interfaces.VCorePlaceholder;
import de.verdox.vcore.playersession.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        RewardIslandHome rewardIslandHome = new RewardIslandHome(plugin);
        RewardCreateIsland rewardCreateIsland = new RewardCreateIsland(plugin);
        PlaceholderManager.getInstance(plugin).registerPlaceholder((player, s) -> {
            IslandPlayerData islandPlayerData = (IslandPlayerData) SessionManager.getInstance().getSession(player).getData(IslandPlayerData.identifier);
            if(islandPlayerData != null){
                Island island = islandPlayerData.getPlayerIsland();
                if(island != null){
                    if(s.contains("%island_worth%")){
                        return s.replace("%island_worth%",island.getWorth()+"");
                    }
                }
            }
            return s;
        });
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
