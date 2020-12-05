package de.verdox.skyislands.subsystems.islands.guis;

import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.playerdata.IslandPlayerData;
import de.verdox.vcore.customguis.consumers.GUIConsumer;
import de.verdox.vcore.customguis.consumers.GUIConsumerType;
import de.verdox.vcore.customguis.guis.GUIItem;
import de.verdox.vcore.playersession.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RewardIslandHome extends GUIConsumer {
    public RewardIslandHome(Plugin plugin) {
        super(plugin);
    }

    @Override
    public GUIConsumerType getType() {
        return GUIConsumerType.REWARD;
    }

    @Override
    public boolean consume(Player player, GUIItem guiItem) {
        IslandPlayerData islandPlayerData = (IslandPlayerData) SessionManager.getInstance().getSession(player).getData(IslandPlayerData.identifier);
        if(islandPlayerData == null)
            return false;
        Island island = islandPlayerData.getPlayerIsland();
        if(island == null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou don't have an Island yet&7!"));
            return false;
        }
        player.teleport(island.getSpawnPoint());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aTeleported you to your island&7!"));
        return true;
    }

    @Override
    public String configIdentifier() {
        return "island_home";
    }
}
