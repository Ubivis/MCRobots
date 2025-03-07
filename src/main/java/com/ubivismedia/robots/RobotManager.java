package com.ubivismedia.robots;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.UUID;


public class RobotManager {
    private final Set<Location> robotBlocks = new HashSet<>();
    private final Set<Player> playersInRobot = new HashSet<>();
    private final HashMap<UUID, Long> lastShotTime = new HashMap<>();
    private final long shotCooldown = 2000;

    public void addBlock(Block block, Player player) {
        robotBlocks.add(block.getLocation());
        checkAndNotifyRobotCompletion(player);
    }

    public void removeBlock(Block block) {
        robotBlocks.remove(block.getLocation());
    }

    public boolean isPartOfRobot(Block block) {
        return robotBlocks.contains(block.getLocation());
    }

    public Set<Location> getRobotBlocks() {
        return robotBlocks;
    }

    public boolean isRobotComplete() {
        int cockpitCount = 0;
        int jointCount = 0;

        for (Location loc : robotBlocks) {
            Block block = loc.getWorld().getBlockAt(loc);
            if (block.getType().toString().equals("IRON_BLOCK")) {
                cockpitCount++;
            } else if (block.getType().toString().equals("PISTON")) {
                jointCount++;
            }
        }

        return cockpitCount >= 1 && jointCount >= 2;
    }

    private void checkAndNotifyRobotCompletion(Player player) {
        if (isRobotComplete()) {
            player.sendMessage("§aYour robot is now complete!");
        }
    }

    public void enterRobot(Player player) {
        if (isRobotComplete()) {
            player.sendMessage(ChatColor.YELLOW + "You have entered your robot. Use WASD to move, sneak (shift) to exit and jump to fire!");
            playersInRobot.add(player);
            player.setAllowFlight(true);
        } else {
            player.sendMessage(ChatColor.RED + "Your robot is not fully assembled yet!");
        }
    }

    // Allows entering the robot by right-clicking the cockpit
    @EventHandler
    public void onCockpitRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType().toString().equals("IRON_BLOCK")) { // Temporary cockpit check
                Player player = event.getPlayer();
                enterRobot(player);
                event.setCancelled(true);
            }
        }
    }

    // Allows the player to exit the robot by sneaking (Shift)
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (playersInRobot.contains(player) && event.isSneaking()) {
            playersInRobot.remove(player);
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.RED + "You have exited your robot.");
        }
    }

    // Allows the robot to fire projectiles when the player jumps, with a cooldown
    @EventHandler
    public void onPlayerJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (playersInRobot.contains(player)) {
            long currentTime = System.currentTimeMillis();
            long lastShot = lastShotTime.getOrDefault(player.getUniqueId(), 0L);

            if (currentTime - lastShot < shotCooldown) {
                player.sendMessage(ChatColor.RED + "Weapon recharging! Wait a moment before firing again.");
                event.setCancelled(true);
                return;
            }

            lastShotTime.put(player.getUniqueId(), currentTime);
            Location launchLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5));
            Arrow arrow = (Arrow) player.getWorld().spawnEntity(launchLocation, EntityType.ARROW);
            arrow.setVelocity(player.getLocation().getDirection().multiply(2));
            player.sendMessage(ChatColor.RED + "Your robot fired a projectile!");
            event.setCancelled(true);
        }
    }
}
