package com.ubivismedia.robots.weapons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RocketLauncherWeapon {
    private static final double PROJECTILE_SPEED = 1.5;
    private static final float EXPLOSION_POWER = 4.0f; // Bigger explosion than Explosive Cannon

    public static void fire(Player player) {
        World world = player.getWorld();
        Location launchLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5));
        Vector direction = player.getLocation().getDirection().multiply(PROJECTILE_SPEED);

        Fireball rocket = world.spawn(launchLocation, Fireball.class);
        rocket.setVelocity(direction);
        rocket.setYield(EXPLOSION_POWER);
        rocket.setIsIncendiary(false); // Prevent fire spread
        rocket.setShooter(player);
    }
}
