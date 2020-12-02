package de.verdox.skyislands.subsystems.islands.playerdata;

import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.model.IslandPosition;
import de.verdox.vcore.playersession.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IslandPlayerData extends PlayerData {

    public static String identifier = "Shattered_Island_Data";

    private final Map<Location, Material> previewCache;
    private final Set<Island> playerIslands;
    private int limit = 1;

    public IslandPlayerData(Player player) {
        super(player);
        this.previewCache = new HashMap<>();
        this.playerIslands = new HashSet<>();
    }

    public int getLimit() {
        return limit;
    }

    public int getIslandsOwned(){
        return playerIslands.size();
    }

    public Island getPlayerIsland(){
        return playerIslands.stream().findAny().orElse(null);
    }

    public boolean addIslandToCache(Island island){
        return playerIslands.add(island);
    }

    public boolean removeIslandFromCache(Island island){
        return playerIslands.remove(island);
    }

    public synchronized void showBorderToPlayer(IslandPosition islandPosition){
        clearBorderPreview();
        Location b1 = new Location(islandPosition.getWorld(),islandPosition.getMaxX(),islandPosition.getHeight(),islandPosition.getMaxZ());
        Location b2 = new Location(islandPosition.getWorld(),islandPosition.getMaxX(),islandPosition.getHeight(),islandPosition.getMinZ());
        Location b3 = new Location(islandPosition.getWorld(),islandPosition.getMinX(),islandPosition.getHeight(),islandPosition.getMinZ());
        Location b4 = new Location(islandPosition.getWorld(),islandPosition.getMinX(),islandPosition.getHeight(),islandPosition.getMaxZ());

        sendFakeBlock(b1,Material.GLOWSTONE);
        sendFakeBlock(b2,Material.GLOWSTONE);
        sendFakeBlock(b3,Material.GLOWSTONE);
        sendFakeBlock(b4,Material.GLOWSTONE);
    }

    private synchronized void sendFakeBlock(Location location, Material fakeMaterial){
        previewCache.put(location,location.getBlock().getType());
        player.sendBlockChange(location, Bukkit.createBlockData(fakeMaterial));
    }

    public synchronized void clearBorderPreview(){
        if(this.previewCache.isEmpty())
            return;
        previewCache.forEach((location, material) -> {
            player.sendBlockChange(location, Bukkit.createBlockData(material));
        });
        this.previewCache.clear();
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public void onSessionRemove() {



    }
}
