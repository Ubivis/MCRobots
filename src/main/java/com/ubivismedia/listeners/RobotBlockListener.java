package com.ubivismedia.listeners;

import com.ubivismedia.robots.RobotManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

public class RobotBlockListener implements Listener {

    private final JavaPlugin plugin;
    private final RobotManager robotManager;

    public RobotBlockListener(JavaPlugin plugin, RobotManager robotManager) {
        this.plugin = plugin;
        this.robotManager = robotManager;
    }

    @EventHandler
    public void onBlockPlace(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta() || item.getItemMeta().getDisplayName() == null) {
            return;
        }

        String displayName = item.getItemMeta().getDisplayName();
        Block block = event.getClickedBlock().getRelative(event.getBlockFace());

        switch (displayName) {
            case "§6Joint Block":
                block.setType(Material.PISTON);
                robotManager.addBlock(block, event.getPlayer());
                break;
            case "§7Armor Plating":
                block.setType(Material.IRON_BLOCK);
                robotManager.addBlock(block, event.getPlayer());
                break;
            case "§cWeapon Mount":
                block.setType(Material.ANVIL);
                robotManager.addBlock(block, event.getPlayer());
                break;
            case "§bEnergy Core":
                block.setType(Material.BEACON);
                robotManager.addBlock(block, event.getPlayer());
                break;
            default:
                return;
        }

        if (event.getPlayer().getGameMode() != org.bukkit.GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }

        event.setCancelled(true);
        checkRobotCompletion(event.getPlayer());
    }

    private void checkRobotCompletion(Player player) {
        if (robotManager.isRobotComplete()) {
            player.sendMessage("§aYour robot is now complete and ready to use! Right-click the Energy Core to activate it.");
        } else {
            player.sendMessage("§eYour robot still needs more components.");
        }
    }

    @EventHandler
    public void onEnergyCoreInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
            Block block = event.getClickedBlock();
            if (block != null && block.getType() == Material.BEACON) {
                Player player = event.getPlayer();
                if (robotManager.isRobotComplete()) {
                    robotManager.enterRobot(player);
                    player.sendMessage("§aYou have entered your robot! Use WASD to move and shift to exit.");
                } else {
                    player.sendMessage("§cYour robot is not fully assembled yet!");
                }
                event.setCancelled(true);
            }
        }
    }
}