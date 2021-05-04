package co.za.bonk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
    public String newFromDiscord(String username) throws SQLException{

        String hashUsername = Hashing.hashString(username);
        String uniqueHash = Hashing.hashString(username + Hashing.randomHex(16));

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
    public int Update(String hash, String key, String value) {
        return 0;
    }

    @Override
    public int Update(String hash, String[] keys, String[] values) {
        return 0;
    }
}