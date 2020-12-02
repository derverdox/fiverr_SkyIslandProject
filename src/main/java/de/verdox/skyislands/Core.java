package de.verdox.skyislands;

import de.verdox.skyislands.dataconnection.MySQLManager;
import de.verdox.skyislands.subsystems.islands.IslandSystem;
import de.verdox.skyislands.subsystems.islands.worldgenerator.VoidChunkGenerator;
import de.verdox.vcore.VCore;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Core extends JavaPlugin {

    private static Core core;
    private static VCore vCore;
    private MySQLManager mySQLManager;

    private IslandSystem islandSystem;

    @Override
    public void onEnable() {
        initCorePlugin();
        initSubsystems();
        startVCore();
        mySQLManager = MySQLManager.getInstance();
    }

    @Override
    public void onLoad() {
        VCore.getInstance(this).onLoad();
    }

    @Override
    public void onDisable() {
        vCore.disable();
    }

    private void initSubsystems(){
        this.islandSystem = new IslandSystem(core);
    }

    private void initCorePlugin(){
        core = this;
        vCore = VCore.getInstance(core);
    }

    private void startVCore(){
        vCore.enable();
        vCore.registerCommands();
        vCore.registerEvents();
    }

    public static Core getInstance(){
        return core;
    }

    public static VCore getVCoreInstance(){
        return vCore;
    }

    public void reloadPlugin(){
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Server is reloading please reconnect."));
        try { this.mySQLManager.getMySQLConnector().reloadDatabase(); } catch (SQLException exception) { exception.printStackTrace(); }
        getVCoreInstance().reloadVCore();
    }

    public IslandSystem getIslandSystem() {
        return islandSystem;
    }

    public MySQLManager getMySQLManager() {
        return mySQLManager;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidChunkGenerator();
    }
}
