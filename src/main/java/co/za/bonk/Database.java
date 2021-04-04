package co.za.bonk;

public abstract class Database {

    public abstract int New(int hash);
    public abstract int Update(int hash, String key, String value);
    public abstract int Update(int hash, String[] keys, String[] values);

}
