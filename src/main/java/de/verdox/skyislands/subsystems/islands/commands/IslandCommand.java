package de.verdox.skyislands.subsystems.islands.commands;

import de.verdox.skyislands.Core;
import de.verdox.skyislands.subsystems.islands.guis.IslandsGUI;
import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.model.IslandManager;
import de.verdox.skyislands.subsystems.islands.model.IslandPosition;
import de.verdox.skyislands.subsystems.islands.playerdata.IslandPlayerData;
import de.verdox.vcore.customguis.guis.GUIManager;
import de.verdox.vcore.playersession.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IslandCommand implements TabExecutor {


    //TODO: Permission Check implementieren!

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        IslandPlayerData islandPlayerData = (IslandPlayerData) SessionManager.getInstance().getSession(player).getData(IslandPlayerData.identifier);

        if(args.length == 0){
            // /islands                         -> Opens Island User GUI
            IslandsGUI islandsGUI = (IslandsGUI) GUIManager.getInstance(Core.getInstance()).getGUIConfig(IslandsGUI.class);
            islandsGUI.openInventory(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cNot implemented yet&7!"));
            return true;
        }
        else if(args.length == 1){
            // /islands auto                    -> Autogenerates an Island
            if(args[0].equalsIgnoreCase("auto")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aYour Island is being created&7!"));
                if(islandPlayerData.getIslandsOwned() >= islandPlayerData.getLimit()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou already own an Island&7!"));
                    return false;
                }
                IslandManager.getInstance().generateIsland(player.getUniqueId(), island -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aNew Island created&7!"));
                    player.teleport(island.getIslandPosition().toLocation().add(0,5,0));
                });
                return true;
            }
            else if(args[0].equalsIgnoreCase("home")){
                Island island = islandPlayerData.getPlayerIsland();
                if(island == null){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou need to create an Island first&7!"));
                    return false;
                }
                player.teleport(island.getSpawnPoint());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aTeleported you to your Island&7!"));
            }
            // /islands delete                  -> Deletes the current Island
            else if(args[0].equalsIgnoreCase("delete")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cNot implemented yet&7!"));
                return true;
            }
            // /islands worth                  -> Gets the Worth of the Island
            else if(args[0].equalsIgnoreCase("worth")){

                IslandPosition islandPosition = IslandManager.getInstance().getIslandPositionAt(player.getLocation());
                Island island = IslandManager.getInstance().getIslandAt(islandPosition);
                if(island == null){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThere is no Island nearby&7!"));
                    return false;
                }
                if(!island.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("sIslands.worthBypass")){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThis is not your Island&7!"));
                    return false;
                }

                if(island.isCachingBlocks()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThis Island is currently caching its Blocks&7!"));
                    return false;
                }

                if(island.isCalculatingWorth()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThis Island is currently calculating its worth&7!"));
                    return false;
                }

                Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(),() -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aWorth is&7: &c"+island.getWorth()));
                });

                return true;
            }
        }
        else if(args.length == 2){
            // /islands addFriend name          -> Adds a friend
            if(args[0].equalsIgnoreCase("addFriend")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cNot implemented yet&7!"));
                return true;
            }
            // /islands removeFriend name       -> Removes a friend
            else if(args[0].equalsIgnoreCase("removeFriend")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cNot implemented yet&7!"));
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggest = new ArrayList<>();

        if(args.length <= 1){
            suggest.add("auto");
            suggest.add("home");
            //suggest.add("delete");
            suggest.add("addFriend");
            suggest.add("removeFriend");
            suggest.add("worth");
        }
        else if(args.length <= 2){
            suggest.clear();
            if(args[0].equalsIgnoreCase("addFriend")){
                Bukkit.getOnlinePlayers().forEach(player -> suggest.add(player.getName()));
            }
            else if(args[0].equalsIgnoreCase("removeFriend")){
                Bukkit.getOnlinePlayers().forEach(player -> suggest.add(player.getName()));
            }
        }

        return suggest;
    }
}
