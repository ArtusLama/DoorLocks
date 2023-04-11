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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConfirmActionGUI  implements Listener {


    Inventory inventory;
    UseConfirmActionGUI target;
    List<ItemStack> inventoryItems = new ArrayList<>();

    public void open(Player player, UseConfirmActionGUI target) {

        this.target = target;


        Bukkit.getPluginManager().registerEvents(this, DoorLocks.getPlugin(DoorLocks.class));

        inventory = Bukkit.createInventory(null, 27);

        inventory.setItem(11, item(Material.LIME_CONCRETE, ChatColor.GREEN + "CONFIRM"));
        inventory.setItem(15, item(Material.RED_CONCRETE, ChatColor.RED + "CANCEL"));

        player.openInventory(inventory);
    }

    public void open(Player player, ItemStack icon, UseConfirmActionGUI target) {

        this.target = target;


        Bukkit.getPluginManager().registerEvents(this, DoorLocks.getPlugin(DoorLocks.class));

        inventory = Bukkit.createInventory(null, 27);

        inventory.setItem(11, item(Material.LIME_CONCRETE, ChatColor.GREEN + "CONFIRM"));
        inventory.setItem(13, icon);
        inventory.setItem(15, item(Material.RED_CONCRETE, ChatColor.RED + "CANCEL"));

        player.openInventory(inventory);
    }


    private ItemStack item(Material material, String name, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));

        item.setItemMeta(itemMeta);

        inventoryItems.add(item);
        return item;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().equals(inventory)) {

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.LIME_CONCRETE && inventoryItems.contains(e.getCurrentItem())) {
                    inventory.close();
                    target.onActionSuccess();
                }
                if (e.getCurrentItem().getType() == Material.RED_CONCRETE && inventoryItems.contains(e.getCurrentItem())) {
                    inventory.close();
                    target.onActionFail();
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