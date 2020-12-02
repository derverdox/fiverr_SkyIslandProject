package de.verdox.skyislands.subsystems.islands.listener;

import de.verdox.skyislands.subsystems.islands.events.BreakOnIslandEvent;
import de.verdox.skyislands.subsystems.islands.events.BuildOnIslandEvent;
import de.verdox.skyislands.subsystems.islands.events.EnterIslandEvent;
import de.verdox.skyislands.subsystems.islands.events.LeaveIslandEvent;
import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.model.IslandManager;
import de.verdox.skyislands.subsystems.islands.model.IslandPosition;
import de.verdox.skyislands.subsystems.islands.playerdata.IslandPlayerData;
import de.verdox.vcore.playersession.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        Location location = e.getBlockAgainst().getLocation();

        IslandPosition islandPosition = IslandManager.getInstance().getIslandPositionAt(location);

        if(islandPosition == null)
            return;

        Island island = IslandManager.getInstance().getIslandAt(islandPosition);
        if(island == null)
            return;

        BuildOnIslandEvent buildOnIslandEvent = new BuildOnIslandEvent(player,island, e.getBlock());
        Bukkit.getPluginManager().callEvent(buildOnIslandEvent);
        if(buildOnIslandEvent.isCancelled())
            e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Location location = e.getBlock().getLocation();

        IslandPosition islandPosition = IslandManager.getInstance().getIslandPositionAt(location);

        if(islandPosition == null)
            return;

        Island island = IslandManager.getInstance().getIslandAt(islandPosition);
        if(island == null)
            return;

        BreakOnIslandEvent breakOnIslandEvent = new BreakOnIslandEvent(player,island, e.getBlock());
        Bukkit.getPluginManager().callEvent(breakOnIslandEvent);
        if(breakOnIslandEvent.isCancelled())
            e.setCancelled(true);
    }

    @EventHandler
    public void move(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location oldLocation = e.getFrom();
        Location newLocation = e.getTo();
        IslandPlayerData islandPlayerData = (IslandPlayerData) SessionManager.getInstance().getSession(player).getData(IslandPlayerData.identifier);

        if(islandPlayerData == null)
            return;

        IslandPosition oldIsland = IslandManager.getInstance().getIslandPositionAt(oldLocation);
        IslandPosition newIsland = IslandManager.getInstance().getIslandPositionAt(newLocation);

        // Not in the Right Map!
        if(oldIsland == null || newIsland == null)
            return;

        if(oldIsland.isInsideIslandArea(oldLocation) && !oldIsland.isInsideIslandArea(newLocation)) {
            Island island = IslandManager.getInstance().getIslandAt(oldIsland);
            if(island == null)
                return;

            LeaveIslandEvent leaveIslandEvent = new LeaveIslandEvent(player,island);
            Bukkit.getPluginManager().callEvent(leaveIslandEvent);
            if(leaveIslandEvent.isCancelled())
                e.setCancelled(true);
        }

        else if(newIsland.isInsideIslandArea(newLocation) && !newIsland.isInsideIslandArea(oldLocation)) {
            Island island = IslandManager.getInstance().getIslandAt(newIsland);
            if(island == null)
                return;
            EnterIslandEvent enterIslandEvent = new EnterIslandEvent(player,island);
            Bukkit.getPluginManager().callEvent(enterIslandEvent);
            if(enterIslandEvent.isCancelled())
                e.setCancelled(true);
        }
    }
}
