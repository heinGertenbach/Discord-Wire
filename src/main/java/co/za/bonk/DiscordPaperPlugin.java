package co.za.bonk;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

public class DiscordPaperPlugin extends JavaPlugin {


    private static DiscordPaperPlugin instance;
    private static Database database;

    private FileConfiguration secretsConfig;


    public static DiscordPaperPlugin getInstance() {
        return instance;
    }

    public static Database getDatabase() {
        return database;
    }

    static Connection connection;

    @Override
    public void onEnable() {
        //set instance of the class:
        instance = this;

        //create the secrets file if it doesn't exist
        createSecretsConfig();
        saveDefaultConfig();

        //Register event listener
        getServer().getPluginManager().registerEvents(new EventListener(), this);
     
        //start the Asyncronous Discord client
        Bukkit.getScheduler().runTaskAsynchronously(this, new DiscordCLientUser());
        
        //command for connecting discord
        getCommand("discord").setExecutor(new DiscordCommand());
        getCommand("discord").setTabCompleter(new DiscordCommandTabCompleter());

        String databaseKey = getConfig().getString("database").toLowerCase();

        switch (databaseKey) {
        
            //MySQL setup:
            case "mysql": {

                //connection info MySQL
                final String host = secretsConfig.getString("sql.hostname"); //"admin.bonk.co.za";
                final String port = secretsConfig.getString("sql.port");
                final String databaseName = getConfig().getString("sqlConfig.databaseName");
                final String username = secretsConfig.getString("sql.username");
                final String password = secretsConfig.getString("sql.password");

                //Output to console database info
                Logger log = getLogger();
                log.info("SQL information:");
                log.info("  hostname: " + host);
                log.info("  port    : " + port);
                log.info("  database: " + databaseName);
                log.info("  username: " + username);
                log.info("  password: " + password);

                //connection
                try {
            
                    SQLDatabase sqlDatabase = new SQLDatabase("jdbc:mysql://" +host+ ":" +port+ "/" +databaseName+ "?autoReconnect=true&useSSL=false", username, password);

                    sqlDatabase.executeStatement("CREATE TABLE IF NOT EXISTS discord_to_minecraft(UserHash CHAR(64) NOT NULL, UniqueHash CHAR(64), MinecraftName varchar(255), MinecraftUUID CHAR(64), PRIMARY KEY (UserHash));");
                    sqlDatabase.executeStatement("CREATE TABLE IF NOT EXISTS minecraft_to_discord(UserHash CHAR(64) NOT NULL, UniqueHash CHAR(64), DiscordName varchar(255), MinecraftUUID CHAR(64), PRIMARY KEY (UserHash));");

                    database = sqlDatabase;
                            
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            case "mongodb": {
                //to-do:


            }
        }
    }

    @Override
    public void onDisable() {
        DiscordCLientUser.getGatway();
    }


    public FileConfiguration getSecrets(){
        return this.secretsConfig;
    }

    private void createSecretsConfig(){
        File configFile = new File(getDataFolder(), "secrets.yml");
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