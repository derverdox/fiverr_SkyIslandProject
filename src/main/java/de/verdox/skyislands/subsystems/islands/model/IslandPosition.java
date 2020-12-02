package de.verdox.skyislands.subsystems.islands.model;

import de.verdox.skyislands.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;

public class IslandPosition {
    private final World world;
    private final int x;
    private final int z;

    private final int maxX;
    private final int minX;
    private final int maxZ;
    private final int minZ;

    private final int radius;
    private final int distanceAfterRadius;
    private final int height;

    private final int middleX;
    private final int middleZ;

    public IslandPosition(World world, int x, int z){
        this.world = world;
        this.x = x;
        this.z = z;

        this.radius = Core.getInstance().getIslandSystem().getMainConfig().getRadius();
        this.distanceAfterRadius = Core.getInstance().getIslandSystem().getMainConfig().getSpaceBetween();
        this.height = Core.getInstance().getIslandSystem().getMainConfig().getHeight();

        this.middleX = x * (radius*2 + distanceAfterRadius);
        this.middleZ = z * (radius*2 + distanceAfterRadius);

        this.maxX = middleX+radius;
        this.minX = middleX-radius;
        this.maxZ = middleZ+radius;
        this.minZ = middleZ-radius;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Location toLocation(){
        return new Location(world, middleX,height,middleZ);
    }

    public void showBordersToPlayer(Player player){
        player.sendBlockChange(new Location(this.world,maxX,height,maxZ),Bukkit.createBlockData(Material.GLOWSTONE));
        player.sendBlockChange(new Location(this.world,maxX,height,minZ),Bukkit.createBlockData(Material.GLOWSTONE));
        player.sendBlockChange(new Location(this.world,minX,height,minZ),Bukkit.createBlockData(Material.GLOWSTONE));
        player.sendBlockChange(new Location(this.world,minX,height,maxZ),Bukkit.createBlockData(Material.GLOWSTONE));
    }

    public boolean isInsideIslandArea(Location location){
        int locX = location.getBlockX();
        int locZ = location.getBlockZ();

        return locX > minX
                && locX < maxX
                && locZ > minZ
                && locZ < maxZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IslandPosition that = (IslandPosition) o;
        return x == that.x &&
                z == that.z &&
                Objects.equals(world, that.world);
    }

    public int getHeight() {
        return height;
    }

    public int getRadius() {
        return radius;
    }

    public int getDistanceAfterRadius() {
        return distanceAfterRadius;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMiddleX() {
        return middleX;
    }

    public int getMiddleZ() {
        return middleZ;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }

    @Override
    public String toString() {
        return "IslandPosition{" +
                "world=" + world +
                ", x=" + x +
                ", z=" + z +
                ", maxX=" + maxX +
                ", minX=" + minX +
                ", maxZ=" + maxZ +
                ", minZ=" + minZ +
                ", radius=" + radius +
                ", distanceAfterRadius=" + distanceAfterRadius +
                ", height=" + height +
                '}';
    }
}
