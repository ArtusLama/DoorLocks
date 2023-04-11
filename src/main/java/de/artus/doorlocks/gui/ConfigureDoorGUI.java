package de.artus.doorlocks.gui;

import de.artus.doorlocks.DoorLocks;
import de.artus.doorlocks.utils.Config;
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

public class ConfigureDoorGUI implements Listener, UseConfirmActionGUI {


    Inventory inventory;
    KeyDoor door;
    List<ItemStack> inventoryItems = new ArrayList<>();



    public void open(Player player, KeyDoor door) {


        Bukkit.getPluginManager().registerEvents(this, DoorLocks.getPlugin(DoorLocks.class));

        this.door = door;
        inventory = Bukkit.createInventory(null, 27);

        inventory.setItem(10, door.key);
        inventory.setItem(19, item(Material.WRITABLE_BOOK, "Change Key"));
        inventory.setItem(12, item(Material.OAK_SIGN, "Door locked?"));
        inventory.setItem(21, item(door.isLocked ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER, door.isLocked ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO"));
        inventory.setItem(26, item(Material.BARRIER, ChatColor.RED + "Delete Door"));




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
                if (inventoryItems.contains(e.getCurrentItem())) {
                    switch (e.getCurrentItem().getType()) {
                        case BARRIER -> new ConfirmActionGUI().open((Player) e.getWhoClicked(), this);
                        case LIME_CONCRETE_POWDER -> {
                            door.isLocked = false;
                            door.saveToConfig();
                            inventory.setItem(21, item(door.isLocked ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER, door.isLocked ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO"));
                        }
                        case RED_CONCRETE_POWDER -> {
                            door.isLocked = true;
                            door.saveToConfig();
                            inventory.setItem(21, item(door.isLocked ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER, door.isLocked ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO"));
                        }
                        case WRITABLE_BOOK -> new ChangeKeyGUI().open((Player) e.getWhoClicked(), door);
                    }
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




    @Override
    public void onActionSuccess() {
        Config.removeDoor(door);
    }

    @Override
    public void onActionFail() {

    }
}
