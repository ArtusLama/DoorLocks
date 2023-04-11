package de.artus.doorlocks.utils;

import de.artus.doorlocks.DoorLocks;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Config {

    public static File ConfigFile = new File(DoorLocks.getPlugin(DoorLocks.class).getDataFolder(), "doors.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(ConfigFile);

    public static void save() {
        try {
            config.save(ConfigFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void setDoor(KeyDoor door) {
        String path = "doors." + door.doorBlock.getLocation();
        if (door.door.getHalf() == Bisected.Half.TOP) path = "doors." + door.doorBlock.getLocation().clone().subtract(0, 1, 0);
        config.set(path + ".key", door.key);
        config.set(path + ".ownerID", door.owner.toString());
        config.set(path + ".isLocked", door.isLocked);
        save();
    }
    public static boolean doorExists(Block door) {
        return config.isSet("doors." + door.getLocation());
    }
    public static ItemStack getDoorKey(Block door) {
        return config.getItemStack("doors." + door.getLocation() + ".key");
    }
    public static UUID getDoorOwner(Block door) {
        return UUID.fromString(config.getString("doors." + door.getLocation() + ".ownerID"));
    }
    public static boolean isDoorLocked(Block door) {
        return config.getBoolean("doors." + door.getLocation() + ".isLocked");
    }

    public static void removeDoor(KeyDoor door) {
        config.set("doors." + door.doorBlock.getLocation(), null);
        save();
    }


}
