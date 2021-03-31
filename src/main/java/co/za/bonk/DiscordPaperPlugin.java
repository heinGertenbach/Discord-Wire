package co.za.bonk;

import org.bukkit.plugin.java.JavaPlugin;

public class DiscordPaperPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        
    }

}