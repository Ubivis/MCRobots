package com.ubivismedia.robots.weapons;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class MachineGunWeapon {
    private static final double BULLET_SPEED = 3.0;
    private static final int SHOTS_PER_BURST = 5;
    private static final long BURST_DELAY = 100; // Milliseconds between shots

    public static void fire(Player player, JavaPlugin plugin) {
        World world = player.getWorld();
        Location launchLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5));
        Vector direction = player.getLocation().getDirection().multiply(BULLET_SPEED);

        for (int i = 0; i < SHOTS_PER_BURST; i++) {
            final int shotIndex = i;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Projectile bullet = world.spawn(launchLocation, Arrow.class);
                bullet.setVelocity(direction);
                bullet.setShooter(player);
            }, shotIndex * BURST_DELAY / 50); // Convert milliseconds to ticks
        }
    }
}
