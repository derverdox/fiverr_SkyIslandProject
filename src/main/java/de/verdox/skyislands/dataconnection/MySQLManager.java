package de.verdox.skyislands.dataconnection;

import de.verdox.skyislands.Core;
import de.verdox.skyislands.subsystems.islands.dataconnection.IslandTable;
import de.verdox.vcore.dataconnection.MySQLTable;
import de.verdox.vcore.dataconnection.files.MySQLCConfigValue;
import de.verdox.vcore.dataconnection.files.MySQLConfig;
import de.verdox.vcore.utils.NumberUtils;

import java.sql.SQLException;

public class MySQLManager {

    private static MySQLManager instance;

    private MySQLConfig mySQLConfig;
    private MySQLConnector mySQLConnector;

    //TODO: TABLES HIER INITIALISIEREN
    private IslandTable islandTable;


    MySQLManager(){
        init();
        createTables();
    }

    private void init(){

        this.mySQLConfig = new MySQLConfig(Core.getInstance(),"mysql.yml","\\dataconnection");
        mySQLConfig.init();

        try {
            this.mySQLConnector = new MySQLConnector(Core.getInstance()
                    ,mySQLConfig.getValue(MySQLCConfigValue.Host)
                    ,mySQLConfig.getValue(MySQLCConfigValue.Database_Name)
                    ,mySQLConfig.getValue(MySQLCConfigValue.User)
                    ,mySQLConfig.getValue(MySQLCConfigValue.Password)
                    , NumberUtils.parseInt(mySQLConfig.getValue(MySQLCConfigValue.Port))
                    ,1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables(){
        this.islandTable = new IslandTable(getMySQLConnector());
    }

    public static MySQLManager getInstance(){
        if(instance == null)
            instance = new MySQLManager();
        return instance;
    }

    public MySQLTable loadTable(Class c){
        return mySQLConnector.getTable(c);
    }

    public MySQLConnector getMySQLConnector() {
        return mySQLConnector;
    }
}

