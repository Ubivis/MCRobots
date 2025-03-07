package com.ubivismedia.blocks;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CockpitBlock {

    public static ItemStack createCockpitItem() {
        ItemStack item = new ItemStack(Material.IRON_BLOCK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Cockpit Block");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void registerRecipe(JavaPlugin plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "cockpit_block");
        ShapedRecipe recipe = new ShapedRecipe(key, createCockpitItem());

        recipe.shape("III", "IRI", "III");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('R', Material.REDSTONE);

        plugin.getServer().addRecipe(recipe);
    }
}
