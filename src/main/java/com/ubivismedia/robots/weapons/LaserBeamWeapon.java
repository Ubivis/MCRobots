package com.ubivismedia.robots.weapons;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class LaserBeamWeapon {
    private static final double RANGE = 20.0;
    private static final double DAMAGE = 6.0;

    public static void fire(Player player) {
        World world = player.getWorld();
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();

        for (double i = 0; i < RANGE; i += 0.5) {
            Location point = start.clone().add(direction.clone().multiply(i));
            world.spawnParticle(Particle.DUST, point, 5, 0.1, 0.1, 0.1, new Particle.DustOptions(Color.RED, 1.0f));

            List<LivingEntity> entities = world.getNearbyEntities(point, 0.5, 0.5, 0.5, e -> e instanceof LivingEntity && !e.equals(player))
                    .stream()
                    .map(e -> (LivingEntity) e)
                    .collect(Collectors.toList());

            if (!entities.isEmpty()) {
                LivingEntity target = entities.get(0);
                target.damage(DAMAGE, player);
                world.playSound(target.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0f, 1.5f);
                break;
            }
        }

        world.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 2.0f);
    }
}
