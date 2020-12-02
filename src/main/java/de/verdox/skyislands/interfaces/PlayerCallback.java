package de.verdox.skyislands.interfaces;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerCallback {
    void callback(Player player);
}
