package de.verdox.skyislands.subsystems.islands.dataconnection;

import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.model.IslandManager;
import de.verdox.vcore.dataconnection.MySQL;
import de.verdox.vcore.dataconnection.MySQLTable;
import de.verdox.vcore.playersession.PlayerData;
import org.bukkit.entity.Player;
import org.hamcrest.core.Is;

import java.sql.*;
import java.util.UUID;

public class IslandTable extends MySQLTable {
    public IslandTable(MySQL mySQL) {
        super(mySQL);
    }

    @Override
    public void initTable() throws SQLException {
        Connection connection = mySQL.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS shattered_islands_islandData (" +
                "id int(11) unsigned NOT NULL AUTO_INCREMENT," +
                "ownerUUID varchar(64) NOT NULL DEFAULT ''," +
                "coordX int(11) NOT NULL DEFAULT '0'," +
                "coordZ int(11) NOT NULL DEFAULT '0'," +
                "worth float(11) NOT NULL DEFAULT '0'," +
                "PRIMARY KEY (id)\n)");
        connection.close();
    }

    public void saveIsland(Island island) throws SQLException {
        Connection connection = mySQL.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO shattered_islands_islandData (ownerUUID,coordX,coordZ, worth) VALUES (?,?,?,?)");
        preparedStatement.setString(1, String.valueOf(island.getOwnerUUID()));
        preparedStatement.setInt(2, island.getIslandPosition().getX());
        preparedStatement.setInt(3, island.getIslandPosition().getZ());
        preparedStatement.setDouble(4, island.getWorth());
        preparedStatement.executeUpdate();
        connection.close();
    }

    public void deleteIsland(Island island) throws SQLException{
        Connection connection = mySQL.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE * FROM shattered_islands_islandData WHERE coordX=? AND coordZ=?");
        preparedStatement.setInt(1,island.getIslandPosition().getX());
        preparedStatement.setInt(2,island.getIslandPosition().getZ());
        preparedStatement.executeUpdate();
        connection.close();
    }

    @Override
    public void loadData() throws SQLException {
        Connection connection = mySQL.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shattered_islands_islandData");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            UUID ownerUUID = UUID.fromString(resultSet.getString("ownerUUID"));
            int coordX = resultSet.getInt("coordX");
            int coordZ = resultSet.getInt("coordZ");
            double worth = resultSet.getDouble("worth");
            Island island = IslandManager.getInstance().loadIsland(ownerUUID,coordX,coordZ);
            island.setWorth(worth);
        }
        connection.close();
    }

    @Override
    public void loadPlayerData(Player player) throws SQLException {}

    @Override
    public boolean hasPlayerData(Player player) throws SQLException {
        return false;
    }

    @Override
    public void createPlayerData(Player player) throws SQLException {}

    @Override
    public void updatePlayerData(Player player) throws SQLException {}

    @Override
    public void onCloseConnection() throws SQLException {
        for (Island island : IslandManager.getInstance().getCache().values()) {
            updateIsland(island);
        }
    }
    public void updateIsland(Island island) throws SQLException{
        Connection connection = mySQL.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE shattered_islands_islandData SET worth=? WHERE coordX=? AND coordZ=?");
        preparedStatement.setDouble(1, island.getWorth());
        preparedStatement.setInt(2, island.getIslandPosition().getX());
        preparedStatement.setInt(3, island.getIslandPosition().getZ());
        preparedStatement.executeUpdate();
        connection.close();
    }

    @Override
    public PlayerData playerDataObject(Player player) {
        return null;
    }

    @Override
    public String identifier() {
        return "Shattered_Islands_IslandTable";
    }
}
