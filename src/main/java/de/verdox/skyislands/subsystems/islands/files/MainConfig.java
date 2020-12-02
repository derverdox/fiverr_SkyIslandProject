package de.verdox.skyislands.subsystems.islands.files;

import de.verdox.vcore.files.Configuration;
import de.verdox.vcore.utils.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class MainConfig extends Configuration {

    private Map<Material, Double> blockWorth;

    public MainConfig(Plugin plugin, String fileName, String pluginDirectory) {
        super(plugin, fileName, pluginDirectory);
    }

    @Override
    public void setupConfig() {

        config.addDefault("Settings.MapName","Shattered_Islands_Map");
        config.addDefault("Settings.schematicName","testIsland");
        config.addDefault("Settings.Radius",20);
        config.addDefault("Settings.SpaceBetween",10);
        config.addDefault("Settings.Height",80);

        config.addDefault("Settings.BlockWorth."+Material.DIRT.name(),1);
        config.addDefault("Settings.BlockWorth."+Material.GRASS.name(),2.0);
        config.addDefault("Settings.BlockWorth."+Material.STONE.name(),3.0);
        config.addDefault("Settings.BlockWorth."+Material.COBBLESTONE.name(),4.0);

        config.options().copyDefaults(true);
        save();
        loadBlockWorth();
    }

    public Map<Material, Double> getBlockWorth() {
        if(this.blockWorth == null)
            this.blockWorth = loadBlockWorth();
        return blockWorth;
    }

    private Map<Material, Double> loadBlockWorth(){
        Map<Material, Double> cache = new HashMap<>();
        if(!config.isSet("Settings.BlockWorth"))
            return cache;
        for (String materialString : config.getConfigurationSection("Settings.BlockWorth").getKeys(false)) {
            Material material = MaterialUtils.parseMaterial(materialString);
            if(material == null)
                continue;
            double worth = config.getDouble("Settings.BlockWorth."+materialString);
            cache.put(material,worth);
        }
        return cache;
    }

    public int getRadius(){
        return config.getInt("Settings.Radius");
    }

    public int getHeight(){
        return config.getInt("Settings.Height");
    }

    public int getSpaceBetween(){
        return config.getInt("Settings.SpaceBetween");
    }

    public String getMapName(){
        return config.getString("Settings.MapName");
    }

    public SchematicFile getSchematic(){
        return FileManager.getInstance().getFile(config.getString("Settings.schematicName").replace(".schem","").replace(".schematic",""));
    }

}
