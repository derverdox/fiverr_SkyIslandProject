package de.verdox.skyislands.subsystems.guis;

import de.verdox.vcore.customguis.guis.CustomGUI;
import de.verdox.vcore.customguis.guis.CustomGUIConfig;
import de.verdox.vcore.customguis.guis.GUIManager;
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
        return "&6Schöner GUI Title";
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    public InventoryType inventoryType() {
        return InventoryType.CHEST;
    }

    @Override
    public void setContent() {
        ItemStack stack = ItemUtil.createStack(plugin, Material.STONE.name(),1,"&cTest","&aLore Line 1");
        TestPrice testPrice = (TestPrice) GUIManager.getInstance(plugin).getGuiConsumerManager().getConsumer(TestPrice.class);
        TestReward testReward = (TestReward) GUIManager.getInstance(plugin).getGuiConsumerManager().getConsumer(TestReward.class);
        addItem("item1",0, stack,"", testPrice,testReward,1,1,true);
    }

    @Override
    public void updateInventory(CustomGUI customGUI) {

    }
}
