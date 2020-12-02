package de.verdox.skyislands.subsystems.guis;

import de.verdox.vcore.customguis.consumers.GUIConsumer;
import de.verdox.vcore.customguis.consumers.GUIConsumerType;
import de.verdox.vcore.customguis.guis.GUIItem;
import de.verdox.vcore.utils.RandomUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TestPrice extends GUIConsumer {
    public TestPrice(Plugin plugin) {
        super(plugin);
    }

    @Override
    public GUIConsumerType getType() {
        return GUIConsumerType.PRICE;
    }

    @Override
    public boolean consume(Player player, GUIItem guiItem) {

        boolean b = RandomUtil.randomBoolean();
        if(b == false){
            player.sendMessage("Das hat nicht geklappt!");
            return false;
        }

        return true;
    }

    @Override
    public String configIdentifier() {
        return "testPriceConsumer";
    }
}
