package com.ubivismedia.robots.weapons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ExplosiveCannonWeapon {
    private static final double PROJECTILE_SPEED = 2.0;
    private static final float EXPLOSION_POWER = 2.0f; // Small explosion

    public static void fire(Player player) {
        World world = player.getWorld();
        Location launchLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5));
        Vector direction = player.getLocation().getDirection().multiply(PROJECTILE_SPEED);

        Fireball fireball = world.spawn(launchLocation, Fireball.class);
        fireball.setVelocity(direction);
        fireball.setYield(EXPLOSION_POWER);
        fireball.setIsIncendiary(false); // Prevent fire spread
        fireball.setShooter(player);
    }
}
