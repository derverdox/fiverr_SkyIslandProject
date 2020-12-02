package de.verdox.skyislands.subsystems.guis;

import de.verdox.vcore.customguis.consumers.GUIConsumer;
import de.verdox.vcore.customguis.consumers.GUIConsumerType;
import de.verdox.vcore.customguis.guis.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TestReward extends GUIConsumer {
    public TestReward(Plugin plugin) {
        super(plugin);
    }

    @Override
    public GUIConsumerType getType() {
        return GUIConsumerType.REWARD;
    }

    @Override
    public boolean consume(Player player, GUIItem guiItem) {
        player.sendMessage("DU ERHÄLTST DIE GOLDENE KASPERKLATSCHE");
        return false;
    }

    @Override
    public String configIdentifier() {
        return "testReward";
    }
}
