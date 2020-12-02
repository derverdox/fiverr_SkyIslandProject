package de.verdox.skyislands.subsystems.islands.events;

import de.verdox.skyislands.subsystems.islands.model.Island;
import org.bukkit.entity.Player;

public class EnterIslandEvent extends IslandEvent {
    public EnterIslandEvent(Player player, Island island) {
        super(player, island);
    }
}
