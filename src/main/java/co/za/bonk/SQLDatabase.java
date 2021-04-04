package co.za.bonk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDatabase extends Database{

    private static Connection connection;
    private static Statement statement;

    public SQLDatabase(String url, String username, String password) throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            e.getStackTrace();
        } 
        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
    }

    int executeUpdate(String sql) throws SQLException {
        return statement.executeUpdate(sql);
    }



    @Override
    public int New(int hash) {
        return 0;
    }

    @Override
    public int Update(int hash, String key, String value) {
        return 0;
    }

    @Override
    public int Update(int hash, String[] keys, String[] values) {
        return 0;
    }

    Connection getConnection() {
        return connection;
    }
}