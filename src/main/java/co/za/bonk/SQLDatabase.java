package co.za.bonk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLDatabase extends Database{

    private Connection connection;

    private String url, username, password;

    public SQLDatabase(String url, String username, String password) throws SQLException{

        this.url = url;
        this.username = username;
        this.password = password;


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            e.getStackTrace();
        } 
        this.connection = createConnection();

        //create required database tables
        this.executeStatement("CREATE TABLE IF NOT EXISTS register(uniqueHash CHAR(64) NOT NULL, discordName varchar(255), discordHash, minecraftName varchar(255)), minecraftUUID CHAR(64), minecraftHash CHAR(64), PRIMARY KEY (uniqueHash)");
        this.executeStatement("CREATE TABLE IF NOT EXISTS discord_to_minecraft(userHash CHAR(64) NOT NULL, minecraftName varchar(255), minecraftUUID CHAR(36), PRIMARY KEY (userHash));");
        this.executeStatement("CREATE TABLE IF NOT EXISTS minecraft_to_discord(userHash CHAR(64) NOT NULL, discordName varchar(255), PRIMARY KEY (userHash));");
    }

    Connection createConnection() throws SQLException {
        //return current connection object if it is still open and exists
        if (this.connection != null && !this.connection.isClosed()) {
            return this.connection;
        }

        this.connection = DriverManager.getConnection(this.url, this.username, this.password); 
        return this.connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public int executeStatement(String sql) throws SQLException{
        PreparedStatement statement = createConnection().prepareStatement(sql);
        return statement.executeUpdate();
    }

    @Override
    public String NewFromDiscord(String discordName) throws SQLException{

        String hashUsername = Hashing.hashString(discordName);
        String uniqueHash = Hashing.hashString(discordName + Hashing.randomHex(16));


        PreparedStatement statement = createConnection().prepareStatement(
            "IF NOT EXISTS (SELECT * FROM discord_to_minecraft WHERE UserHash =" +hashUsername+ ") BEGIN " +
                "INSERT INTO discord_to_minecraft (UserHash, UniqueHash) VALUES (" +hashUsername+ ", " +uniqueHash+ ") "+
            "END "
        );

        int returnCode = statement.executeUpdate();
        DiscordPaperPlugin.getInstance().getLogger().info("Sql update with code: " + returnCode);
        return uniqueHash;
    }

    @Override
    public int Register(String uniqueHash, String minecraftName, String minecraftUUID) {
        // TODO Auto-generated method stub
        return 0;
    }
}