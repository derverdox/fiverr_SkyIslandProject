package de.verdox.skyislands.subsystems.islands.guis;

import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.model.IslandManager;
import de.verdox.skyislands.subsystems.islands.playerdata.IslandPlayerData;
import de.verdox.vcore.customguis.consumers.GUIConsumer;
import de.verdox.vcore.customguis.consumers.GUIConsumerType;
import de.verdox.vcore.customguis.guis.GUIItem;
import de.verdox.vcore.playersession.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RewardCreateIsland extends GUIConsumer {
    public RewardCreateIsland(Plugin plugin) {
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
        if(IslandManager.getInstance().isAlreadyGenerating(player.getUniqueId())){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cAlready generating&7!"));
            return false;
        }
        Island island = islandPlayerData.getPlayerIsland();
        if(island != null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou already have an island&7!"));
            return false;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aCreating a new island&7!"));
        IslandManager.getInstance().generateIsland(player.getUniqueId(),island1 -> {
            player.teleport(island1.getSpawnPoint());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aNew island created&7!"));
        });
        return true;
    }

    @Override
    public String configIdentifier() {
        return "island_create";
    }
}
