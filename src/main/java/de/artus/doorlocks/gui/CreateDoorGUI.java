package de.artus.doorlocks.gui;

import de.artus.doorlocks.DoorLocks;
import de.artus.doorlocks.utils.KeyDoor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CreateDoorGUI implements Listener {


    Inventory inventory;
    ItemStack applyItem;
    KeyDoor door;

    public void open(Player player, KeyDoor door) {

        Bukkit.getPluginManager().registerEvents(this, DoorLocks.getPlugin(DoorLocks.class));

        this.door = door;
        inventory = Bukkit.createInventory(null, 27);
        applyItem = item(Material.LIME_CONCRETE, ChatColor.GREEN + "APPLY KEY", "by clicking your locking this door with this item:", door.key.getType().name());

        inventory.setItem(11, door.key);
        inventory.setItem(15, applyItem);

        player.openInventory(inventory);
    }

    private ItemStack item(Material material, String name, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));

        item.setItemMeta(itemMeta);
        return item;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().equals(inventory)) {

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().isSimilar(applyItem)) {
                    door.saveToConfig();
                    e.getInventory().close();
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inventory)) {
            e.setCancelled(true);
        }
    }


}
