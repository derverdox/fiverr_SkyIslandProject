package de.verdox.skyislands.dataconnection;

import de.verdox.vcore.dataconnection.MySQL;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;

public class MySQLConnector extends MySQL {

    public MySQLConnector(Plugin plugin, String host, String databaseName, String userName, String password, int port, int maxPoolSize) throws SQLException {
        super(plugin, host, databaseName, userName, password, port, maxPoolSize);
    }
}