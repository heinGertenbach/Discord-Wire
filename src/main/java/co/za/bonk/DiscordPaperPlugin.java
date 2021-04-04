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
    private static Statement statement;

    private FileConfiguration secretsConfig;


    public static DiscordPaperPlugin getInstance() {
        return instance;
    }

    public static Statement getStatement() {
        return statement;
    }

    String host, port, database, username, password;
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

        //connection info
        host = secretsConfig.getString("sql.hostname"); //"admin.bonk.co.za";
        port = secretsConfig.getString("sql.port");
        database = secretsConfig.getString("sql.database");
        username = secretsConfig.getString("sql.username");
        password = secretsConfig.getString("sql.password");

        Logger log = getLogger();
        log.info("SQL information:");
        log.info("  hostname: " + host);
        log.info("  port    : " + port);
        log.info("  database: " + database);
        log.info("  username: " + username);
        log.info("  password: " + password);

        //connection
        try {    
            openConnection();
            statement = connection.createStatement();
                       
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void openConnection() throws SQLException,
            ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        SQLDatabase database = new SQLDatabase("jdbc:mysql://" +this.host+ ":" +this.port+ "/" +this.database+ "?autoReconnect=true&useSSL=false", username, password);
        connection = database.getConnection();
    }
}