package co.za.bonk;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public class DiscordPaperPlugin extends JavaPlugin {


    private File configFile;
    private FileConfiguration secretsConfig;


    @Override
    public void onLoad() {
        
    }

    @Override
    public void onEnable() {
        //Register event listener
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        createSecretsConfig();
    }

    @Override
    public void onDisable() {
        
    }


    public FileConfiguration getSecrets(){
        return this.secretsConfig;
    }

    private void createSecretsConfig(){
        configFile = new File(getDataFolder(), "secrets.yml");
        if(!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("secrets.yml", false);
        }

        secretsConfig = new YamlConfiguration();
        try {
            secretsConfig.load(configFile);

        } catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

}