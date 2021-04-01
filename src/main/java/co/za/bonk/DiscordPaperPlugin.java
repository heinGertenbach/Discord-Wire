package co.za.bonk;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.sql.*;

import com.google.common.util.concurrent.Service.State;

public class DiscordPaperPlugin extends JavaPlugin {


    private static DiscordPaperPlugin instance;
    private static Statement statement;

    private File configFile;
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
        this.getCommand("discord").setExecutor(new discordCommand());
        
        //connection info
        host = "admin.bonk.co.za";
        port = "3306";
        database = "minecraft";
        username = "General2";
        password = "P@sswerd2gen";

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

    public void openConnection() throws SQLException,
            ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.host+ ":" + this.port + "/" + this.database,
                this.username, this.password);
    }
}