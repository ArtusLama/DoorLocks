package de.artus.doorlocks.utils;



import de.artus.doorlocks.gui.CreateDoorGUI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DoorManager {





    public static boolean canOpenDoor(KeyDoor door, ItemStack key) {
        if (door.isLocked) return door.key.isSimilar(key);
        return true;
    }


    public static void createNewDoor(Player player, Location location, ItemStack key, boolean locked) {
        ItemStack keyItem = key.clone();
        keyItem.setAmount(1);
        KeyDoor door = new KeyDoor(location.getBlock(), keyItem, player.getUniqueId(), locked);
        new CreateDoorGUI().open(player, door);
    }
}
