package com.ubivismedia.blocks;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RobotBlocks {

    public static ItemStack createBlock(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void registerRecipes(JavaPlugin plugin) {
        // Joint Block
        NamespacedKey jointKey = new NamespacedKey(plugin, "joint_block");
        ShapedRecipe jointRecipe = new ShapedRecipe(jointKey, createBlock("§6Joint Block", Material.PISTON));
        jointRecipe.shape(" I ", "ISI", " I ");
        jointRecipe.setIngredient('I', Material.IRON_INGOT);
        jointRecipe.setIngredient('S', Material.SLIME_BALL);
        plugin.getServer().addRecipe(jointRecipe);

        // Armor Plating
        NamespacedKey armorKey = new NamespacedKey(plugin, "armor_plating");
        ShapedRecipe armorRecipe = new ShapedRecipe(armorKey, createBlock("§7Armor Plating", Material.IRON_BLOCK));
        armorRecipe.shape("III", "IOI", "III");
        armorRecipe.setIngredient('I', Material.IRON_INGOT);
        armorRecipe.setIngredient('O', Material.OBSIDIAN);
        plugin.getServer().addRecipe(armorRecipe);

        // Weapon Mount
        NamespacedKey weaponMountKey = new NamespacedKey(plugin, "weapon_mount");
        ShapedRecipe weaponMountRecipe = new ShapedRecipe(weaponMountKey, createBlock("§cWeapon Mount", Material.ANVIL));
        weaponMountRecipe.shape("III", " R ", "III");
        weaponMountRecipe.setIngredient('I', Material.IRON_INGOT);
        weaponMountRecipe.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(weaponMountRecipe);

        // Energy Core
        NamespacedKey energyCoreKey = new NamespacedKey(plugin, "energy_core");
        ShapedRecipe energyCoreRecipe = new ShapedRecipe(energyCoreKey, createBlock("§bEnergy Core", Material.BEACON));
        energyCoreRecipe.shape(" D ", "DRD", " D ");
        energyCoreRecipe.setIngredient('D', Material.DIAMOND);
        energyCoreRecipe.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(energyCoreRecipe);
    }
}