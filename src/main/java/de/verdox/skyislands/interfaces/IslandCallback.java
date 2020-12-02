package de.verdox.skyislands.interfaces;

import de.verdox.skyislands.subsystems.islands.model.Island;

@FunctionalInterface
public interface IslandCallback {
    void callback(Island island);
}
