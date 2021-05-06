package co.za.bonk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;

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

    @Override
    public String NewFromDiscord(String discordName) throws SQLException{

        String discordHash = Hashing.hashString(discordName);
        String uniqueHash = Hashing.hashString(discordName + Hashing.randomHex(16));

        Hashtable<String, Object> userObject = new Hashtable<String, Object>();
        userObject.put("discordName", discordName);
        userObject.put("discordHash", discordHash);

        add("register", "uniqueHash", uniqueHash, userObject);

        return uniqueHash;
    }

    @Override
    public int Register(String uniqueHash, String minecraftName, String minecraftUUID) throws SQLException{

        String minecraftHash = Hashing.hashString(minecraftName);

        Hashtable<String, Hashtable<String, Object>> register = get("register");

        if (register.containsKey(uniqueHash)) {
            Hashtable<String, Object> userRegister = register.get(uniqueHash);

            String discordName = (String) userRegister.get("discordName");
            String discordHash = (String) userRegister.get("discordHash");

            Hashtable<String, Object> userDTM = new Hashtable<String, Object>();
            Hashtable<String, Object> userMTD = new Hashtable<String, Object>();

            userDTM.put("minecraftName", minecraftName);
            userDTM.put("minecraftUUID", minecraftUUID);

            userMTD.put("discordName", discordName);

            add("discord_to_minecraft", "userHash", discordHash, userDTM);
            add("minecraft_to_discord", "userHash", minecraftHash, userMTD);

        }

        return 1;
    }

    @Override
    public String FromMinecraft(String minecraftHash) throws Exception {
        return null;
    }

    @Override
    public void Disable() {
        try {
            this.executeStatement(
                "DELETE FROM register"
            );
        } catch (SQLException e) {
            DiscordPaperPlugin.getInstance().getLogger().warning(e.getMessage());
        }
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

    public boolean executeStatement(String sql) throws SQLException{
        PreparedStatement statement = createConnection().prepareStatement(sql);
        return statement.execute();
    }



    void add(String table, String keyName, String key, Hashtable<String, Object> data) throws SQLException {
        String sqlKeys = "";
        String sqlValues = "";
        String set = "";
        for (String dataKey : data.keySet()){
            String value = ""+ data.get(dataKey);
            set += dataKey +"='"+ value +"',";
            sqlKeys += dataKey +",";
            sqlValues += "'"+ value +"',";
        }
        set = set.substring(0, set.length()-1);
        sqlKeys = sqlKeys.substring(0, sqlKeys.length()-1);
        sqlValues = sqlValues.substring(0, sqlValues.length()-1);


        this.executeStatement(
            "IF EXISTS (SELECT * FROM "+ table +" WHERE "+ keyName +"="+ key +")"+
            "BEGIN"+
            "   update "+ table +" set "+ set +
            "END ELSE BEGIN"+
            "   INSERT INTO "+ table +"("+ sqlKeys +") VALUES ("+ sqlValues +");"
        );
    }

    Hashtable<String, Hashtable<String, Object>> get(String table) throws SQLException{
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM "+ table +";");
        ResultSet result = statement.executeQuery();
        ResultSetMetaData metaData = result.getMetaData();

        int columns = metaData.getColumnCount();

        Hashtable<String, Hashtable<String, Object>> resultTable = new Hashtable<String, Hashtable<String, Object>>();
        String key;
        

        while (result.next()) {
            Hashtable<String, Object> row = new Hashtable<String, Object>();    
            key = result.getString(0);
            for (int i = 1; i <= columns; i++) {
                String columnKey = metaData.getCatalogName(i);
                Object data = result.getObject(i);
                row.put(columnKey, data);
            }
            resultTable.put(key, row);
        }

        return null;
    }

    Object get(String table, String value) {

        return null;
    }

    Object get(String table, int index) {

        return null;
    }

    void remove(String table, String key) throws SQLException{
        this.executeStatement(
            "DELETE FROM "+ table +"WHERE key="+ key +";"
            );
    }
}