package de.artus.doorlocks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class KeyDoor {


    public Block doorBlock;
    public Door door;
    public ItemStack key;
    public UUID owner;
    public boolean isLocked;

    public KeyDoor(Block doorBlock, ItemStack key, UUID owner, boolean isLocked) {
        this.doorBlock = doorBlock;
        this.door = (Door) doorBlock.getBlockData();
        this.owner = owner;
        this.isLocked = isLocked;
        this.key = key;


    }

    public static KeyDoor loadDoor(Block doorBlock) {


        if (!Config.doorExists(doorBlock)) return null;

        return new KeyDoor(doorBlock,
                Config.getDoorKey(doorBlock),
                Config.getDoorOwner(doorBlock),
                Config.isDoorLocked(doorBlock));
    }
    public void saveToConfig() {
        Config.setDoor(this);
    }


}
