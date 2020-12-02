package de.verdox.skyislands.subsystems.islands.events;

import de.verdox.skyislands.subsystems.islands.model.Island;
import de.verdox.vcore.events.VCoreEvent;
import org.bukkit.entity.Player;

public abstract class IslandEvent extends VCoreEvent {

    private final Player player;
    private final Island island;
    private boolean cancelled;

    public IslandEvent(Player player, Island island){
        this.player = player;
        this.island = island;
    }

    public Island getIsland() {
        return island;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
