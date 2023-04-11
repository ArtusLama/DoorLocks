package de.artus.doorlocks.gui;

import de.artus.doorlocks.DoorLocks;
import de.artus.doorlocks.utils.KeyDoor;
import de.artus.doorlocks.utils.UseConfirmActionGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ChangeKeyGUI implements Listener, UseConfirmActionGUI {


    Inventory inventory;
    KeyDoor door;
    ItemStack keyItem;
    boolean hasSelectedItem = false;


    public void open(Player player, KeyDoor door) {

        Bukkit.getPluginManager().registerEvents(this, DoorLocks.getPlugin(DoorLocks.class));

        this.door = door;
        inventory = Bukkit.createInventory(null, 27);


        keyItem = item(Material.BARRIER, ChatColor.RED + "Click on an item in your inventory");
        inventory.setItem(13, keyItem);

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
        if (e.getClickedInventory() == e.getWhoClicked().getInventory() && !hasSelectedItem) {
            if (e.getCurrentItem() == null) return;
            ItemStack key = e.getCurrentItem().clone();
            key.setAmount(1);
            keyItem = key;
            new ConfirmActionGUI().open((Player) e.getWhoClicked(), keyItem, this);
            hasSelectedItem = true;
            e.setCancelled(true);
        }
        if (e.getInventory().equals(inventory)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(inventory)) {
            hasSelectedItem = true;
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inventory)) {
            e.setCancelled(true);
        }
    }


    @Override
    public void onActionSuccess() {
        door.key = keyItem;
        door.saveToConfig();
    }

    @Override
    public void onActionFail() {

    }
}
