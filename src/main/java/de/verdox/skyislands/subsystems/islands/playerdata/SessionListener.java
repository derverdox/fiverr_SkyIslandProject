package de.verdox.skyislands.subsystems.islands.playerdata;

import de.verdox.skyislands.subsystems.islands.model.IslandManager;
import de.verdox.vcore.playersession.PlayerSession;
import de.verdox.vcore.playersession.events.PlayerSessionCreateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SessionListener implements Listener {

    @EventHandler
    public void onSessionCreate(PlayerSessionCreateEvent e){
        Player player = e.getPlayer();
        PlayerSession session = e.getPlayerSession();
        IslandPlayerData islandPlayerData = new IslandPlayerData(player);
        session.addDataToSession(islandPlayerData);
        IslandManager.getInstance().getCache().values().forEach(island -> {
            if(island.getOwnerUUID().equals(player.getUniqueId()))
                islandPlayerData.addIslandToCache(island);
        });
    }

}
