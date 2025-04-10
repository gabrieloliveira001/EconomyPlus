package net.byebye.balance;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {

    public static Main m;
    public static FileConfiguration cf;
    public static BukkitScheduler sh;
    public static PluginManager pm;
    private Economy economy;

    @Override
    public void onLoad() {
        m = this;
        cf = getConfig();
        sh = Bukkit.getScheduler();
        pm = Bukkit.getPluginManager();

    }

    @Override
    public void onEnable() {
        pm.registerEvents(new Events(), this);
        m.getCommand("pagar").setExecutor(new Commands());
        m.getCommand("saldo").setExecutor(new Commands());
        m.getCommand("versaldo").setExecutor(new Commands());
        m.getCommand("addsaldo").setExecutor(new Commands());
        m.getCommand("setsaldo").setExecutor(new Commands());
        m.getCommand("teste" +
                "").setExecutor(new Commands());
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

        HandlerList.unregisterAll(new Events());

    }
}
