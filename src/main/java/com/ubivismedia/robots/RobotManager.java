package com.ubivismedia.robots;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.bukkit.Material;
import java.util.*;

public class RobotManager {
    private final Set<Location> robotBlocks = new HashSet<>();
    private final Map<UUID, Boolean> playersInRobot = new HashMap<>();
    private final Map<UUID, Integer> robotHealth = new HashMap<>();
    private final Map<UUID, Integer> robotEnergy = new HashMap<>();
    private final Map<UUID, Integer> robotAmmo = new HashMap<>();
    private final Map<UUID, Long> lastShotTime = new HashMap<>();
    private final long shotCooldown = 2000;

    public void addBlock(Block block, Player player) {
        robotBlocks.add(block.getLocation());
        checkAndNotifyRobotCompletion(player);
    }

    public void removeBlock(Block block) {
        robotBlocks.remove(block.getLocation());
    }

    private void moveBlock(Location oldLoc, Location newLoc) {
        Block oldBlock = oldLoc.getWorld().getBlockAt(oldLoc);
        Block newBlock = newLoc.getWorld().getBlockAt(newLoc);

        if (newBlock.getType() == Material.AIR) {
            newBlock.setType(oldBlock.getType());
            oldBlock.setType(Material.AIR);

            robotBlocks.remove(oldLoc);
            robotBlocks.add(newLoc);
        }
    }


    public void moveJoint(Player player) {
        for (Location loc : new HashSet<>(robotBlocks)) {
            Block block = loc.getWorld().getBlockAt(loc);
            if (block.getType() == Material.PISTON) {
                Location newLoc = loc.clone().add(0, 1, 0);
                moveBlock(loc, newLoc);
                player.sendMessage(ChatColor.YELLOW + "Robot joint moved!");
            }
        }
    }



    public boolean isPartOfRobot(Block block) {
        return robotBlocks.contains(block.getLocation());
    }

    public Set<Location> getRobotBlocks() {
        return robotBlocks;
    }

    public boolean isPlayerInsideRobot(Player player) {
        return playersInRobot.containsKey(player.getUniqueId());
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
            player.sendMessage(ChatColor.GREEN + "Your robot is now complete!");
        }
    }

    public void enterRobot(Player player) {
        if (isRobotComplete()) {
            player.sendMessage(ChatColor.YELLOW + "You have entered your robot. Use WASD to move, sneak (shift) to exit and jump to fire!");
            playersInRobot.put(player.getUniqueId(), true);
            player.setAllowFlight(true);
        } else {
            player.sendMessage(ChatColor.RED + "Your robot is not fully assembled yet!");
        }
    }

    public void exitRobot(Player player) {
        playersInRobot.remove(player.getUniqueId());
        player.setAllowFlight(false);
    }

    public boolean isPlayerInRobot(Player player) {
        return playersInRobot.getOrDefault(player.getUniqueId(), false);
    }

    public int getRobotHealth(Player player) {
        return robotHealth.getOrDefault(player.getUniqueId(), 100);
    }

    public int getRobotEnergy(Player player) {
        return robotEnergy.getOrDefault(player.getUniqueId(), 100);
    }

    public int getRobotAmmo(Player player) {
        return robotAmmo.getOrDefault(player.getUniqueId(), 50);
    }

    @EventHandler
    public void onCockpitRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType().toString().equals("IRON_BLOCK")) {
                Player player = event.getPlayer();
                enterRobot(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (playersInRobot.containsKey(player.getUniqueId()) && event.isSneaking()) {
            exitRobot(player);
            player.sendMessage(ChatColor.RED + "You have exited your robot.");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (playersInRobot.containsKey(player.getUniqueId())) {
            moveRobotJoints(player);
        }
    }

    private void moveRobotJoints(Player player) {
        for (Location loc : new HashSet<>(robotBlocks)) {
            Block block = loc.getWorld().getBlockAt(loc);
            if (block.getType() == Material.PISTON) {
                Location newLoc = loc.clone().add(0, 1, 0);


                if (newLoc.getBlockY() > loc.getBlockY() + 2) {
                    continue;
                }

                moveBlock(loc, newLoc);
                player.sendMessage(ChatColor.YELLOW + "Robot joint moved!");
            }
        }
    }



    @EventHandler
    public void onPlayerJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (playersInRobot.containsKey(player.getUniqueId())) {
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