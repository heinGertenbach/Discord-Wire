package co.za.bonk;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONDatabase extends Database {

    JsonObject jsonObject;
    

    public JSONDatabase() {
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        File databaseFile = new File(plugin.getDataFolder(), "database.json");
        if(!databaseFile.exists()) {
            databaseFile.getParentFile().mkdirs();
            plugin.saveResource("database.json", false);
        }

        try {
            FileReader fileReader = new FileReader(databaseFile);
            JsonParser jsonParser = new JsonParser();
            this.jsonObject = (JsonObject) jsonParser.parse(fileReader);
        } catch (Exception e) {
            plugin.getLogger().warning(e.toString());
        }
    }

    @Override
    public String NewFromDiscord(String discordName){

        String discordHash = Hashing.hashString(discordName);
        String uniqueHash = Hashing.hashString(discordName + Hashing.randomHex(16));

        //update JSON database:
        JsonObject registerObject = (JsonObject) this.jsonObject.get("register");
        
        if (!registerObject.has(uniqueHash)) {
            JsonObject userObject = new JsonObject();
            userObject.addProperty("discordName", discordName);
            userObject.addProperty("discordHash", discordHash);
            registerObject.add(uniqueHash, userObject);
        }
        
        this.uploadNew();

        return uniqueHash;
    }

    @Override
    public int Register(String uniqueHash, String minecraftName, String minecraftUUID) {

        String minecraftHash = Hashing.hashString(minecraftName);
        
        JsonObject register = (JsonObject) this.jsonObject.get("register");
        JsonObject dTM = (JsonObject) this.jsonObject.get("discord_to_minecraft");
        JsonObject mTD = (JsonObject) this.jsonObject.get("minecraft_to_discord");

        if (register.has(uniqueHash)) {
            JsonObject userRegister = (JsonObject) register.get(uniqueHash);

            String discordName = userRegister.get("discordName").getAsString();
            String discordHash = userRegister.get("discordHash").getAsString();

            JsonObject userDTM = new JsonObject();
            JsonObject userMTD = new JsonObject();

            userDTM.addProperty("minecraftName", minecraftName);
            userDTM.addProperty("minecraftUUID", minecraftUUID);

            userMTD.addProperty("discordName", discordName);

            dTM.add(discordHash, userDTM);
            mTD.add(minecraftHash, userMTD);
            register.remove(uniqueHash);

            jsonObject.add("discord_to_minecraft", dTM);
            jsonObject.add("minecraft_to_discord", mTD);
            jsonObject.add("register", register);

            this.uploadNew();

            //succesfull
            return 0;
        }       
        //player not found on register list 
        return 1;
    }

    @Override
    public String fromMinecraft(String minecraftHash) throws Exception {
        JsonObject mTD = (JsonObject) this.jsonObject.get("minecraft_to_discord");
        JsonObject userMTD = (JsonObject) mTD.get(minecraftHash);

        if (userMTD != null) {
            return userMTD.get("discordName").getAsString();
        }
        return null;
    }

    private void uploadNew() {
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        try {
            File databaseFile = new File(plugin.getDataFolder(), "database.json");
            Files.write(Paths.get(databaseFile.toString()), this.jsonObject.toString().getBytes());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
}
