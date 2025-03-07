package com.ubivismedia;

import com.ubivismedia.blocks.CockpitBlock;
import com.ubivismedia.listeners.CockpitBlockListener;
import com.ubivismedia.robots.RobotManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RobotAddOn extends JavaPlugin {

    private RobotManager robotManager;

    @Override
    public void onEnable() {
        getLogger().info("RobotAddOn has been enabled!");

        // Register the crafting recipe for the Cockpit Block
        CockpitBlock.registerRecipe(this);

        // Initialize RobotManager
        robotManager = new RobotManager();

        // Register event listeners
        getServer().getPluginManager().registerEvents(new CockpitBlockListener(this, robotManager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("RobotAddOn has been disabled!");
    }
}
