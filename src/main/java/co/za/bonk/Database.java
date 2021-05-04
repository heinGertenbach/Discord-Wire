package co.za.bonk;

public abstract class Database {

    public abstract String newFromDiscord(String username) throws Exception;
    public abstract int Update(String hash, String key, String value);
    public abstract int Update(String hash, String[] keys, String[] values);

}
