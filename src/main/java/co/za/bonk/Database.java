package co.za.bonk;

public abstract class Database {

    public abstract String NewFromDiscord(String discordName) throws Exception;
    public abstract int Register(String uniqueHash, String minecraftName, String minecraftUUID) throws Exception;

}
