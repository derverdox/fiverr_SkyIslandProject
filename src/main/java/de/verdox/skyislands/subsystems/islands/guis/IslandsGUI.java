package de.verdox.skyislands.subsystems.islands.guis;

import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.skyislands.subsystems.islands.playerdata.IslandPlayerData;
import de.verdox.vcore.customguis.guis.CustomGUI;
import de.verdox.vcore.customguis.guis.CustomGUIConfig;
import de.verdox.vcore.customguis.guis.GUIItem;
import de.verdox.vcore.customguis.guis.GUIManager;
import de.verdox.vcore.playersession.SessionManager;
import de.verdox.vcore.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class IslandsGUI extends CustomGUIConfig {
    public IslandsGUI(Plugin plugin, String fileName, String pluginDirectory) {
        super(plugin, fileName, pluginDirectory);
    }

    @Override
    public String guiName() {
        return "IslandsGUI";
    }

    @Override
    public String displayName() {
        return "&bSchattered Islands";
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    public InventoryType inventoryType() {
        return InventoryType.HOPPER;
    }

    @Override
    public void setContent() {
        ItemStack islandHome = ItemUtil.createStack(plugin, Material.GREEN_STAINED_GLASS_PANE.name(),1,"&aIsland Home","&7Teleports you to your island!", "&6Worth&7: %island_worth%");
        RewardIslandHome rewardIslandHome = (RewardIslandHome) GUIManager.getInstance(plugin).getGuiConsumerManager().getConsumer(RewardIslandHome.class);
        addItem("homeItem",2, islandHome,"", null,rewardIslandHome,1,1,true);
    }

    @Override
    public void updateInventory(CustomGUI customGUI) {
        IslandPlayerData islandPlayerData = (IslandPlayerData) SessionManager.getInstance().getSession(customGUI.getPlayer()).getData(IslandPlayerData.identifier);
        if(islandPlayerData == null)
            return;
        Island island = islandPlayerData.getPlayerIsland();
        if(island != null)
            return;

        RewardCreateIsland rewardCreateIsland = (RewardCreateIsland) GUIManager.getInstance(plugin).getGuiConsumerManager().getConsumer(RewardCreateIsland.class);

        ItemStack newIsland = ItemUtil.createStack(plugin, Material.GREEN_STAINED_GLASS_PANE.name(),1,"&eNew island","&7Creates a new island!");
        GUIItem newIslandItem = customGUI.constructItem(2,newIsland, "",null,null,rewardCreateIsland,null,true);
        customGUI.injectItem(newIslandItem);
    }
}
