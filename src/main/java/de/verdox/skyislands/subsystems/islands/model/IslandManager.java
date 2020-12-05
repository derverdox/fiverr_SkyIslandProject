package de.verdox.skyislands.subsystems.islands.model;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import de.verdox.skyislands.Core;
import de.verdox.skyislands.interfaces.IslandCallback;
import de.verdox.skyislands.subsystems.islands.dataconnection.IslandTable;
import de.verdox.skyislands.subsystems.islands.files.SchematicFile;
import de.verdox.skyislands.subsystems.islands.playerdata.IslandPlayerData;
import de.verdox.vcore.playersession.SessionManager;
import de.verdox.vcore.utils.RandomUtil;
import org.bukkit.*;

import java.sql.SQLException;
import java.util.*;

public class IslandManager {
    public static IslandManager instance;

    public static IslandManager getInstance() {
        if(instance == null)
            instance = new IslandManager();
        return instance;
    }

    private World voidMap;
    private final Map<IslandPosition, Island> cache = new HashMap<>();
    private final Set<UUID> generatorCache = new HashSet<>();

    IslandManager(){ }

    public void createMap(){
        WorldCreator worldCreator = new WorldCreator(Core.getInstance().getIslandSystem().getMainConfig().getMapName());
        worldCreator.generator(Core.getInstance().getName());

        this.voidMap = Bukkit.createWorld(worldCreator);
        this.voidMap.setGameRule(GameRule.DO_MOB_SPAWNING,false);
        this.voidMap.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
        this.voidMap.setGameRule(GameRule.SHOW_DEATH_MESSAGES,false);
        this.voidMap.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS,false);
    }

    public Island getIslandAt(IslandPosition islandPosition){
        return cache.get(islandPosition);
    }

    public IslandPosition getIslandPositionAt(Location location){
        if(!location.getWorld().equals(this.voidMap))
            return null;
        double locX = location.getX();
        double locZ = location.getZ();

        int radius = Core.getInstance().getIslandSystem().getMainConfig().getRadius();
        int distanceBetween = Core.getInstance().getIslandSystem().getMainConfig().getSpaceBetween();

        int posX = (int) Math.round(locX / (radius*2 + distanceBetween));
        int posZ = (int) Math.round(locZ / (radius*2 + distanceBetween));

        IslandPosition islandPosition = new IslandPosition(voidMap,posX,posZ);
        return islandPosition;
    }

    public void generateIsland(UUID ownerUUID, IslandCallback callback){

        if(generatorCache.contains(ownerUUID))
            return;
        generatorCache.add(ownerUUID);

        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(),() -> {

            Island island = createIsland(ownerUUID);
            IslandTable islandTable = (IslandTable) Core.getInstance().getMySQLManager().loadTable(IslandTable.class);
            try { islandTable.saveIsland(island); } catch (SQLException exception) { exception.printStackTrace(); }
            this.cache.put(island.getIslandPosition(),island);
            SchematicFile schematicFile = Core.getInstance().getIslandSystem().getMainConfig().getSchematic();
            if(schematicFile == null)
                throw new NullPointerException("SchematicFile could not be found!");

            pasteSchematic(schematicFile,island.getIslandPosition().toLocation());
            if(Bukkit.getPlayer(ownerUUID) != null) {
                IslandPlayerData islandPlayerData = (IslandPlayerData) SessionManager.getInstance().getSession(Bukkit.getPlayer(ownerUUID)).getData(IslandPlayerData.identifier);
                if(islandPlayerData != null)
                    islandPlayerData.addIslandToCache(island);

                Bukkit.getScheduler().runTask(Core.getInstance(), () -> callback.callback(island));
            }
            generatorCache.remove(ownerUUID);
        });
    }

    public boolean isAlreadyGenerating(UUID uuid){
        return generatorCache.contains(uuid);
    }

    public Island loadIsland(UUID ownerUUID, int coordX, int coordZ){
        IslandPosition islandPosition = new IslandPosition(voidMap,coordX,coordZ);
        if(isPositionTaken(islandPosition))
            return getIslandAt(islandPosition);
        Island island = new Island(ownerUUID,islandPosition);
        this.cache.put(islandPosition,island);
        if(Bukkit.getPlayer(ownerUUID) != null){
            IslandPlayerData islandPlayerData = (IslandPlayerData) SessionManager.getInstance().getSession(Bukkit.getPlayer(ownerUUID)).getData(IslandPlayerData.identifier);
            if(islandPlayerData != null)
                islandPlayerData.addIslandToCache(island);
        }
        return island;
    }

    private Island createIsland(UUID ownerUUID){

        // Calculate a random Number
        IslandPosition freePos = getFreePosition();
        Island island = new Island(ownerUUID,freePos);
        return island;
    }

    public Map<IslandPosition, Island> getCache() {
        return cache;
    }

    public boolean isPositionTaken(IslandPosition islandPosition){
        return cache.containsKey(islandPosition);
    }

    private IslandPosition getFreePosition(){
        int randomNumber = RandomUtil.getRandomNumberInRange(0,100);
        int randomX = 0;
        int randomZ = 0;

        IslandPosition islandPosition = new IslandPosition(this.voidMap,randomX,randomZ);

        while(isPositionTaken(islandPosition)){
            if(randomNumber <= 25){
                randomX--;
            }
            else if(randomNumber <= 50){
                randomX++;
            }
            else if(randomNumber <= 75){
                randomZ--;
            }
            else if(randomNumber <= 100){
                randomZ++;
            }
            islandPosition = new IslandPosition(this.voidMap,randomX,randomZ);
        }
        return islandPosition;
    }

    private void pasteSchematic(SchematicFile schematicFile, Location location){
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(location.getWorld());
        BlockVector3 vector3 = BukkitAdapter.asBlockVector(location);
        schematicFile.getClipboard().paste(world,vector3);
    }

}
