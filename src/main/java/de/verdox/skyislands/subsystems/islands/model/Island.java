package de.verdox.skyislands.subsystems.islands.model;

import de.verdox.skyislands.Core;
import de.verdox.skyislands.dataconnection.MySQLManager;
import de.verdox.skyislands.subsystems.islands.dataconnection.IslandTable;
import de.verdox.vcore.VCore;
import de.verdox.vcore.dataconnection.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Island {

    private final UUID ownerUUID;
    private final IslandPosition islandPosition;

    private boolean calculatingWorth = false;
    private boolean cachingBlocks = false;

    private double worth = 0;
    private Map<Material, Double> blockWorth;
    private final Map<Material, Integer> blockCache;

    Island(UUID ownerUUID, IslandPosition islandPosition){
        //VCore.getInstance().consoleMessage("Loading Island at: "+islandPosition);
        this.ownerUUID = ownerUUID;
        this.islandPosition = islandPosition;
        this.blockWorth = Core.getInstance().getIslandSystem().getMainConfig().getBlockWorth();
        this.blockCache = new HashMap<>();
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(),() -> {
            cacheBlocks();
            calculateWorth();
            IslandTable islandTable = (IslandTable) MySQLManager.getInstance().getMySQLConnector().getTable(IslandTable.class);
            try { islandTable.updateIsland(this); } catch (SQLException exception) { exception.printStackTrace(); }
            VCore.getInstance().consoleMessage("Worth of island updated: "+islandPosition);
        });
    }

    public double calculateWorth(){
        //VCore.getInstance().consoleMessage("Start calculating worth for: "+islandPosition);
        this.calculatingWorth = true;
        updateBlockWorth();
        worth = 0;
            blockCache.forEach((material, integer) -> {
                if(!blockWorth.containsKey(material))
                    return;
                worth += (blockWorth.get(material) * integer);
            });
        this.calculatingWorth = false;
        //VCore.getInstance().consoleMessage("Finished calculating worth for: "+islandPosition);
        return worth;
    }

    public void breakBlock(Block block){
        updateBlockWorth();
        if(block == null || block.getType().equals(Material.AIR))
            return;
        Material material = block.getType();
        if(blockWorth == null || !blockWorth.containsKey(material))
            return;
        deCacheBlock(block);
        worth-=blockWorth.get(material);
    }

    public void placeBlock(Block block){
        updateBlockWorth();
        if(block == null || block.getType().equals(Material.AIR))
            return;
        Material material = block.getType();
        if(blockWorth == null || !blockWorth.containsKey(material))
            return;
        cacheBlock(block);
        worth+=blockWorth.get(material);
    }

    public boolean isCachingBlocks() {
        return cachingBlocks;
    }

    public boolean isCalculatingWorth() {
        return calculatingWorth;
    }

    public double getWorth() { return worth; }

    public Location getSpawnPoint(){ return new Location(islandPosition.getWorld(),islandPosition.getMiddleX(),islandPosition.getHeight(),islandPosition.getMiddleZ()); }

    public UUID getOwnerUUID() { return ownerUUID; }

    public IslandPosition getIslandPosition() { return islandPosition; }

    // Intern Methods

    private void updateBlockWorth(){
        if(blockWorth == null)
            this.blockWorth = Core.getInstance().getIslandSystem().getMainConfig().getBlockWorth();
    }

    private void cacheBlocks(){
        //VCore.getInstance().consoleMessage("Start blockCaching for: "+islandPosition);
        updateBlockWorth();
            cachingBlocks = true;
            for(int x = islandPosition.getMinX(); x < islandPosition.getMaxX(); x++){
                for(int y = 0; y <= islandPosition.getWorld().getMaxHeight(); y++){
                    for(int z = islandPosition.getMinZ(); z < islandPosition.getMaxX(); z++){
                        Block block = islandPosition.getWorld().getBlockAt(x,y,z);
                        cacheBlock(block);
                    }
                }
            }
        //VCore.getInstance().consoleMessage("Finished blockCaching for: "+islandPosition);
        cachingBlocks = false;
    }

    private void cacheBlock(Block block){
        updateBlockWorth();
        if(block == null || block.getType().equals(Material.AIR))
            return;
        Material material = block.getType();
        int newValue = 1;
        if(blockCache.containsKey(material)){
            Integer integer = blockCache.get(material);
            newValue+= integer.intValue();
        }
        blockCache.put(material,newValue);
    }

    private void deCacheBlock(Block block){
        updateBlockWorth();
        if(block == null || block.getType().equals(Material.AIR))
            return;
        Material material = block.getType();
        if(!blockCache.containsKey(material))
            return;
        int newValue = blockCache.get(material).intValue()-1;
        if(newValue == 0)
            blockCache.remove(material);
        else
            blockCache.put(material,newValue);
    }

    public void setWorth(double worth) {
        this.worth = worth;
    }
}
