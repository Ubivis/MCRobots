package com.ubivismedia.listeners;

import com.ubivismedia.blocks.CockpitBlock;
import com.ubivismedia.robots.RobotManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

public class CockpitBlockListener implements Listener {

    private final JavaPlugin plugin;
    private final RobotManager robotManager;

    public CockpitBlockListener(JavaPlugin plugin, RobotManager robotManager) {
        this.plugin = plugin;
        this.robotManager = robotManager;
    }

    @EventHandler
    public void onPlayerPlaceCockpit(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals("§6Cockpit Block")) {
            return;
        }

        Block block = event.getClickedBlock().getRelative(event.getBlockFace());
        block.setType(Material.IRON_BLOCK);

        // Register the block in the RobotManager
        robotManager.addBlock(block, event.getPlayer());

        if (event.getPlayer().getGameMode() != org.bukkit.GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }

        event.setCancelled(true);
    }
}
