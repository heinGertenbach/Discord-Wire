package co.za.bonk;

public abstract class Database {

    public abstract String NewFromDiscord(String discordName) throws Exception;
    public abstract int Register(String uniqueHash, String minecraftName, String minecraftUUID) throws Exception;
    public abstract String FromMinecraft(String minecraftHash) throws Exception;
    public abstract void Disable();

}
