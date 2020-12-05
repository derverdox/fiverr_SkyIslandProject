package de.verdox.skyislands.subsystems.islands.listener;

import de.verdox.skyislands.subsystems.islands.events.BreakOnIslandEvent;
import de.verdox.skyislands.subsystems.islands.events.BuildOnIslandEvent;
import de.verdox.skyislands.subsystems.islands.model.Island;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomListener implements Listener {

    @EventHandler
    public void buildOnIsland(BuildOnIslandEvent e){
        Player player = e.getPlayer();
        Island island = e.getIsland();

        if(!island.getOwnerUUID().equals(player.getUniqueId())){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThats not your Island&7!"));
            e.setCancelled(true);
            return;
        }

        Block buildBlock = e.getBlock();
        island.placeBlock(buildBlock);
        sendPlayerRightMessage(island,player);
    }

    @EventHandler
    public void breakOnIsland(BreakOnIslandEvent e){
        Player player = e.getPlayer();
        Island island = e.getIsland();

        if(!island.getOwnerUUID().equals(player.getUniqueId())){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThats not your Island&7!"));
            e.setCancelled(true);
            return;
        }

        Block brokenBlock = e.getBlock();
        island.breakBlock(brokenBlock);
        sendPlayerRightMessage(island,player);
    }

    private void sendPlayerRightMessage(Island island, Player player){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.translateAlternateColorCodes('&',"&8[&aInsel&7-&aWert&8] &6"+island.getWorth())));
    }

}
