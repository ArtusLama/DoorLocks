package de.artus.doorlocks.events;

import de.artus.doorlocks.gui.ConfigureDoorGUI;
import de.artus.doorlocks.utils.Config;
import de.artus.doorlocks.utils.DoorManager;
import de.artus.doorlocks.utils.KeyDoor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class DoorInteractEvent implements Listener {


    @EventHandler
    public void onDoorPower(BlockRedstoneEvent e) {
        if (e.getBlock().getBlockData() instanceof Door door) {

            Block doorBlock = door.getHalf() == Bisected.Half.TOP ? e.getBlock().getRelative(0, -1, 0) : e.getBlock();
            if (Config.doorExists(doorBlock)) {
                KeyDoor keyDoor = KeyDoor.loadDoor(doorBlock);
                if (keyDoor.isLocked) {
                    e.setNewCurrent(keyDoor.door.isOpen() ? 15 : 0);
                }
            }
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getBlockData() instanceof Door interactedDoor) {

                Block doorBlock = interactedDoor.getHalf() == Bisected.Half.TOP ? e.getClickedBlock().getRelative(0, -1, 0) : e.getClickedBlock();

                KeyDoor door = KeyDoor.loadDoor(doorBlock);
                if (door != null) {
                    if (e.getPlayer().isSneaking()) {
                        if (door.owner.equals(e.getPlayer().getUniqueId())) {
                            new ConfigureDoorGUI().open(e.getPlayer(), door);
                        } else {
                            e.getPlayer().sendMessage(ChatColor.RED + "This door is already owned by " + ChatColor.BOLD + Bukkit.getOfflinePlayer(door.owner).getName());
                        }
                        e.setCancelled(true);
                        return;
                    }
                    if (door.isLocked) {
                        if (DoorManager.canOpenDoor(door, e.getItem())) {
                            if (door.doorBlock.getType() == Material.IRON_DOOR) {
                                e.setCancelled(true);
                                Sound sound = door.door.isOpen() ? Sound.BLOCK_IRON_DOOR_CLOSE : Sound.BLOCK_IRON_DOOR_OPEN;

                                door.door.setOpen(!door.door.isOpen());
                                door.doorBlock.setBlockData(door.door);

                                door.doorBlock.getWorld().playSound(e.getClickedBlock().getLocation(), sound, 1, 1);

                            }
                        } else {
                            e.setCancelled(true);
                        }
                    }
                } else if (e.getPlayer().isSneaking()) {
                    if (e.hasItem()) {
                        DoorManager.createNewDoor(e.getPlayer(), e.getClickedBlock().getLocation(), e.getItem(), true);
                        e.setCancelled(true);
                    }
                    else e.getPlayer().sendMessage(ChatColor.RED + "Please select an item in your hotbar you want as your key and try again!");
                }
            }
        }


    }


}
