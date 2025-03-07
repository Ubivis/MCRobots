package com.ubivismedia.listeners;

import com.ubivismedia.robots.RobotWeaponType;
import com.ubivismedia.robots.weapons.MachineGunWeapon;
import com.ubivismedia.robots.weapons.ExplosiveCannonWeapon;
import com.ubivismedia.robots.weapons.LaserBeamWeapon;
import com.ubivismedia.robots.weapons.RocketLauncherWeapon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class WeaponSelectionListener implements Listener {
    private final HashMap<UUID, RobotWeaponType> playerWeapons = new HashMap<>();
    private final JavaPlugin plugin;

    public WeaponSelectionListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            RobotWeaponType currentWeapon = playerWeapons.getOrDefault(player.getUniqueId(), RobotWeaponType.MACHINE_GUN);

            // Cycle to the next weapon
            RobotWeaponType[] weapons = RobotWeaponType.values();
            int nextIndex = (currentWeapon.ordinal() + 1) % weapons.length;
            RobotWeaponType newWeapon = weapons[nextIndex];
            playerWeapons.put(player.getUniqueId(), newWeapon);

            player.sendMessage(ChatColor.AQUA + "Weapon switched to: " + ChatColor.YELLOW + newWeapon.name().replace("_", " "));
        }
    }

    public RobotWeaponType getPlayerWeapon(Player player) {
        return playerWeapons.getOrDefault(player.getUniqueId(), RobotWeaponType.MACHINE_GUN);
    }

    // Fires the selected weapon when jumping (robot attack action)
    @EventHandler
    public void onPlayerJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        RobotWeaponType selectedWeapon = getPlayerWeapon(player);

        switch (selectedWeapon) {
            case MACHINE_GUN:
                MachineGunWeapon.fire(player, plugin); // Pass plugin instance
                player.sendMessage(ChatColor.RED + "Firing Machine Gun!");
                break;
            case EXPLOSIVE_CANNON:
                ExplosiveCannonWeapon.fire(player);
                player.sendMessage(ChatColor.RED + "Firing Explosive Cannon!");
                break;
            case LASER_BEAM:
                LaserBeamWeapon.fire(player);
                player.sendMessage(ChatColor.RED + "Firing Laser Beam!");
                break;
            case ROCKET_LAUNCHER:
                RocketLauncherWeapon.fire(player);
                player.sendMessage(ChatColor.RED + "Firing Rocket Launcher!");
                break;
        }

        event.setCancelled(true); // Prevent unintended jumping
    }
}
