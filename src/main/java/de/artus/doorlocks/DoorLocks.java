package de.artus.doorlocks;

import de.artus.doorlocks.events.DoorInteractEvent;
import de.artus.doorlocks.gui.CreateDoorGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DoorLocks extends JavaPlugin {

    @Override
    public void onEnable() {


        PluginManager pl = Bukkit.getPluginManager();
        pl.registerEvents(new DoorInteractEvent(), this);
        pl.registerEvents(new CreateDoorGUI(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
