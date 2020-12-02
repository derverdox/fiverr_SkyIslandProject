package de.verdox.skyislands.subsystems.islands.events;

import de.verdox.skyislands.subsystems.islands.model.Island;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BreakOnIslandEvent extends IslandEvent {
    private Block block;

    public BreakOnIslandEvent(Player player, Island island, Block block) {
        super(player, island);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
